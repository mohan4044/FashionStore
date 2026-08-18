package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Cart;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.Product;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {

        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
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

        /*
         * Get logged-in user.
         */
        User user =
                (User) session.getAttribute("loggedInUser");

        int userId = user.getUserId();

        /*
         * Get or create cart.
         */
        Cart cart = cartDAO.getOrCreateCart(userId);

        if (cart == null) {

            request.setAttribute(
                    "error",
                    "Unable to load your cart."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/cart.jsp"
            ).forward(request, response);

            return;
        }

        /*
         * Get all cart items.
         */
        List<CartItem> cartItems =
                cartItemDAO.getCartItemsByCartId(
                        cart.getCartId()
                );

        /*
         * Create a list of products corresponding
         * to the cart items.
         */
        List<Product> products = new java.util.ArrayList<>();

        for (CartItem item : cartItems) {

            Product product =
                    productDAO.getProductById(
                            item.getProductId()
                    );

            products.add(product);
        }

        /*
         * Send data to cart.jsp.
         */
        request.setAttribute("cart", cart);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("products", products);

        /*
         * Forward to cart page.
         */
        request.getRequestDispatcher(
                "/WEB-INF/views/cart.jsp"
        ).forward(request, response);
    }
}