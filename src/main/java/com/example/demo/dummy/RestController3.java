package com.example.demo.dummy;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dummy")
public class RestController3 {

    @RequestMapping(value = "/test3", method = {RequestMethod.DELETE, RequestMethod.GET})
    public void rest() {

    }
}
