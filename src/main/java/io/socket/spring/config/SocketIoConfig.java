package io.socket.spring.config;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;

import io.socket.spring.SocketIoServer;
import io.socket.spring.annotation.Namespace;
import io.socket.spring.annotation.OnConnect;
import io.socket.spring.annotation.OnDisconnect;
import io.socket.spring.annotation.OnEvent;

@Configuration
public class SocketIoConfig implements BeanPostProcessor {

    Logger                 log = LoggerFactory.getLogger(SocketIoConfig.class);

    @Autowired
    private SocketIoServer server;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        // find OnEvent Handlers
        ReflectionUtils.doWithMethods(bean.getClass(), new MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {

                String className = bean.getClass().getSimpleName();
                String methodName = method.getName();
                Namespace namespace = method.getAnnotation(Namespace.class);
                OnEvent onEvent = method.getAnnotation(OnEvent.class);

                log.info("OnEvent: \"{}\" -> {}.{}", onEvent.value(), className, methodName);

                server.addHandler(namespace, onEvent.value(), bean, method);
            }
        }, new MethodFilter() {
            @Override
            public boolean matches(Method method) {
                return method.isAnnotationPresent(OnEvent.class);
            }
        });

        // find OnConnect Handlers
        ReflectionUtils.doWithMethods(bean.getClass(), new MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                String className = bean.getClass().getSimpleName();
                String methodName = method.getName();
                Namespace namespace = method.getAnnotation(Namespace.class);

                log.info("OnConnect: {}.{}", className, methodName);

                server.addHandler(namespace, "connect", bean, method);
            }
        }, new MethodFilter() {
            @Override
            public boolean matches(Method method) {
                return method.isAnnotationPresent(OnConnect.class);
            }
        });

        // find OnDisconnect Handlers
        ReflectionUtils.doWithMethods(bean.getClass(), new MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                String className = bean.getClass().getSimpleName();
                String methodName = method.getName();
                Namespace namespace = method.getAnnotation(Namespace.class);

                log.info("OnDisconnect: {}.{}", className, methodName);

                server.addHandler(namespace, "disconnect", bean, method);
            }
        }, new MethodFilter() {
            @Override
            public boolean matches(Method method) {
                return method.isAnnotationPresent(OnDisconnect.class);
            }
        });

        return bean;
    }

}