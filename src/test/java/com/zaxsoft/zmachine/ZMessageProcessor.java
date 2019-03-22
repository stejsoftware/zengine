package com.zaxsoft.zmachine;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ZMessageProcessor implements Publisher<ZMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(ZMessageProcessor.class);

    private static ZMessageListener zMessageListener;

    public static void register(ZMessageListener listener) {
        LOG.info("register");
        zMessageListener = listener;
    }

    public static void start(File story) {
        LOG.info("start: ", story.getAbsolutePath());

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
            while (zcpu.is_running()) ;
            zMessageListener.onDone();
        } else {
            throw new ZException("Did not start.");
        }
    }

    @Override
    public void subscribe(Subscriber<? super ZMessage> s) {
    }
}
