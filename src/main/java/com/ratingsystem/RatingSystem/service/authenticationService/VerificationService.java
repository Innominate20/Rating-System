package com.ratingsystem.RatingSystem.service.authenticationService;

import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationService {

    private final RedisService redisService;
    private final SellerRepository sellerRepository;

    @Autowired
    public VerificationService(RedisService redisService,SellerRepository sellerRepository) {
        this.redisService = redisService;
        this.sellerRepository = sellerRepository;
    }

    @Transactional
    public ResponseEntity<String> verificationMethod(String token){

        Optional<String> optional = redisService.getUserEmail(token);

        if(optional.isPresent()){
            String email = optional.get();
            Optional<Seller> userToVerify =  sellerRepository.findByEmail(email);
            Seller tmpSeller = userToVerify.get();
            tmpSeller.setVerified(true);
            sellerRepository.save(tmpSeller);

            redisService.deleteToken(token);
           return ResponseEntity.status(HttpStatus.ACCEPTED).body("User verified successfully !");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong token or Time expired !");
    }
}
