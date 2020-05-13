package io.socket.spring;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.emitter.Emitter;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoSocket;
import io.socket.spring.annotation.Namespace;

public class SocketIoServer extends Emitter {
    private static final Logger                  log  = LoggerFactory.getLogger(SocketIoServer.class);

    private final Map<String, SocketIoNamespace> nsps = new HashMap<>();

    public SocketIoServer(final EngineIoServer server) {
        server.on("connection", sockets -> {

            EngineIoSocket engineSocket = (EngineIoSocket) sockets[0];

            SocketIoSocket socket = new SocketIoSocket(engineSocket);
            SocketIoClient client = new SocketIoClient(this, socket);

            log.info("websocket connection: [{}]", engineSocket.getId());

            // connect to the default namesapce
            client.connect("/");
        });
    }

    public SocketIoNamespace getNamespace(final String name) {
        return this.nsps.get(name);
    }

    public void addHandler(Namespace namespace, String event, Object bean, Method method) {
        String namespaceValue = "/";

        if (namespace != null) {
            namespaceValue = namespace.value();
        }

        if (!nsps.containsKey(namespaceValue)) {
            nsps.put(namespaceValue, new SocketIoNamespace(namespaceValue));
        }

        nsps.get(namespaceValue).addHandler(event, bean, method);
    }
}