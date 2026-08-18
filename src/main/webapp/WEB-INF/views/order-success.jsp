<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.fashionstore.model.Order" %>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Order Successful | Fashion Store</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/assets/css/style.css">

<style>

.success-page {

    max-width: 900px;

    margin: 70px auto;

    padding: 0 25px;

}

.success-box {

    background: #ffffff;

    border: 1px solid #e1e5e8;

    padding: 50px;

    text-align: center;

}

.success-icon {

    width: 70px;

    height: 70px;

    margin: 0 auto 25px;

    border-radius: 50%;

    background: #182f3d;

    color: #ffffff;

    display: flex;

    align-items: center;

    justify-content: center;

    font-size: 38px;

    font-weight: bold;

}

.success-title {

    margin: 0 0 15px;

    color: #182f3d;

    font-size: 36px;

}

.success-message {

    color: #666;

    font-size: 16px;

    margin-bottom: 35px;

}

.order-details {

    max-width: 600px;

    margin: 0 auto 35px;

    border-top: 1px solid #ddd;

    border-bottom: 1px solid #ddd;

    padding: 20px 0;

}

.order-row {

    display: flex;

    justify-content: space-between;

    align-items: center;

    padding: 13px 5px;

    border-bottom: 1px solid #eeeeee;

}

.order-row:last-child {

    border-bottom: none;

}

.order-label {

    color: #777;

    font-size: 15px;

}

.order-value {

    color: #182f3d;

    font-weight: 600;

    font-size: 15px;

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

    color: #ffffff;

    text-decoration: none;

    font-size: 15px;

    font-weight: 600;

    border: 1px solid #182f3d;

}

.success-button:hover {

    background: #294b5d;

}

.secondary-button {

    background: #ffffff;

    color: #182f3d;

}

.secondary-button:hover {

    background: #f5f5f5;

}

.checkout-warning {

    margin: 20px auto;

    padding: 12px 15px;

    max-width: 600px;

    background: #fff3f3;

    border: 1px solid #e5bcbc;

    color: #a33;

}

@media (max-width: 600px) {

    .success-page {

        margin: 40px auto;

        padding: 0 15px;

    }

    .success-box {

        padding: 35px 20px;

    }

    .success-title {

        font-size: 28px;

    }

    .order-row {

        gap: 20px;

    }

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

        <% } else { %>

        <div class="checkout-warning">
            Order was created, but order details could not be loaded.
        </div>

        <% } %>

        <% if (checkoutWarning != null) { %>

        <div class="checkout-warning">
            <%= checkoutWarning %>
        </div>

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