package com.example.demo.dummy;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Controller1 {

    @GetMapping("/view1")
    public void view() {

    }
}
