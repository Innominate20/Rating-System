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

    private final SellerService sellerService;
    private final CommentService commentService;

    @Autowired
    public AdminService(SellerService sellerService, CommentService commentService) {

        this.sellerService = sellerService;
        this.commentService = commentService;
    }

    @Transactional
    public ResponseEntity<?> approveSellers(String ids){

        return sellerService.changeStatusToApproved(ids);
    }

    public ResponseEntity<?> getUnapprovedSellers(){

        return sellerService.findUnapprovedSellers();
    }

    @Transactional
    public ResponseEntity<?> approveComments(String ids){

        return commentService.changeStatusToApproved(ids);
    }

    public ResponseEntity<?> getUnapprovedComments(){

        return commentService.findUnapprovedComments();
    }

}
