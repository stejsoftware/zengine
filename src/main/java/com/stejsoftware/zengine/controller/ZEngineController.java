package com.stejsoftware.zengine.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cpu")
public class ZEngineController {

    @GetMapping()
    public ResponseEntity<Map<String, String>> index() {
        final Map<String, String> data = new HashMap<String, String>();
        data.put("foo", "bar");
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Long>> findByIdWithCustomEtag(@PathVariable("id") final Long id) {
        final Map<String, Long> data = new HashMap<String, Long>();
        data.put("id", id);
        return ResponseEntity.ok().body(data);
    }

}