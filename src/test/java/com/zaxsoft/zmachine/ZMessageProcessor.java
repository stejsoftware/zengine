package com.zaxsoft.zmachine;

import java.io.File;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZMessageProcessor implements Publisher<ZMessage> {

    private static ZMessageListener<ZMessage> zMessageListener;

    public static void register(ZMessageListener<ZMessage> listener) {
        log.info("register");
        zMessageListener = listener;
    }

    public static void start(File story) {
        log.info("start: ", story.getAbsolutePath());

        ZCPU zcpu = new ZCPU(new ZMachineTestInterface() {
            @Override
            public void showStatusBar(String s, int a, int b, boolean flag) {

            }

            @Override
            public int readLine(StringBuffer sb, int time) {
                return 0;
            }

            @Override
            public int readChar(int time) {
                return 0;
            }

            @Override
            public void showString(String s) {

            }

            @Override
            public void quit() {

            }
        });
        zcpu.initialize(story.getAbsolutePath());

        if (zcpu.start()) {
            while (zcpu.is_running())
                ;
            zMessageListener.onDone();
        } else {
            throw new ZException("Did not start.");
        }
    }

    @Override
    public void subscribe(Subscriber<? super ZMessage> s) {
    }
}
