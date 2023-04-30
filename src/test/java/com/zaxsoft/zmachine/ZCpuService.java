package com.zaxsoft.zmachine;

import com.stejsoftware.zengine.ZUserInterfaceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Slf4j
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

    @Test
    public void cpuTest() {
        ZCPU cpu = new ZCPU(new ZUserInterfaceImpl());

        cpu.initialize("C:\\Projects\\z_engine\\archive\\zork1.z3");

        assertThat(cpu.is_running(), is(false));

        assertThat(cpu.start(), is(true));
        assertThat(cpu.is_running(), is(true));

        log.info("cpu");

        cpu.stop();

        assertThat(cpu.is_running(), is(false));
    }
}
