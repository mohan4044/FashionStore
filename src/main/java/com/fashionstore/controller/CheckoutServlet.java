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
                    request.getContextPath() + "/login"
            );

            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

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

        System.out.println(
                "===== CHECKOUT POST STARTED ====="
        );

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            System.out.println(
                    "CHECKOUT ERROR: SESSION IS NULL"
            );

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {

            System.out.println(
                    "CHECKOUT ERROR: USER IS NOT LOGGED IN"
            );

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        int userId =
                loggedInUser.getUserId();

        System.out.println(
                "USER ID = " + userId
        );

        String deliveryAddress =
                request.getParameter("deliveryAddress");

        String paymentMethod =
                request.getParameter("paymentMethod");

        System.out.println(
                "DELIVERY ADDRESS = " + deliveryAddress
        );

        System.out.println(
                "PAYMENT METHOD = " + paymentMethod
        );

        if (deliveryAddress == null ||
                deliveryAddress.trim().isEmpty()) {

            System.out.println(
                    "CHECKOUT ERROR: DELIVERY ADDRESS EMPTY"
            );

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

            System.out.println(
                    "CHECKOUT ERROR: PAYMENT METHOD EMPTY"
            );

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

            System.out.println(
                    "CHECKOUT ERROR: INVALID PAYMENT METHOD = "
                            + paymentMethod
            );

            showError(
                    request,
                    response,
                    userId,
                    "Invalid payment method."
            );

            return;
        }

        System.out.println(
                "PAYMENT VALIDATION PASSED"
        );

        Cart cart =
                cartDAO.getCartByUserId(userId);

        System.out.println(
                "CART = " +
                        (cart == null
                                ? "NULL"
                                : cart.getCartId())
        );

        if (cart == null) {

            System.out.println(
                    "CHECKOUT ERROR: CART IS NULL"
            );

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

        System.out.println(
                "CART ITEMS = " +
                        (cartItems == null
                                ? "NULL"
                                : cartItems.size())
        );

        if (cartItems == null ||
                cartItems.isEmpty()) {

            System.out.println(
                    "CHECKOUT ERROR: CART ITEMS EMPTY"
            );

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

        System.out.println(
                "===== VALIDATING CART ITEMS ====="
        );

        for (CartItem cartItem : cartItems) {

            System.out.println(
                    "--------------------------------"
            );

            System.out.println(
                    "CART ITEM ID = "
                            + cartItem.getCartItemId()
            );

            System.out.println(
                    "PRODUCT ID = "
                            + cartItem.getProductId()
            );

            System.out.println(
                    "SIZE = "
                            + cartItem.getSizeLabel()
            );

            System.out.println(
                    "QUANTITY = "
                            + cartItem.getQuantity()
            );

            System.out.println(
                    "UNIT PRICE = "
                            + cartItem.getUnitPrice()
            );

            if (cartItem.getQuantity() <= 0) {

                System.out.println(
                        "CHECKOUT ERROR: INVALID QUANTITY"
                );

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

            System.out.println(
                    "PRODUCT = " +
                            (product == null
                                    ? "NULL"
                                    : product.getProductName())
            );

            if (product == null) {

                System.out.println(
                        "CHECKOUT ERROR: PRODUCT NOT FOUND"
                );

                showError(
                        request,
                        response,
                        userId,
                        "A product in your cart no longer exists."
                );

                return;
            }

            System.out.println(
                    "PRODUCT ACTIVE = "
                            + product.isActive()
            );

            if (!product.isActive()) {

                System.out.println(
                        "CHECKOUT ERROR: PRODUCT INACTIVE"
                );

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

            System.out.println(
                    "PRODUCT SIZE = " +
                            (productSize == null
                                    ? "NULL"
                                    : productSize.getProductSizeId())
            );

            if (productSize == null) {

                System.out.println(
                        "CHECKOUT ERROR: PRODUCT SIZE NOT FOUND"
                );

                showError(
                        request,
                        response,
                        userId,
                        "Selected size is no longer available for "
                                + product.getProductName()
                );

                return;
            }

            System.out.println(
                    "SIZE AVAILABLE = "
                            + productSize.isAvailable()
            );

            if (!productSize.isAvailable()) {

                System.out.println(
                        "CHECKOUT ERROR: SIZE NOT AVAILABLE"
                );

                showError(
                        request,
                        response,
                        userId,
                        "Selected size is currently unavailable for "
                                + product.getProductName()
                );

                return;
            }

            System.out.println(
                    "STOCK QUANTITY = "
                            + productSize.getStockQuantity()
            );

            if (productSize.getStockQuantity()
                    < cartItem.getQuantity()) {

                System.out.println(
                        "CHECKOUT ERROR: INSUFFICIENT STOCK"
                );

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

                System.out.println(
                        "CHECKOUT ERROR: UNIT PRICE NULL"
                );

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

            System.out.println(
                    "ITEM SUBTOTAL = "
                            + itemSubtotal
            );

            System.out.println(
                    "RUNNING TOTAL = "
                            + totalAmount
            );

            checkoutItems.add(
                    new CheckoutItemData(
                            cartItem,
                            product,
                            productSize,
                            itemSubtotal
                    )
            );
        }

        System.out.println(
                "===== CART VALIDATION COMPLETE ====="
        );

        System.out.println(
                "TOTAL AMOUNT = " + totalAmount
        );

        System.out.println(
                "===== CREATING ORDER ====="
        );

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

        System.out.println(
                "ORDER USER ID = "
                        + order.getUserId()
        );

        System.out.println(
                "ORDER TOTAL = "
                        + order.getTotalAmount()
        );

        System.out.println(
                "ORDER PAYMENT METHOD = "
                        + order.getPaymentMethod()
        );

        System.out.println(
                "ORDER PAYMENT STATUS = "
                        + order.getPaymentStatus()
        );

        System.out.println(
                "ORDER STATUS = "
                        + order.getOrderStatus()
        );

        boolean orderCreated =
                orderDAO.createOrder(order);

        System.out.println(
                "ORDER CREATED = "
                        + orderCreated
                        + " | ORDER ID = "
                        + order.getOrderId()
        );

        if (!orderCreated ||
                order.getOrderId() <= 0) {

            System.out.println(
                    "CHECKOUT ERROR: ORDER CREATION FAILED"
            );

            showError(
                    request,
                    response,
                    userId,
                    "Unable to create your order. Please try again."
            );

            return;
        }

        System.out.println(
                "===== CREATING ORDER ITEMS ====="
        );
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

        System.out.println(
                "ADDING ORDER ITEM: "
                        + product.getProductName()
        );

        boolean itemCreated =
                orderItemDAO.addOrderItem(
                        orderItem
                );

        System.out.println(
                "ORDER ITEM CREATED = "
                        + itemCreated
        );

        if (!itemCreated) {

            System.out.println(
                    "CHECKOUT ERROR: ORDER ITEM CREATION FAILED"
            );

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

    System.out.println(
            "===== ORDER ITEMS CREATED ====="
    );

    System.out.println(
            "===== DECREASING STOCK ====="
    );

    for (CheckoutItemData itemData :
            checkoutItems) {

        ProductSize productSize =
                itemData.getProductSize();

        CartItem cartItem =
                itemData.getCartItem();

        System.out.println(
                "DECREASING STOCK"
        );

        System.out.println(
                "PRODUCT SIZE ID = "
                        + productSize.getProductSizeId()
        );

        System.out.println(
                "QUANTITY = "
                        + cartItem.getQuantity()
        );

        boolean stockUpdated =
                productSizeDAO.decreaseStock(
                        productSize.getProductSizeId(),
                        cartItem.getQuantity()
                );

        System.out.println(
                "STOCK UPDATED = "
                        + stockUpdated
        );

        if (!stockUpdated) {

            System.out.println(
                    "CHECKOUT ERROR: STOCK UPDATE FAILED"
            );

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

    System.out.println(
            "===== STOCK UPDATED SUCCESSFULLY ====="
    );

    System.out.println(
            "===== CLEARING CART ====="
    );

    boolean cartCleared =
            cartItemDAO.clearCart(
                    cart.getCartId()
            );

    System.out.println(
            "CART CLEARED = "
                    + cartCleared
    );

    if (!cartCleared) {

        System.out.println(
                "WARNING: ORDER PLACED BUT CART NOT CLEARED"
        );

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

    System.out.println(
            "===== CART CLEARED SUCCESSFULLY ====="
    );

    System.out.println(
            "===== CHECKOUT SUCCESS ====="
    );

    System.out.println(
            "ORDER ID = "
                    + order.getOrderId()
    );

    System.out.println(
            "TOTAL AMOUNT = "
                    + order.getTotalAmount()
    );

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

    System.out.println(
            "SHOWING CHECKOUT ERROR: "
                    + message
    );

    Cart cart =
            cartDAO.getCartByUserId(userId);

    List<CartItem> cartItems =
            null;

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
        