package com.quickcrave.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.quickcrave.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class CreateFoodRequest {
    private String name;
    private String description;
    private Long price;

    private Category category;
    private List<String> images;

    private Long resturantId;

    @JsonAlias("vegetarin")
    private Boolean vegetarian = false;

    @JsonAlias("seasional")
    private Boolean seasonal = false;

    @JsonSetter(nulls = Nulls.SKIP)
    private List<IngredientReferenceRequest> ingredients;

}
