package edu.java.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "link")
@Getter
public class Link {
    @Getter
    @ManyToMany(mappedBy = "links")
    Set<Chat> chats = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url")
    private String url;
    @Setter
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    public Link(String url, LocalDateTime lastUpdate) {
        this.url = url;
        this.lastUpdate = lastUpdate;
    }

    public Link() {
    }

}
