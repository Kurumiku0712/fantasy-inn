package com.percyyang.FantasyInn.service.interfaces;

import com.percyyang.FantasyInn.entity.Match;

import java.util.List;

public interface IMatchService {

    Match createNewMatch(String chatbotId);

    List<Match> getAllMatches();

}
