package com.stejsoftware.zengine.controller;

import java.util.Arrays;

import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import io.socket.spring.SocketIoSocket;
import io.socket.spring.annotation.Namespace;
import io.socket.spring.annotation.OnConnect;
import io.socket.spring.annotation.OnDisconnect;
import io.socket.spring.annotation.OnEvent;

@Controller
public class SocketIoController {

    private static final Logger log = LoggerFactory.getLogger(SocketIoController.class);

    @OnConnect("/")
    public void connection(SocketIoSocket socket, final String namespace) {
        log.info("got connection: {} -> {}", namespace, socket.getId());
    }

    @OnConnect("/")
    public void connection2(final String namespace, SocketIoSocket socket) {
        log.info("got connection: {} -> {}", namespace, socket.getId());
    }

    @OnDisconnect("/")
    public void disconnection(SocketIoSocket socket, final String namespace) {
        log.info("lost connection: {} -> {}", namespace, socket.getId());
    }

    @OnEvent("list")
    public void getStoryList(SocketIoSocket socket) {
        socket.emit("story_list", Arrays.asList("zork.z5"));
    }

    @Namespace("/admin")
    @OnEvent("chat message")
    public void chatMessage(SocketIoSocket socket, String message) {
        log.info("chat message: {}", JSONStringer.valueToString(message));
        socket.emit("chat message", message);
    }
}