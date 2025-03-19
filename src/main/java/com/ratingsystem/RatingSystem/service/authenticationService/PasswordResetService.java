package com.ratingsystem.RatingSystem.service.authenticationService;

import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.exception.MailSendFailure;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import com.ratingsystem.RatingSystem.util.TokenGeneratorUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    private final RedisService redisService;
    private final EmailService emailService;
    private final SellerRepository sellerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(RedisService redisService, EmailService emailService, SellerRepository sellerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.redisService = redisService;
        this.emailService = emailService;
        this.sellerRepository = sellerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> sendPasswordResetCode(String email){

        var optionalSeller = sellerRepository.findByEmail(email);
        if (optionalSeller.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with this email does not exist !");
        }

        String token  = TokenGeneratorUtil.generateToken();

        redisService.storeToken(token, email);

        try {
            emailService.sendVerificationCode(email, token);
        }catch (MessagingException messagingException){

            throw new MailSendFailure("Email failed to send : "+ messagingException.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Verification code sent to the email address!");
    }

    @Transactional
    public ResponseEntity<?> resetPassword(String token, String password){

        var optionalEmail = redisService.getUserEmail(token);

        if (optionalEmail.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong token or time expired !");
        }

        String sellerEmail = optionalEmail.get();

         var optionalSeller = sellerRepository.findByEmail(sellerEmail);

         Seller sellerToChangePassword = optionalSeller.get();

         sellerToChangePassword.setPassword(passwordEncoder.encode(password));

         sellerRepository.save(sellerToChangePassword);

         redisService.deleteToken(token);

         return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully !");
    }

    public ResponseEntity<?> validateToken(String token){

        var optionalEmail = redisService.getUserEmail(token);

        if (optionalEmail.isEmpty()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token is not valid or is expired !");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Token is valid !");
    }

}
