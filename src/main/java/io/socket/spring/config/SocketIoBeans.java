package io.socket.spring.config;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.socket.engineio.server.EngineIoServer;
import io.socket.spring.SocketIoServer;

@Configuration
public class SocketIoBeans {

    @Bean
    public EngineIoServer engineIoServer() {
        return new EngineIoServer();
    }

    @Bean
    public SocketIoServer socketIoServer(final EngineIoServer server) {
        return new SocketIoServer(server);
    }

    public static <T> String toString(T[] array) {
        StringBuilder sb = new StringBuilder();

        for (T item : array) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            if (item != null) {
                sb.append(item.getClass().getSimpleName());
            }
            else {
                sb.append("null");
            }
        }

        return sb.toString();
    }

    public static String toString(Method method) {

        StringBuilder sb = new StringBuilder();

        for (Class<?> param : method.getParameterTypes()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            if (param != null) {
                sb.append(param.getSimpleName());
            }
            else {
                sb.append("null");
            }
        }

        return new StringBuilder().append(method.getDeclaringClass().getSimpleName()).append('.').append(method.getName()).append("( ").append(sb).append(" ) ").toString();
    }

    public static String toString(List<Object> list) {
        return toString(list.toArray());
    }

    public static <T> String toString(Class<T>[] array) {
        StringBuilder sb = new StringBuilder();

        for (Class<T> item : array) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            if (item != null) {
                sb.append(item.getSimpleName());
            }
            else {
                sb.append("null");
            }
        }

        return sb.toString();
    }
}