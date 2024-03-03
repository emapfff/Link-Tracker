package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.URL;
import java.util.List;

public class ListCommand extends Command {
    public ListCommand(Command nextCommand) {
        super(nextCommand);
    }

    @Override
    public SendMessage handleCommand(Update update, Bot telegramBot) {
        if (getCommand(update).equals("/list")) {
            List<URL> listOfURLs = telegramBot.getListOfURLS(getUserId(update));
            return new SendMessage(getChatId(update), convertUrlListToString(listOfURLs));
        }
        return super.handleCommand(update, telegramBot);
    }

    /**
     * Convert all url links for list of strings of links
     * @param urlList list consists url links
     * @return strings of url links
     */
    static String convertUrlListToString(List<URL> urlList) {
         String listOfURLs = urlList.stream()
            .map(URL::toString)
            .reduce((url1, url2) -> url1 + "\n" + url2)
            .orElse("");
         return (listOfURLs.isEmpty()) ? "Список ссылок пуст." : listOfURLs;
    }
}
