package com.example.demo.dummy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RestController1 {

    @GetMapping("/test1")
    public void rest() {

    }
}
