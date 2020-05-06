package com.stejsoftware.zengine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import io.socket.emitter.Emitter;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;

@Controller
public class SocketIoController {

    private final Logger log = LoggerFactory.getLogger(SocketIoController.class);

    SocketIoController(final SocketIoServer socketIoServer) {
        socketIoServer.namespace("/").on("connection", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                SocketIoSocket socket = (SocketIoSocket) args[0];
                socket.send("chat message", "Hello Socket.IO");
            }
        }).on("chat message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                log.debug(">>>> {}", args);
            }
        }).on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                log.debug(">>>> {}", args);
            }
        });

    }

}