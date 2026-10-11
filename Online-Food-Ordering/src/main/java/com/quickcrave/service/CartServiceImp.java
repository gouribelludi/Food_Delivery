package com.quickcrave.service;

import com.quickcrave.model.Cart;
import com.quickcrave.model.CartItems;
import com.quickcrave.model.Food;
import com.quickcrave.model.User;
import com.quickcrave.repository.CartItemRepository;
import com.quickcrave.repository.CartRepository;

import com.quickcrave.request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class CartServiceImp implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartItems addItemToCart(AddCartItemRequest req, String jwt) throws Exception {

        User user=userService.findUserByjwtToken(jwt);

        Food food=foodService.findFoodById(req.getFoodId());

        Cart cart=cartRepository.findByCustomerId(user.getId());

        for(CartItems cartItems:cart.getCartItems())
        {
            if(cartItems.getFood().equals(food)){
                int newQuantity=cartItems.getQuantity()+req.getQuantity();
                return updateCartItemQuantity(cartItems.getId(),newQuantity);
            }
        }

        CartItems newCartItem=new CartItems();
        newCartItem.setFood(food);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setIngredient(req.getIngredients());
        newCartItem.setTotalPrice(req.getQuantity()*food.getPrice());

        CartItems savedCartItem=cartItemRepository.save(newCartItem);

        cart.getCartItems().add(savedCartItem);

        return savedCartItem;
    }

    @Override
    public CartItems updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
       Optional<CartItems> cartItemsOptional=cartItemRepository.findById(cartItemId);
       if(cartItemsOptional.isEmpty())
       {
           throw new Exception("cart item not found");
       }
       CartItems item=cartItemsOptional.get();
       item.setQuantity(quantity);
//adding price of items in cart
        item.setTotalPrice(item.getFood().getPrice() * quantity);

        return cartItemRepository.save(item);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {

        User user=userService.findUserByjwtToken(jwt);

        Cart cart=cartRepository.findByCustomerId(user.getId());

        Optional<CartItems> cartItemsOptional=cartItemRepository.findById(cartItemId);
        if(cartItemsOptional.isEmpty())
        {
            throw new Exception("cart item not found");
        }
       CartItems item=cartItemsOptional.get();

        cart.getCartItems().remove(item);

        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotals(Cart cart) throws Exception {
        Long total=0L;

        for(CartItems cartItems:cart.getCartItems())
        {
            total+= cartItems.getFood().getPrice()*cartItems.getQuantity();
        }
        return total;
    }

    @Override
    public Cart findCartById(Long id) throws Exception {
        Optional<Cart> optionalCart=cartRepository.findById(id);
        if(optionalCart.isEmpty())
        {throw new Exception("cart not found by id"+id);}
        return optionalCart.get();
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
        Cart cart = cartRepository.findByCustomerId(userId);

        if (cart == null) {
            throw new Exception("Cart not found for user ID: " + userId);
        }

        cart.setTotal(calculateCartTotals(cart));
        return cart;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
//        User user=userService.findUserByjwtToken(jwt);
        Cart cart=findCartByUserId(userId);
        cart.getCartItems().clear();
        return cartRepository.save(cart);
    }
}
