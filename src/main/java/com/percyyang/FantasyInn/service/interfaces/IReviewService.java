package com.percyyang.FantasyInn.service.interfaces;

import com.percyyang.FantasyInn.entity.Review;

public interface IReviewService {

    Review createReview(String reviewBody, String imdbId);

}
