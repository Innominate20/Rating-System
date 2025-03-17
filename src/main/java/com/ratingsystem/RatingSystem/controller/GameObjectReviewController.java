package com.ratingsystem.RatingSystem.controller;

import com.ratingsystem.RatingSystem.service.GameObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
public class GameObjectReviewController {

    private final GameObjectService gameObjectService;

    @Autowired
    public GameObjectReviewController(GameObjectService gameObjectService) {
        this.gameObjectService = gameObjectService;
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> reviewSellers(){
        return gameObjectService.getGameObjects();
    }


}
