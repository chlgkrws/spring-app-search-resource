package com.example.demo.dummy;

import com.example.demo.service.ResourceCollector;
import com.example.demo.service.ResourceCollector.ApiResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExampleController {

    private final ResourceCollector collector;

    @GetMapping("/call")
    public List<ApiResource> call() {
        return this.collector.getResources();
    }
}
