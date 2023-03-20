package com.practicaDWS.baskotdrippy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorHandler {

    @GetMapping("/error")
    public String error(){
        return "error";
    }

}
