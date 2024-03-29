package com.stejsoftware.zengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @RequestMapping("/zvm")
    public String zvm() {
        return "zvm";
    }

    @RequestMapping("/zvm2")
    public String zvm2() {
        return "zvm2";
    }
}