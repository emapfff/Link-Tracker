package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dto.RemoveLinkRequest;
import edu.java.bot.clients.ScrapperClient;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    @Autowired
    private ScrapperClient scrapperClient;

    @Override
    public String name() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(URI.create(update.message().text()));
        scrapperClient.deleteLink(chatId, removeLinkRequest).block();
        return new SendMessage(chatId, "Ссылка была успешно доабвлена");

    }
}
