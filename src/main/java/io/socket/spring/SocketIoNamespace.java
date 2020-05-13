package io.socket.spring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.socket.emitter.Emitter;
import io.socket.spring.config.SocketIoBeans;

public class SocketIoNamespace extends Emitter {
    private static final Logger        log     = LoggerFactory.getLogger(SocketIoNamespace.class);
    private final List<SocketIoSocket> sockets = new ArrayList<>();
    private final String               name;

    public SocketIoNamespace(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addSocket(SocketIoSocket socket) {
        sockets.add(socket);
    }

    public void addHandler(String event, Object bean, Method method) {
        if (event != null && event.length() > 0) {
            log.info("addHandler: \"{}\" -> {}", event, SocketIoBeans.toString(method));

            on(event, eventArgs -> {
                log.debug("event: {}", event);
                log.debug("  args:   ({})", SocketIoBeans.toString(eventArgs));
                log.debug("  params: ({})", SocketIoBeans.toString(method.getParameterTypes()));

                List<Parameter> paramList = Arrays.asList(method.getParameters());
                List<Object> eventArgList = Arrays.asList(eventArgs);

                for (SocketIoSocket socket : sockets) {
                    List<Object> args = new ArrayList<Object>();

                    for (Parameter param : paramList) {
                        Object arg = null;

                        for (Object eventArg : eventArgList) {
                            if (param.getType() == SocketIoServer.class) {
                                arg = socket;
                            }
                            else {
                                if (eventArg != null) {
                                    if (eventArg.getClass() == param.getType()) {
                                        arg = eventArg;
                                    }
                                }
                            }
                        }

                        args.add(arg);
                    }

                    log.info("  call:   {}.{}({})", method.getDeclaringClass().getSimpleName(), method.getName(), SocketIoBeans.toString(args));

                    // try {
                    // method.invoke(bean, args.toArray());
                    // }
                    // catch (IllegalAccessException | IllegalArgumentException |
                    // InvocationTargetException ex) {
                    // log.error("call: error:", ex);
                    // }
                }
            });
        }
    }

}