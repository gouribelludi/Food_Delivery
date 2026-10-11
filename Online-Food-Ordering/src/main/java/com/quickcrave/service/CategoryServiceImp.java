package com.quickcrave.service;

import com.quickcrave.model.Category;
import com.quickcrave.model.Resturant;
import com.quickcrave.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {


    @Autowired
    private RestautaurntService restautaurntService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(String name, Long userId) throws Exception {

        Resturant resturant=restautaurntService.getResturantByUserId(userId);
        Category category=new Category();
        category.setName(name);
        category.setResturant(resturant);

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoryByRestaurantId(Long id) throws Exception {
       Resturant resturant=restautaurntService.findResturantById(id);
        return categoryRepository.findByResturantId(id);
    }

    @Override
    public Category findCategoryById(Long id) throws Exception {
        Optional<Category> optionalCategory=categoryRepository.findById(id);

        if (optionalCategory.isEmpty()
        ) {

            throw  new Exception("category not found");
        }
        return optionalCategory.get();
    }
}
