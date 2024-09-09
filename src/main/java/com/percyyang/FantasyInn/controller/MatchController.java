package com.percyyang.FantasyInn.controller;

import com.percyyang.FantasyInn.entity.Match;
import com.percyyang.FantasyInn.service.impl.MatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/chatbot/matches")
    public Match createNewMatch(@RequestBody CreateMatchRequest request) {
        String chatbotId = request.chatbotId();
        if (chatbotId == null || chatbotId.isEmpty()) {
            throw new IllegalArgumentException("Chatbot ID must not be null or empty");
        }
        return matchService.createNewMatch(request.chatbotId());
    }

    public record CreateMatchRequest(String chatbotId) {}

    @GetMapping("/chatbot/matches")
    public List<Match> getAllMatches(){
        return matchService.getAllMatches();
    }

}
