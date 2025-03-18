package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.enums.Status;
import com.ratingsystem.RatingSystem.exception.InvalidIdException;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public ResponseEntity<?> findUnapprovedSellers(){
        var unapprovedSellers = sellerRepository.findByStatus(Status.PENDING);

        if (unapprovedSellers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No unapproved sellers found !");
        }

        return ResponseEntity.status(HttpStatus.OK).body(unapprovedSellers);
    }

    @Transactional
    public ResponseEntity<?> changeStatusToApproved(String ids){

        try {
            List<Integer> idList = Arrays.stream(ids.split(","))
                    .map(Integer::parseInt)
                    .toList();

            var sellersById = sellerRepository.findAllById(idList);

            if (sellersById.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No unapproved sellers with the id : " + ids);
            }

            var approvedSellers = sellersById.stream()
                    .peek(comment -> comment.setStatus(Status.APPROVED))
                    .toList();

            int numberOfApprovedSellers = approvedSellers.size();

            sellerRepository.saveAll(approvedSellers);
            return ResponseEntity.status(HttpStatus.OK).body(numberOfApprovedSellers +" sellers approved successfully !");
        }catch (Exception e){
            throw new InvalidIdException("InvalidIdFormat : " + ids);
        }
    }
}
