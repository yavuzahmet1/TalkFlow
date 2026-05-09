package com.yavuzahmet.talkflow.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, String> {

    List<ChatResponse> findByRecieverId(String recieverId);

}
