package com.yavuzahmet.talkflow.chat;

import java.util.List;

import com.yavuzahmet.talkflow.common.BaseAuditingEntity;
import com.yavuzahmet.talkflow.message.Message;
import com.yavuzahmet.talkflow.message.MessageState;
import com.yavuzahmet.talkflow.message.MessageType;
import com.yavuzahmet.talkflow.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_SENDER_ID, query = "SELECT DISTINCT c FROM Chat c WHERE c.sender.id = :senderId OR c.recipient.id = :senderId")
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_RECIPIENT_ID, query = "SELECT DISTINCT c FROM Chat c WHERE (c.sender.id = :senderId AND c.recipient.id = :recipientId)")
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

    @Transient
    public String getLastMessageContent() {
        if (messages != null && !messages.isEmpty()) {
            if (messages.get(0).getType() == MessageType.TEXT) {
                return "Text";
            } else if (messages.get(0).getType() == MessageType.IMAGE) {
                return "Image";
            }

            return messages.get(0).getContent();
        }
        return null;
    }

    @Transient
    public MessageType getLastMessageType() {
        if (messages != null && !messages.isEmpty()) {
            return messages.get(0).getType();
        }
        return null;
    }

}