package edu.java;

import edu.java.service.LinkUpdaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final LinkUpdaterService linkUpdaterService;

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        linkUpdaterService.checkUpdates();
        log.info("Update");
    }
}
