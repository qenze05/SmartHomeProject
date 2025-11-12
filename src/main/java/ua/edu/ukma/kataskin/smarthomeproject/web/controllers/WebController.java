package ua.edu.ukma.kataskin.smarthomeproject.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/signin")
    public String signin() {
        return "auth/login";
    }
}

