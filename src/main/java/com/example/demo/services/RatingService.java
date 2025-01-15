package com.example.demo.services;

import com.example.demo.entities.Rating;
import com.example.demo.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Rating findById(String id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        if (!rating.isPresent()) {
            throw new RuntimeException("Not found");
        }
        return rating.get();
    }

    public String save(Rating rating) {
        Rating newRating = ratingRepository.save(rating);

        return newRating.getId();
    }

    public Rating update(String id, Rating rating) {
        Rating oldRating = ratingRepository.findById(id).get();

        if (oldRating == null) {
            throw new RuntimeException("Rating not found with id: " + id);
        }

        if (rating.getUserId() != null) oldRating.setUserId(rating.getUserId());
        if (rating.getRating() != null) oldRating.setRating(rating.getRating());
        if (rating.getMovieId() != null) oldRating.setMovieId(rating.getMovieId());
        if (rating.getTimestamp() != null) oldRating.setTimestamp(rating.getTimestamp());

        ratingRepository.save(oldRating);

        return oldRating;
    }

    public void delete(String id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        if (!rating.isPresent()) {
            throw new RuntimeException("Rating not found with id: " + id);
        }

        ratingRepository.deleteById(id);
    }
}
