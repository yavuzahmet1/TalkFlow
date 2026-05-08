package com.yavuzahmet.talkflow.chat;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByRecieverId(Authentication currentUser) {

        String recieverId = currentUser.getName();
        return chatRepository.findByRecieverId(recieverId);
    }

}
