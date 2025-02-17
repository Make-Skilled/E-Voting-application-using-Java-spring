package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Candidate;
import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByPincode(String pincode);
    Optional<Candidate> findByAadharNumber(String aadharNumber);
    boolean existsByAadharNumber(String aadharNumber);
}
