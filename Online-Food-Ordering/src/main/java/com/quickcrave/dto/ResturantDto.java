package com.quickcrave.dto;

import com.quickcrave.model.Resturant;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class ResturantDto extends Resturant {

    private String title;

    @Column(length = 1000)
    private List<String> images;

    private String description;
    private Long id;


}
