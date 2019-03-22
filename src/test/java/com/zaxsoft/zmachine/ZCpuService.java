package com.zaxsoft.zmachine;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.io.File;

public class ZCpuService {
    public Flux<ZMessage> initialize(File storyFile) {
        ZMachineTestInterface zFace = new ZMachineTestInterface() {
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
        };
        ZCPU zCpu = new ZCPU(zFace);

        zCpu.initialize(storyFile.getAbsolutePath());
        zCpu.start();

        return null;
    }

    public <R> Publisher<? extends R> processEvent(ZMessage zMessage) {
        zMessage.process();
        return null;
    }
}
