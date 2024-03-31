package edu.java.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "github_links")
public class GithubLink {
    @OneToOne
    Link links;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "link_id")
    private Long linkId;
    @Column(name = "count_branches")
    private Integer countBranches;

    public GithubLink(Long linkId, Integer countBranches) {
        this.linkId = linkId;
        this.countBranches = countBranches;
    }

    public GithubLink() {

    }
}
