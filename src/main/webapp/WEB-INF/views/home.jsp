<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Fashion Store</title>

    <!-- Global CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">

    <!-- Home Page CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/home.css">

    <style>

        /* =====================================================
           FEATURED PRODUCTS
           ===================================================== */

        .featured-section {
            max-width: 1200px;
            margin: 60px auto;
            padding: 0 30px;
        }

        .section-heading {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .section-heading h2 {
            color: #182f3d;
            margin: 0;
        }

        .section-heading a {
            color: #182f3d;
            text-decoration: none;
        }

        .section-heading a:hover {
            text-decoration: underline;
        }


        /* =====================================================
           PRODUCT GRID
           ===================================================== */

        .product-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 25px;
        }


        /* =====================================================
           PRODUCT CARD
           ===================================================== */

        .product-card {
            display: block;

            text-decoration: none;

            color: inherit;

            background: #ffffff;

            border: 1px solid #eeeeee;

            padding-bottom: 20px;

            transition:
                transform 0.2s ease,
                box-shadow 0.2s ease;
        }

        .product-card:hover {
            transform: translateY(-5px);

            box-shadow:
                0 8px 20px rgba(0, 0, 0, 0.10);
        }


        /* =====================================================
           PRODUCT IMAGE
           ===================================================== */

        .product-image {
            width: 100%;

            height: 300px;

            overflow: hidden;

            background: #f5f5f5;

            display: flex;

            align-items: center;

            justify-content: center;
        }

        .product-image img {
            width: 100%;

            height: 100%;

            object-fit: cover;

            display: block;
        }


        /* =====================================================
           PRODUCT TEXT
           ===================================================== */

        .product-card h3 {
            margin:
                18px 15px 8px;

            color: #182f3d;

            font-size: 18px;
        }

        .product-card p {
            margin:
                0 15px 10px;

            color: #777777;
        }

        .product-price {
            margin:
                0 15px;

            color: #182f3d;

            font-weight: bold;

            font-size: 17px;
        }


        /* =====================================================
           MOBILE
           ===================================================== */

        @media (max-width: 900px) {

            .product-grid {
                grid-template-columns:
                    repeat(2, 1fr);
            }
        }


        @media (max-width: 550px) {

            .featured-section {
                padding: 0 15px;
            }

            .product-grid {
                grid-template-columns:
                    1fr;
            }

            .section-heading {
                gap: 15px;

                align-items: flex-start;

                flex-direction: column;
            }
        }

    </style>

</head>


<body>


<!-- =====================================================
     NAVBAR
     ===================================================== -->

<jsp:include page="/WEB-INF/views/partials/navbar.jsp" />


<!-- =====================================================
     HOME PAGE
     ===================================================== -->

<main class="home-page">


    <!-- =================================================
         HERO SECTION
         ================================================= -->

    <section class="hero-section">

        <div class="hero-content">

            <div class="hero-text">

                <p class="eyebrow">
                    New Collection
                </p>

                <h1>
                    Style that speaks for you.
                </h1>

                <p>
                    Discover contemporary fashion designed
                    for every style, occasion and moment.
                </p>

                <a
                    href="${pageContext.request.contextPath}/products"
                    class="hero-button">

                    Shop Now

                </a>

            </div>


            <div class="hero-image">

                <span>
                    Fashion Store
                </span>

            </div>

        </div>

    </section>



    <!-- =================================================
         CATEGORY SECTION
         ================================================= -->

    <section class="category-section">

        <div class="section-heading">

            <h2>
                Shop by Category
            </h2>

            <a
                href="${pageContext.request.contextPath}/products">

                View All

            </a>

        </div>


        <div class="category-grid">


            <a
                href="${pageContext.request.contextPath}/products?category=Men"
                class="category-card">

                <h3>
                    Men
                </h3>

            </a>


            <a
                href="${pageContext.request.contextPath}/products?category=Women"
                class="category-card">

                <h3>
                    Women
                </h3>

            </a>


            <a
                href="${pageContext.request.contextPath}/products?category=Kids"
                class="category-card">

                <h3>
                    Kids
                </h3>

            </a>


            <a
                href="${pageContext.request.contextPath}/products?category=Footwear"
                class="category-card">

                <h3>
                    Footwear
                </h3>

            </a>


        </div>

    </section>



    <!-- =================================================
         FEATURED PRODUCTS
         ================================================= -->

    <section class="featured-section">


        <div class="section-heading">

            <h2>
                Featured Products
            </h2>

            <a
                href="${pageContext.request.contextPath}/products">

                View All Products

            </a>

        </div>



        <div class="product-grid">


            <!-- =================================================
                 PRODUCT 1
                 Classic Black T-Shirt
                 Database product_id = 1
                 ================================================= -->

            <a
                href="${pageContext.request.contextPath}/product?id=1"
                class="product-card">


                <div class="product-image">

                    <img
                        src="${pageContext.request.contextPath}/assets/images/black-tshirt.jpg"
                        alt="Classic Black T-Shirt">

                </div>


                <h3>
                    Classic Black T-Shirt
                </h3>


                <p>
                    Roadster
                </p>


                <div class="product-price">
                    ₹799
                </div>


            </a>



            <!-- =================================================
                 PRODUCT 5
                 Floral Summer Dress
                 Database product_id = 5
                 ================================================= -->

            <a
                href="${pageContext.request.contextPath}/product?id=5"
                class="product-card">


                <div class="product-image">

                    <img
                        src="${pageContext.request.contextPath}/assets/images/floral-dress.jpg"
                        alt="Floral Summer Dress">

                </div>


                <h3>
                    Floral Summer Dress
                </h3>


                <p>
                    AND
                </p>


                <div class="product-price">
                    ₹1,999
                </div>


            </a>



            <!-- =================================================
                 PRODUCT 11
                 White Casual Sneakers
                 Database product_id = 11
                 ================================================= -->

            <a
                href="${pageContext.request.contextPath}/product?id=11"
                class="product-card">


                <div class="product-image">

                    <img
                        src="${pageContext.request.contextPath}/assets/images/white-sneakers.jpeg"
                        alt="White Casual Sneakers">

                </div>


                <h3>
                    White Casual Sneakers
                </h3>


                <p>
                    Puma
                </p>


                <div class="product-price">
                    ₹2,999
                </div>


            </a>



            <!-- =================================================
                 PRODUCT 17
                 Mens Cotton Kurta
                 Database product_id = 17
                 ================================================= -->

            <a
                href="${pageContext.request.contextPath}/product?id=17"
                class="product-card">


                <div class="product-image">

                    <img
                        src="${pageContext.request.contextPath}/assets/images/mens-kurta.jpeg"
                        alt="Mens Cotton Kurta">

                </div>


                <h3>
                    Mens Cotton Kurta
                </h3>


                <p>
                    Manyavar
                </p>


                <div class="product-price">
                    ₹1,899
                </div>


            </a>


        </div>

    </section>


</main>



<!-- =====================================================
     FOOTER
     ===================================================== -->

<jsp:include page="/WEB-INF/views/partials/footer.jsp" />


</body>

</html>