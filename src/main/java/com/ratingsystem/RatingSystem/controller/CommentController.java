package com.ratingsystem.RatingSystem.controller;

import com.ratingsystem.RatingSystem.dto.CommentRequest;
import com.ratingsystem.RatingSystem.dto.UserRegisterRequest;
import com.ratingsystem.RatingSystem.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {

        this.commentService = commentService;
    }

    @PostMapping("/comment/{email}")
    public ResponseEntity<String> submitCommentMethod(@PathVariable("email") String email, HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody CommentRequest commentRequest){

        return commentService.submitComment(email,request,response,commentRequest);
    }

    @GetMapping("/comment/me")
    public ResponseEntity<?> getMyComments(HttpServletRequest request){

        return commentService.getComments(request);
    }

    @GetMapping("/comment/email/{email}")
    public ResponseEntity<?> getSellerCommentMethod(@PathVariable("email") String email){

        return commentService.getSellerComments(email);
    }

    @GetMapping("/comment/id/{id}")
    public ResponseEntity<?> getCommentMethod(@PathVariable("id") int id){

        return commentService.getComment(id);
    }

    @DeleteMapping("comment/{id}")
    public ResponseEntity<?> deleteCommentMethod(@PathVariable("id") int id,HttpServletRequest request){

        return commentService.deleteComment(id,request);
    }

    @PutMapping("comment/{id}")
    public ResponseEntity<?> updateCommentMethod(@PathVariable("id") int id,HttpServletRequest request, @RequestBody CommentRequest commentRequest){

        return commentService.updateComment(id, request,commentRequest);
    }

    @PostMapping("/comment/create/profile")
    public ResponseEntity<?> createSellerProfile(@Valid @RequestBody UserRegisterRequest userRegisterRequest){

        return commentService.createSellerFromComment(userRegisterRequest);
    }

}
