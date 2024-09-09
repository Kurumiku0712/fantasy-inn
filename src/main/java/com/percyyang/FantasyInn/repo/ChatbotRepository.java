package com.percyyang.FantasyInn.repo;

import com.percyyang.FantasyInn.entity.Chatbot;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatbotRepository extends MongoRepository<Chatbot, String> {

    // 从集合中随机抽取 1 个文档
    @Aggregation(pipeline = {"{ $sample: { size: 1 } }"})
    Chatbot getRandomChatbot();

}
