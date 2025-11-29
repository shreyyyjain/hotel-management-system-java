package com.shrey.hotel.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shrey.hotel.model.FoodItem;
import com.shrey.hotel.repository.FoodItemRepository;

@RestController
@RequestMapping("/food")
public class FoodController {
    private final FoodItemRepository foodItemRepository;

    public FoodController(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFood() {
        return ResponseEntity.ok(foodItemRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getFoodById(@PathVariable long id) {
        return foodItemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<List<FoodItem>> getFoodByCuisine(@PathVariable String cuisine) {
        return ResponseEntity.ok(foodItemRepository.findByCuisine(cuisine));
    }

    @GetMapping("/cuisines")
    public ResponseEntity<List<String>> getAllCuisines() {
        List<String> cuisines = foodItemRepository.findAll().stream()
                .map(FoodItem::getCuisine)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return ResponseEntity.ok(cuisines);
    }
}
