package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.exception.InvalidIdException;
import com.ratingsystem.RatingSystem.util.CookieUtility;
import com.ratingsystem.RatingSystem.dto.CommentRequest;
import com.ratingsystem.RatingSystem.dto.UserRegisterRequest;
import com.ratingsystem.RatingSystem.entity.Comment;
import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.enums.Status;
import com.ratingsystem.RatingSystem.repository.CommentRepository;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    private final SellerRepository sellerRepository;
    private final CommentRepository commentRepository;
    private final RegisterService registerService;

    @Autowired
    public CommentService(SellerRepository sellerRepository, CommentRepository commentRepository, RegisterService registerService) {
        this.sellerRepository = sellerRepository;
        this.commentRepository = commentRepository;
        this.registerService = registerService;
    }

    @Transactional
    public ResponseEntity<String> submitComment(String email, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, CommentRequest commentRequest){
        Optional<Seller> optionalSeller = sellerRepository.findByEmail(email);

        if (optionalSeller.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller's profile not found !");
        }

        Seller seller = optionalSeller.get();

        String isCookiePresent = CookieUtility.getIdFromCookie(httpServletRequest,"UserId");

        if (isCookiePresent == null){
            UUID uuid = UUID.randomUUID();
            Cookie cookie = new Cookie("UserId", uuid.toString());
            cookie.setMaxAge(60 * 60 * 24 * 360);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            httpServletResponse.addCookie(cookie);

            var comment = Comment.builder()
                    .authorId(uuid)
                    .created_at(LocalDate.now())
                    .rating(commentRequest.getRating())
                    .message(commentRequest.getMessage())
                    .status(Status.PENDING)
                    .seller(seller)
                    .build();

            commentRepository.save(comment);

            return ResponseEntity.status(HttpStatus.CREATED).body("Comment saved, waiting administrator approval ! \n Warning : Do not delete cookies in order to keep connection with your comments !");
        }

        var comment = Comment.builder()
                .authorId(UUID.fromString(isCookiePresent))
                .created_at(LocalDate.now())
                .rating(commentRequest.getRating())
                .message(commentRequest.getMessage())
                .status(Status.PENDING)
                .seller(seller)
                .build();

        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body("Comment saved, waiting administrator approval !");
    }

    public ResponseEntity<?> getSellerComments(String email){

        Optional<Seller> optionalSeller = sellerRepository.findByEmail(email);

        if (optionalSeller.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller's profile not found !");
        }

        Seller seller = optionalSeller.get();

        var commentList = seller.getComments();

        if (commentList == null){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller has no comments yet !");
        }


        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    public ResponseEntity<?> getComment(int id){
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No comments found with the id : "+ id);
        }

        Comment comment = optionalComment.get();

        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @Transactional
    public ResponseEntity<String> deleteComment(int id, HttpServletRequest httpServletRequest){

        String uuid = CookieUtility.getIdFromCookie(httpServletRequest, "UserId");

        if (uuid == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You do not have comment ownership !");
        }

        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment with the id : "+ id + " - does not exists !");
        }

        Comment comment = optionalComment.get();

        if (! comment.getAuthorId().equals(UUID.fromString(uuid))){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the author of the comment !");
        }

        commentRepository.delete(comment);

        return ResponseEntity.status(HttpStatus.OK).body("Comment deleted successfully !");
    }

    @Transactional
    public ResponseEntity<String> updateComment(int id, HttpServletRequest httpServletRequest, CommentRequest commentRequest){

        String uuid = CookieUtility.getIdFromCookie(httpServletRequest, "UserId");

        if (uuid == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You do not have comment ownership !");
        }

        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment with the id : "+ id + "- does not exists !");
        }

        Comment comment = optionalComment.get();

        if (! comment.getAuthorId().equals(UUID.fromString(uuid))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the author of the comment !");
        }

        comment.setMessage(commentRequest.getMessage());
        comment.setRating(commentRequest.getRating());
        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body("Comment updated successfully !");
    }

    @Transactional
    public ResponseEntity<?> createSellerFromComment(UserRegisterRequest userRegisterRequest){

        return registerService.registerSeller(userRegisterRequest);
    }

    public ResponseEntity<?> getComments(HttpServletRequest request){

         String uuid = CookieUtility.getIdFromCookie(request,"UserId");

         if (uuid == null){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You have no comments or lost association with them !");
         }

        var commentList =  commentRepository.findByAuthorId(UUID.fromString(uuid));

        if (commentList.isEmpty()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No comments found !");
        }

        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    //retrieve unapproved comments
    public ResponseEntity<?> findUnapprovedComments(){

        var pendingComments = commentRepository.findByStatus(Status.PENDING);

        if (pendingComments.isEmpty()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No unapproved comments !");
            }

        return ResponseEntity.status(HttpStatus.OK).body(pendingComments);
    }


    @Transactional
    public ResponseEntity<?> changeStatusToApproved(String ids){

        try {
            List<Integer> idList = Arrays.stream(ids.split(","))
                    .map(Integer::parseInt)
                    .toList();

            var commentsToApprove = commentRepository.findAllById(idList);

            if (commentsToApprove.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No unapproved comments with the id : "+ids);
            }

            var approvedComments = commentsToApprove.stream()
                    .peek(comment -> comment.setStatus(Status.APPROVED))
                    .toList();

            int numberOfApprovedComments = approvedComments.size();

            commentRepository.saveAll(approvedComments);
            return ResponseEntity.status(HttpStatus.OK).body(numberOfApprovedComments +" comments approved successfully !");
        }catch (Exception e){
            throw new InvalidIdException("InvalidIdFormat : " + ids);
        }

    }

}
