package com.quickcrave.service;

import com.quickcrave.model.IngredieantItems;
import com.quickcrave.model.IngredientCategory;

import java.util.List;

public interface
IngredientsService {

    public IngredientCategory createIngredientCategory (String name,Long restaurantId) throws Exception;

    public IngredientCategory findIngredientCategoryById(Long id) throws Exception;

    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception;

    public IngredieantItems createIngredientItem(Long restaurantId,String ingredientName,Long categoryId)throws Exception;

    public List<IngredieantItems> findRestaurantIngredients(Long restaurantId);

    public IngredieantItems updateStock(Long id) throws Exception;
}
