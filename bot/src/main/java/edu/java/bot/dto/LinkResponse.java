package edu.java.bot.dto;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {}
