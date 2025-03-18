package com.ratingsystem.RatingSystem.repository;

import com.ratingsystem.RatingSystem.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
