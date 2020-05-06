package com.stejsoftware.zengine;

import com.zaxsoft.zmachine.ZCPU;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ZEngineBean {

    @Bean
    public ZUserInterfaceImpl ui() {
        return new ZUserInterfaceImpl();
    };

    @Bean
    public ZCPU cpu() {
        return new ZCPU(ui());
    };

}