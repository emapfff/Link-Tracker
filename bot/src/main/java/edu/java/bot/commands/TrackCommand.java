package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dto.AddLinkRequest;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.tools.LinkParser;
import edu.java.bot.tools.Resource;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final ScrapperClient scrapperClient;
    private final LinkParser linkParser;

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
        URI url = URI.create(update.message().text());
        if (linkParser.parse(url) == Resource.INCORRECT_URL) {
            return new SendMessage(chatId, "Указана неверная ссылка.\n"
                + "Требуются ссылки репозиториев Github или вопросов из StackOverflow");
        }
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(update.message().text()));
        return scrapperClient.addLink(chatId, addLinkRequest)
            .map(
                linkResponse -> new SendMessage(chatId, "Ссылка успешна добавлена")
            )
            .onErrorResume(
                throwable -> Mono.just(new SendMessage(chatId, throwable.getMessage()))
            )
            .block();
    }
}
