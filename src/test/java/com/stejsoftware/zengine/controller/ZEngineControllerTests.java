package com.stejsoftware.zengine.controller;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZEngineControllerTests {

    @Autowired
    private ZEngineController zEngineController;

    @Test
    @Ignore
    public void startTest() throws Exception {
        System.out.println(zEngineController.index());
        System.out.println(zEngineController.findByIdWithCustomEtag(1l));
    }
}
