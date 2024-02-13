package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class untrackCommand extends Command{
    public untrackCommand(Command nextCommand) {
        super(nextCommand);
    }

    @Override
    public SendMessage handleCommand(Update update, Bot telegramBot) {
        if (getCommand(update).equals("/untrack")){
            telegramBot.executeCommand(new SendMessage(super.getChatId(update), "Отправьте ссылку для прекращения отслеживания."));
            String urlStr = waitingUpdate(update, telegramBot);
            return handleDeleteUrl(update, urlStr, telegramBot);
        }
        return super.handleCommand(update, telegramBot);

    }
}
