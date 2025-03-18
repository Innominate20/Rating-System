package com.ratingsystem.RatingSystem.repository;

import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.enums.Status;
import io.lettuce.core.dynamic.annotation.Param;
import org.hibernate.mapping.Selectable;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
    Optional<Seller> findByEmail(String email);

    List<Seller> findByStatus(Status status);

    @Query("SELECT c FROM Seller c WHERE c.email IN :emails")
    List<Seller> findAllByEmail(@Param("emails") List<String> emails);
}
