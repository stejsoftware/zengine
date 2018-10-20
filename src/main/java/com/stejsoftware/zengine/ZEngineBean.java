package com.stejsoftware.zengine;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ZEngineBean {

    @Bean
    public ZUserInterfaceImpl getZUI() {
        return new ZUserInterfaceImpl();
    };
    
}