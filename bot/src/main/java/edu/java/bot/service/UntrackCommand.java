package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Bot;
import java.net.URL;

public class UntrackCommand extends Command {
    public UntrackCommand(Command nextCommand) {
        super(nextCommand);
    }

    @Override
    public SendMessage handleCommand(Update update, Bot telegramBot) {
        if (getCommand(update).equals("/untrack")) {
            if  (telegramBot.getListOfURLS(getUserId(update)).isEmpty()) {
                return new SendMessage(
                    getChatId(update),
                    "Список ссылок пуст. Для удаления ссылки список не должен быть пустым.");
            } else {
                telegramBot.executeCommand(new SendMessage(
                    super.getChatId(update),
                    "Отправьте ссылку для прекращения отслеживания."
                ));
            }
            String urlStr = telegramBot.waitingNewMessage(update);
            return handleDeleteUrl(update, urlStr, telegramBot);
        }
        return super.handleCommand(update, telegramBot);
    }

    /**
     * method needs for deleting links from map, after user used /untrack command
     * @param update get last message
     * @param urlStr link, which needs delete from map
     * @param bot current bot
     * @return message about deleting link
     */
    SendMessage handleDeleteUrl(Update update, String urlStr, Bot bot) {
        try {
            URL url = new URL(urlStr);
            if (!bot.containsURL(getUserId(update), url)) {
                return new SendMessage(getChatId(update), "Данной ссылки нет в списке.");
            } else {
                bot.deleteURLs(getUserId(update), url);
                return new SendMessage(getChatId(update), "Ссылка была удалена из списка.");
            }
        } catch (Exception e) {
            return new SendMessage(
                getChatId(update),
                "Неверна указана ссылка. Для отправки ссылки введите команду /track");
        }
    }
}
