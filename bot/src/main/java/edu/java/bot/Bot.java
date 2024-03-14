package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.ChainCommands;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bot {
    @Setter
    @Getter
    private TelegramBot telegramBot;
    private final ChainCommands chainCommands;
    public Map<Long, List<URL>> references;

    @Autowired
    public Bot(ApplicationConfig applicationConfig) {
        this.chainCommands = new ChainCommands();
        this.references = new HashMap<>();
        this.telegramBot = new TelegramBot(applicationConfig.telegramToken());
    }

    /**
     * creating menu with list of commands in telegram bot
     */
    public void createMenu() {
        BotCommand startCommand = new BotCommand("/start", "зарегистрировать пользователя");
        BotCommand helpCommand = new BotCommand("/help", "вывести окно с командами");
        BotCommand trackCommand = new BotCommand("/track", "начать отслеживание ссылки");
        BotCommand untrackCommand = new BotCommand("/untrack", "прекратить отслеживание ссылки");
        BotCommand listCommand = new BotCommand("/list", "показать список отслеживаемых ссылок");
        SetMyCommands allCommands = new SetMyCommands(startCommand, helpCommand,
            trackCommand, untrackCommand, listCommand);
        this.telegramBot.execute(allCommands);
    }

    /**
     * processUpdate method needs for get message from user and do it every time while bot is working
     */
    public void processUpdate() {
        createMenu();
        int offset = 0;
        while (true) {
            GetUpdates getUpdates = new GetUpdates().limit(1).offset(offset + 1).timeout(0);
            GetUpdatesResponse updatesResponse = telegramBot.execute(getUpdates);
            List<Update> updates = updatesResponse.updates();
            if (!updates.isEmpty()) {
                offset = updates.getFirst().updateId();
                updates.forEach(update -> executeCommand(chainCommands.handleCommand(update, this)));
            }
        }
    }

    /**
     * this method needs for get one message from user
     * it works while bot do not get some messages from user
     *
     * @param update last message from user
     * @return getting message from user
     */
    public String waitingNewMessage(Update update) {
        List<Update> lastUpdate = new ArrayList<>();
        GetUpdates getUpdate;
        while (lastUpdate.isEmpty()) {
            getUpdate = new GetUpdates().limit(1).offset(update.updateId() + 1).timeout(0);
            lastUpdate = this.telegramBot.execute(getUpdate).updates();
        }
        String message = lastUpdate.getFirst().message().text();
        getUpdate = new GetUpdates().limit(1).offset(lastUpdate.getFirst().updateId() + 1).timeout(0);
        this.telegramBot.execute(getUpdate).updates();
        return message;
    }

    public void addUser(Long id) {
        if (!references.containsKey(id)) {
            references.put(id, new ArrayList<>());
        }
    }

    public boolean containsURL(Long id, URL url) {
        return references.get(id).stream().anyMatch(urlI -> urlI.toString().equals(url.toString()));
    }

    public void addURLs(Long id, URL url) {
        references.get(id).add(url);
    }

    public void deleteURLs(Long id, URL url) {
        references.get(id).remove(url);
    }

    public List<URL> getListOfURLS(Long id) {
        return references.get(id);
    }

    public void executeCommand(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }
}
