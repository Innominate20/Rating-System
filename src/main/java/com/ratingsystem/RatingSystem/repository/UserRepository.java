package com.ratingsystem.RatingSystem.repository;

import com.ratingsystem.RatingSystem.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
