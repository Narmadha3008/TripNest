package com.tripnest.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripnest.dto.ai.DestinationRecommendRequest;
import com.tripnest.dto.ai.DestinationRecommendResponse;
import com.tripnest.model.Destination;
import com.tripnest.model.User;
import com.tripnest.repository.DestinationRepository;
import com.tripnest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AiDestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    @Autowired
    private AiService aiService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<DestinationRecommendResponse> getRecommendations(DestinationRecommendRequest req, String userEmail) {
        User user = (userEmail != null) ? userRepository.findByEmail(userEmail) : null;
        Set<String> userFavorites = extractFavorites(user);

        List<Destination> allDestinations = destinationRepository.findAll();
        List<DestinationRecommendResponse> results = new ArrayList<>();

        for (Destination d : allDestinations) {
            DestinationRecommendResponse item = new DestinationRecommendResponse();
            item.setId(d.getId());
            item.setName(d.getName());
            item.setCountry(d.getCountry());
            item.setCategory(d.getCategory());
            item.setDescription(d.getDescription());
            item.setImageUrl(d.getImageUrl());
            item.setBestTimeToVisit(d.getBestTimeToVisit() != null ? d.getBestTimeToVisit() : "October - March");
            item.setEstimatedBudget(d.getEstimatedBudget() != null ? d.getEstimatedBudget() : "INR 15,000 - 30,000");
            item.setFavorite(userFavorites.contains(d.getName().toLowerCase()));

            KnowledgeBaseService.DestinationInfo kbInfo = knowledgeBaseService.getDestination(d.getName());
            if (kbInfo != null) {
                for (KnowledgeBaseService.AttractionInfo att : kbInfo.getAttractions()) {
                    item.getKeyAttractions().add(att.getName());
                }
            } else {
                item.getKeyAttractions().add("Scenic Viewpoints");
                item.getKeyAttractions().add("Cultural Heritage Sites");
                item.getKeyAttractions().add("Local Markets & Cuisine");
            }

            int score = calculateMatchScore(d, kbInfo, req);
            item.setMatchScore(score);
            item.setMatchReason(generateReason(d, kbInfo, req, score));
            item.setSuggestedDuration(estimateDuration(req.getDurationDays(), d.getName()));

            results.add(item);
        }

        // Sort descending by match score
        results.sort((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()));
        return results;
    }

    private int calculateMatchScore(Destination d, KnowledgeBaseService.DestinationInfo kbInfo, DestinationRecommendRequest req) {
        int score = 70; // baseline
        String destCategory = (d.getCategory() != null) ? d.getCategory().toLowerCase() : "";
        String climate = (kbInfo != null && kbInfo.getClimate() != null) ? kbInfo.getClimate().toLowerCase() : "";

        // Check climate match
        if (req.getClimate() != null && !req.getClimate().trim().isEmpty()) {
            String reqClimate = req.getClimate().toLowerCase();
            if (climate.contains(reqClimate) || destCategory.contains(reqClimate)) {
                score += 15;
            }
        }

        // Check interest matches
        if (req.getInterests() != null && !req.getInterests().isEmpty()) {
            for (String interest : req.getInterests()) {
                String in = interest.toLowerCase();
                if (destCategory.contains(in) || (kbInfo != null && kbInfo.getInterests().stream().anyMatch(i -> i.toLowerCase().contains(in)))) {
                    score += 8;
                }
            }
        }

        return Math.min(98, score);
    }

    private String generateReason(Destination d, KnowledgeBaseService.DestinationInfo kbInfo, DestinationRecommendRequest req, int score) {
        StringBuilder sb = new StringBuilder();
        sb.append(score >= 85 ? "Exceptional match! " : "Great option: ");
        sb.append(d.getName()).append(" is renowned for ").append(d.getCategory() != null ? d.getCategory() : "scenic travel");

        if (req.getInterests() != null && !req.getInterests().isEmpty()) {
            sb.append(", aligning seamlessly with your interest in ").append(String.join(", ", req.getInterests()));
        }
        sb.append(". Best experienced during ").append(d.getBestTimeToVisit() != null ? d.getBestTimeToVisit() : "peak travel seasons").append(".");
        return sb.toString();
    }

    private String estimateDuration(int requestedDays, String name) {
        if (requestedDays > 0) {
            return requestedDays + " Days recommended";
        }
        return "3 to 5 Days recommended";
    }

    private Set<String> extractFavorites(User user) {
        Set<String> set = new HashSet<>();
        if (user != null && user.getFavoriteDestinations() != null) {
            String[] parts = user.getFavoriteDestinations().split(",");
            for (String p : parts) {
                if (!p.trim().isEmpty()) set.add(p.trim().toLowerCase());
            }
        }
        return set;
    }

    public List<String> getUserFavorites(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null || user.getFavoriteDestinations() == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(user.getFavoriteDestinations().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public boolean toggleFavorite(String destinationName, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        List<String> favs = new ArrayList<>(getUserFavorites(userEmail));
        boolean added;
        String cleanName = destinationName.trim();

        if (favs.stream().anyMatch(f -> f.equalsIgnoreCase(cleanName))) {
            favs.removeIf(f -> f.equalsIgnoreCase(cleanName));
            added = false;
        } else {
            favs.add(cleanName);
            added = true;
        }

        user.setFavoriteDestinations(String.join(", ", favs));
        userRepository.save(user);
        return added;
    }
}