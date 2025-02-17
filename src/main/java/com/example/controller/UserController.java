package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.example.model.Users;
import com.example.model.Vote;
import com.example.model.Candidate;
import com.example.repository.UserRepository;
import com.example.repository.VoteRepository;
import com.example.repository.CandidateRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new Users());
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String aadhar, 
                       @RequestParam String mobileNumber,
                       HttpSession session,
                       Model model) {
        Optional<Users> userOpt = userRepository.findByAadharAndMobileNumber(aadhar, mobileNumber);
        if (userOpt.isPresent()) {
            session.setAttribute("userId", userOpt.get().getId());
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Users user, Model model) {
        try {
            // Check if user already exists
            if (userRepository.findByAadhar(user.getAadhar()) != null) {
                model.addAttribute("error", "User with this Aadhar number already exists");
                return "register";
            }
            userRepository.save(user);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // Get candidates from user's pincode
        List<Candidate> candidates = candidateRepository.findByPincode(user.getPincode());
        
        // Calculate total votes and vote count for each candidate
        long totalVotes = 0;
        for (Candidate candidate : candidates) {
            long voteCount = voteRepository.countByCandidate(candidate);
            candidate.setVoteCount(voteCount);
            totalVotes += voteCount;
        }

        // Check if user has already voted
        boolean hasVoted = voteRepository.findByVoter(user) != null;

        model.addAttribute("user", user);
        model.addAttribute("candidates", candidates);
        model.addAttribute("hasVoted", hasVoted);
        model.addAttribute("totalVotes", totalVotes);
        
        return "dashboard";
    }

    @PostMapping("/vote")
    public String vote(@RequestParam Long candidateId, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // Check if user has already voted
        if (voteRepository.findByVoter(user) != null) {
            model.addAttribute("message", "You have already cast your vote!");
            return "redirect:/dashboard";
        }

        // Get candidate
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null) {
            model.addAttribute("message", "Invalid candidate selected!");
            return "redirect:/dashboard";
        }

        // Create and save vote
        Vote vote = new Vote(user, candidate);
        voteRepository.save(vote);

        model.addAttribute("message", "Successfully voted for " + candidate.getName());
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}