package com.percyyang.FantasyInn.controller;

import com.percyyang.FantasyInn.entity.Chatbot;
import com.percyyang.FantasyInn.service.impl.ChatbotCreationService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatbotController {

    private final ChatbotCreationService chatbotCreationService;

    public ChatbotController(ChatbotCreationService chatbotCreationService) {
        this.chatbotCreationService = chatbotCreationService;
    }

    @PostMapping("/chatbot/create")
    public void createChatbots() {
        chatbotCreationService.createChatbots(1);
    }

    @GetMapping("/chatbot/random")
    public Chatbot getRandomProfile() {
        return chatbotCreationService.getRandomChatbot();
    }

    @GetMapping("/chatbot/all")
    public List<Chatbot> getAllChatbots() { return chatbotCreationService.getAllChatbots(); }

}
