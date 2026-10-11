package com.quickcrave.request;

import com.quickcrave.model.Address;
import com.quickcrave.model.ContactInformation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreteResturantRequest {
    private  Long id;
    private String name;
    private  String description;
    private  String cuisineType;
    private Address address;
    private ContactInformation contactInformation;
    private String opningHours;
    private List<String> images;


}
