package com.stejsoftware.zengine.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ZEngineRestControllerTest {
    @Autowired
    private ZEngineRestController zEngineRestController;

    @Test
    void contextLoads() {
        assertThat(zEngineRestController).isNotNull();
    }
}