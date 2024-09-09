package com.percyyang.FantasyInn.service.impl;

import com.percyyang.FantasyInn.entity.Chatbot;
import com.percyyang.FantasyInn.entity.Conversation;
import com.percyyang.FantasyInn.entity.Match;
import com.percyyang.FantasyInn.repo.ChatbotRepository;
import com.percyyang.FantasyInn.repo.ConversationRepository;
import com.percyyang.FantasyInn.service.interfaces.IMatchService;
import com.percyyang.FantasyInn.repo.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MatchService implements IMatchService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ChatbotRepository chatbotRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Override
    public Match createNewMatch(String chatbotId) {
        Chatbot chatbot = chatbotRepository.findById(chatbotId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find a profile with ID " + chatbotId
                ));

        // Make sure there are no existing conversations with this profile already
        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                chatbot.getId(),
                new ArrayList<>()
        );
        conversationRepository.save(conversation);
        Match match = new Match(
                UUID.randomUUID().toString(),
                chatbot,
                conversation.id()
        );
        matchRepository.save(match);
        return match;

    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }
}
