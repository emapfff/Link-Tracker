package edu.java.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "chat")
public class Chat {
    @ManyToMany
    Set<Link> linkSet;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tg_chat_id")
    private Long tgChatId;

    public Chat(Long tgChatId) {
        this.tgChatId = tgChatId;
    }

    public Chat() {

    }
}
