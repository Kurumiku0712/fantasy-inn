package com.percyyang.FantasyInn.service.interfaces;

import com.percyyang.FantasyInn.entity.ChatMessage;
import com.percyyang.FantasyInn.entity.Chatbot;
import com.percyyang.FantasyInn.entity.Conversation;

public interface IConversationService {

    Conversation getConversation(String conversationId);

    Conversation addMessageToConversation(String conversationId, ChatMessage chatMessage);

    Conversation generateProfileResponse(Conversation conversation, Chatbot chatbot, Chatbot userChatbot);

}
