package io.socket.spring;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.parser.Packet;
import io.socket.parser.Parser;

public class SocketIoClient {
    private static final Logger  log = LoggerFactory.getLogger(SocketIoClient.class);

    private final SocketIoServer server;
    private final SocketIoSocket socket;

    public SocketIoClient(final SocketIoServer socketIoserver, final SocketIoSocket socketIoSocket) {
        server = socketIoserver;
        socket = socketIoSocket;

        socket.on("packet", args -> {
            Packet<?> packet = (Packet<?>) args[0];

            switch (packet.type) {
            case Parser.CONNECT:
                connect(packet.nsp);
                break;

            case Parser.EVENT:
                try {
                    JSONArray json = new JSONArray(packet.data.toString());
                    emitEvent(packet.nsp, json.get(0).toString(), json.opt(1));
                }
                catch (JSONException ex) {
                    log.error(ex.getMessage());
                }
                break;

            default:
                log.info("unknown packet: {}({}) {}", Parser.types[packet.type], packet.type, packet.nsp);
            }

        });
    }

    protected <T> void emitEvent(String namespace, String event, T data) {
        log.info("emitEvent: {} {} {}", namespace, JSONStringer.valueToString(event), JSONStringer.valueToString(data));

        SocketIoNamespace nsp = server.getNamespace(namespace);

        if (nsp != null) {
            nsp.emit(event, data);
        }
    }

    public void connect(String namespace) {
        log.info("connect: {}", namespace);

        SocketIoNamespace nsp = server.getNamespace(namespace);

        if (nsp != null) {
            socket.packet(Parser.CONNECT, namespace);
            nsp.emit("connect", namespace);
        }
        else {
            log.warn("connect: unknown namespace: {}", namespace);
            socket.packet(Parser.ERROR, namespace, "Invalid namespace");
        }
    }

}