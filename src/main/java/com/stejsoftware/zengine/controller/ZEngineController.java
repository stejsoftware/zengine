package com.stejsoftware.zengine.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.stejsoftware.zengine.ZUserInterfaceImpl;
import com.stejsoftware.zengine.data.Response;
import com.zaxsoft.zmachine.ZCPU;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cpu")
public class ZEngineController {

    @Autowired
    ZUserInterfaceImpl ui;

    @Autowired
    ZCPU cpu;

    @GetMapping(produces = "application/json")
    public Response index() {
        return new Response("error");
    }

    @GetMapping("/start")
    public Response start() {
        Response response = new Response("ok");

        File story = new File("src/main/resources/static/stories/minizork.z3");

        cpu.initialize(story.getAbsolutePath());
        // response.setStart(cpu.start());

        return (response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Long>> findByIdWithCustomEtag(@PathVariable("id") final Long id) {
        final Map<String, Long> data = new HashMap<String, Long>();
        data.put("id", id);
        return ResponseEntity.ok().body(data);
    }

}