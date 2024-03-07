package edu.java.bot.controller;

import dto.LinkUpdateRequest;
import exceptions.IncorrectParametersExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class BotController {
    @PostMapping("/updates")
    @Operation(summary = "Отправить обновление", description = "Обновление обработано")
    @ApiResponse(responseCode = "200", description = "Обновление обработано")
    public Mono<Void> sendUpdates(@RequestBody LinkUpdateRequest linkUpdateRequest)
        throws IncorrectParametersExceptions {
        if (linkUpdateRequest == null) {
            throw new IncorrectParametersExceptions("");
        }
        log.info("Send updates!");
        return Mono.empty();
    }
}
