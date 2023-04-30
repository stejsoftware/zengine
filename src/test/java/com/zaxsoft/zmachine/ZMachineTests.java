package com.zaxsoft.zmachine;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class ZMachineTests {
    boolean done = false;
    private File storyFile;

    @BeforeEach
    public void setup() throws IOException {
        String testStoryFile = "stories/minizork.z3";
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(testStoryFile);

        if (url != null) {
            File file = new File(url.getFile());

            log.info("{}", file.getAbsolutePath());
            storyFile = file;

            System.setIn(new ByteArrayInputStream("quit\r\ny\r\n".getBytes()));
        } else {
            fail(String.format("Test Story Not Found: %s", testStoryFile));
        }
    }

    @AfterEach
    public void teardown() {
        System.setIn(System.in);
    }

    @Test
    public void test01() {
        Flux.create(sink -> {
                    try (Scanner scanner = new Scanner(System.in)) {
                        ZCPU cpu = new ZCPU(new ZMachineTestInterface() {
                            @Override
                            public int readLine(StringBuffer sb, int time) {
                                try {
                                    sb.append(scanner.nextLine());
                                } catch (NoSuchElementException ex) {
                                    log.error("error:", ex);
                                }

                                return '\n';
                            }

                            @Override
                            public int readChar(int time) {
                                return '\n';
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
                                done = true;
                                sink.complete();
                                log.info("quit()");
                            }
                        });

                        sink.onRequest(consumer -> {
                            cpu.initialize(storyFile.getAbsolutePath());
                            cpu.start();

                            log.info("consumer: {}", consumer);

                            while (cpu.is_running() && !sink.isCancelled() && !done) {
                                Thread.yield();
                            }
                        });
                    }
                })
                .doOnError(e -> log.error("Error: ", e))
                .doOnComplete(() -> log.info("onComplete"))
                .subscribe(System.out::print);
    }

    @Test
    public void test02() {
        Flux.create(sink -> {
            try (Scanner scanner = new Scanner(System.in)) {
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
            }

        }).subscribe(System.out::print);
    }
}
