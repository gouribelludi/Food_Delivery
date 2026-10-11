package com.quickcrave.service;

import com.quickcrave.model.*;
import com.quickcrave.repository.*;
import com.quickcrave.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemReopsitory orderItemReopsitory;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestautaurntService restautaurntService;

    @Autowired
    private CartService cartService;

    @Override
    public Order createOrder(OrderRequest order, User user) throws Exception {
        Address shippingAddress=order.getDeliveryAddress();
        Address saveAddress=addressRepository.save(shippingAddress);

        if (!user.getAddresses().contains(saveAddress))
        {
            user.getAddresses().add(saveAddress);
            userRepository.save(user);
        }
        Resturant resturant=restautaurntService.findResturantById(order.getRestaurantId());

        Order createdOrder=new Order();
        createdOrder.setCustomer(user);
        createdOrder.setCreatedAt(new Date());
        createdOrder.setOrderStatus("Pending");
        createdOrder.setDeliveryAddress(saveAddress);
        createdOrder.setResturant(resturant);

        Cart cart=cartService.findCartByUserId(user.getId());

        List<OrderItem> orderItems=new ArrayList<>();

        for(CartItems cartItems:cart.getCartItems())
        {
            OrderItem orderItem=new OrderItem();
            orderItem.setFood(cartItems.getFood());
            orderItem.setIngredients(cartItems.getIngredient());
            orderItem.setQuantity(cartItems.getQuantity());
            orderItem.setTotalPrice(cartItems.getTotalPrice());

            OrderItem savedOrderItem=orderItemReopsitory.save(orderItem);
            orderItems.add(savedOrderItem);

        }
        Long  totalPrice=cartService.calculateCartTotals(cart);
        createdOrder.setItems(orderItems);
        createdOrder.setTotalPrice(totalPrice);

        Order savedOrder=orderRepository.save(createdOrder);
        resturant.getOrders().add(savedOrder);

        return createdOrder;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        Order order=findOrderById(orderId);
        if(orderStatus.equals("OUT_FOR_DELIVERY")
                || orderStatus.equals("DELIVERED")
                ||orderStatus.equals("COMPLETED")
                ||orderStatus.equals("PENDING"))
        {
                  order.setOrderStatus(orderStatus);
                  return orderRepository.save(order);
        }
        throw new Exception("Please select a valis order status");


    }

    @Override
    public void cancleOrder(Long orderId) throws Exception {

        Order order=findOrderById(orderId);
        orderRepository.deleteById(orderId);

    }

    @Override
    public List<Order> getUsersOrder(Long userId) throws Exception {

        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception {
        List<Order> orders= orderRepository.findByResturantId(restaurantId);
        if(orderStatus!=null)
        {
            orders =orders.stream().filter(order ->
                    order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
        }
        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> optionalOrder=orderRepository.findById(orderId);
        if(optionalOrder.isEmpty())
        {
            throw new Exception("Order not found");
        }
        return optionalOrder.get();
    }
}
