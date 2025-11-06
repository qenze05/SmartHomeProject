package ua.edu.ukma.kataskin.smarthomeproject.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("usernameTitle", "Smart Home");
        return "index";
    }

    @GetMapping("/signin")
    public String signin() {
        return "auth/login";
    }
}

