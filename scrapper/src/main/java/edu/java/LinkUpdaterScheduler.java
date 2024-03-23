package edu.java;

import edu.java.service.scheduler.LinkUpdaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class LinkUpdaterScheduler {
    @Autowired
    private LinkUpdaterService linkUpdaterService;

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        //linkUpdaterService.checkUpdates();
        log.info("Update");
    }
}
