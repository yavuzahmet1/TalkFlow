package com.yavuzahmet.talkflow.chat;

import java.util.List;

import com.yavuzahmet.talkflow.common.BaseAuditingEntity;
import com.yavuzahmet.talkflow.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Chat extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @OneToMany(mappedBy = "chat")
    @OrderBy("createdDate DESC")
    private List<Message> messages;

    private String name;
    private String description;

    @Transient
    public String getChatName(String currentUserId) {

        if (sender != null && sender.getId().equals(currentUserId)) {
            return recipient.getFirstName() + " " + recipient.getLastName();
        }

        else if (recipient != null && recipient.getId().equals(currentUserId)) {
            return sender.getFirstName() + " " + sender.getLastName();
        }

        return this.name;
    }

    @Transient
    public Long getUnreadMessages(String senderId) {

        return messages.stream()
                .filter(message -> message.getReceiverId().equals(senderId))
                .filter(message -> MessageState.SENT.equals(message.getState()))
                .count();
    }

}