package com.percyyang.FantasyInn.controller;

import com.percyyang.FantasyInn.entity.Review;
import com.percyyang.FantasyInn.service.impl.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping("/comments")
    public ResponseEntity<Review> createReview(@RequestBody Map<String, String> payload) {
        String imdbId = payload.get("imdbId");
        String reviewBody = payload.get("reviewBody");

        System.out.println("Received imdbId: " + imdbId);  // 添加日志查看 imdbId
        System.out.println("Payload received: " + payload);

        if (imdbId == null || imdbId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Review createdReview = service.createReview(reviewBody, imdbId);
        return new ResponseEntity<>(createdReview, HttpStatus.OK);
    }
}
