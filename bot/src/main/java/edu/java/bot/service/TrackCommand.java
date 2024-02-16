package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.URL;

public class TrackCommand extends Command {
    public TrackCommand(Command nextCommand) {
        super(nextCommand);
    }

    @Override
    public SendMessage handleCommand(Update update, Bot telegramBot) {
        if (getCommand(update).equals("/track")) {
            telegramBot.executeCommand(new SendMessage(super.getChatId(update), "Отправьте ссылку для отслеживания."));
            String urlStr = waitingNewMessage(update, telegramBot);
            return handleAddUrl(update, urlStr, telegramBot);
        }
        return super.handleCommand(update, telegramBot);
    }

    SendMessage handleAddUrl(Update update, String urlStr, Bot bot) {
        try {
            URL url = new URL(urlStr);
            if (bot.containsURL(getUserId(update), url)) {
                return new SendMessage(getChatId(update), "Ссылка уже находится в списке.");
            } else {
                bot.addURLs(getUserId(update), url);
                return new SendMessage(getChatId(update), "Ссылка добавлена.");
            }
        } catch (Exception e) {
            return new SendMessage(
                getChatId(update),
                "Неверна указана ссылка. Для отправки ссылки введите команду /track");
        }
    }

}
