package com.stejsoftware.zengine.controller;

import com.stejsoftware.zengine.data.GameRepository;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoServerOptions;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoServerOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ControllerConfig {
    @Value("${zengine.story.folder}")
    private String storyFolder;
    private final GameRepository gameRepository;

    public ControllerConfig(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public String getStoryFolder() {
        return storyFolder;
    }

    public GameRepository getGameRepository() {
        return gameRepository;
    }

    @Bean
    public EngineIoServerOptions engineIoServerOptions() {
        return EngineIoServerOptions.newFromDefault()
                .setAllowedCorsOrigins(List.of(
                        "http://localhost:8080",
                        "http://localhost:3000"
                ).toArray(String[]::new));
    }

    @Bean
    public EngineIoServer engineIoServer(EngineIoServerOptions engineIoServerOptions) {
        return new EngineIoServer(engineIoServerOptions);
    }

    @Bean
    public SocketIoServerOptions socketIoServerOptions() {
        return SocketIoServerOptions.newFromDefault();
    }

    @Bean
    public SocketIoServer socketIoServer(EngineIoServer engineIoServer, SocketIoServerOptions socketIoServerOptions) {
        return new SocketIoServer(engineIoServer, socketIoServerOptions);
    }
}
