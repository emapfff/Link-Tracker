package edu.java.controller;

import dto.AddLinkRequest;
import dto.RemoveLinkRequest;
import edu.java.domain.dto.LinkDto;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcTgChatService;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ScrapperController.class)
class ScrapperControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JdbcLinkService jdbcLinkService;

    @MockBean
    private JdbcTgChatService jdbcTgChatService;

    @Test
    void registrationChatWithStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/{id}", 123))
            .andExpect(status().isOk());
    }

    @Test
    void removeChatWithStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-chat/{id}", 1))
            .andExpect(status().isOk());
    }

    @Test
    void getLinksWithStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/links?tgChatId=1"))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void addLinkWithStatus200() throws Exception {
        AddLinkRequest addLinkRequest = new AddLinkRequest(new URI("https://link"));
        when(jdbcLinkService.add(any(), any(), any())).thenReturn(new LinkDto());
        mockMvc.perform(MockMvcRequestBuilders.post("/links?tgChatId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(addLinkRequest)))
            .andExpect(status().isOk());
    }

    @Test
    void deleteLinkWithStatus200() throws Exception {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(new URI("https://link"));

        when(jdbcLinkService.remove(any(), any())).thenReturn(new LinkDto());
        mockMvc.perform(MockMvcRequestBuilders.delete("/links?tgChatId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(removeLinkRequest)))
            .andExpect(status().isOk());
    }
}
