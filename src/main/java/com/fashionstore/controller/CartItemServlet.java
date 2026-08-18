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
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        /*
         * =====================================================
         * CHECK LOGIN
         * =====================================================
         */

        if (session == null ||
                !(session.getAttribute("loggedInUser")
                        instanceof User)) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        User user =
                (User) session.getAttribute("loggedInUser");


        /*
         * =====================================================
         * GET ACTION
         * =====================================================
         */

        String action =
                request.getParameter("action");


        /*
         * =====================================================
         * UPDATE CART ITEM
         * =====================================================
         *
         * cart.jsp sends:
         *
         * action=update
         * cartItemId=...
         * quantity=...
         *
         */

        if ("update".equalsIgnoreCase(action)) {

            updateCartItem(
                    request,
                    response,
                    user
            );

            return;
        }


        /*
         * =====================================================
         * REMOVE CART ITEM
         * =====================================================
         *
         * cart.jsp sends:
         *
         * action=remove
         * cartItemId=...
         *
         */

        if ("remove".equalsIgnoreCase(action)) {

            removeCartItem(
                    request,
                    response,
                    user
            );

            return;
        }


        /*
         * =====================================================
         * ADD PRODUCT TO CART
         * =====================================================
         *
         * Product details page sends:
         *
         * productId
         * sizeLabel
         * quantity
         * unitPrice
         *
         */

        addProductToCart(
                request,
                response,
                user
        );
    }


    /*
     * =========================================================
     * UPDATE CART ITEM
     * =========================================================
     */

    private void updateCartItem(
            HttpServletRequest request,
            HttpServletResponse response,
            User user)
            throws IOException {

        try {

            String cartItemIdParameter =
                    request.getParameter("cartItemId");

            String quantityParameter =
                    request.getParameter("quantity");


            /*
             * Check parameters.
             */

            if (cartItemIdParameter == null ||
                    quantityParameter == null ||
                    cartItemIdParameter.trim().isEmpty() ||
                    quantityParameter.trim().isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid cart item information."
                );

                return;
            }


            int cartItemId =
                    Integer.parseInt(
                            cartItemIdParameter
                    );

            int quantity =
                    Integer.parseInt(
                            quantityParameter
                    );


            /*
             * Quantity must be greater than zero.
             */

            if (quantity <= 0) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/cart"
                );

                return;
            }


            /*
             * Get user's cart.
             */

            Cart cart =
                    cartDAO.getOrCreateCart(
                            user.getUserId()
                    );

            if (cart == null) {

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to retrieve cart."
                );

                return;
            }


            /*
             * Get cart item.
             */

            CartItem cartItem =
                    cartItemDAO.getCartItemById(
                            cartItemId
                    );


            /*
             * Security:
             *
             * Make sure this cart item belongs
             * to the currently logged-in user's cart.
             */

            if (cartItem == null ||
                    cartItem.getCartId()
                            != cart.getCartId()) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Cart item not found."
                );

                return;
            }


            /*
             * Update quantity.
             */

            boolean updated =
                    cartItemDAO.updateQuantity(
                            cartItemId,
                            quantity
                    );


            /*
             * Redirect back to cart.
             */

            if (updated) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/cart"
                );

            } else {

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to update cart item."
                );
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid cart item information."
            );
        }
    }


    /*
     * =========================================================
     * REMOVE CART ITEM
     * =========================================================
     */

    private void removeCartItem(
            HttpServletRequest request,
            HttpServletResponse response,
            User user)
            throws IOException {

        try {

            String cartItemIdParameter =
                    request.getParameter("cartItemId");


            if (cartItemIdParameter == null ||
                    cartItemIdParameter.trim().isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid cart item information."
                );

                return;
            }


            int cartItemId =
                    Integer.parseInt(
                            cartItemIdParameter
                    );


            /*
             * Get user's cart.
             */

            Cart cart =
                    cartDAO.getOrCreateCart(
                            user.getUserId()
                    );

            if (cart == null) {

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to retrieve cart."
                );

                return;
            }


            /*
             * Get cart item.
             */

            CartItem cartItem =
                    cartItemDAO.getCartItemById(
                            cartItemId
                    );


            /*
             * Security:
             *
             * Make sure the cart item belongs
             * to this user's cart.
             */

            if (cartItem == null ||
                    cartItem.getCartId()
                            != cart.getCartId()) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Cart item not found."
                );

                return;
            }


            /*
             * Remove item.
             */

            boolean removed =
                    cartItemDAO.removeCartItem(
                            cartItemId
                    );


            if (removed) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/cart"
                );

            } else {

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to remove cart item."
                );
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid cart item information."
            );
        }
    }


    /*
     * =========================================================
     * ADD PRODUCT TO CART
     * =========================================================
     */

    private void addProductToCart(
            HttpServletRequest request,
            HttpServletResponse response,
            User user)
            throws IOException {

        try {

            String productIdParameter =
                    request.getParameter("productId");

            String sizeLabel =
                    request.getParameter("sizeLabel");

            String quantityParameter =
                    request.getParameter("quantity");

            String unitPriceParameter =
                    request.getParameter("unitPrice");


            /*
             * Check required parameters.
             */

            if (productIdParameter == null ||
                    quantityParameter == null ||
                    unitPriceParameter == null ||
                    sizeLabel == null ||
                    sizeLabel.trim().isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid cart item information."
                );

                return;
            }


            int productId =
                    Integer.parseInt(
                            productIdParameter
                    );

            int quantity =
                    Integer.parseInt(
                            quantityParameter
                    );

            BigDecimal unitPrice =
                    new BigDecimal(
                            unitPriceParameter
                    );


            /*
             * Basic validation.
             */

            if (quantity <= 0) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Quantity must be greater than zero."
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
                 * Increase quantity.
                 */

                cartItemDAO.increaseQuantity(
                        existingItem.getCartItemId(),
                        quantity
                );

            } else {

                /*
                 * Product does not exist.
                 * Create new cart item.
                 */

                CartItem cartItem =
                        new CartItem();

                cartItem.setCartId(
                        cart.getCartId()
                );

                cartItem.setProductId(
                        productId
                );

                cartItem.setSizeLabel(
                        sizeLabel
                );

                cartItem.setQuantity(
                        quantity
                );

                cartItem.setUnitPrice(
                        unitPrice
                );

                cartItemDAO.addCartItem(
                        cartItem
                );
            }


            /*
             * Redirect to cart.
             */

            response.sendRedirect(
                    request.getContextPath()
                            + "/cart"
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid cart item information."
            );
        }
    }
}