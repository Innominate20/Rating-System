package com.ratingsystem.RatingSystem.repository;

import com.ratingsystem.RatingSystem.entity.GameObject;
import com.ratingsystem.RatingSystem.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameObjectRepository extends JpaRepository<GameObject, Integer> {
    Optional<List<GameObject>> findBySeller(Seller seller);
}
