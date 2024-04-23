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
@Table(name = "github_links")
@Getter
public class GithubLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "count_branches")
    @Setter
    private Integer countBranches;
    @ManyToOne
    @JoinColumn(name = "link_id", referencedColumnName = "id", nullable = false)
    private Link link;

    public GithubLink(Link link, Integer countBranches) {
        this.link = link;
        this.countBranches = countBranches;
    }

    public GithubLink() {
    }
}
