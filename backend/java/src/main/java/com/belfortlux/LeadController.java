package com.belfortlux;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LeadController {
    public static class LeadRequest {
        public String name;
        public String company;
        public String email;
        public int budget;
        public String timeline;
        public String message;
        public boolean consent;
    }

    private boolean validEmail(String email) {
        return email != null && email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }

    private int score(LeadRequest lead) {
        int score = 0;

        if (lead.name != null && lead.name.trim().length() >= 2) {
            score += 10;
        }

        if (lead.company != null && lead.company.trim().length() >= 2) {
            score += 10;
        }

        if (validEmail(lead.email)) {
            score += 25;
        }

        score += Math.max(0, Math.min(5, lead.budget)) * 8;

        String timeline = lead.timeline == null ? "" : lead.timeline.toLowerCase();

        if (timeline.equals("urgent")) {
            score += 20;
        } else if (timeline.equals("quarter")) {
            score += 12;
        } else if (timeline.equals("month")) {
            score += 8;
        } else if (timeline.equals("later")) {
            score += 3;
        }

        if (lead.message != null) {
            score += Math.min(20, lead.message.trim().length() / 10);
        }

        if (lead.consent) {
            score += 5;
        }

        return Math.min(100, score);
    }

    @GetMapping("/")
    public Map<String, String> status() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        return response;
    }

    @PostMapping("/leads")
    public ResponseEntity<Map<String, Object>> createLead(@RequestBody LeadRequest lead) {
        if (lead.name == null || lead.email == null || !validEmail(lead.email)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid lead payload");
            return ResponseEntity.badRequest().body(error);
        }

        int score = score(lead);
        String tier = score >= 75 ? "hot" : score >= 45 ? "warm" : "cold";

        Map<String, Object> response = new HashMap<>();
        response.put("score", score);
        response.put("tier", tier);

        return ResponseEntity.ok(response);
    }
}
