package com.stejsoftware.zengine.controller;

import com.stejsoftware.zengine.data.Output;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZEngineController {

    @PostMapping(value = "/input")
    public Output input(@RequestBody Object command) {
        Output output = new Output();
        output.setName(command.toString());
        return output;
    }
}