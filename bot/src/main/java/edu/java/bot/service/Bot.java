package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class Bot {
    private static TelegramBot telegramBot;


    public Bot (String telegramToken){
        telegramBot = new TelegramBot(telegramToken);
    }

    public void processUpdate(){
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(System.out::println);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null){
                e.response().errorCode();
                e.response().description();
            } else {
                e.printStackTrace();
            }
        });
    }
}
