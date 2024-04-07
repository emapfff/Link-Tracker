package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dto.AddLinkRequest;
import edu.java.bot.clients.ScrapperClient;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    @Autowired
    private ScrapperClient scrapperClient;

    @Override
    public String name() {
        return "/track";
    }

    @Override
    public String description() {
        return "начать отслеживание ссылки. \n"
            + "После введении команды напишите в отдельном сообщении ссылку, которую хотите отслеживать.";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(update.message().text()));
        scrapperClient.addLink(chatId, addLinkRequest).block();
        return new SendMessage(chatId, "Ссылка была успешно доабвлена");
    }
}
