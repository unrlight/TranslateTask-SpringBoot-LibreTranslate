package com.tinktask.translatetask.controllers;

import com.tinktask.translatetask.services.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/translate")
public class TranslateController {

    @Autowired
    private TranslateService translateService;

    @PostMapping
    public String translateText(@RequestParam String text, @RequestParam String from, @RequestParam String to, HttpServletRequest request) {
        String userIp = request.getRemoteAddr();
        return translateService.translateText(text, from, to, userIp);
    }

    @GetMapping
    public String translateTextGet(@RequestParam String text, @RequestParam String from, @RequestParam String to, HttpServletRequest request) {
        String userIp = request.getRemoteAddr();
        return translateService.translateText(text, from, to, userIp);
    }
}