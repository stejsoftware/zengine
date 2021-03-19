package io.socket.spring;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.client.Ack;
import io.socket.engineio.server.EngineIoSocket;
import io.socket.parser.IOParser;
import io.socket.parser.Packet;
import io.socket.parser.Parser;

public class Socket {
    private static final Logger    log               = LoggerFactory.getLogger(Socket.class);

    private final EngineIoSocket   engineIoSocket;
    private final SocketIoServer   socketIoServer;

    private Namespace              socketIoNamespace = null;
    private Map<Integer, Ack>      acks              = new HashMap<>();

    private final IOParser.Decoder decoder           = new IOParser.Decoder();
    private final IOParser.Encoder encoder           = new IOParser.Encoder();

    Socket(final EngineIoSocket engineIoSocket, final SocketIoServer socketIoServer) {
        this.engineIoSocket = engineIoSocket;
        this.socketIoServer = socketIoServer;

        decoder.onDecoded(packet -> {
            log.debug("[{}] receive: {}({}) {} {}", getId(), Parser.types[packet.type], packet.type, packet.nsp, packet.data);

            switch (packet.type) {
            case Parser.CONNECT:
                connect(packet.nsp);
                break;

            case Parser.DISCONNECT:
                disconnect(packet.nsp);
                break;

            case Parser.EVENT:
            case Parser.BINARY_EVENT:
                event((Packet<?>) packet);
                break;

            case Parser.ACK:
            case Parser.BINARY_ACK:
                ack((Packet<?>) packet);
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

        Namespace nsp = socketIoServer.getNamespace(namespace);

        if (nsp != null) {
            if (socketIoNamespace != null) {
                socketIoNamespace.disconnect(this);
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

    public void disconnect(final String namespace) {

    }

    public <T> void event(final Packet<T> packet) {
        try {
            if (packet.id >= 0) {
                log.debug("packet.id: {}", packet.id);

                // create an ack callback
                acks.put(packet.id, ack -> {
                    log.debug("callback: {}", JSONStringer.valueToString(ack));
                });
            }

        }
        catch (JSONException ex) {
            log.error("error", ex);
        }
    }

    public <T> void ack(final Packet<T> packet) {

    }

    public void emit(String event, Object data) {
        packet(Parser.EVENT, socketIoNamespace.getName(), Arrays.asList(event, data));
    }

    public void emit(String event, Ack ack) {

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