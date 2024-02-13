package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class listCommand extends Command{
    public listCommand(Command nextCommand) {
        super(nextCommand);
    }

    @Override
    public SendMessage handleCommand(Update update, Bot telegramBot) {
        if (getCommand(update).equals("/list")){
            List<URL> listOfURLs = telegramBot.getListOfURLS(getChatId(update));
            return new SendMessage(getChatId(update), convertUrlListToString(listOfURLs));
        }
        return super.handleCommand(update, telegramBot);
    }

    static String convertUrlListToString (List<URL> urlList){
         String listOfURLs = urlList.stream()
            .map(URL::toString)
            .reduce((url1, url2) -> url1 + "\n" + url2)
            .orElse("");
         return (listOfURLs.isEmpty()) ? "Список ссылок пуст." : listOfURLs;
    }

}
