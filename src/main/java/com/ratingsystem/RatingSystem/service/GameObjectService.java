package com.ratingsystem.RatingSystem.service;

import com.ratingsystem.RatingSystem.dto.GameObjectRequest;
import com.ratingsystem.RatingSystem.entity.GameObject;
import com.ratingsystem.RatingSystem.entity.Seller;
import com.ratingsystem.RatingSystem.repository.GameObjectRepository;
import com.ratingsystem.RatingSystem.repository.SellerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameObjectService {
    private final GameObjectRepository gameObjectRepository;
    private final SellerRepository sellerRepository;

    @Autowired
    public GameObjectService(GameObjectRepository gameObjectRepository, SellerRepository userRepository) {
        this.gameObjectRepository = gameObjectRepository;
        this.sellerRepository = userRepository;
    }

    public Seller findUserHelper(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Optional<Seller> optional =  sellerRepository.findByEmail(userEmail);

        return optional.get();
    }

    @Transactional
    public ResponseEntity<String> saveGameObject(GameObjectRequest gameObjectRequest){
        Seller seller = findUserHelper();

        var gameObjectToSave = GameObject.builder()
                .text(gameObjectRequest.getText())
                .title(gameObjectRequest.getTitle())
                .created_at(LocalDate.now())
                .updated_at(LocalDate.now())
                .seller(seller)
                .build();

        gameObjectRepository.save(gameObjectToSave);

        return ResponseEntity.status(HttpStatus.CREATED).body("GameObject saved successfully !");
    }

    @Transactional
    public ResponseEntity<String> updateGameObject(int id, GameObjectRequest gameObjectRequest){

        Seller seller = findUserHelper();
        Optional<GameObject> optional = gameObjectRepository.findById(id);

        if (optional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("GameObject not found !");
        }
        GameObject tmpGameObject = optional.get();

        if (tmpGameObject.getSeller().getId() != seller.getId()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of the GameObject !");
        }
        tmpGameObject.setText(gameObjectRequest.getText());
        tmpGameObject.setTitle(gameObjectRequest.getTitle());
        tmpGameObject.setUpdated_at(LocalDate.now());

        gameObjectRepository.save(tmpGameObject);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("GameObject updated successfully !");
    }

    @Transactional
    public ResponseEntity<String> deleteGameObject(int id){
        Seller seller = findUserHelper();

        Optional<GameObject> optional = gameObjectRepository.findById(id);
        if (optional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("GameObject with the id : "+id+ " not found !");
        }

        GameObject gameObjectToDelete = optional.get();

        if(gameObjectToDelete.getSeller().getId() != seller.getId()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of the GameObject");
        }

        gameObjectRepository.delete(gameObjectToDelete);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("GameObject deleted successfully !");
    }

    public ResponseEntity<?> getGameObjects(){
        List<GameObject> gameObjectsList = gameObjectRepository.findAll();

        if (gameObjectsList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No gameObjects found !");
        }

        var itemsBySeller = gameObjectsList.stream()
                .collect(Collectors.groupingBy(seller-> seller.getSeller().getEmail()));

        return ResponseEntity.ok(itemsBySeller);
    }

    public ResponseEntity<?> getMyGameObjects(){
        Seller seller = findUserHelper();

        Optional<List<GameObject>> optionalItems = gameObjectRepository.findBySeller(seller);

        if (optionalItems.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User : "+ seller.getEmail() + " does not have gameObjects yet !");
        }

        return ResponseEntity.status(HttpStatus.OK).body(optionalItems.get());
    }
}
