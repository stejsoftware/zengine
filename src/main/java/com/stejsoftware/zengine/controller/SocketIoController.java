package com.stejsoftware.zengine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoSocket;
import io.socket.parser.IOParser;
import io.socket.parser.Packet;
import io.socket.parser.Parser;

@Controller
public class SocketIoController {

    private static final Logger log     = LoggerFactory.getLogger(SocketIoController.class);
    private IOParser.Decoder    decoder = new IOParser.Decoder();

    SocketIoController(EngineIoServer server) {

        server.on("connection", args -> {
            final EngineIoSocket socket = (EngineIoSocket) args[0];

            log.info("connection: {}", socket.getId());

            send(socket, new Packet<>(Parser.CONNECT));

            decoder.onDecoded(packet -> {
                log.info("receive: {} {} {}", packet.type, packet.nsp, packet.data);

                if (packet.type == Parser.CONNECT) {
                    Packet<?> a = new Packet<>(Parser.CONNECT);
                    a.nsp = packet.nsp;
                    send(socket, a);
                }

                if (packet.type == Parser.EVENT) {
                    send(socket, packet);
                }
            });

            socket.on("data", items -> {
                for (Object item : items) {
                    decoder.add(item.toString());
                }
            });
        });
    }

    private <T> void send(EngineIoSocket socket, Packet<T> socketPacket) {
        log.info("send: {} {} {}", socketPacket.type, socketPacket.nsp, socketPacket.data);
        new IOParser.Encoder().encode(socketPacket, items -> {
            for (Object item : items) {
                log.info("out: {}", item);
                socket.send(new io.socket.engineio.parser.Packet<>(io.socket.engineio.parser.Packet.MESSAGE, item));
            }
        });
    }
}