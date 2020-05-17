package io.socket.spring;

import java.util.function.Consumer;

import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ack<T> implements Consumer<T> {
    private static final Logger log = LoggerFactory.getLogger(Ack.class);

    @Override
    public void accept(T param) {
        log.debug("ack: {}", JSONStringer.valueToString((param)));
    }
}