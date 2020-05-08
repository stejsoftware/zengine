package com.stejsoftware.zengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.socket.engineio.server.EngineIoServer;

@Configuration
public class ServerConfig {

    @Bean
    public EngineIoServer engineIoServer() {
        return new EngineIoServer();
    }

}