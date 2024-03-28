package edu.java.controller;

import dto.AddLinkRequest;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.domain.dto.LinkDto;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcTgChatService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @MockBean
    private GitHubClient gitHubClient;

    @MockBean
    private StackOverflowClient stackOverflowClient;
    @InjectMocks
    private ScrapperController scrapperController;

    @Rollback
    @Test
    void registrationChatWithStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tg-chat/{id}", 123))
            .andExpect(status().isOk());
    }

    @Rollback
    @Test
    void removeChatWithStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tg-chat/{id}", 1))
            .andExpect(status().isOk());
    }

    @Rollback
    @Test
    void getLinksWithStatus200() throws Exception {
        List<LinkDto> linkDtoList = new ArrayList<>();
        LinkDto link1 = new LinkDto(1L, URI.create("http://mycore"), OffsetDateTime.now() );
        linkDtoList.add(link1);
        when(jdbcLinkService.listAll(anyLong())).thenReturn(linkDtoList);


        mockMvc.perform(MockMvcRequestBuilders.get("/links")
                .header("Tg-Chat-Id", 1234)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Rollback
    @Test
    void addLinkWithStatus200() throws Exception {
        AddLinkRequest addLinkRequest = new AddLinkRequest(new URI("https://link"));
        LinkDto link1 = new LinkDto(1L, URI.create("http://link"), OffsetDateTime.now());
        when(jdbcLinkService.add(anyLong(), any())).thenReturn(link1);

        mockMvc.perform(MockMvcRequestBuilders.post("/links")
                .header("Tg-Chat-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(addLinkRequest))
            )
            .andExpect(status().isOk());

    }

    @Rollback
    @Test
    void deleteLinkWithStatus200() throws Exception {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(new URI("https://link"));
        LinkDto link1 = new LinkDto(1L, URI.create("https://link"), OffsetDateTime.now());
        when(jdbcLinkService.remove(anyLong(), any())).thenReturn(link1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/links?")
                .header("Tg-Chat-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(removeLinkRequest)))
            .andExpect(status().isOk());
    }
}
