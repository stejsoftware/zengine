package io.socket.spring;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoSocket;

public class SocketIoServer {
    private static final Logger                  log  = LoggerFactory.getLogger(SocketIoServer.class);

    private final Map<String, SocketIoNamespace> nsps = new HashMap<>();

    public SocketIoServer(final EngineIoServer server) {
        addNamespace("/");

        server.on("connection", sockets -> {

            EngineIoSocket socket = (EngineIoSocket) sockets[0];
            SocketIoClient client = new SocketIoClient(this, socket);

            log.info("connection: [{}]", client.getId());

            client.connect("/");
        });
    }

    public SocketIoNamespace getNamespace(final String name) {
        return this.nsps.get(name);
    }

    public SocketIoNamespace addNamespace(final String name) {
        return this.nsps.put(name, new SocketIoNamespace(name));
    }

}