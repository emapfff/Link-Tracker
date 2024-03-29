package edu.java.controller;

import dto.AddLinkRequest;
import dto.RemoveLinkRequest;
import edu.java.domain.dto.LinkDto;
import edu.java.exceptions.IncorrectParametersException;
import edu.java.exceptions.LinkNotFoundException;
import edu.java.service.LinkService;
import edu.java.service.TgChatService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)
@WebMvcTest(ScrapperController.class)
class ScrapperControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkService linkService;

    @MockBean
    private TgChatService tgChatService;
    @InjectMocks
    private ScrapperController scrapperController;

    @Test
    void registrationChatWithStatus200() throws Exception {
        mockMvc.perform(post("/tg-chat/{id}", 123))
            .andExpect(status().isOk());
    }

    @Test
    void registrationChatWithStatus4xx() throws Exception {
        mockMvc.perform(post("/tg-chat/{id}", ""))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void removeChatWithStatus200() throws Exception {
        mockMvc.perform(delete("/tg-chat/{id}", 123L))
            .andExpect(status().isOk());
    }

    @Test
    void removeChatWithStatus4xx() throws Exception {
        mockMvc.perform(delete("/tg-chat/{id}", ""))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void getLinksWithStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/links").header("Tg-Chat-Id", 1))
            .andExpect(status().isOk());
    }

    @Test
    void getLinksWithStatus400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/links").header("Tg-Chat-Id", ""))
            .andExpect(status().isBadRequest());
    }
    @Test
    void addLinkWithStatus200() throws Exception {
        AddLinkRequest addLinkRequest = new AddLinkRequest(new URI("https://link"));
        LinkDto linkDto = new LinkDto(1L, URI.create("https://example.com"), null);
        when(linkService.add(eq(123L), any(URI.class))).thenReturn(linkDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/links")
                .header("Tg-Chat-Id", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(addLinkRequest)))
            .andExpect(status().isOk());
    }

    @Test
    void addLinkWithStatus400() throws Exception {
        when(linkService.add(eq(123L), any(URI.class))).thenThrow(new IncorrectParametersException("Invalid link"));

        mockMvc.perform(post("/links")
                .header("Tg-Chat-Id", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("link", "invalid_link"))))
            .andExpect(status().isBadRequest());

    }
    @Test
    void deleteLinkWithStatus200() throws Exception {
        LinkDto linkDto = new LinkDto(1L, URI.create("https://example.com"), null);
        when(linkService.remove(eq(123L), any(URI.class))).thenReturn(linkDto);

        mockMvc.perform(delete("/links")
                .header("Tg-Chat-Id", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("link", "https://example.com"))))
            .andExpect(status().isOk());
    }

    @Test
    void deleteLink_NonExistentLink() throws Exception {
        when(linkService.remove(eq(123L), any(URI.class))).thenThrow(new LinkNotFoundException("Link not found"));

        mockMvc.perform(delete("/links")
                .header("Tg-Chat-Id", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("link", "https://example.com"))))
            .andExpect(status().isNotFound());
    }


}
