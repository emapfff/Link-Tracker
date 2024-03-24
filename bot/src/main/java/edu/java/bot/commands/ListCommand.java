package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    @Autowired
    private ScrapperClient scrapperClient;
    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chaId = update.message().chat().id();
        return null;
    }
}
