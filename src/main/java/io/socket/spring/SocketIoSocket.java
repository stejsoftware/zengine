package io.socket.spring;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.engineio.server.EngineIoSocket;
import io.socket.parser.IOParser;
import io.socket.parser.Packet;
import io.socket.parser.Parser;

public class SocketIoSocket {
    private static final Logger    log               = LoggerFactory.getLogger(SocketIoSocket.class);

    private final EngineIoSocket   engineIoSocket;
    private final SocketIoServer   socketIoServer;

    private SocketIoNamespace      socketIoNamespace = null;

    private final IOParser.Decoder decoder           = new IOParser.Decoder();
    private final IOParser.Encoder encoder           = new IOParser.Encoder();

    SocketIoSocket(final EngineIoSocket engineIoSocket, final SocketIoServer socketIoServer) {
        this.engineIoSocket = engineIoSocket;
        this.socketIoServer = socketIoServer;

        decoder.onDecoded(packet -> {
            log.debug("[{}] receive: {}({}) {} {}", getId(), Parser.types[packet.type], packet.type, packet.nsp, packet.data);

            switch (packet.type) {
            case Parser.CONNECT:
                connect(packet.nsp);
                break;

            case Parser.EVENT:
                try {
                    JSONArray json = new JSONArray(packet.data.toString());
                    socketIoNamespace.emit(json.get(0).toString(), json.opt(1));
                }
                catch (JSONException ex) {
                    log.error("error", ex);
                }
                break;

            default:
                log.debug("unknown packet: {}({}) {}", Parser.types[packet.type], packet.type, packet.nsp);
            }
        });

        engineIoSocket.on("data", items -> {
            for (final Object item : items) {
                decoder.add(item.toString());
            }
        });

        engineIoSocket.once("close", args -> {
            log.info("websocket closed: [{}]", engineIoSocket.getId());
            
            if (socketIoNamespace != null) {
                socketIoNamespace.disconnect(this);
            }
        });
    }

    public String getId() {
        return engineIoSocket.getId();
    }

    public void connect(final String namespace) {
        log.debug("connect: {} -> {}", getId(), namespace);

        SocketIoNamespace nsp = socketIoServer.getNamespace(namespace);

        if (nsp != null) {
            if (socketIoNamespace != null) {
                socketIoNamespace.disconnect(this);

                // packet(Parser.DISCONNECT);
            }

            socketIoNamespace = nsp;
            socketIoNamespace.connect(this);

            packet(Parser.CONNECT);
        }
        else {
            log.warn("connect: unknown namespace: {}", namespace);

            packet(Parser.ERROR, namespace, "Invalid namespace");
        }
    }

    public void emit(String event, Object data) {
        packet(Parser.EVENT, socketIoNamespace.getName(), Arrays.asList(event, data));
    }

    void packet(int type) {
        packet(type, socketIoNamespace.getName(), null);
    }

    <T> void packet(final int type, final String namespace, final T data) {
        Packet<String> packet = new Packet<>();

        packet.type = type;
        packet.nsp = namespace;
        packet.data = data != null ? JSONStringer.valueToString(data) : null;

        packet(packet);
    }

    void packet(Packet<?> socketPacket) {
        log.debug("[{}] send: {}({}) {} {}", engineIoSocket.getId(), Parser.types[socketPacket.type], socketPacket.type, socketPacket.nsp, socketPacket.data);

        encoder.encode(socketPacket, items -> {
            for (Object item : items) {
                engineIoSocket.send(new io.socket.engineio.parser.Packet<>(io.socket.engineio.parser.Packet.MESSAGE, item));
            }
        });
    }
}