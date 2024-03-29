package com.stejsoftware.zengine.controller;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import io.socket.client.Ack;
import io.socket.spring.Socket;
import io.socket.spring.annotation.Of;
import io.socket.spring.annotation.OnConnect;
import io.socket.spring.annotation.OnDisconnect;
import io.socket.spring.annotation.OnEvent;

@Controller
public class SocketIoController {

    private static final Logger log = LoggerFactory.getLogger(SocketIoController.class);

    @OnConnect("/")
    public void connection(Socket socket, final String namespace) {
        log.info("got connection: {} -> {}", namespace, socket.getId());
    }

    @OnConnect("/")
    public void connection2(final String namespace, Socket socket) {
        log.info("got connection: {} -> {}", namespace, socket.getId());
    }

    @OnDisconnect("/")
    public void disconnection(Socket socket, final String namespace) {
        log.info("lost connection: {} -> {}", namespace, socket.getId());
    }

    @OnEvent("list")
    public void getStoryList(Ack ack) {
        if (ack != null) {
            ack.call(Arrays.asList("zork", "zorkII", "zorkIII"));
        }
    }

    @OnEvent("add")
    public void add(JSONArray operands, Ack callback) {
        Integer a = operands.getInt(0);
        Integer b = operands.getInt(1);

        if (callback != null) {
            callback.call(a + b);
        }
    }

    @Of("/admin")
    @OnEvent("chat message")
    public void chatMessage(Socket socket, String message) {
        log.info("chat message: {}", JSONStringer.valueToString(message));
        socket.emit("chat message", message);
    }
}