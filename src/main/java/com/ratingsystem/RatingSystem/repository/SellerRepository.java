package com.ratingsystem.RatingSystem.repository;

import com.ratingsystem.RatingSystem.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
    Optional<Seller> findByEmail(String email);
}
