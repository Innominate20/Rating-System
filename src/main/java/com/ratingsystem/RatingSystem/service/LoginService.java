package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.dto.LoginRequest;
import com.ratingsystem.RatingSystem.entity.Admin;
import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.enums.Status;
import com.ratingsystem.RatingSystem.jwt.JwtUtility;
import com.ratingsystem.RatingSystem.repository.AdminRepository;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;
    private final JwtUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public LoginService(SellerRepository userRepository, AdminRepository adminRepository, JwtUtility jwtUtility, AuthenticationManager authenticationManager) {
        this.sellerRepository = userRepository;
        this.adminRepository = adminRepository;
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<String> loginUser(LoginRequest loginRequest){
        Optional<Seller> optionalSeller = sellerRepository.findByEmail(loginRequest.getEmail());

        if (optionalSeller.isPresent()){

            Seller tmpUSeller = optionalSeller.get();
            if (!tmpUSeller.isVerified()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User needs verification !");
            }
            else if(tmpUSeller.getStatus() != Status.APPROVED){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not approved by the administrator yet !");
            }

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
            );
            UserDetails userDetails  = (UserDetails) authentication.getPrincipal();

            String token = jwtUtility.generateToke(userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
        }

        else {
            Optional<Admin> optionalAdmin = adminRepository.findByEmail(loginRequest.getEmail());

            if (optionalAdmin.isPresent()){

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
                );
                UserDetails userDetails  = (UserDetails) authentication.getPrincipal();

                String token = jwtUtility.generateToke(userDetails.getUsername());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found !");
    }

}
