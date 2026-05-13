package com.yavuzahmet.talkflow.message;

import com.yavuzahmet.talkflow.chat.Chat;
import com.yavuzahmet.talkflow.chat.ChatRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    public void saveMessage(MessageRequest messageRequest) {
        Chat chat=chatRepository.findById(messageRequest.getChatId()).orElseThrow(()->new EntityNotFoundException("Chat"));
        Message message=new Message();
        message.setContent(messageRequest.getContent());
        message.setChat(chat);
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getReceiverId());
        message.setType(messageRequest.getType());
        message.setState(MessageState.SENT);

        messageRepository.save(message);

    }
}
