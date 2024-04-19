package edu.java.bot.service;

import dto.LinkUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BotQueueConsumer {
    @KafkaListener(
        topics = "${app.scrapper-topic.name}",
        groupId = "group_id",
        containerFactory = "listenerContainerFactory"
    )
    public void listen(@Payload @NotNull LinkUpdateRequest linkUpdateRequest, @NotNull Acknowledgment acknowledgment) {
        log.info("{} {} {}", linkUpdateRequest.description(), linkUpdateRequest.url(), linkUpdateRequest.id());
        acknowledgment.acknowledge();
    }
}
