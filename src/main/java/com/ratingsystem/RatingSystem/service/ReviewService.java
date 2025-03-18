package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.entity.Comment;
import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.enums.Status;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import lombok.Getter;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.channels.SeekableByteChannel;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final SellerRepository sellerRepository;

    // static class for structured profile identification
    private static record ProfileData(  String email, String  name, String surname){}

    @Autowired
    public ReviewService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public ResponseEntity<?> getSellersRating(){

        var sellerList = sellerRepository.findAll();

        if (sellerList.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sellers found !");
        }

        var sellersRating = sellerList.stream()
                .filter(seller -> seller.getComments() != null)
                .collect(Collectors.groupingBy(
                        Seller::getEmail,
                        Collectors.averagingDouble(seller -> seller.getComments().stream()
                                .filter(comment -> comment != null && comment.getStatus() != Status.PENDING)
                                .mapToDouble(Comment::getRating)
                                .average()
                                .orElse(0.0))
                ));
        if (sellersRating.isEmpty()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sellers has no comments !");
        }

        var formattedTopSellers = sellersRating.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.format("%.2f average rating", entry.getValue()),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return ResponseEntity.status(HttpStatus.OK).body(formattedTopSellers);
    }

    public ResponseEntity<?> getTopSellers(int topN) {
        Map<String,Double> sellersRating  = new HashMap<>();

        var sellerList = sellerRepository.findAll();

        if (sellerList.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sellers found !");
        }

        sellersRating = sellerList.stream()
                .filter(seller -> seller.getComments() != null)
                .collect(Collectors.groupingBy(
                        Seller::getEmail,
                        Collectors.averagingDouble(seller -> seller.getComments().stream()
                                .filter(comment -> comment != null && comment.getStatus() != Status.PENDING)
                                .mapToDouble(Comment::getRating)
                                .average()
                                .orElse(0.0))
                ));


        if (sellersRating.isEmpty()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sellers has comments !");
        }

        var topSellers = sellersRating.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(topN)
                .collect(Collectors.toMap(Map.Entry::getKey,  Map.Entry::getValue));

        var formattedTopSellers = topSellers.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.format("%.2f average rating", entry.getValue()),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return ResponseEntity.status(HttpStatus.OK).body(formattedTopSellers);
    }

    public ResponseEntity<?> filterSellerByRating(double ratingLower, double ratingUpper, double ratingEquals){
        Map<String, Double> sellersRating = new HashMap<>();

        var sellerList = sellerRepository.findAll();

        if (sellerList.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sellers found !");
        }

        sellersRating = sellerList.stream()
                .filter(seller -> seller.getComments() != null)
                .collect(Collectors.groupingBy(
                        Seller::getEmail,
                        Collectors.averagingDouble(seller -> seller.getComments().stream()
                                .filter(comment -> comment != null && comment.getStatus() != Status.PENDING)
                                .mapToDouble(Comment::getRating)
                                .average()
                                .orElse(0.0))
                ));

        var filteredRatings = sellersRating.entrySet().stream()
                .filter(key -> ratingLower ==0 || key.getValue() > ratingLower)
                .filter(key -> ratingUpper == 0 || key.getValue() < ratingUpper)
                .filter(key -> ratingEquals == 0 || key.getValue() == ratingEquals)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (filteredRatings.isEmpty()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No seller for the given rating criteria");
        }

        var formattedTopSellers = filteredRatings.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.format("%.2f average rating", entry.getValue()),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return ResponseEntity.status(HttpStatus.OK).body(formattedTopSellers);
    }

    public ResponseEntity<?> filterGameObject(String gameObjectTitle, int idLess, int idMore, int idEquals, int sellerId, LocalDate createdBefore, LocalDate createdAfter){
        var sellersList = sellerRepository.findAll();

        if (sellersList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No gameObjects found !");
        }

        var gameObjectsWanted = sellersList.stream()
                .filter(seller -> seller.getGameObjects() != null)
                .collect(Collectors.toMap(
                        Seller::getEmail,
                        seller -> seller.getGameObjects().stream()
                                .filter(gameObject -> gameObjectTitle == null || gameObject.getTitle().equals(gameObjectTitle))
                                .filter(gameObject -> idLess == 0 || gameObject.getId() < idLess)
                                .filter(gameObject -> idMore == 0 || gameObject.getId() > idMore)
                                .filter(gameObject -> idEquals == 0 || gameObject.getId() == idEquals)
                                .filter(gameObject -> sellerId == 0 || gameObject.getSeller().getId() == sellerId)
                                .filter(gameObject -> createdAfter == null || gameObject.getCreated_at().isAfter(createdAfter))
                                .filter(gameObject -> createdBefore == null || gameObject.getCreated_at().isBefore(createdBefore))
                                .collect(Collectors.toList())
                        )
                );
        if (gameObjectsWanted.isEmpty()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No gameObjects with the provided criteria !");
        }

        return ResponseEntity.status(HttpStatus.OK).body(gameObjectsWanted);
    }

    public ResponseEntity<?> viewProfile(){

        var sellers = sellerRepository.findAll();

        if (sellers.isEmpty()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sellers found !");
        }


        var sellerProfileInfo = sellers.stream()
                .collect(Collectors.toMap(
                        seller -> new ProfileData(seller.getEmail(), seller.getFirstName(),seller.getLastName()),
                        seller -> Optional.ofNullable(seller.getGameObjects()).orElse(Collections.emptyList()),
                        (current, proposed) -> current
                ));

        return ResponseEntity.status(HttpStatus.OK).body(sellerProfileInfo);
    }

}
