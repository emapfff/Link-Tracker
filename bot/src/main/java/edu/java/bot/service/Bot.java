package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;

public class Bot {
    private static TelegramBot telegramBot;
    private static ChainCommands chainCommands;
    public Bot (String telegramToken){
        chainCommands = new ChainCommands();
        telegramBot = new TelegramBot(telegramToken);
    }

    public void processUpdate(){
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                executeCommand(chainCommands.handleCommand(update));
            });
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

    private void executeCommand(SendMessage sendMessage){
        telegramBot.execute(sendMessage);
    }

}
