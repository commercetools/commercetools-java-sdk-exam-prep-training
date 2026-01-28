package com.training.handson.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    @GetMapping(value = {"/", "/api/**"})
    public String redirect() {
        return "forward:/index.html";
    }
}

