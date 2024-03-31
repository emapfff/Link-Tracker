package edu.java.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "link")
public class Link {
    @ManyToMany
    Set<Chat> chats;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url")
    private String url;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    public Link(String url, LocalDateTime lastUpdate) {
        this.url = url;
        this.lastUpdate = lastUpdate;
    }

    public Link() {

    }
}
