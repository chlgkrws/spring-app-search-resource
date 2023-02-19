package com.example.demo.dummy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dummy")
public class RestController2 {

    @GetMapping("/test2")
    public void rest() {

    }
}
