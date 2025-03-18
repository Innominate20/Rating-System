package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.dto.UserRegisterRequest;
import com.ratingsystem.RatingSystem.entity.Admin;
import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.enums.Role;
import com.ratingsystem.RatingSystem.enums.Status;
import com.ratingsystem.RatingSystem.exception.MailSendFailure;
import com.ratingsystem.RatingSystem.repository.AdminRepository;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import com.ratingsystem.RatingSystem.util.TokenGeneratorUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class RegisterService {
    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisService redisService;

    @Autowired
    public RegisterService(SellerRepository sellerRepository, AdminRepository adminRepository, BCryptPasswordEncoder passwordEncoder,
                           EmailService emailService, RedisService redisService){
        this.sellerRepository = sellerRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.emailService = emailService;

    }

    @Transactional
    public ResponseEntity<String> registerSeller(UserRegisterRequest userRegisterRequest) {


        // check if the Seller already exists
        Optional<Seller> tmpSeller = sellerRepository.findByEmail(userRegisterRequest.getEmail());
        if (tmpSeller.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exists !");
        }

        Seller newSeller = Seller.builder()
                .firstName(userRegisterRequest.getFirstName())
                .lastName(userRegisterRequest.getLastName())
                .email(userRegisterRequest.getEmail())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .created_at(LocalDate.now())
                .role(Role.SELLER)
                .verified(false)
                .status(Status.PENDING)
                .build();

        sellerRepository.save(newSeller);


        String token = TokenGeneratorUtil.generateToken();
        redisService.storeToken(token,userRegisterRequest.getEmail());

        try {
            emailService.sendVerificationCode(userRegisterRequest.getEmail(),token);

        } catch (MessagingException e) {
            throw new MailSendFailure("Email failed to send : " + e.getMessage());
        }

        return  ResponseEntity.status(HttpStatus.CREATED).body("User created, waiting verification !");
    }

    public ResponseEntity<String> registerAdmin(UserRegisterRequest userRegisterRequest){
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(userRegisterRequest.getEmail());
        if (optionalAdmin.isPresent()){
            ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exists !");
        }

        Admin newAdmin = Admin.builder()
                .firstName(userRegisterRequest.getFirstName())
                .lastName(userRegisterRequest.getLastName())
                .created_at(LocalDate.now())
                .email(userRegisterRequest.getEmail())
                .verified(true)
                .status(Status.APPROVED)
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .role(Role.ADMIN)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body("User created, created !");
    }
}
