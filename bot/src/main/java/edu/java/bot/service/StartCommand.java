package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Bot;

public class StartCommand extends Command {
    public StartCommand(Command nextCommand) {
        super(nextCommand);
    }

    @Override
    public SendMessage handleCommand(Update update, Bot telegramBot) {
        if (super.getCommand(update).equals("/start")) {
            telegramBot.addUser(getUserId(update));
            return new SendMessage(super.getChatId(update), "Добро пожаловать в Worker бота, "
                + getUserName(update) + "!");
        }
        return super.handleCommand(update, telegramBot);
    }
}
