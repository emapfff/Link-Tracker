package edu.java.service;

import dto.LinkUpdateRequest;

public interface NotificationSender {
    void send(LinkUpdateRequest linkUpdateRequest);
}
