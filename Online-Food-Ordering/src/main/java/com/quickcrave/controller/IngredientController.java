package com.quickcrave.controller;

import com.quickcrave.model.IngredieantItems;
import com.quickcrave.model.IngredientCategory;
import com.quickcrave.request.IngredientCategoryRequest;
import com.quickcrave.request.IngredientRequest;
import com.quickcrave.service.IngredientsService;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientController {

    @Autowired
    private IngredientsService ingredientsService;


    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(@RequestBody IngredientCategoryRequest req) throws Exception {
        IngredientCategory item=ingredientsService.createIngredientCategory(req.getName(), req.getRestaurantId());
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PostMapping()
    public ResponseEntity<IngredieantItems> createIngredientItem(@RequestBody IngredientRequest req) throws Exception {
        IngredieantItems item=ingredientsService.createIngredientItem(req.getRestaurantId(),req.getName(), req.getCategoryId());
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping ("/{id}/stoke")
    public ResponseEntity<IngredieantItems> updateIngredientStock(@PathVariable Long id) throws Exception {
        IngredieantItems item=ingredientsService.updateStock(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<IngredieantItems>> getRestaurantIngredient(@PathVariable Long id) throws Exception {
        List<IngredieantItems> item=ingredientsService.findRestaurantIngredients(id);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/{id}/category")
    public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientCategory(@PathVariable Long id) throws Exception {
        List<IngredientCategory> item=ingredientsService.findIngredientCategoryByRestaurantId(id);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

}
