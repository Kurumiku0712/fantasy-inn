package com.percyyang.FantasyInn.repo;

import com.percyyang.FantasyInn.entity.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {


}
