package com.ratingsystem.RatingSystem.controller;

import com.ratingsystem.RatingSystem.dto.GameObjectRequest;
import com.ratingsystem.RatingSystem.entity.GameObject;
import com.ratingsystem.RatingSystem.repository.GameObjectRepository;
import com.ratingsystem.RatingSystem.service.GameObjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gameObject")
public class GameObjectController {

    private final GameObjectService gameObjectService;

    @Autowired
    public GameObjectController(GameObjectService gameObjectService) {
        this.gameObjectService = gameObjectService;
    }

    @PostMapping("/item")
    public ResponseEntity<String> addGameObject(@Valid @RequestBody GameObjectRequest gameObjectRequest){
        return gameObjectService.saveGameObject(gameObjectRequest);
    }

    @GetMapping("/item/all")
    public ResponseEntity<?> getGameObjects(){
        return gameObjectService.getGameObjects();
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<String> updateGameObject(@PathVariable("id") int id, @RequestBody GameObjectRequest gameObjectRequest){
        return gameObjectService.updateGameObject(id,gameObjectRequest);
    }

    @DeleteMapping("item/{id}")
    public ResponseEntity<String> deleteGameObject(@PathVariable("id") int id){
        return gameObjectService.deleteGameObject(id);
    }

    @GetMapping("/item/me")
    public ResponseEntity<?> getMyItems(){
        return gameObjectService.getMyGameObjects();
    }
}
