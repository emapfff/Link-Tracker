package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;


public class ChainCommands {
    private Command chain;

    public ChainCommands() {
        buildChain();
    }

    private void buildChain() {
        this.chain = new StartCommand(
            new HelpCommand(
                new TrackCommand(
                    new ListCommand(
                        new UntrackCommand(
                            null)))));
    }


    public SendMessage handleCommand(Update update, Bot telegramBot) {
        return chain.handleCommand(update, telegramBot);
    }
}
