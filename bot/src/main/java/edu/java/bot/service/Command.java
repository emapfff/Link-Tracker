package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public abstract class Command {
    private final Command nextCommand;


    public Command(Command nextCommand){
        this.nextCommand = nextCommand;
    }

    public SendMessage handleCommand(Update update){
        if (nextCommand != null){
            return nextCommand.handleCommand(update);
        }
        return new SendMessage(update.message().chat().id(), "Неверная команда!");
    }

    protected String getCommand(Update update){
        return update.message().text();
    }

    protected long getChatId(Update update){
        return update.message().chat().id();
    }

}



