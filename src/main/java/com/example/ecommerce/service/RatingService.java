package com.example.ecommerce.service;

import com.example.ecommerce.entity.Rating;
import com.example.ecommerce.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating getRatingById(Long id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        return rating.orElse(null);
    }


    public Rating createRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating updateRating(Long id, Rating ratingDetails) {
        Optional<Rating> existingRatingOpt = ratingRepository.findById(id);

        if (existingRatingOpt.isPresent()) {
            Rating existingRating = existingRatingOpt.get();
            existingRating.setRatingValue(ratingDetails.getRatingValue());
            existingRating.setCustomer(ratingDetails.getCustomer());
            existingRating.setItem(ratingDetails.getItem());
            return ratingRepository.save(existingRating);
        } else {
            return null;
        }
    }

    public boolean deleteRating(Long id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
