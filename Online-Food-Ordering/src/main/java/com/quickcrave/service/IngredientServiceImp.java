package com.quickcrave.service;

import com.quickcrave.model.IngredieantItems;
import com.quickcrave.model.IngredientCategory;
import com.quickcrave.model.Resturant;
import com.quickcrave.repository.IngredientCategoryRepository;
import com.quickcrave.repository.IngredientItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImp implements IngredientsService{

    @Autowired
    private IngredientItemRepository ingredientItemRepository;

    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository;

    @Autowired
    private RestautaurntService restautaurntService;


    @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {
        Resturant resturant=restautaurntService.findResturantById(restaurantId);

        IngredientCategory category=new IngredientCategory();
        category.setResturant(resturant);
        category.setName(name);
        return ingredientCategoryRepository.save(category);
    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long id) throws Exception {

        Optional<IngredientCategory> opt=ingredientCategoryRepository.findById(id);

        if(opt.isEmpty())
        {
            throw new Exception("ingrdeint category not found");
        }

        return opt.get();
    }

    @Override
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception {

        restautaurntService.findResturantById(id);
        return ingredientCategoryRepository.findByResturantId(id);
    }

    @Override
    public IngredieantItems createIngredientItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception {

        Resturant resturant=restautaurntService.findResturantById(restaurantId);
        IngredientCategory category=findIngredientCategoryById(categoryId);

        IngredieantItems item=new IngredieantItems();
        item.setName(ingredientName);
        item.setResturant(resturant);
        item.setCategory(category);

        IngredieantItems ingredient=ingredientItemRepository.save(item);
        category.getIngredieant().add(ingredient);
        return ingredient;
    }

    @Override
    public List<IngredieantItems> findRestaurantIngredients(Long restaurantId) {

        return ingredientItemRepository.findByResturantId(restaurantId);
    }

    @Override
    public IngredieantItems updateStock(Long id) throws Exception {
        Optional<IngredieantItems> optionalIngredieantItem=ingredientItemRepository.findById(id);
        if(optionalIngredieantItem.isEmpty())
        {
            throw  new Exception("Ingredient not found");
        }
        IngredieantItems ingredieantItem=optionalIngredieantItem.get();
        ingredieantItem.setInStock(!ingredieantItem.isInStock());

        return ingredientItemRepository.save(ingredieantItem);
    }
}

