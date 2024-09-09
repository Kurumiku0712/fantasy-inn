package com.percyyang.FantasyInn.controller;

import com.percyyang.FantasyInn.entity.Chatbot;
import com.percyyang.FantasyInn.service.impl.ChatbotCreationService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ChatbotController {

    private final ChatbotCreationService chatbotCreationService;

    public ChatbotController(ChatbotCreationService chatbotCreationService) {
        this.chatbotCreationService = chatbotCreationService;
    }

    @PostMapping("/chatbot/create")
    public Chatbot createChatbots() {
        return chatbotCreationService.createChatbots(1);
    }

    @GetMapping("/chatbot/random")
    public Chatbot getRandomProfile() {
        return chatbotCreationService.getRandomChatbot();
    }

}
