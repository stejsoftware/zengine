package com.stejsoftware.zengine.controller;

import com.stejsoftware.zengine.ZEngine;
import com.stejsoftware.zengine.data.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ZEngineRestController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ZEngineRestControllerRequestTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ZEngine mockZEngine;
    @MockBean
    private ControllerConfig mockControllerConfig;

    @Test
    void stories() throws Exception {
        when(mockControllerConfig.getStoryFolder())
                .thenReturn("");

        mockMvc.perform(get("/v0/stories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void listGames() throws Exception {
        when(mockZEngine.listGames())
                .thenReturn(List.of(Game.builder().build()));

        mockMvc.perform(get("/v0/games"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void startGame() throws Exception {
        when(mockZEngine.startGame(any(Game.class)))
                .thenReturn(Game.builder().build());

        mockMvc.perform(post("/v0/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getGame() throws Exception {
        File story = new File("story.z3");

        when(mockZEngine.getGame(anyString()))
                .thenReturn(Game.builder()
                        .id("1")
                        .storyFile(story)
                        .name("name")
                        .statusMessage("at the house")
                        .gameScore(1)
                        .turnsTaken(2)
                        .build());

        mockMvc.perform(get("/v0/games/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.storyFile").value(story.getAbsolutePath()))
                .andExpect(jsonPath("$.name").value("name"))
        ;
    }

    @Test
    void listMoves() {
    }

    @Test
    void executeMove() {
    }

    @Test
    void getMove() {
    }
}