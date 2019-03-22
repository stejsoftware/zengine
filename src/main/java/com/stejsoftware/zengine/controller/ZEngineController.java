package com.stejsoftware.zengine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ZEngineController {
    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("title", "Zork Online");

        return "index";
    }

    private List<Map<String, String>> getStories() {
        return Arrays.asList(
                new HashMap<String, String>() {{
                    put("label", "Mini Zork");
                    put("uri", "/stories/minizork.z3");
                }},
                new HashMap<String, String>() {{
                    put("label", "Fred");
                    put("uri", "/stories/fred.z3");
                }}
        );
    }

    @GetMapping(value = "/stories")
    public String stories(Model model) {
        model.addAttribute("title", "Zork Online :: Stories");
        model.addAttribute("stories", getStories());

        return "stories";
    }
}