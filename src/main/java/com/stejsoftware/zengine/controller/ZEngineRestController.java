package com.stejsoftware.zengine.controller;

import com.stejsoftware.zengine.ZEngine;
import com.stejsoftware.zengine.data.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api")
public class ZEngineRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ZEngineRestController.class);

    @Autowired
    private ZEngine engine;

    @PostMapping(value = "/input")
    public Output input(@RequestBody Object command) {
        Output output = new Output();
        output.setName(command.toString());
        return output;
    }

    @GetMapping(value = "/start")
    public Output startGame(@RequestParam(name = "uri") URI uri) throws IOException {

        if (!uri.isAbsolute()) {
            try {
                uri = new URI("http://localhost:8080" + uri.toASCIIString());
            } catch (URISyntaxException e) {
                LOG.error("error:", e);
            }
        }

        //Game game = engine.start(new ClassPathResource("minizork.z3").getFile());
        //return Output.ok(game.getId());

        return Output.ok(uri.toASCIIString());
    }

    @GetMapping(value = "/games")
    public List<String> games() throws IOException {
        return engine.gameList();
    }

    @GetMapping(value = "/stories")
    public List<Map<String, String>> stories(HttpRequest request) throws IOException {
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
}
