package com.ratingsystem.RatingSystem.repository;

import com.ratingsystem.RatingSystem.entity.Comment;
import com.ratingsystem.RatingSystem.enums.Status;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByAuthorId(UUID authorId);
    List<Comment> findByStatus(Status status);
}
