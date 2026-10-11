package com.quickcrave.controller;

import com.quickcrave.model.Food;
import com.quickcrave.model.Resturant;
import com.quickcrave.model.User;
import com.quickcrave.request.CreateFoodRequest;
import com.quickcrave.response.MessageResponse;
import com.quickcrave.service.FoodService;
import com.quickcrave.service.RestautaurntService;
import com.quickcrave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestautaurntService restautaurntService;


    @PostMapping()
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req,
                                           @RequestHeader("Authorization") String jwt) throws Exception
    {
        Resturant restaurant = getUserRestaurant(jwt);
        Food food = foodService.createFood(req, req.getCategory(), restaurant);

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String jwt) throws Exception
    {
        Resturant restaurant = getUserRestaurant(jwt);
        Food food = foodService.findFoodById(id);
        ensureFoodBelongsToRestaurant(food, restaurant);
        foodService.deletFood(id);

        MessageResponse response = new MessageResponse();
        response.setMessage("Food deleted successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFoodAvaibilityStatus(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String jwt) throws Exception
    {
        Resturant restaurant = getUserRestaurant(jwt);
        Food existingFood = foodService.findFoodById(id);
        ensureFoodBelongsToRestaurant(existingFood, restaurant);
        Food food = foodService.updateAvalilibilityStatus(id);

        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    private Resturant getUserRestaurant(String jwt) throws Exception {
        User user = userService.findUserByjwtToken(jwt);
        Resturant restaurant = restautaurntService.getResturantByUserId(user.getId());
        if (restaurant == null || restaurant.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found for this user.");
        }
        return restaurant;
    }

    private void ensureFoodBelongsToRestaurant(Food food, Resturant restaurant) {
        if (food.getResturant() == null
                || !restaurant.getId().equals(food.getResturant().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Menu item not found for this restaurant."
            );
        }
    }

}
