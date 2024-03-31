package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dto.LinkResponse;
import dto.ListLinksResponse;
import edu.java.bot.clients.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListCommand implements Command {
    @Autowired
    private ScrapperClient scrapperClient;

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
