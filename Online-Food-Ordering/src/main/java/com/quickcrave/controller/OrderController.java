package com.quickcrave.controller;

import com.quickcrave.model.CartItems;
import com.quickcrave.model.Order;
import com.quickcrave.model.User;
import com.quickcrave.request.AddCartItemRequest;
import com.quickcrave.request.OrderRequest;
import com.quickcrave.response.PaymentResponse;
import com.quickcrave.service.OrderService;
import com.quickcrave.service.PaymentService;
import com.quickcrave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @PostMapping("/order")
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody OrderRequest req,
                                                       @RequestHeader("Authorization") String jwt) throws Exception {

        User user=userService.findUserByjwtToken(jwt);
        Order order=orderService.createOrder(req,user);
        PaymentResponse res=paymentService.createPaymentLink(order);
        return new  ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/order/user")
    public ResponseEntity<List<Order> > getOrderHistory(
                                             @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByjwtToken(jwt);
        List<Order> orders=orderService.getUsersOrder(user.getId());
        return new  ResponseEntity<>(orders, HttpStatus.OK);
    }



}
