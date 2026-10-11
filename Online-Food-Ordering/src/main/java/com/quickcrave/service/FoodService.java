package com.quickcrave.service;

import com.quickcrave.model.Category;
import com.quickcrave.model.Food;
import com.quickcrave.model.Resturant;
import com.quickcrave.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    public Food createFood(CreateFoodRequest req , Category category, Resturant resturant);

    void  deletFood(long foodId) throws  Exception;

    public List<Food> getRestaurantsFood(Long restaurantId ,
                                         boolean isVegitarin,
                                         boolean isNonVeg ,
                                         boolean isSeasonaol ,
                                         String foodCategory);

    //to search food
    public  List<Food> searchFood(String keyword);

    //to search food using id
    public Food findFoodById(Long foodId) throws Exception;

    //to see food is avaliable or not
    public Food updateAvalilibilityStatus(Long foodId) throws Exception;
}
