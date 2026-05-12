package com.yavuzahmet.talkflow.chat;

import java.util.List;
import java.util.Optional;

import com.yavuzahmet.talkflow.user.User;
import com.yavuzahmet.talkflow.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper mapper;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository,ChatMapper mapper) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
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

    public String createChat(String senderId, String receiverId) {

        Optional<Chat> exitingChat=chatRepository.findChatsByReceiverAndSender(senderId,receiverId);

        if (exitingChat.isPresent()) {
            return exitingChat.get().getId();
        }

        User sender=userRepository.findByPublicId(senderId).orElseThrow(()->new EntityNotFoundException("User not found : "+senderId));
        User receiver=userRepository.findByPublicId(senderId).orElseThrow(()->new EntityNotFoundException("User not found : "+receiverId));

        Chat chat=new Chat();
        chat.setSender(sender);
        chat.setRecipient(receiver);

        Chat savedChat=chatRepository.save(chat);
        return savedChat.getId();
    }
}