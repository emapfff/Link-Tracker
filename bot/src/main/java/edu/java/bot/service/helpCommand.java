package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class helpCommand extends Command{
    public helpCommand(Command nextCommand) {
        super(nextCommand);
    }
    @Override
    public SendMessage handleCommand(Update update, Bot telegramBot){
        if (super.getCommand(update).equals("/help")){
            return new SendMessage(super.getChatId(update),
                "/start -- зарегистрировать пользователя\n" +
                "/help -- вывести окно с командами\n" +
                "/track -- начать отслеживание ссылки\n" +
                "/untrack -- прекратить отслеживание ссылки\n" +
                "/list -- показать список отслеживаемых ссылок)\n");
        }
        return super.handleCommand(update, telegramBot);
    }
}
