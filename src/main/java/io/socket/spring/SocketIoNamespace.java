package io.socket.spring;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.emitter.Emitter;

public class SocketIoNamespace extends Emitter {
    private static final Logger log = LoggerFactory.getLogger(SocketIoNamespace.class);
    private final String        name;

    public SocketIoNamespace(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addHandler(String event, Object bean, Method method) {
        if (event != null && event.length() > 0) {
            log.info("addHandler: \"{}\" -> {}.{}", event, bean.getClass().getSimpleName(), method.getName());

            this.listeners(event).add(new EventHandler<>(bean, method));
        }
    }

}