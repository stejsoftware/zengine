package io.socket.spring;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.emitter.Emitter;
import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoSocket;
import io.socket.spring.annotation.Namespace;

public class SocketIoServer extends Emitter {
    private static final Logger                  log        = LoggerFactory.getLogger(SocketIoServer.class);

    private final Map<String, SocketIoNamespace> namespaces = new HashMap<>();

    public SocketIoServer(final EngineIoServer server) {
        server.on("connection", sockets -> {
            EngineIoSocket engineIoSocket = (EngineIoSocket) sockets[0];
            SocketIoSocket socket = new SocketIoSocket(engineIoSocket, this);

            log.debug("websocket connection: [{}]", engineIoSocket.getId());

            // connect to the default namesapce
            socket.connect("/");
        });
    }

    public SocketIoNamespace getNamespace(final String name) {
        return namespaces.get(name);
    }

    public void addHandler(final Namespace namespace, final String event, final Object bean, final Method method) {
        String namespaceValue = "/";

        if (namespace != null) {
            namespaceValue = namespace.value();
        }

        if (!namespaces.containsKey(namespaceValue)) {
            namespaces.put(namespaceValue, new SocketIoNamespace(namespaceValue));
        }

        namespaces.get(namespaceValue).addHandler(event, bean, method);
    }

    public <T> void emit(final String namespace, final String event, final T data) {
        log.debug("server emit: {} {} {}", namespace, JSONStringer.valueToString(event), JSONStringer.valueToString(data));

        if (namespaces.containsKey(namespace)) {
            namespaces.get(namespace).emit(event, data);
        }
        else {
            log.warn("namespace not found: {}", namespace);
        }
    }

}