package com.quickcrave.request;

import com.quickcrave.model.Address;
import lombok.Data;

@Data
public class OrderRequest {
    private  Long restaurantId;
    private Address deliveryAddress;
}

