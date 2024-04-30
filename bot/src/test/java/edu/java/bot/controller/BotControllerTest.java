package edu.java.bot.controller;

import dto.LinkUpdateRequest;
import edu.java.bot.service.Bot;
import edu.java.bot.service.UpdateService;
import java.net.URI;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BotController.class)
class BotControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private UpdateService updateService;

    @MockBean
    private Bot bot;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sendUpdates() throws Exception {
        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1L,
            new URI("http://localhost"),
            "test updates",
            Arrays.asList(1L, 2L, 3L)
        );
        doNothing().when(bot).executeCommand(any());
        doNothing().when(updateService).sendUpdate(linkUpdateRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(linkUpdateRequest)))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
