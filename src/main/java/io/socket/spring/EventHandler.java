package io.socket.spring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.emitter.Emitter.Listener;

public class EventHandler<T> implements Listener {
    private static final Logger log = LoggerFactory.getLogger(SocketIoServer.class);
    private final T             bean;
    private final Method        method;

    public EventHandler(T bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public Method getMethod() {
        return this.method;
    }

    @Override
    public void call(Object... args) {
        try {
            log.info("call: {}.{}", bean.getClass().getSimpleName(), method.getName());
            method.invoke(bean, args);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            log.error(ex.getMessage());
        }
    }
}