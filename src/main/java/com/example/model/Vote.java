package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = "voter_id")
})
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private Users voter;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(name = "voted_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime votedAt;

    public Vote() {
        this.votedAt = LocalDateTime.now();
    }

    public Vote(Users voter, Candidate candidate) {
        this.voter = voter;
        this.candidate = candidate;
        this.votedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getVoter() {
        return voter;
    }

    public void setVoter(Users voter) {
        this.voter = voter;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public LocalDateTime getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(LocalDateTime votedAt) {
        this.votedAt = votedAt;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", voter=" + voter +
                ", candidate=" + candidate +
                ", votedAt=" + votedAt +
                '}';
    }
}
