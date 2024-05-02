package edu.java.bot.service;

import edu.java.bot.dto.LinkUpdateRequest;

public interface Listener {
    void listen(LinkUpdateRequest linkUpdateRequest);
}
