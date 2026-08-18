package com.fashionstore.controller;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductSizeDAO;

import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.impl.ProductSizeDAOImpl;

import com.fashionstore.model.Cart;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductSize;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ProductDAO productDAO;
    private ProductSizeDAO productSizeDAO;

    @Override
    public void init() {

        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
        productDAO = new ProductDAOImpl();
        productSizeDAO = new ProductSizeDAOImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        int userId =
                loggedInUser.getUserId();

        Cart cart =
                cartDAO.getCartByUserId(userId);

        if (cart == null) {

            request.setAttribute(
                    "checkoutError",
                    "Your cart is empty."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/checkout.jsp"
            ).forward(request, response);

            return;
        }

        List<CartItem> cartItems =
                cartItemDAO.getCartItemsByCartId(
                        cart.getCartId()
                );

        if (cartItems == null ||
                cartItems.isEmpty()) {

            request.setAttribute(
                    "checkoutError",
                    "Your cart is empty."
            );

            request.setAttribute(
                    "cart",
                    cart
            );

            request.setAttribute(
                    "cartItems",
                    cartItems
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/checkout.jsp"
            ).forward(request, response);

            return;
        }

        request.setAttribute(
                "cart",
                cart
        );

        request.setAttribute(
                "cartItems",
                cartItems
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/checkout.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");

            return;
        }

        int userId =
                loggedInUser.getUserId();

        String deliveryAddress =
                request.getParameter("deliveryAddress");

        String paymentMethod =
                request.getParameter("paymentMethod");

        if (deliveryAddress == null ||
                deliveryAddress.trim().isEmpty()) {

            showError(
                    request,
                    response,
                    userId,
                    "Please enter your delivery address."
            );

            return;
        }

        if (paymentMethod == null ||
                paymentMethod.trim().isEmpty()) {

            showError(
                    request,
                    response,
                    userId,
                    "Please select a payment method."
            );

            return;
        }

        if (!paymentMethod.equals("COD") &&
                !paymentMethod.equals("UPI") &&
                !paymentMethod.equals("CARD")) {

            showError(
                    request,
                    response,
                    userId,
                    "Invalid payment method."
            );

            return;
        }

        Cart cart =
                cartDAO.getCartByUserId(userId);

        if (cart == null) {

            showError(
                    request,
                    response,
                    userId,
                    "Your cart is empty."
            );

            return;
        }

        List<CartItem> cartItems =
                cartItemDAO.getCartItemsByCartId(
                        cart.getCartId()
                );

        if (cartItems == null ||
                cartItems.isEmpty()) {

            showError(
                    request,
                    response,
                    userId,
                    "Your cart is empty."
            );

            return;
        }

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        List<CheckoutItemData> checkoutItems =
                new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            if (cartItem.getQuantity() <= 0) {

                showError(
                        request,
                        response,
                        userId,
                        "Invalid quantity in your cart."
                );

                return;
            }

            Product product =
                    productDAO.getProductById(
                            cartItem.getProductId()
                    );

            if (product == null) {

                showError(
                        request,
                        response,
                        userId,
                        "A product in your cart no longer exists."
                );

                return;
            }

            if (!product.isActive()) {

                showError(
                        request,
                        response,
                        userId,
                        product.getProductName()
                                + " is currently unavailable."
                );

                return;
            }

            ProductSize productSize =
                    productSizeDAO.getProductSize(
                            cartItem.getProductId(),
                            cartItem.getSizeLabel()
                    );

            if (productSize == null) {

                showError(
                        request,
                        response,
                        userId,
                        "Selected size is no longer available for "
                                + product.getProductName()
                );

                return;
            }

            if (!productSize.isAvailable()) {

                showError(
                        request,
                        response,
                        userId,
                        "Selected size is currently unavailable for "
                                + product.getProductName()
                );

                return;
            }

            if (productSize.getStockQuantity()
                    < cartItem.getQuantity()) {

                showError(
                        request,
                        response,
                        userId,
                        "Insufficient stock for "
                                + product.getProductName()
                                + " - Size "
                                + cartItem.getSizeLabel()
                );

                return;
            }

            if (cartItem.getUnitPrice() == null) {

                showError(
                        request,
                        response,
                        userId,
                        "Invalid product price in your cart."
                );

                return;
            }

            BigDecimal itemSubtotal =
                    cartItem.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            cartItem.getQuantity()
                                    )
                            );

            totalAmount =
                    totalAmount.add(itemSubtotal);

            checkoutItems.add(
                    new CheckoutItemData(
                            cartItem,
                            product,
                            productSize,
                            itemSubtotal
                    )
            );
        }

        Order order =
                new Order();

        order.setUserId(userId);

        order.setTotalAmount(
                totalAmount
        );

        order.setPaymentMethod(
                paymentMethod
        );

        order.setPaymentStatus(
                "Pending"
        );

        order.setOrderStatus(
                "Placed"
        );

        order.setDeliveryAddress(
                deliveryAddress.trim()
        );

        boolean orderCreated =
                orderDAO.createOrder(order);

        if (!orderCreated ||
                order.getOrderId() <= 0) {

            showError(
                    request,
                    response,
                    userId,
                    "Unable to create your order. Please try again."
            );

            return;
        }

        for (CheckoutItemData itemData :
                checkoutItems) {

            CartItem cartItem =
                    itemData.getCartItem();

            Product product =
                    itemData.getProduct();

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrderId(
                    order.getOrderId()
            );

            orderItem.setProductId(
                    product.getProductId()
            );

            orderItem.setProductName(
                    product.getProductName()
            );

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setUnitPrice(
                    cartItem.getUnitPrice()
            );

            orderItem.setSubtotal(
                    itemData.getItemSubtotal()
            );

            orderItem.setSizeLabel(
                    cartItem.getSizeLabel()
            );

            boolean itemCreated =
                    orderItemDAO.addOrderItem(
                            orderItem
                    );

            if (!itemCreated) {

                orderDAO.cancelOrder(
                        order.getOrderId()
                );

                showError(
                        request,
                        response,
                        userId,
                        "Unable to create your order items."
                );

                return;
            }
        }

        for (CheckoutItemData itemData :
                checkoutItems) {

            ProductSize productSize =
                    itemData.getProductSize();

            CartItem cartItem =
                    itemData.getCartItem();

            boolean stockUpdated =
                    productSizeDAO.decreaseStock(
                            productSize.getProductSizeId(),
                            cartItem.getQuantity()
                    );

            if (!stockUpdated) {

                orderDAO.cancelOrder(
                        order.getOrderId()
                );

                showError(
                        request,
                        response,
                        userId,
                        "Stock changed while placing your order. "
                                + "Please try again."
                );

                return;
            }
        }

        boolean cartCleared =
                cartItemDAO.clearCart(
                        cart.getCartId()
                );

        if (!cartCleared) {

            request.setAttribute(
                    "order",
                    order
            );

            request.setAttribute(
                    "checkoutWarning",
                    "Your order was placed, but the cart could not be cleared."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/order-success.jsp"
            ).forward(request, response);

            return;
        }

        request.setAttribute(
                "order",
                order
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/order-success.jsp"
        ).forward(request, response);
    }

    private void showError(
            HttpServletRequest request,
            HttpServletResponse response,
            int userId,
            String message)
            throws ServletException, IOException {

        Cart cart =
                cartDAO.getCartByUserId(userId);

        List<CartItem> cartItems = null;

        if (cart != null) {

            cartItems =
                    cartItemDAO.getCartItemsByCartId(
                            cart.getCartId()
                    );
        }

        request.setAttribute(
                "checkoutError",
                message
        );

        request.setAttribute(
                "cart",
                cart
        );

        request.setAttribute(
                "cartItems",
                cartItems
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/checkout.jsp"
        ).forward(request, response);
    }

    private static class CheckoutItemData {

        private final CartItem cartItem;
        private final Product product;
        private final ProductSize productSize;
        private final BigDecimal itemSubtotal;

        public CheckoutItemData(
                CartItem cartItem,
                Product product,
                ProductSize productSize,
                BigDecimal itemSubtotal) {

            this.cartItem = cartItem;
            this.product = product;
            this.productSize = productSize;
            this.itemSubtotal = itemSubtotal;
        }

        public CartItem getCartItem() {
            return cartItem;
        }

        public Product getProduct() {
            return product;
        }

        public ProductSize getProductSize() {
            return productSize;
        }

        public BigDecimal getItemSubtotal() {
            return itemSubtotal;
        }
    }
}