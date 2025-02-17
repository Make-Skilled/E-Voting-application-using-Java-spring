package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import com.example.model.Candidate;
import com.example.repository.CandidateRepository;
import com.example.repository.VoteRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CandidateRepository candidateRepository;
    
    @Autowired
    private VoteRepository voteRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            session.setAttribute("adminId", username);
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid credentials");
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, 
                          @RequestParam(required = false) String pincode,
                          Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        List<Candidate> candidates;
        if (pincode != null && !pincode.trim().isEmpty()) {
            candidates = candidateRepository.findByPincode(pincode);
        } else {
            candidates = candidateRepository.findAll();
        }

        candidates.forEach(candidate -> {
            long voteCount = voteRepository.countByCandidate(candidate);
            candidate.setVoteCount(voteCount);
        });

        model.addAttribute("candidates", candidates);
        return "admin/dashboard";
    }

    @GetMapping("/add-candidate")
    public String addCandidatePage(HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        return "admin/add-candidate";
    }

    @PostMapping("/add-candidate")
    public String addCandidate(@RequestParam String name,
                              @RequestParam String partyName,
                              @RequestParam String aadharNumber,
                              @RequestParam String address,
                              @RequestParam String pincode,
                              @RequestParam MultipartFile imageFile,
                              HttpSession session,
                              Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        try {
            // Validate Aadhar number
            if (candidateRepository.existsByAadharNumber(aadharNumber)) {
                model.addAttribute("error", "Candidate with this Aadhar number already exists");
                return "admin/add-candidate";
            }

            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the image file
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath);

            // Create and save candidate
            Candidate candidate = new Candidate();
            candidate.setName(name);
            candidate.setPartyName(partyName);
            candidate.setAadharNumber(aadharNumber);
            candidate.setAddress(address);
            candidate.setPincode(pincode);
            candidate.setImagePath("/uploads/" + fileName);

            candidateRepository.save(candidate);
            return "redirect:/admin/dashboard";

        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload image: " + e.getMessage());
            return "admin/add-candidate";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
