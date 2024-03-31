package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
        return scrapperClient.registrationChat(chatId)
            .then(Mono.just(new SendMessage(chatId, "Чат успешно добавлен")))
            .onErrorResume(throwable -> Mono.just(new SendMessage(chatId, throwable.getMessage())))
            .block();
    }
}
