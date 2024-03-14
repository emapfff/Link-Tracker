package edu.java.bot.controller;

import dto.LinkUpdateRequest;
import java.net.URI;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BotController.class)
class BotControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    void sendUpdates() throws Exception {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1,
            new URI("http://localhost"),
            "test updates",
            Arrays.asList(1, 2, 3)
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(linkUpdateRequest)))
            .andExpect(status().isOk())
            .andDo(print());

    }
}
