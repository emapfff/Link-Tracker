package edu.java.service;

import edu.java.dto.LinkUpdateRequest;

public interface NotificationSender {
    void send(LinkUpdateRequest linkUpdateRequest);
}
