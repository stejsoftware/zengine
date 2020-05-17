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

public class Namespace extends Emitter {
    private static final Logger        log     = LoggerFactory.getLogger(Namespace.class);
    private final List<Socket> sockets = new ArrayList<>();
    private final String               name;

    public Namespace(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addHandler(final String event, final Object bean, final Method method) {
        if (event != null && event.length() > 0) {
            log.debug("addHandler: \"{},{}\" -> {}", name, event, SocketIoBeans.toString(method));

            on(event, eventArgs -> {
                log.debug("event: {}", event);
                log.debug("  args:   ({})", SocketIoBeans.toString(eventArgs));
                log.debug("  params: ({})", SocketIoBeans.toString(method.getParameterTypes()));

                List<Parameter> paramList = Arrays.asList(method.getParameters());
                List<Object> eventArgList = Arrays.asList(eventArgs);

                for (Socket socket : sockets) {
                    List<Object> args = new ArrayList<Object>();

                    for (Parameter param : paramList) {
                        Object arg = null;

                        if (param.getType() == Socket.class) {
                            arg = socket;
                        }
                        else if (param.getType() == Ack.class) {
                            arg = new Ack<>();
                        }
                        else {
                            for (Object eventArg : eventArgList) {
                                if (eventArg != null) {
                                    if (eventArg.getClass() == param.getType()) {
                                        arg = eventArg;
                                    }
                                }
                            }
                        }

                        args.add(arg);
                    }

                    log.debug("  call:   {}.{}({})", method.getDeclaringClass().getSimpleName(), method.getName(), SocketIoBeans.toString(args));

                    try {
                        method.invoke(bean, args.toArray());
                    }
                    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        log.error("call: error:", ex);
                    }
                }
            });
        }
    }

    public void connect(final Socket socketIoSocket) {
        log.debug("connect: {} -> {}", socketIoSocket.getId(), this.name);
        sockets.add(socketIoSocket);
    }

    public void disconnect(Socket socketIoSocket) {
        log.debug("disconnect: {} -> {}", socketIoSocket.getId(), this.name);
        sockets.remove(socketIoSocket);
    }
}