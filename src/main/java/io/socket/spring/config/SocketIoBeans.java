package io.socket.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.socket.engineio.server.EngineIoServer;
import io.socket.spring.SocketIoServer;

@Configuration
public class SocketIoBeans {

    @Bean
    public EngineIoServer engineIoServer() {
        return new EngineIoServer();
    }

    @Bean
    public SocketIoServer socketIoServer(EngineIoServer server) {
        return new SocketIoServer(server);
    }

}