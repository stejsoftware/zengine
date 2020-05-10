package com.stejsoftware.zengine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import io.socket.spring.SocketIoSocket;
import io.socket.spring.annotation.OnConnect;
import io.socket.spring.annotation.OnEvent;

@Controller
public class SocketIoController {

    private static final Logger log = LoggerFactory.getLogger(SocketIoController.class);

    @OnConnect("/")
    public void connection(SocketIoSocket socket) {
        log.info("got connection");
    }

    @OnEvent("chat message")
    public void chat_message1(Object data) {
        log.info("chat message 1: {}", data);
    }

    @OnEvent(event = "chat message")
    public void chat_message2(Object data) {
        log.info("chat message 2: {}", data);
    }

    @OnEvent(event = "chat message", namespace = "/bar")
    public void bar_chat_message(Object data) {
        log.info("chat message for bar: {}", data);
    }

    @OnEvent(event = "foo")
    public void foo(Object data) {
        log.info("foo: {}", data);
    }

    @OnEvent(event = "foo", namespace = "/bar")
    public void bar_foo(Object data) {
        log.info("foo for bar: {}", data);
    }
}