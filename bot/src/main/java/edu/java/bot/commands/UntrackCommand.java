package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dto.AddLinkRequest;
import dto.RemoveLinkRequest;
import edu.java.bot.tools.LinkParse;
import edu.java.bot.clients.ScrapperClient;
import java.net.URI;
import edu.java.bot.tools.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UntrackCommand implements Command {
    @Autowired
    private ScrapperClient scrapperClient;
    @Autowired
    private LinkParse linkParse;

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
        URI url = URI.create(update.message().text());
        if (linkParse.parse(url) == Urls.INCORRECT_URL) {
            return new SendMessage(chatId, "Указана неверная ссылка." +
                "\nТребуются ссылки репозитория Github или вопроса из StackOverflow");
        }
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(URI.create(update.message().text()));
        return scrapperClient.deleteLink(chatId, removeLinkRequest)
            .map(
                linkResponse -> new SendMessage(chatId, "Ссылка успешна удалена")
            )
            .onErrorResume(
                throwable -> Mono.just(new SendMessage(chatId, throwable.getMessage()))
            )
            .block();
    }
}
