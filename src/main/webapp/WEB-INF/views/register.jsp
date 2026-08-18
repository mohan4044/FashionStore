<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Create Account | Fashion Store</title>

    <!-- Global CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/style.css">

    <!-- Register CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/register.css">

</head>

<body>

    <!-- =====================================================
         NAVBAR
         ===================================================== -->

    <jsp:include page="/WEB-INF/views/partials/navbar.jsp" />


    <!-- =====================================================
         REGISTRATION PAGE
         ===================================================== -->

    <main class="auth-page">

        <div class="auth-container">

            <div class="auth-header">

                <h1>Create Account</h1>

                <p>
                    Join Fashion Store and discover your style.
                </p>

            </div>


            <!-- Error Message -->

            <% if (request.getAttribute("error") != null) { %>

                <div class="form-error">

                    <%= request.getAttribute("error") %>

                </div>

            <% } %>


            <!-- Registration Form -->

            <form
                action="${pageContext.request.contextPath}/register"
                method="post"
                class="auth-form">


                <!-- Full Name -->

                <div class="form-group">

                    <label for="fullName">
                        Full Name
                    </label>

                    <input
                        type="text"
                        id="fullName"
                        name="fullName"
                        value="${param.fullName}"
                        placeholder="Enter your full name"
                        required>

                </div>


                <!-- Email -->

                <div class="form-group">

                    <label for="email">
                        Email
                    </label>

                    <input
                        type="email"
                        id="email"
                        name="email"
                        value="${param.email}"
                        placeholder="Enter your email"
                        required>

                </div>


                <!-- Phone -->

                <div class="form-group">

                    <label for="phone">
                        Phone Number
                    </label>

                    <input
                        type="tel"
                        id="phone"
                        name="phone"
                        value="${param.phone}"
                        placeholder="Enter your phone number"
                        required>

                </div>


                <!-- Gender -->

                <div class="form-group">

                    <label for="gender">
                        Gender
                    </label>

                    <select
                        id="gender"
                        name="gender"
                        required>

                        <option value="">
                            Select Gender
                        </option>

                        <option value="Male">
                            Male
                        </option>

                        <option value="Female">
                            Female
                        </option>

                        <option value="Other">
                            Other
                        </option>

                    </select>

                </div>


                <!-- Address -->

                <div class="form-group">

                    <label for="address">
                        Address
                    </label>

                    <textarea
                        id="address"
                        name="address"
                        rows="3"
                        placeholder="Enter your delivery address"
                        required>${param.address}</textarea>

                </div>


                <!-- Password -->

                <div class="form-group">

                    <label for="password">
                        Password
                    </label>

                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Create a password"
                        required>

                </div>


                <!-- Confirm Password -->

                <div class="form-group">

                    <label for="confirmPassword">
                        Confirm Password
                    </label>

                    <input
                        type="password"
                        id="confirmPassword"
                        name="confirmPassword"
                        placeholder="Confirm your password"
                        required>

                </div>


                <!-- Submit -->

                <button
                    type="submit"
                    class="auth-button">

                    Create Account

                </button>

            </form>


            <!-- Login Link -->

            <div class="auth-footer">

                <p>
                    Already have an account?

                    <a href="${pageContext.request.contextPath}/login">
                        Login
                    </a>
                </p>

            </div>

        </div>

    </main>


    <!-- =====================================================
         FOOTER
         ===================================================== -->

    <jsp:include page="/WEB-INF/views/partials/footer.jsp" />

</body>

</html>