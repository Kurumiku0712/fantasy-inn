package com.percyyang.FantasyInn.repo;

import com.percyyang.FantasyInn.entity.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationRepository extends MongoRepository<Conversation, String> {


}
