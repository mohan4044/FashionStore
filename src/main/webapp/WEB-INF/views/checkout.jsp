<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.fashionstore.model.Cart" %>
<%@ page import="com.fashionstore.model.CartItem" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Checkout | Fashion Store</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/style.css">

    <style>

        .checkout-page {
            max-width: 1200px;
            margin: 50px auto;
            padding: 0 30px;
        }

        .checkout-title {
            font-size: 38px;
            margin-bottom: 8px;
            color: #182f3d;
        }

        .checkout-subtitle {
            color: #777;
            margin-bottom: 40px;
        }

        .checkout-layout {
            display: grid;
            grid-template-columns: 1.5fr 1fr;
            gap: 40px;
            align-items: start;
        }

        .checkout-section {
            border: 1px solid #ddd;
            padding: 30px;
            background: #fff;
        }

        .checkout-section h2 {
            margin-top: 0;
            margin-bottom: 25px;
            color: #182f3d;
        }

        .form-group {
            margin-bottom: 22px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
        }

        .form-group input,
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 13px;
            border: 1px solid #ccc;
            font-size: 15px;
            box-sizing: border-box;
        }

        .form-group textarea {
            min-height: 120px;
            resize: vertical;
        }

        .payment-option {
            border: 1px solid #ddd;
            padding: 15px;
            margin-bottom: 12px;
        }

        .payment-option label {
            cursor: pointer;
        }

        .payment-option input {
            margin-right: 10px;
        }

        .order-item {
            display: flex;
            justify-content: space-between;
            gap: 20px;
            padding: 18px 0;
            border-bottom: 1px solid #eee;
        }

        .order-item-name {
            font-weight: 600;
            color: #333;
        }

        .order-item-details {
            color: #777;
            font-size: 14px;
            margin-top: 7px;
            line-height: 1.6;
        }

        .order-item-price {
            font-weight: 600;
            white-space: nowrap;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
        }

        .summary-total {
            border-top: 1px solid #ddd;
            margin-top: 15px;
            padding-top: 20px;
            font-size: 22px;
            font-weight: bold;
            color: #182f3d;
        }

        .place-order-button {
            width: 100%;
            padding: 16px;
            margin-top: 25px;
            border: none;
            background: #182f3d;
            color: white;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
        }

        .place-order-button:hover {
            background: #294b5d;
        }

        .checkout-error {
            padding: 15px;
            background: #f8eaea;
            color: #a33;
            margin-bottom: 25px;
        }

        .empty-checkout {
            text-align: center;
            padding: 60px 20px;
        }

        .empty-checkout a {
            display: inline-block;
            margin-top: 20px;
            padding: 12px 25px;
            background: #182f3d;
            color: white;
            text-decoration: none;
        }

        @media (max-width: 800px) {

            .checkout-layout {
                grid-template-columns: 1fr;
            }

            .checkout-page {
                padding: 0 15px;
            }
        }

    </style>

</head>

<body>

<jsp:include page="partials/navbar.jsp"/>

<%
    String checkoutError =
            (String) request.getAttribute("checkoutError");

    Cart cart =
            (Cart) request.getAttribute("cart");

    List<CartItem> cartItems =
            (List<CartItem>) request.getAttribute("cartItems");

    BigDecimal subtotal = BigDecimal.ZERO;

    if (cartItems != null) {

        for (CartItem item : cartItems) {

            if (item.getUnitPrice() != null) {

                BigDecimal itemTotal =
                        item.getUnitPrice()
                            .multiply(
                                BigDecimal.valueOf(
                                    item.getQuantity()
                                )
                            );

                subtotal = subtotal.add(itemTotal);
            }
        }
    }
%>


<div class="checkout-page">

    <h1 class="checkout-title">
        Checkout
    </h1>

    <p class="checkout-subtitle">
        Complete your order details.
    </p>


    <% if (checkoutError != null) { %>

        <div class="checkout-error">
            <%= checkoutError %>
        </div>

    <% } %>


    <% if (cartItems == null || cartItems.isEmpty()) { %>

        <div class="checkout-section empty-checkout">

            <h2>Your cart is empty</h2>

            <p>
                Add some products to your cart before checking out.
            </p>

            <a href="<%= request.getContextPath() %>/products">
                Continue Shopping
            </a>

        </div>

    <% } else { %>


        <div class="checkout-layout">


            <!-- ========================= -->
            <!-- DELIVERY INFORMATION -->
            <!-- ========================= -->

            <div class="checkout-section">

                <h2>Delivery Information</h2>

                <form method="post"
                      action="<%= request.getContextPath() %>/checkout">


                    <div class="form-group">

                        <label for="deliveryAddress">
                            Delivery Address
                        </label>

                        <textarea
                                id="deliveryAddress"
                                name="deliveryAddress"
                                placeholder="Enter your complete delivery address"
                                required></textarea>

                    </div>


                    <h2>Payment Method</h2>


                    <div class="payment-option">

                        <label>

                            <input
                                    type="radio"
                                    name="paymentMethod"
                                    value="COD"
                                    required>

                            Cash on Delivery

                        </label>

                    </div>


                    <div class="payment-option">

                        <label>

                            <input
                                    type="radio"
                                    name="paymentMethod"
                                    value="UPI">

                            UPI

                        </label>

                    </div>


                    <div class="payment-option">

                        <label>

                            <input
                                    type="radio"
                                    name="paymentMethod"
                                    value="CARD">

                            Card

                        </label>

                    </div>


                    <button
                            type="submit"
                            class="place-order-button">

                        Place Order

                    </button>


                </form>

            </div>



            <!-- ========================= -->
            <!-- ORDER SUMMARY -->
            <!-- ========================= -->

            <div class="checkout-section">

                <h2>Order Summary</h2>


                <%
                    for (CartItem item : cartItems) {

                        BigDecimal itemTotal = BigDecimal.ZERO;

                        if (item.getUnitPrice() != null) {

                            itemTotal =
                                    item.getUnitPrice()
                                        .multiply(
                                            BigDecimal.valueOf(
                                                item.getQuantity()
                                            )
                                        );
                        }
                %>


                    <div class="order-item">

                        <div>

                            <div class="order-item-name">

                                Product #<%= item.getProductId() %>

                            </div>


                            <div class="order-item-details">

                                Size:
                                <%= item.getSizeLabel() %>

                                <br>

                                Quantity:
                                <%= item.getQuantity() %>

                            </div>

                        </div>


                        <div class="order-item-price">

                            ₹<%= itemTotal %>

                        </div>

                    </div>


                <%
                    }
                %>


                <div class="summary-row">

                    <span>
                        Subtotal
                    </span>

                    <span>
                        ₹<%= subtotal %>
                    </span>

                </div>


                <div class="summary-row">

                    <span>
                        Delivery
                    </span>

                    <span>
                        Calculated at checkout
                    </span>

                </div>


                <div class="summary-row summary-total">

                    <span>
                        Total
                    </span>

                    <span>
                        ₹<%= subtotal %>
                    </span>

                </div>


            </div>


        </div>


    <% } %>

</div>


<jsp:include page="partials/footer.jsp"/>


</body>
</html>