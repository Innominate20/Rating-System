package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.entity.Comment;
import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.enums.Role;
import com.ratingsystem.RatingSystem.enums.Status;
import com.ratingsystem.RatingSystem.repository.CommentRepository;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import jakarta.transaction.Transactional;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final SellerRepository sellerRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public AdminService(SellerRepository sellerRepository, CommentRepository commentRepository) {
        this.sellerRepository = sellerRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public ResponseEntity<String> approveUser(String email){
        Optional<Seller> optionalSeller = sellerRepository.findByEmail(email);
        if (optionalSeller.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No users with this email found !");
        }
        Seller sellerToApprove = optionalSeller.get();

        sellerToApprove.setStatus(Status.APPROVED);
        sellerRepository.save(sellerToApprove);
        return ResponseEntity.status(HttpStatus.OK).body("User approved successfully !");
    }

    public ResponseEntity<?> getSellers(){

        var sellers  = sellerRepository.findAll();
        if(sellers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sellers found !");
        }
        return ResponseEntity.status(HttpStatus.OK).body(sellers);
    }

    @Transactional
    public ResponseEntity<?> approveComment(int id){
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if(optionalComment.isEmpty()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment with the id : " + id+ " not found !");
        }

        Comment comment = optionalComment.get();
        comment.setStatus(Status.APPROVED);

        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body("Comment approved successfully !");
    }

}
