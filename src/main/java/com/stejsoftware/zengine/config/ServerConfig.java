package com.stejsoftware.zengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoServer;

@Configuration
public class ServerConfig {

    @Bean
    public EngineIoServer engineIoServer() {
        return new EngineIoServer();
    }

    @Bean
    public SocketIoServer socketIoServer() {
        return new SocketIoServer(engineIoServer());
    }
}