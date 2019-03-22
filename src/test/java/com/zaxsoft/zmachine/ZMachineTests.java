package com.zaxsoft.zmachine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.WorkQueueProcessor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Scanner;

public class ZMachineTests {
    private static final Logger LOG = LoggerFactory.getLogger(ZMachineTests.class);
    boolean done = false;
    private File storyFile;

    @Before
    public void setup() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("minizork.z3").getFile());
        LOG.info("{}", file.getAbsolutePath());
        storyFile = file;

        System.setIn(new ByteArrayInputStream("quit\r\ny\r\n".getBytes()));
    }

    @After
    public void teardown() {
        System.setIn(System.in);
    }

    @Test
    public void test01() {
        Flux<String> bridge = WorkQueueProcessor.create(sink -> {
            ZCPU cpu = new ZCPU(new ZMachineTestInterface() {
                @Override
                public int readLine(StringBuffer sb, int time) {
                    Scanner scanner = new Scanner(System.in);
                    sb.append(scanner.nextLine());
                    return 0;
                }

                @Override
                public int readChar(int time) {
                    return 0;
                }

                @Override
                public void showString(String s) {
                    sink.next(s);
                }

                @Override
                public void showStatusBar(String s, int a, int b, boolean flag) {
                    sink.next(String.format("%s %d %d %s", s, a, b, flag ? "T" : "F"));
                }

                @Override
                public void quit() {
                    sink.complete();
                }
            });

            sink.onRequest(s -> {
                cpu.initialize(storyFile.getAbsolutePath());
                cpu.start();

                while (cpu.is_running() && !sink.isCancelled()) {

                }
            });
        });

        bridge
                .doOnError(e -> LOG.error("Error: ", e))
                .doOnComplete(() -> LOG.info("onDone"))
                .subscribe(System.out::print);
    }

    @Test
    public void test02() {
        Flux.create(sink -> {
            Scanner scanner = new Scanner(System.in);

            ZCPU cpu = new ZCPU(new ZMachineTestInterface() {
                @Override
                public void showStatusBar(String s, int a, int b, boolean flag) {
                    sink.next(String.format("%s (%d, %d)\n", s, a, b));
                }

                @Override
                public int readLine(StringBuffer sb, int time) {
                    sb.append(scanner.nextLine());
                    return 0;
                }

                @Override
                public int readChar(int time) {
                    return 0;
                }

                @Override
                public void showString(String s) {
                    sink.next(s);
                }

                @Override
                public void quit() {
                    sink.complete();
                    done = true;
                }
            });

            cpu.initialize(storyFile.getAbsolutePath());
            cpu.start();

            while (cpu.is_running()) {
                if (done) {
                    cpu.stop();
                }
            }

        }).subscribe(System.out::print);
    }

}
