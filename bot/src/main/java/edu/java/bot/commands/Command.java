package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    String name();

    String description();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        String message = update.message().text();
        return message.equals("/track") || message.equals("/untrack");
    }

    default BotCommand toApiCommand() {
        return new BotCommand(name(), description());
    }
}
