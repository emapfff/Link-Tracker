package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

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



