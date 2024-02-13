package edu.java.bot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class trackCommand extends Command{
    public trackCommand(Command nextCommand) {
        super(nextCommand);
    }

    @Override
    public SendMessage handleCommand(Update update, Bot telegramBot) {
        if (getCommand(update).equals("/track")){
            telegramBot.executeCommand(new SendMessage(super.getChatId(update), "Отправьте ссылку для отслеживания."));
            String urlStr = waitingUpdate(update, telegramBot);
            return handleAddUrl(update, urlStr, telegramBot);
        }
        return super.handleCommand(update, telegramBot);

    }

}
