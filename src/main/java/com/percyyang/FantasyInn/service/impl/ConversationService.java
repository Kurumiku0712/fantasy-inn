package com.percyyang.FantasyInn.service.impl;

import com.percyyang.FantasyInn.entity.ChatMessage;
import com.percyyang.FantasyInn.entity.Chatbot;
import com.percyyang.FantasyInn.entity.Conversation;
import com.percyyang.FantasyInn.repo.ChatbotRepository;
import com.percyyang.FantasyInn.repo.ConversationRepository;
import com.percyyang.FantasyInn.service.interfaces.IConversationService;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationService implements IConversationService {

    @Autowired
    private final ConversationRepository conversationRepository;

    @Autowired
    private final ChatbotRepository chatbotRepository;

    private OpenAiChatModel chatModel;

    public ConversationService(ConversationRepository conversationRepository, ChatbotRepository chatbotRepository, OpenAiChatModel chatModel) {
        this.conversationRepository = conversationRepository;
        this.chatbotRepository = chatbotRepository;
        this.chatModel = chatModel;
    }

    @Override
    public Conversation getConversation(String conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find conversation with the ID " + conversationId
                ));
    }

    @Override
    public Conversation addMessageToConversation(String conversationId, ChatMessage chatMessage) {
        // 首先确认这个conversation存在
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find conversation with the ID " + conversationId
                ));
        String matchChatbotId = conversation.chatbotId();

        // 确认这个chatbot存在
        Chatbot chatbot = chatbotRepository.findById(matchChatbotId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find a chatbot with ID " + matchChatbotId
                ));
        // 确认发出这条message的用户存在
        Chatbot userChatbot = chatbotRepository.findById(chatMessage.authorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find an author with ID " + chatMessage.authorId()
                ));

        // 传入的 ChatMessage 通过 @RequestBody 获取，但为了确保消息的时间戳准确性，服务器需要为消息添加当前时间
        // 这种做法符合安全和一致性的要求，确保消息时间是可信的
        ChatMessage messageWithTime = new ChatMessage(
                chatMessage.messageText(),
                chatMessage.authorId(),
                LocalDateTime.now()
        );
        conversation.messages().add(messageWithTime);
        generateProfileResponse(conversation, chatbot, userChatbot);
        conversationRepository.save(conversation);
        return conversation;
    }

    @Override
    public Conversation generateProfileResponse(Conversation conversation, Chatbot chatbot, Chatbot userChatbot) {
        // System message
        String systemMessageStr =
                "You are a " + chatbot.getAge() + " year old " + chatbot.getEthnicity() + " " +
                        chatbot.getGender() + " called " + chatbot.getFirstName() + " " +
                        chatbot.getLastName() + " matched\n" +
                        "with a " + userChatbot.getAge() + " year old " + userChatbot.getEthnicity() +
                        " " + userChatbot.getGender() + " called " + userChatbot.getFirstName() + " " +
                        userChatbot.getLastName() + " on Tinder.\n" +
                        "This is an in-app text conversation between you two.\n" +
                        "Pretend to be the provided person, respond to the conversation, " +
                        "provide recommendation(hotel, inn, mansion, etc.) " + "customer services.\n" +
                        "Your bio is: " + chatbot.getBio() + " and your Myers Briggs " +
                        "personality type is " + chatbot.getPersonality() + ". Respond in the role " +
                        "of this person only.\n" + " # Personality and Tone:\n\n" +
                        "The message should look like what a Tinder user writes in response to chat. " +
                        "Keep it short and brief. No hashtags or generic messages.\n" +
                        "Be friendly, approachable, and slightly playful.\n" +
                        "Reflect confidence and genuine interest in getting to know the other person.\n" +
                        "Use humor and wit appropriately to make the conversation enjoyable.\n" +
                        "Match the tone of the user's messages—be more casual or serious as needed.\n\n" +
                        " # Conversation Starters:\n\n" +
                        "Use unique and intriguing openers to spark interest.\n" +
                        "Avoid generic greetings like \"Hi\" or \"Hey\"; " +
                        "instead, ask interesting questions or make personalized comments " +
                        "based on the other person's profile.\n\n" +
                        " # Profile Insights:\n\n" +
                        "Use information from the other person's profile to " +
                        "create tailored messages.\n" + "Show genuine curiosity about their " +
                        "hobbies, interests, and background.\n" + "Compliment specific details " +
                        "from their profile to make them feel special.\n\n" +
                        " # Engagement:\n\n" +
                        "Ask open-ended questions to keep the conversation flowing.\n" +
                        "Share interesting anecdotes or experiences related to the topic " +
                        "of conversation.\n" + "Respond promptly to keep the momentum of the chat going.\n\n" +
                        " # Creativity:\n\n" +
                        "Incorporate playful banter, wordplay, or light teasing to add a fun " +
                        "element to the chat.\n" +
                        "Suggest fun activities or ideas for a potential date.\n\n" +
                        " # Respect and Sensitivity:\n\n" +
                        "Always be respectful and considerate of the other person's feelings.\n" +
                        "Avoid controversial or sensitive topics unless the other " +
                        "person initiates them.\n" +
                        "Be mindful of boundaries and avoid overly personal or " +
                        "intrusive questions early in the conversation.";

        SystemMessage systemMessage = new SystemMessage(systemMessageStr);

        List<AbstractMessage> conversationMessages = conversation.messages().stream().map(message -> {
            if (message.authorId().equals(chatbot.getId())) {
                return new AssistantMessage(message.messageText());
            } else {
                return new UserMessage(message.messageText());
            }
        }).toList();

        List<Message> allMessages = new ArrayList<>();
        allMessages.add(systemMessage);
        allMessages.addAll(conversationMessages);

        Prompt prompt = new Prompt(allMessages);
        ChatResponse response = chatModel.call(prompt);
        conversation.messages().add(new ChatMessage(
                response.getResult().getOutput().getContent(),
                chatbot.getId(),
                LocalDateTime.now()
        ));
        return conversation;
    }
}
