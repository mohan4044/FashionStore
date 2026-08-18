package com.fashionstore.controller;

import java.io.IOException;
import java.math.BigDecimal;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.model.Cart;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart-item")
public class CartItemServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;

    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        /*
         * User must be logged in.
         */
        if (session == null ||
            session.getAttribute("loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        User user =
                (User) session.getAttribute("loggedInUser");

        try {

            int productId = Integer.parseInt(
                    request.getParameter("productId")
            );

            String sizeLabel =
                    request.getParameter("sizeLabel");

            int quantity = Integer.parseInt(
                    request.getParameter("quantity")
            );

            BigDecimal unitPrice =
                    new BigDecimal(
                            request.getParameter("unitPrice")
                    );


            /*
             * Basic validation.
             */
            if (sizeLabel == null ||
                sizeLabel.trim().isEmpty() ||
                quantity <= 0) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/product?id=" + productId
                );

                return;
            }


            /*
             * Get or create user's cart.
             */
            Cart cart =
                    cartDAO.getOrCreateCart(
                            user.getUserId()
                    );

            if (cart == null) {

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to create or retrieve cart."
                );

                return;
            }


            /*
             * Check whether the same product + size
             * already exists in the cart.
             */
            CartItem existingItem =
                    cartItemDAO.getCartItem(
                            cart.getCartId(),
                            productId,
                            sizeLabel
                    );


            if (existingItem != null) {

                /*
                 * Product already exists.
                 * Increase its quantity.
                 */
                cartItemDAO.increaseQuantity(
                        existingItem.getCartItemId(),
                        quantity
                );

            } else {

                /*
                 * Product does not exist in cart.
                 * Create a new cart item.
                 */
                CartItem cartItem = new CartItem();

                cartItem.setCartId(cart.getCartId());
                cartItem.setProductId(productId);
                cartItem.setSizeLabel(sizeLabel);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);

                cartItemDAO.addCartItem(cartItem);
            }


            /*
             * Redirect to cart.
             */
            response.sendRedirect(
                    request.getContextPath() + "/cart"
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid cart item information."
            );
        }
    }
}