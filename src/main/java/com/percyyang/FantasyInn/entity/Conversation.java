package com.percyyang.FantasyInn.entity;

import java.util.List;

public record Conversation(
        String id,
        String chatbotId,
        List<ChatMessage> messages
) {
}
