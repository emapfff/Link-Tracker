package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.dto.LinkResponse;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String name() {
        return "/list";
    }

    @Override
    public String description() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        return scrapperClient.getLinks(chatId)
            .map(
                listLinksResponse -> {
                    if (listLinksResponse.size() == 0) {
                        return new SendMessage(chatId, "Отсутсвуют отслеживаемые ссылки, пожалуйста, добавьте их.");
                    } else {
                        List<URI> urls = listLinksResponse.links().stream().map(LinkResponse::url).toList();
                        return new SendMessage(
                            chatId,
                            urls.stream()
                                .map(URI::toString)
                                .collect(Collectors.joining("\n"))
                        );
                    }
                }
            )
            .onErrorResume(
                throwable -> Mono.just(new SendMessage(chatId, throwable.getMessage()))
            )
            .block();

    }
}
