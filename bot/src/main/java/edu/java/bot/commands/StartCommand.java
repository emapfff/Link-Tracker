package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    @Autowired
    private ScrapperClient scrapperClient;

    @Override
    public String name() {
        return "/start";
    }

    @Override
    public String description() {
        return "зарегистрировать пользователя";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String userName = update.message().chat().firstName() + " " + update.message().chat().lastName();
        scrapperClient.registrationChat(chatId);
        return new SendMessage(chatId, "Добро пожаловать в worker бота, " + userName + "!");
    }
}
