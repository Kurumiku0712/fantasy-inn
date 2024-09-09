package com.percyyang.FantasyInn.repo;

import com.percyyang.FantasyInn.entity.Chatbot;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatbotRepository extends MongoRepository<Chatbot, String> {

    // 从集合中随机抽取 1 个文档，排除 id 为 'user' 的文档
    @Aggregation(pipeline = {
            "{ $match: { _id: { $ne: 'user' } } }",
            "{ $sample: { size: 1 } }"
    })
    Chatbot getRandomChatbot();

    // 自定义查询，排除 id 为 "user" 的 Chatbot
    @Query("{ 'id': { $ne: 'user' } }")
    List<Chatbot> findAllExcludingUser();

}
