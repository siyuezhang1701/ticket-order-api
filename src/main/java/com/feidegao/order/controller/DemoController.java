package com.feidegao.order.controller;

import com.feidegao.order.service.DemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/heartbeat")
    public ResponseEntity<String> heartbeat() {
        return ResponseEntity.ok("LLAP!");
    }

    @PostMapping("/demos")
    public ResponseEntity<URI> create() {
        return ResponseEntity.created(demoUrlBuilder(demoService.save())).build();
    }

    private URI demoUrlBuilder(String id){
        return URI.create(String.format("/demos/%s", id));
    }
}
