<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.fashionstore.model.Order" %>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>Order Successful | Fashion Store</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/style.css">

    <style>

        .success-page {
            max-width: 800px;
            margin: 80px auto;
            padding: 0 30px;
            text-align: center;
        }

        .success-box {
            border: 1px solid #ddd;
            padding: 50px 40px;
            background: #fff;
        }

        .success-icon {
            font-size: 60px;
            margin-bottom: 20px;
        }

        .success-title {
            font-size: 36px;
            color: #182f3d;
            margin-bottom: 15px;
        }

        .success-message {
            color: #666;
            font-size: 17px;
            margin-bottom: 30px;
        }

        .order-details {
            border-top: 1px solid #ddd;
            border-bottom: 1px solid #ddd;
            padding: 25px 0;
            margin-bottom: 30px;
        }

        .order-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
        }

        .order-label {
            color: #777;
        }

        .order-value {
            font-weight: 600;
            color: #333;
        }

        .success-buttons {
            display: flex;
            justify-content: center;
            gap: 15px;
            flex-wrap: wrap;
        }

        .success-button {
            display: inline-block;
            padding: 14px 28px;
            background: #182f3d;
            color: white;
            text-decoration: none;
            border: none;
        }

        .success-button:hover {
            background: #294b5d;
        }

        .secondary-button {
            background: white;
            color: #182f3d;
            border: 1px solid #182f3d;
        }

        .secondary-button:hover {
            background: #f5f5f5;
        }

    </style>

</head>

<body>

<jsp:include page="partials/navbar.jsp"/>

<%

Order order =
        (Order) request.getAttribute("order");

String checkoutWarning =
        (String) request.getAttribute("checkoutWarning");

%>

<div class="success-page">

    <div class="success-box">

        <div class="success-icon">
            ✓
        </div>

        <h1 class="success-title">
            Order Placed Successfully!
        </h1>

        <p class="success-message">

            Thank you for shopping with Fashion Store.

            Your order has been placed successfully.

        </p>

        <% if (order != null) { %>

        <div class="order-details">

            <div class="order-row">

                <span class="order-label">
                    Order ID
                </span>

                <span class="order-value">
                    #<%= order.getOrderId() %>
                </span>

            </div>

            <div class="order-row">

                <span class="order-label">
                    Total Amount
                </span>

                <span class="order-value">
                    ₹<%= order.getTotalAmount() %>
                </span>

            </div>

            <div class="order-row">

                <span class="order-label">
                    Payment Method
                </span>

                <span class="order-value">
                    <%= order.getPaymentMethod() %>
                </span>

            </div>

            <div class="order-row">

                <span class="order-label">
                    Payment Status
                </span>

                <span class="order-value">
                    <%= order.getPaymentStatus() %>
                </span>

            </div>

            <div class="order-row">

                <span class="order-label">
                    Order Status
                </span>

                <span class="order-value">
                    <%= order.getOrderStatus() %>
                </span>

            </div>

        </div>

        <% } %>

        <% if (checkoutWarning != null) { %>

        <p style="color:#a33;">
            <%= checkoutWarning %>
        </p>

        <% } %>

        <div class="success-buttons">

            <a class="success-button"
               href="<%= request.getContextPath() %>/products">

                Continue Shopping

            </a>

            <a class="success-button secondary-button"
               href="<%= request.getContextPath() %>/">

                Back to Home

            </a>

        </div>

    </div>

</div>

<jsp:include page="partials/footer.jsp"/>

</body>

</html>