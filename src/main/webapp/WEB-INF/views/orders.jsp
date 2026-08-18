<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Order" %>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>My Orders | Fashion Store</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/style.css">

    <style>

        .orders-page {
            max-width: 1200px;
            margin: 50px auto;
            padding: 0 30px;
        }

        .orders-title {
            font-size: 38px;
            margin-bottom: 8px;
            color: #182f3d;
        }

        .orders-subtitle {
            color: #777;
            margin-bottom: 40px;
        }

        .order-card {
            border: 1px solid #ddd;
            background: #fff;
            padding: 25px;
            margin-bottom: 20px;
        }

        .order-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #eee;
            padding-bottom: 18px;
            margin-bottom: 18px;
        }

        .order-number {
            font-size: 18px;
            font-weight: bold;
            color: #182f3d;
        }

        .order-number a {
            color: #182f3d;
            text-decoration: none;
        }

        .order-number a:hover {
            text-decoration: underline;
        }

        .order-date {
            color: #777;
            font-size: 14px;
        }

        .order-info {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
        }

        .info-label {
            display: block;
            color: #777;
            font-size: 13px;
            margin-bottom: 6px;
        }

        .info-value {
            font-weight: 600;
        }

        .status {
            display: inline-block;
            padding: 6px 12px;
            border: 1px solid #ddd;
            font-size: 13px;
        }

        .empty-orders {
            border: 1px solid #ddd;
            padding: 60px 20px;
            text-align: center;
        }

        .empty-orders h2 {
            color: #182f3d;
        }

        .shop-button {
            display: inline-block;
            margin-top: 20px;
            padding: 12px 25px;
            background: #182f3d;
            color: white;
            text-decoration: none;
        }

        @media (max-width: 800px) {

            .orders-page {
                padding: 0 15px;
            }

            .order-info {
                grid-template-columns: 1fr;
            }

            .order-header {
                flex-direction: column;
                align-items: flex-start;
                gap: 10px;
            }

        }

    </style>

</head>

<body>

<jsp:include page="partials/navbar.jsp"/>

<%

    List<Order> orders =
            (List<Order>) request.getAttribute("orders");

%>

<div class="orders-page">

    <h1 class="orders-title">
        My Orders
    </h1>

    <p class="orders-subtitle">
        View your previous orders and their current status.
    </p>


    <%

        if (orders == null || orders.isEmpty()) {

    %>


        <div class="empty-orders">

            <h2>
                No orders yet
            </h2>

            <p>
                You haven't placed any orders yet.
            </p>

            <a class="shop-button"
               href="<%= request.getContextPath() %>/products">

                Start Shopping

            </a>

        </div>


    <%

        } else {

            for (Order order : orders) {

    %>


        <div class="order-card">


            <div class="order-header">


                <div class="order-number">

                    <a href="<%= request.getContextPath() %>/order-details?orderId=<%= order.getOrderId() %>">

                        Order #<%= order.getOrderId() %>

                    </a>

                </div>


                <div class="order-date">

                    <%= order.getOrderDate() %>

                </div>


            </div>


            <div class="order-info">


                <div>

                    <span class="info-label">
                        Total
                    </span>

                    <span class="info-value">

                        ₹<%= order.getTotalAmount() %>

                    </span>

                </div>


                <div>

                    <span class="info-label">
                        Payment
                    </span>

                    <span class="info-value">

                        <%= order.getPaymentMethod() %>

                    </span>

                </div>


                <div>

                    <span class="info-label">
                        Payment Status
                    </span>

                    <span class="status">

                        <%= order.getPaymentStatus() %>

                    </span>

                </div>


                <div>

                    <span class="info-label">
                        Order Status
                    </span>

                    <span class="status">

                        <%= order.getOrderStatus() %>

                    </span>

                </div>


                <div>

                    <span class="info-label">
                        Delivery Address
                    </span>

                    <span class="info-value">

                        <%= order.getDeliveryAddress() %>

                    </span>

                </div>


            </div>


        </div>


    <%

            }

        }

    %>


</div>


<jsp:include page="partials/footer.jsp"/>


</body>

</html>