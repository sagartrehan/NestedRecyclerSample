package com.ecommerce.cart;

import java.util.List;

public interface CartCache {
    boolean addToCart(CartItem cartItem);
    boolean removeFromCart(CartItem cartItem);
    List<CartItem> getProductsInCart();
    boolean isInCart(CartItem cartItem);
}
