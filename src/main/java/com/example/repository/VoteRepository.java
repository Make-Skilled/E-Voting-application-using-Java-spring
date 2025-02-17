package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Vote;
import com.example.model.Users;
import com.example.model.Candidate;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByVoter(Users voter);
    long countByCandidate(Candidate candidate);
    boolean existsByVoter(Users voter);
}
