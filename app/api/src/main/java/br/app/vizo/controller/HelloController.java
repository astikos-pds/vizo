package br.app.vizo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello")
public record HelloController() {

    @GetMapping
    public String hello() {
        return "Hello, world!";
    }
}
