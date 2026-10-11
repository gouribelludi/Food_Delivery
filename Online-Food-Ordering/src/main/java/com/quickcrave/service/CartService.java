package com.quickcrave.service;

import com.quickcrave.model.Cart;
import com.quickcrave.model.CartItems;
import com.quickcrave.request.AddCartItemRequest;

public interface CartService {
    public CartItems addItemToCart(AddCartItemRequest req , String jwt) throws  Exception;

    public CartItems updateCartItemQuantity(Long cartItemId,int quantity) throws Exception;

    public Cart removeItemFromCart(Long cartItemId ,String jwt) throws Exception;

    public Long calculateCartTotals(Cart cart) throws Exception;

    public Cart findCartById(Long id) throws Exception;

    public Cart findCartByUserId(Long userId) throws Exception;

    public Cart clearCart(Long userId) throws Exception;
}
