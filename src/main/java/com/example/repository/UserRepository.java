package com.example.repository;

import com.example.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByAadharAndMobileNumber(String aadhar, String mobileNumber);
    Users findByAadhar(String aadhar);
    long countByPincode(String pincode);
}
