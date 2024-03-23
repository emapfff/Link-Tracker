package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("edu.java.bot.service.Bot")
public class Bot {
    private final TelegramBot telegramBot;
    private final List<Command> commands;

    @Autowired
    public Bot(ApplicationConfig applicationConfig, List<Command> commands) {
        this.telegramBot = new TelegramBot(applicationConfig.telegramToken());
        this.commands = commands;
    }

    /**
     * creating menu with list of commands in telegram bot
     */
    public void createMenu() {
        SetMyCommands allCommands = new SetMyCommands(
            commands.stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new)
        );
        this.telegramBot.execute(allCommands);
    }

    public void start() {
        createMenu();
        this.telegramBot.setUpdatesListener(this::process);
    }

    public int process(List<Update> list) {
        Update lastUpdate = list.getLast();
        if (lastUpdate != null && lastUpdate.message().text() != null) {
            boolean foundCommand = false;
            String message = lastUpdate.message().text();
            for (Command command : commands) {
                if (message.equals(command.command())) {
                    if (command.supports(lastUpdate)) {
                        executeCommand(new SendMessage(
                                lastUpdate.message().chat().id(),
                                "Укажите, пожалуйста, ссылку"
                            )
                        );
                        lastUpdate = waitingNewMessage(lastUpdate);
                        executeCommand(command.handle(lastUpdate));
                    }
                    foundCommand = true;
                    executeCommand(command.handle(lastUpdate));
                    break;
                }
            }
            if (!foundCommand) {
                long chatId = lastUpdate.message().chat().id();
                this.executeCommand(new SendMessage(chatId, "Команда не найдена."));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public Update waitingNewMessage(Update update) {
        List<Update> lastUpdate = new ArrayList<>();
        GetUpdates getUpdate;
        while (lastUpdate.isEmpty()) {
            getUpdate = new GetUpdates().limit(1).offset(update.updateId() + 1).timeout(0);
            lastUpdate = this.telegramBot.execute(getUpdate).updates();
        }
        getUpdate = new GetUpdates().limit(1).offset(lastUpdate.getFirst().updateId() + 1).timeout(0);
        this.telegramBot.execute(getUpdate).updates();
        return lastUpdate.getFirst();
    }

    public void executeCommand(SendMessage sendMessage) {
        this.telegramBot.execute(sendMessage);
    }
}
