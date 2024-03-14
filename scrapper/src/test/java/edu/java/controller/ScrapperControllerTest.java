package edu.java.controller;

import dto.AddLinkRequest;
import dto.RemoveLinkRequest;
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
@WebMvcTest(ScrapperController.class)
class ScrapperControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    void registrationChatWithStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/{id}", 123))
            .andExpect(status().isOk());
    }

    @Test
    void registrationChatWithStatus400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/{id}", -1))
            .andExpect(status().isBadRequest());
    }

    @Test
    void removeChatWithStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-chat/{id}", 1))
            .andExpect(status().isOk());
    }

    @Test
    void removeChatWithStatus400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-chat/{id}", -1))
            .andExpect(status().isBadRequest());
    }

    @Test
    void removeChatWithStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-chat/{id}", 0))
            .andExpect(status().isNotFound());
    }

    @Test
    void getLinksWithStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/links?tgChatId=1"))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void getLinksWithStatus400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/links?tgChatId=-1"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void addLinkWithStatus200() throws Exception {
        AddLinkRequest addLinkRequest = new AddLinkRequest("https://link");

        mockMvc.perform(MockMvcRequestBuilders.post("/links?tgChatId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(addLinkRequest)))
            .andExpect(status().isOk());
    }

    @Test
    void addLinkWithStatus400LinkIncorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/links?tgChatId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void addLinkWithStatus400TgChatIdIncorrect() throws Exception {
        AddLinkRequest addLinkRequest = new AddLinkRequest("https://link");

        mockMvc.perform(MockMvcRequestBuilders.post("/links?tgChatId=-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(addLinkRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteLinkWithStatus200() throws Exception {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest("https://link");

        mockMvc.perform(MockMvcRequestBuilders.delete("/links?tgChatId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(removeLinkRequest)))
            .andExpect(status().isOk());
    }

    @Test
    void deleteLinkWithStatus400() throws Exception {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest("https://link");

        mockMvc.perform(MockMvcRequestBuilders.delete("/links?tgChatId=-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(removeLinkRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteLinkWithStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/links?tgChatId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isNotFound());
    }

}
