package com.percyyang.FantasyInn.service.impl;

import com.mongodb.client.result.UpdateResult;
import com.percyyang.FantasyInn.entity.Movie;
import com.percyyang.FantasyInn.entity.Review;
import com.percyyang.FantasyInn.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId) {
        Review review = repository.insert(new Review(reviewBody, LocalDateTime.now(), LocalDateTime.now()));

        UpdateResult result = mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds", review.getId()))
                .first();

        if (result.getMatchedCount() == 0) {
            // 表示没有找到匹配的电影
            System.out.println("No movie found with imdbId: " + imdbId);
        } else if (result.getModifiedCount() == 0) {
            // 找到了电影但没有进行更新，可能是 reviewId 已经存在
            System.out.println("Movie found but reviewIds not updated.");
        } else {
            System.out.println("Review successfully added to movie with imdbId: " + imdbId);
        }
        return review;
    }
}
