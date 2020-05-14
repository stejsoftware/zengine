package io.socket.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.socket.spring.annotation.OnConnect;
import io.socket.spring.annotation.OnDisconnect;

@Component
public class SocketIoHandler {
    Logger log = LoggerFactory.getLogger(SocketIoHandler.class);

    @OnConnect("/")
    public void defaultConnectHandler(final SocketIoSocket socket) {
        log.debug("connect to namespace: / ({})", socket);
    }

    @OnDisconnect("/")
    public void defaultDisconnectHandler(final SocketIoSocket socket) {
        log.debug("disconnect from namespace: / ({})", socket);
    }
}