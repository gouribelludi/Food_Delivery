package com.quickcrave.service;

import com.quickcrave.model.Category;
import com.quickcrave.model.Food;
import com.quickcrave.model.IngredieantItems;
import com.quickcrave.model.Resturant;
import com.quickcrave.repository.IngredientItemRepository;
import com.quickcrave.repository.FoodRepository;
import com.quickcrave.request.CreateFoodRequest;
import com.quickcrave.request.IngredientReferenceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.quickcrave.repository.CategoryRepository;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FoodServiceImp implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IngredientItemRepository ingredientItemRepository;


    @Override
    public Food createFood(CreateFoodRequest req, Category category, Resturant resturant) {
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Food details are required.");
        }
        if (category == null || category.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A food category is required.");
        }
        if (resturant == null || resturant.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found.");
        }
        if (req.getName() == null || req.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Food name is required.");
        }
        if (req.getPrice() == null || req.getPrice() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Food price must be greater than zero.");
        }

        Category persistedCategory = categoryRepository.findById(category.getId())
                .filter(existing -> existing.getResturant() != null
                        && existing.getResturant().getId().equals(resturant.getId()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "The selected category does not belong to this restaurant."
                ));

        Food food = new Food();
        food.setFoodCategory(persistedCategory);
        food.setResturant(resturant);
        food.setDescription(req.getDescription());
        food.setImages(req.getImages() == null ? new ArrayList<>() : req.getImages());
        food.setName(req.getName());
        food.setPrice(req.getPrice());
        food.setIngredieantItems(resolveIngredients(req.getIngredients(), resturant));
        food.setAvailable(true);
        food.setSeasonal(Boolean.TRUE.equals(req.getSeasonal()));
        food.setVegetarian(Boolean.TRUE.equals(req.getVegetarian()));
        food.setCreationDate(new Date());

        Food saveFood= foodRepository.save(food);
        resturant.getFoods().add(saveFood);
        return saveFood;
    }

    private List<IngredieantItems> resolveIngredients(
            List<IngredientReferenceRequest> references,
            Resturant resturant
    ) {
        if (references == null || references.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> ingredientIds = references.stream()
                .map(IngredientReferenceRequest::getId)
                .toList();
        if (ingredientIds.stream().anyMatch(id -> id == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredient IDs are required.");
        }

        Map<Long, IngredieantItems> ingredientsById = ingredientItemRepository.findAllById(ingredientIds)
                .stream()
                .collect(Collectors.toMap(IngredieantItems::getId, Function.identity()));

        return ingredientIds.stream()
                .map(id -> {
                    IngredieantItems ingredient = ingredientsById.get(id);
                    if (ingredient == null || ingredient.getResturant() == null
                            || !resturant.getId().equals(ingredient.getResturant().getId())) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "The selected ingredient does not belong to this restaurant."
                        );
                    }
                    return ingredient;
                })
                .toList();
    }

    @Override
    public void deletFood(long foodId) throws Exception {


        Food food=findFoodById(foodId);
        Resturant resturant = food.getResturant();
        food.setResturant(null);
        foodRepository.save(food);
        if (resturant != null) {
            resturant.getFoods().removeIf(existing -> existing.getId().equals(foodId));
        }
    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isVegitarin,
                                         boolean isNonVeg,
                                         boolean isSeasonaol,
                                         String foodCategory) {
        List<Food> foods=foodRepository.findByResturantId(restaurantId);

        if(isVegitarin)
        {
            foods=filterByVegetarian(foods,isVegitarin);
        }
        if(isNonVeg)
        {
            foods=filterByNonveg(foods,isNonVeg);

        }
        if(isSeasonaol)
        {
            foods=filterBySeasonal(foods,isSeasonaol);
        }
        if(foodCategory!=null && !foodCategory.equals(""))
        {
            foods=filterByCategory(foods,foodCategory);
        }
        return foods;
    }

    private List<Food> filterByCategory(List<Food> foods,

                                        String foodCategory) {
        return foods.stream().filter(food -> {
            if(food.getFoodCategory()!=null)
            {
                return food.getFoodCategory().getName().equals(foodCategory);
            }
            return false;
        }).collect(Collectors.toList());

    }

    private List<Food> filterBySeasonal(List<Food> foods,
                                        boolean isSeasonaol) {
        return foods.stream().filter(food -> food.isSeasonal()== isSeasonaol).collect(Collectors.toList());

    }

    private List<Food> filterByNonveg(List<Food> foods, boolean isNonVeg) {
        return foods.stream().filter(food -> food.isVegetarian()== false).collect(Collectors.toList());

    }

    private List<Food> filterByVegetarian(List<Food> foods,
                                          boolean isVegitarin) {
        return foods.stream().filter(food -> food.isVegetarian()== isVegitarin).collect(Collectors.toList());

    }

    @Override
    public List<Food> searchFood(String keyword) {

        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        Optional<Food> optionalFood=foodRepository.findById(foodId);
        if(optionalFood.isEmpty())
        {
            throw new Exception("food not found .......");
        }
        return optionalFood.get();
    }

    @Override
    public Food updateAvalilibilityStatus(Long foodId) throws Exception {
        Food food=findFoodById(foodId);
        food.setAvailable(!food.isAvailable());

        return foodRepository.save(food);
    }
}
