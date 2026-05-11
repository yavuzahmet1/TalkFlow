package com.yavuzahmet.talkflow.chat;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper mapper;

    public ChatService(ChatRepository chatRepository, ChatMapper mapper) {
        this.chatRepository = chatRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsBySenderId(Authentication currentUser) {
        final String userId = currentUser.getName();

        return chatRepository.findChatsBySenderId(userId)
                .stream()
                .map(chat -> mapper.toChatResponse(chat, userId))
                .toList();
    }
}