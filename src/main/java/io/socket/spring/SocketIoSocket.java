package io.socket.spring;

import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.emitter.Emitter;
import io.socket.engineio.server.EngineIoSocket;
import io.socket.parser.IOParser;
import io.socket.parser.Packet;
import io.socket.parser.Parser;

public class SocketIoSocket extends Emitter {
    private static final Logger           log     = LoggerFactory.getLogger(SocketIoSocket.class);

    private final EngineIoSocket          socket;

    private static final IOParser.Decoder decoder = new IOParser.Decoder();
    private static final IOParser.Encoder encoder = new IOParser.Encoder();

    public SocketIoSocket(final EngineIoSocket engineIoSocket) {
        socket = engineIoSocket;

        decoder.onDecoded(packet -> {
            log.info("[{}] receive: {}({}) {} '{}'", socket.getId(), Parser.types[packet.type], packet.type, packet.nsp, packet.data);
            emit("packet", packet);
        });

        socket.on("data", items -> {
            for (Object item : items) {
                decoder.add(item.toString());
            }
        });
    }

    public String getId() {
        return socket.getId();
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
        log.info("[{}] send: {}({}) {} {}", socket.getId(), Parser.types[socketPacket.type], socketPacket.type, socketPacket.nsp, socketPacket.data);

        encoder.encode(socketPacket, items -> {
            for (Object item : items) {
                socket.send(new io.socket.engineio.parser.Packet<>(io.socket.engineio.parser.Packet.MESSAGE, item));
            }
        });
    }
}