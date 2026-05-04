package com.yavuzahmet.talkflow.user;

import java.time.LocalDateTime;
import java.util.List;

import com.yavuzahmet.talkflow.chat.Chat;
import com.yavuzahmet.talkflow.common.BaseAuditingEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "users")
public class User extends BaseAuditingEntity {

    private static final int ONLINE_TIMEOUT_MINUTES = 5;

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime lastSeen;

    @OneToMany(mappedBy = "sender")
    private List<Chat> chatsAsSender;

    @OneToMany(mappedBy = "recipient")
    private List<Chat> chatsAsRecipient;

    @Transient
    public boolean isOnline() {

        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(ONLINE_TIMEOUT_MINUTES));

    }

}
