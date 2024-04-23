package edu.java.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stackoverflow_link")
@Getter
public class StackOverflowLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "answer_count")
    @Setter
    private Integer answerCount;
    @ManyToOne
    @JoinColumn(name = "link_id", referencedColumnName = "id", nullable = false)
    private Link link;

    public StackOverflowLink(Link link, Integer answerCount) {
        this.link = link;
        this.answerCount = answerCount;
    }

    public StackOverflowLink() {
    }
}
