package com.ratingsystem.RatingSystem.controller;

import com.ratingsystem.RatingSystem.cookieUtil.CookieUtility;
import com.ratingsystem.RatingSystem.dto.CommentRequest;
import com.ratingsystem.RatingSystem.dto.UserRegisterRequest;
import com.ratingsystem.RatingSystem.entity.Comment;
import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.enums.Status;
import com.ratingsystem.RatingSystem.repository.CommentRepository;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import com.ratingsystem.RatingSystem.service.CommentService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/comment/{email}")
    public ResponseEntity<?> getSellerCommentMethod(@PathVariable("email") String email){
        return commentService.getSellerComments(email);
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<?> getCommentMethod(@PathVariable("id") int id){

        return commentService.getComment(id);
    }

    @DeleteMapping("comment/id")
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
