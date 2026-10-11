package com.quickcrave.repository;

import com.quickcrave.model.IngredieantItems;
import com.quickcrave.model.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory,Long> {

    List<IngredientCategory> findByResturantId(Long id);


}
