package com.quickcrave.service;

import com.quickcrave.model.Order;
import com.quickcrave.model.User;
import com.quickcrave.request.OrderRequest;

import java.util.List;

public interface OrderService {

    public Order createOrder(OrderRequest order, User user) throws Exception;

    public Order updateOrder(Long orderId,String orderStatus) throws Exception;

    public void cancleOrder(Long orderId) throws Exception;

    public List<Order> getUsersOrder(Long userId) throws Exception;

    public List<Order> getRestaurantsOrder(Long restaurantId,String orderStatus) throws Exception;

    public  Order findOrderById (Long orderId) throws Exception;



}
