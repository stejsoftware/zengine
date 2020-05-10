package com.stejsoftware.zengine.config;

import com.stejsoftware.zengine.ZUserInterfaceImpl;
import com.zaxsoft.zmachine.ZCPU;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZEngineBeans {

    @Bean
    public ZUserInterfaceImpl ui() {
        return new ZUserInterfaceImpl();
    };

    @Bean
    public ZCPU cpu(ZUserInterfaceImpl ui) {
        return new ZCPU(ui);
    };

}