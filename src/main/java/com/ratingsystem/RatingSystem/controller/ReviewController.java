package com.ratingsystem.RatingSystem.controller;

import com.ratingsystem.RatingSystem.service.GameObjectService;
import com.ratingsystem.RatingSystem.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/seller/profile")
    public ResponseEntity<?> viewProfile(){

        return reviewService.viewProfile();
    }

    @GetMapping("/seller/rating")
    public ResponseEntity<?> getRatingsBySeller(){

        return reviewService.getSellersRating();
    }

    @GetMapping("/seller/top/{topN}")
    public ResponseEntity<?> getTopSellers(@PathVariable("topN") int topN){

        return reviewService.getTopSellers(topN);
    }

    @PostMapping("/gameObject/filter")
    public ResponseEntity<?> filterGameObjects(@RequestParam(required = false) String gameObjectTitle,
                                               @RequestParam(required = false, defaultValue = "0") int idLess,
                                               @RequestParam(required = false,defaultValue = "0") int idMore,
                                               @RequestParam(required = false,defaultValue = "0") int idEquals,
                                               @RequestParam(required = false,defaultValue = "0") int sellerId,
                                               @RequestParam(required = false) LocalDate createdBefore,
                                               @RequestParam(required = false) LocalDate createdAfter){

        return reviewService.filterGameObject(gameObjectTitle,idLess,idMore,idEquals,sellerId,createdBefore,createdAfter);
    }

    @PostMapping("/seller/filterByRating")
    public ResponseEntity<?> filterByRating(@RequestParam(required = false, defaultValue = "0") double ratingLower,
                                            @RequestParam(required = false, defaultValue = "0") double ratingUpper,
                                            @RequestParam(required = false, defaultValue = "0") double ratingEquals){

        return reviewService.filterSellerByRating(ratingLower,ratingUpper,ratingEquals);
    }

}
