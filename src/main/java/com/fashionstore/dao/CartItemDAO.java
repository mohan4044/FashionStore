package com.fashionstore.dao;

import com.fashionstore.model.CartItem;
import java.util.List;

public interface CartItemDAO {

    boolean addCartItem(CartItem cartItem);

    CartItem getCartItemById(int cartItemId);

    CartItem getCartItem(
            int cartId,
            int productId,
            String sizeLabel
    );

    List<CartItem> getCartItemsByCartId(int cartId);

    boolean updateQuantity(
            int cartItemId,
            int quantity
    );

    boolean increaseQuantity(
            int cartItemId,
            int quantity
    );

    boolean decreaseQuantity(
            int cartItemId,
            int quantity
    );

    boolean removeCartItem(int cartItemId);

    boolean removeProductFromCart(
            int cartId,
            int productId,
            String sizeLabel
    );

    boolean clearCart(int cartId);

    int getCartItemCount(int cartId);

    boolean cartItemExists(
            int cartId,
            int productId,
            String sizeLabel
    );
}