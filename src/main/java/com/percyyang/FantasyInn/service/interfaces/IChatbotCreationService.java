package com.percyyang.FantasyInn.service.interfaces;

import com.percyyang.FantasyInn.entity.Chatbot;

import java.util.List;

public interface IChatbotCreationService {

    void createChatbots(int numberOfChatbots);

    void saveChatbotInfoToJson(List<Chatbot> generatedChatbots);

    Chatbot generateChatbotImage(Chatbot chatbot);

    void saveChatbotInfoToDB();

    Chatbot getRandomChatbot();

    List<Chatbot> getAllChatbots();
}
