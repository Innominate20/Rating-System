package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.entity.Admin;
import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.repository.AdminRepository;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public MyUserDetailService(SellerRepository sellerRepository, AdminRepository adminRepository) {
        this.sellerRepository = sellerRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<Seller> optionalSeller = sellerRepository.findByEmail(userEmail);
        String userToAuthenticateEmail ;
        String userToAuthenticatePassword;
        String userToAuthenticateRole;

        if (optionalSeller.isPresent()){
           var user =  optionalSeller.get();
           userToAuthenticateEmail = user.getEmail();
           userToAuthenticatePassword = user.getPassword();
           userToAuthenticateRole = user.getRole().toString();
        }
        else if(adminRepository.findByEmail(userEmail).isPresent()){
            var user = adminRepository.findByEmail(userEmail).get();
            userToAuthenticateEmail = user.getEmail();
            userToAuthenticatePassword = user.getPassword();
            userToAuthenticateRole = user.getRole().toString();
        }
        else {
            throw new UsernameNotFoundException("User not found !");
        }


        return new User(
                userToAuthenticateEmail,
                userToAuthenticatePassword,
               List.of(new SimpleGrantedAuthority("ROLE_" + userToAuthenticateRole))
        );
    }
}
