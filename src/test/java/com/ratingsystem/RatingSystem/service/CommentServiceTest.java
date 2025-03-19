package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.entity.Comment;
import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testGetComment(){

        Comment comment = Comment.builder()

                .message("new comment")
                .build();

         commentRepository.save(comment);

         ResponseEntity<?> response = commentService.getComment(comment.getId());

         assertEquals(HttpStatus.OK, response.getStatusCode(), "Status codes should be the same !");
    }

}
