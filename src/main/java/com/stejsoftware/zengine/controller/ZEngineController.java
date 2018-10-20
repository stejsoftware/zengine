package com.stejsoftware.zengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ZEngineController {

    @PostMapping("/input")
    public String input(String comand) {
        return "greeting";
    }

}