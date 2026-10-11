package com.quickcrave.controller;

import com.quickcrave.model.Cart;
import com.quickcrave.model.CartItems;
import com.quickcrave.model.User;
import com.quickcrave.request.AddCartItemRequest;
import com.quickcrave.request.UpdateCartItemRequest;
import com.quickcrave.service.CartService;
import com.quickcrave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @PutMapping("/cart/add")
    public ResponseEntity<CartItems> addItemToCart(@RequestBody AddCartItemRequest req,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {
        CartItems cartItems=cartService.addItemToCart(req,jwt);

       return new  ResponseEntity<>(cartItems,HttpStatus.OK);
    }

    @PutMapping("/cart-item/add/update")
    public ResponseEntity<CartItems> updateCartItemQuantity(@RequestBody UpdateCartItemRequest req,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {
        CartItems cartItem=cartService.updateCartItemQuantity(req.getCartItemId(),req.getQuantity());

        return new  ResponseEntity<>(cartItem,HttpStatus.OK);
    }

    @DeleteMapping("/cart-item/{id}/remove")
    public ResponseEntity<Cart> removeCartItem(@PathVariable Long id,
                                                            @RequestHeader("Authorization") String jwt) throws Exception {
        Cart cart=cartService.removeItemFromCart(id,jwt);

        return new  ResponseEntity<>(cart,HttpStatus.OK);
    }

    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(@RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByjwtToken(jwt);
        Cart cart=cartService.clearCart(user.getId());

        return new  ResponseEntity<>(cart,HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws Exception {

        User user=userService.findUserByjwtToken(jwt);
        Cart cart=cartService.findCartByUserId(user.getId());

        return new  ResponseEntity<>(cart,HttpStatus.OK);
    }

}
