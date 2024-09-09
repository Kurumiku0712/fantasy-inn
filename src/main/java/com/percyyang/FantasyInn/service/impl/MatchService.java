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
import java.util.Optional;
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

        // 检查是否已经存在与该chatbotId相关的Match
        Optional<Match> existingMatch = matchRepository.findByChatbotId(chatbotId);
        if (existingMatch.isPresent()) {
            // 如果存在，直接返回现有的Match
            return existingMatch.get();
        }

        // 如果不存在，继续创建新的Match
        Chatbot chatbot = chatbotRepository.findById(chatbotId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find a profile with ID " + chatbotId
                ));

        // Make sure there are no existing conversations with this profile already
        // 创建新的Conversation
        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                chatbot.getId(),
                new ArrayList<>()
        );
        conversationRepository.save(conversation);

        // 创建新的Match
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

    @Override
    public void  deleteAllMatches() { matchRepository.deleteAll(); }

}
