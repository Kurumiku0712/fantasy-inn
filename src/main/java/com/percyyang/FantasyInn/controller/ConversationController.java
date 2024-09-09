package com.percyyang.FantasyInn.controller;

import com.percyyang.FantasyInn.entity.ChatMessage;
import com.percyyang.FantasyInn.entity.Chatbot;
import com.percyyang.FantasyInn.entity.Conversation;
import com.percyyang.FantasyInn.repo.ChatbotRepository;
import com.percyyang.FantasyInn.repo.ConversationRepository;
import com.percyyang.FantasyInn.service.impl.ConversationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class ConversationController {

    private final ConversationService conversationService;

    private final ConversationRepository conversationRepository;

    private final ChatbotRepository chatbotRepository;

    public ConversationController(ConversationService conversationService, ConversationRepository conversationRepository, ChatbotRepository chatbotRepository) {
        this.conversationService = conversationService;
        this.conversationRepository = conversationRepository;
        this.chatbotRepository = chatbotRepository;
    }

    // test, 新建一个对话, 需要传入chatbotId
    @PostMapping("/chatbot/conversations")
    public Conversation createNewConversation(@RequestBody CreateConversationRequest request) {

        // 首先确认这个chatbot存在
        chatbotRepository.findById(request.chatbotId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 还没有service层, 使用手动创建
        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                request.chatbotId(),
                new ArrayList<>()
        );
        conversationRepository.save(conversation);
        return conversation;
    }

    public record CreateConversationRequest(String chatbotId) {}


    // 获取一个对话, 需要传入conversationId
    @GetMapping("/chatbot/conversations/{conversationId}")
    public Conversation getConversation(
            @PathVariable String conversationId
    ) {
        return conversationService.getConversation(conversationId);
    }

    // 向对话中添加一条消息, 需要传入conversationId和ChatMessage对象
    @CrossOrigin(origins = "*")
    @PostMapping("/chatbot/conversations/{conversationId}")
    public Conversation addMessageToConversation(
            @PathVariable String conversationId,
            @RequestBody ChatMessage chatMessage
    ) {
        return conversationService.addMessageToConversation(conversationId, chatMessage);
    }

}
