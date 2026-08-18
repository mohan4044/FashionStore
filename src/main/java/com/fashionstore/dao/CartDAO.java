package com.fashionstore.dao;

import com.fashionstore.model.Cart;

public interface CartDAO {

    boolean createCart(Cart cart);

    Cart getCartById(int cartId);

    Cart getCartByUserId(int userId);

    boolean updateCart(Cart cart);

    boolean deleteCart(int cartId);

    boolean cartExistsForUser(int userId);

    Cart getOrCreateCart(int userId);
}