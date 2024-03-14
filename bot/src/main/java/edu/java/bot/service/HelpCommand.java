package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Bot;

public class HelpCommand extends Command {
    public HelpCommand(Command nextCommand) {
        super(nextCommand);
    }

    @Override
    public SendMessage handleCommand(Update update, Bot telegramBot) {
        if (super.getCommand(update).equals("/help")) {
            return new SendMessage(
                super.getChatId(update),
                """
                    /start -- зарегистрировать пользователя
                    /help -- вывести окно с командами
                    /track -- начать отслеживание ссылки
                    /untrack -- прекратить отслеживание ссылки
                    /list -- показать список отслеживаемых ссылок
                    """
            );
        }
        return super.handleCommand(update, telegramBot);
    }
}
