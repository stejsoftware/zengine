package com.stejsoftware.zengine;

import org.reactivestreams.Processor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputProcessor implements Processor<String, String> {

    private final Logger LOG = LoggerFactory.getLogger(InputProcessor.class);

    @Override
    public void onSubscribe(Subscription s) {
        LOG.info(">>> onSubscribe: {}", s.toString());
    }

    @Override
    public void onNext(String t) {
        LOG.info(">>> onNext: {}", t);
    }

    @Override
    public void onError(Throwable t) {
        LOG.info(">>> onError: {}", t.getMessage());
    }

    @Override
    public void onComplete() {
        LOG.info(">>> onComplete");
    }

    @Override
    public void subscribe(Subscriber<? super String> s) {
        LOG.info(">>> subscribe: {}", s.toString());
    }

}