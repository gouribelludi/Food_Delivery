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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestautaurntService restautaurntService;


    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String name,
                                           @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

        List<Food> food=foodService.searchFood(name);

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getResturantFood(@RequestParam (required = false) boolean vegetarian,
                                                       @RequestParam (required = false) boolean seasonal,
                                                       @RequestParam (required = false) boolean nonveg,
                                                       @RequestParam (required = false) String food_categoary,
                                                 @PathVariable Long restaurantId,
                                                 @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user=userService.findUserByjwtToken(jwt);

        List<Food> food=foodService.getRestaurantsFood(restaurantId,vegetarian,nonveg,seasonal,food_categoary);

        return new ResponseEntity<>(food, HttpStatus.OK);
    }



}
