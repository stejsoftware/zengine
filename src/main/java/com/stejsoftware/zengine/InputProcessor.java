package com.stejsoftware.zengine;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Processor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class InputProcessor implements Processor<String, String> {
    @Override
    public void onSubscribe(Subscription s) {
        log.info(">>> onSubscribe: {}", s.toString());
    }

    @Override
    public void onNext(String t) {
        log.info(">>> onNext: {}", t);
    }

    @Override
    public void onError(Throwable t) {
        log.info(">>> onError: {}", t.getMessage());
    }

    @Override
    public void onComplete() {
        log.info(">>> onComplete");
    }

    @Override
    public void subscribe(Subscriber<? super String> s) {
        log.info(">>> subscribe: {}", s.toString());
    }

}