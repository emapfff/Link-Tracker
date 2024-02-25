package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    private final Command nextCommand;

    public Command(Command nextCommand) {
        this.nextCommand = nextCommand;
    }

    public SendMessage handleCommand(Update update, Bot telegramBot) {
        if (nextCommand != null) {
            return nextCommand.handleCommand(update, telegramBot);
        }
        return new SendMessage(getChatId(update), "Неверная команда!");
    }

    /**
     * this method needs for get one message from user
     * it works while bot do not get some messages from user
     * @param update last message from user
     * @param telegramBot current bot
     * @return getting message from user
     */
    protected String waitingNewMessage(Update update, Bot telegramBot) {
        List<Update> lastUpdate = new ArrayList<>();
        GetUpdates getUpdate;
        while (lastUpdate.isEmpty()) {
            getUpdate = new GetUpdates().limit(1).offset(update.updateId() + 1).timeout(0);
            lastUpdate = telegramBot.getTelegramBot().execute(getUpdate).updates();
        }
        String message = lastUpdate.getFirst().message().text();
        getUpdate = new GetUpdates().limit(1).offset(lastUpdate.getFirst().updateId() + 1).timeout(0);
        telegramBot.getTelegramBot().execute(getUpdate).updates();
        return message;
    }

    protected String getCommand(Update update) {
        return update.message().text();
    }

    protected Long getUserId(Update update) {
        return update.message().chat().id();
    }

    protected Long getChatId(Update update) {
        return update.message().chat().id();
    }

    protected String getUserName(Update update) {
        return update.message().chat().firstName() + " " + update.message().chat().lastName();
    }

}



