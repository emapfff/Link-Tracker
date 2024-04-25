package edu.java.bot.service;

import dto.LinkUpdateRequest;

public interface Listener {
    void listen(LinkUpdateRequest linkUpdateRequest);
}
