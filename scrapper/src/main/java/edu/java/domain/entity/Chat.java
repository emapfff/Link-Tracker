package edu.java.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "chat")
@Getter
public class Chat {
    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name = "consists",
               joinColumns = {@JoinColumn(name = "chat_id")},
               inverseJoinColumns = {@JoinColumn(name = "link_id")})
    Set<Link> links = new HashSet<>();
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

    public void addLink(@NotNull Link link) {
        this.links.add(link);
        link.getChats().add(this);
    }

    public void removeLink(Link link) {
        this.links.remove(link);
        link.getChats().remove(this);
    }
}
