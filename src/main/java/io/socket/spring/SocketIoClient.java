package io.socket.spring;

import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.engineio.server.EngineIoSocket;
import io.socket.parser.IOParser;
import io.socket.parser.Packet;
import io.socket.parser.Parser;

public class SocketIoClient {
    private static final Logger           log     = LoggerFactory.getLogger(SocketIoClient.class);

    private SocketIoServer                server;
    private EngineIoSocket                engine;

    private static final IOParser.Decoder decoder = new IOParser.Decoder();
    private static final IOParser.Encoder encoder = new IOParser.Encoder();

    public SocketIoClient(final SocketIoServer socketIoserver, final EngineIoSocket engineIoSocket) {
        server = socketIoserver;
        engine = engineIoSocket;

        // socket.on("data", data -> {
        // log.info("data: [{}]", data);
        // });

        // // send(socket, new Packet<>(Parser.CONNECT));
        // send(socket, new Packet<>(Parser.ERROR, "not implemented"));

        decoder.onDecoded(packet -> {
            log.info("receive: {}({}) {} '{}'", Parser.types[packet.type], packet.type, packet.nsp, packet.data);

            if (packet.type == Parser.CONNECT) {
                connect(packet.nsp);
            }

            if (packet.type == Parser.EVENT) {
                packet(packet);
            }
        });

        engine.on("data", items -> {
            for (Object item : items) {
                decoder.add(item.toString());
            }
        });

    }

    public String getId() {
        return engine.getId();
    }

    public void packet(int type, String nsp) {
        packet(type, nsp, null);
    }

    public <T> void packet(int type, String nsp, T data) {
        Packet<String> packet = new Packet<>();

        packet.type = type;
        packet.nsp = nsp;
        packet.data = data != null ? JSONStringer.valueToString(data) : null;

        packet(packet);
    }

    public void packet(Packet<?> socketPacket) {
        log.info("send: {}({}) {} {}", Parser.types[socketPacket.type], socketPacket.type, socketPacket.nsp, socketPacket.data);

        encoder.encode(socketPacket, items -> {
            for (Object item : items) {
                engine.send(new io.socket.engineio.parser.Packet<>(io.socket.engineio.parser.Packet.MESSAGE, item));
            }
        });
    }

    public void connect(String namespace) {
        SocketIoNamespace nsp = server.getNamespace(namespace);

        if (nsp != null) {
            packet(Parser.CONNECT, namespace);
        }
        else {
            packet(Parser.ERROR, namespace, "Invalid namespace");
        }
    }
}