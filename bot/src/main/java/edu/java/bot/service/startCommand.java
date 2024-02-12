package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class startCommand extends Command{
    public startCommand(Command nextCommand) {
        super(nextCommand);
    }

    public SendMessage handleCommand (Update update) {
        if (super.getCommand(update).equals("/start")) {
            return new SendMessage(super.getChatId(update), "Добро пожаловать в Worker бота!");
        } else {
            return super.handleCommand(update);
        }
    }
}
