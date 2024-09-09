package com.percyyang.FantasyInn;

import com.percyyang.FantasyInn.controller.ChatbotController;
import com.percyyang.FantasyInn.repo.ChatbotRepository;
import com.percyyang.FantasyInn.repo.ConversationRepository;
import com.percyyang.FantasyInn.repo.MatchRepository;
import com.percyyang.FantasyInn.service.impl.ChatbotCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FantasyInnApplication implements CommandLineRunner {

	@Autowired
	private ChatbotRepository chatbotRepository;

	@Autowired
	private ConversationRepository conversationRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private ChatbotCreationService chatbotCreationService;


    public static void main(String[] args) {
		SpringApplication.run(FantasyInnApplication.class, args);
	}

	public void run(String... args) {

		clearAllData();
		chatbotCreationService.saveChatbotInfoToDB();

	}

	private void clearAllData() {
		conversationRepository.deleteAll();
		matchRepository.deleteAll();
		chatbotRepository.deleteAll();
	}

}
