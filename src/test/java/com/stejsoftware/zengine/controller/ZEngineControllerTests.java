package com.stejsoftware.zengine.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZEngineControllerTests {

    @Autowired
    private ZEngineController zEngineController;

    @Test
    public void startTest() throws Exception {
    }
}
