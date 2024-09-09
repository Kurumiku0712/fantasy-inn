package com.percyyang.FantasyInn.repo;

import com.percyyang.FantasyInn.entity.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface MatchRepository extends MongoRepository<Match, String> {

    // 查询嵌套对象中的chatbot.id字段
    @Query("{'chatbot.id': ?0}")
    Optional<Match> findByChatbotId(String chatbotId);

}
