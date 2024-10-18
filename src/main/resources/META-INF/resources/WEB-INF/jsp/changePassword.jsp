<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Change Password Web Page</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <style>
        .cascading-right {
            margin-right: -50px;
        }

        @media (max-width: 991.98px) {
            .cascading-right {
                margin-right: 0;
            }
        }
    </style>
    <script type="text/javascript">
        function passwordMatch(confirmPassword) {
            if (confirmPassword.value != document.getElementById("password").value) {
                confirmPassword.setCustomValidity("Passwords do not match");
            } else {
                confirmPassword.setCustomValidity("");
            }
        }
    </script>
</head>
<body>
<!-- Section: Design Block -->
<section class="text-center text-lg-start">

    <!-- Jumbotron -->
    <div class="container py-4">
        <div class="row g-0 align-items-center">
            <div class="col-lg-6 mb-5 mb-lg-0">
                <div class="card cascading-right" style="
            background: hsla(0, 0%, 100%, 0.55);
            backdrop-filter: blur(30px);
            ">
                    <div class="card-body p-5 shadow-5 text-center">
                        <h2 class="fw-bold mb-5">Change Password</h2>

                        <div id="errorMessage" class="divider d-flex align-items-center my-4 text-danger">
                            <p class="text-center fw-bold mx-3 mb-0">${errorMessage}</p>
                        </div>

                        <form action="changePassword" method="post">

                            <!-- Password input -->
                            <div class="form-outline mb-4">
                                <input type="password" id="password" name="password"
                                       oninput="this.setCustomValidity('')"
                                       class="form-control" required/>
                                <label class="form-label" for="form3Example4">Password</label>
                            </div>

                            <!-- Confirm Password input -->
                            <div class="form-outline mb-4">
                                <input type="password" id="confirmPassword"
                                       oninput="passwordMatch(this)"
                                       class="form-control" required/>
                                <label class="form-label" for="form3Example4">Confirm Password</label>
                            </div>

                            <input type="hidden" name="resetPasswordToken" value="${resetPasswordToken}"/>

                            <input name="_csrf" id="csrf" type="hidden" value="${_csrf.token}"/>

                            <!-- Submit button -->
                            <button type="submit" class="btn btn-primary btn-block mb-4">
                                Change Password
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <div class="col-lg-6 mb-5 mb-lg-0">
                <img src="https://mdbootstrap.com/img/new/ecommerce/vertical/004.jpg" class="w-100 rounded-4 shadow-4"
                     alt=""/>
            </div>
        </div>
    </div>
    <!-- Jumbotron -->
</section>
<!-- Section: Design Block -->
</body>
</html>