<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.fashionstore.model.Cart" %>
<%@ page import="com.fashionstore.model.CartItem" %>
<%@ page import="com.fashionstore.model.Product" %>

<%
    Cart cart = (Cart) request.getAttribute("cart");

    @SuppressWarnings("unchecked")
    List<CartItem> cartItems =
            (List<CartItem>) request.getAttribute("cartItems");

    @SuppressWarnings("unchecked")
    List<Product> products =
            (List<Product>) request.getAttribute("products");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Shopping Cart | Fashion Store</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/cart.css">

</head>

<body>

    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />


    <main class="cart-page">

        <div class="cart-container">

            <div class="cart-header">

                <h1>Shopping Cart</h1>

                <p>
                    Review your selected products.
                </p>

            </div>


            <% if (request.getAttribute("error") != null) { %>

                <div class="cart-error">

                    <%= request.getAttribute("error") %>

                </div>

            <% } %>


            <%
                if (cartItems != null && !cartItems.isEmpty()) {
            %>

                <div class="cart-layout">


                    <!-- =================================================
                         CART ITEMS
                         ================================================= -->

                    <section class="cart-items">

                        <%
                            BigDecimal cartTotal = BigDecimal.ZERO;

                            for (int i = 0; i < cartItems.size(); i++) {

                                CartItem item = cartItems.get(i);

                                Product product = null;

                                if (products != null &&
                                    i < products.size()) {

                                    product = products.get(i);
                                }

                                BigDecimal itemTotal =
                                        item.getUnitPrice()
                                            .multiply(
                                                BigDecimal.valueOf(
                                                    item.getQuantity()
                                                )
                                            );

                                cartTotal =
                                        cartTotal.add(itemTotal);
                        %>


                        <article class="cart-item">


                            <!-- =================================================
                                 PRODUCT IMAGE
                                 ================================================= -->

                            <div class="cart-item-image">

                                <%
                                    if (product != null &&
                                        product.getImageUrl() != null &&
                                        !product.getImageUrl()
                                               .trim()
                                               .isEmpty()) {
                                %>

                                    <img
                                        src="<%= product.getImageUrl() %>"
                                        alt="<%= product.getProductName() %>">

                                <%
                                    } else {
                                %>

                                    <span>
                                        No Image
                                    </span>

                                <%
                                    }
                                %>

                            </div>


                            <!-- =================================================
                                 PRODUCT INFORMATION
                                 ================================================= -->

                            <div class="cart-item-info">

                                <%
                                    if (product != null) {
                                %>

                                    <p class="cart-item-brand">
                                        <%= product.getBrand() %>
                                    </p>

                                    <h2>
                                        <%= product.getProductName() %>
                                    </h2>

                                <%
                                    } else {
                                %>

                                    <h2>
                                        Product #<%= item.getProductId() %>
                                    </h2>

                                <%
                                    }
                                %>


                                <p>
                                    Size:
                                    <strong>
                                        <%= item.getSizeLabel() %>
                                    </strong>
                                </p>


                                <p class="cart-item-price">

                                    ₹<%= item.getUnitPrice() %>

                                </p>


                                <!-- =================================================
                                     QUANTITY
                                     ================================================= -->

                                <div class="cart-item-quantity">

                                    <form
                                        action="${pageContext.request.contextPath}/cart-item"
                                        method="post">

                                        <input
                                            type="hidden"
                                            name="action"
                                            value="update">

                                        <input
                                            type="hidden"
                                            name="cartItemId"
                                            value="<%= item.getCartItemId() %>">

                                        <label for="quantity-<%= item.getCartItemId() %>">
                                            Quantity
                                        </label>

                                        <input
                                            type="number"
                                            id="quantity-<%= item.getCartItemId() %>"
                                            name="quantity"
                                            value="<%= item.getQuantity() %>"
                                            min="1">

                                        <button type="submit">
                                            Update
                                        </button>

                                    </form>

                                </div>


                                <!-- =================================================
                                     REMOVE
                                     ================================================= -->

                                <form
                                    action="${pageContext.request.contextPath}/cart-item"
                                    method="post">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="remove">

                                    <input
                                        type="hidden"
                                        name="cartItemId"
                                        value="<%= item.getCartItemId() %>">

                                    <button
                                        type="submit"
                                        class="remove-item">

                                        Remove

                                    </button>

                                </form>

                            </div>


                            <!-- =================================================
                                 ITEM TOTAL
                                 ================================================= -->

                            <div class="cart-item-total">

                                ₹<%= itemTotal %>

                            </div>

                        </article>


                        <%
                            }
                        %>

                    </section>


                    <!-- =================================================
                         CART SUMMARY
                         ================================================= -->

                    <aside class="cart-summary">

                        <h2>
                            Order Summary
                        </h2>


                        <div class="summary-row">

                            <span>
                                Subtotal
                            </span>

                            <strong>
                                ₹<%= cartTotal %>
                            </strong>

                        </div>


                        <div class="summary-row">

                            <span>
                                Delivery
                            </span>

                            <span>
                                Calculated at checkout
                            </span>

                        </div>


                        <div class="summary-total">

                            <span>
                                Total
                            </span>

                            <strong>
                                ₹<%= cartTotal %>
                            </strong>

                        </div>


                        <a
                            href="${pageContext.request.contextPath}/checkout"
                            class="checkout-button">

                            Proceed to Checkout

                        </a>


                        <a
                            href="${pageContext.request.contextPath}/products"
                            class="continue-shopping">

                            ← Continue Shopping

                        </a>

                    </aside>

                </div>


            <%
                } else {
            %>


                <!-- =================================================
                     EMPTY CART
                     ================================================= -->

                <section class="empty-cart">

                    <h2>
                        Your cart is empty
                    </h2>

                    <p>
                        You haven't added any products yet.
                    </p>

                    <a
                        href="${pageContext.request.contextPath}/products"
                        class="shop-button">

                        Start Shopping

                    </a>

                </section>


            <%
                }
            %>

        </div>

    </main>


    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>

</html>