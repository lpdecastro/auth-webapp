# Auth Web Application

A Spring Boot-based authentication web application featuring user registration, login, password management, email verification, and profile management.

## Features

### Authentication and Authorization
- **User Registration**: New users can register by providing basic details like name, email, and password.
- **User Login**: Registered users can log in using their email and password.
- **Forgot Password**: Users can request a reset password link to their registered email if they forget their password.
- **Reset Password**: Users can reset their password using the link provided via email.
- **Email Verification**: After registration, users receive a verification email to confirm their email address.
- **Resend Email Verification**: Users can request a resend of the email verification link if their email is not verified.
- **Change Password**: Users can change their password from the Edit Profile page.
- **Edit Profile**: Users can update their first name, last name, and email address.
- **Locked Content**: Access to specific content is locked behind authentication.

## Project Structure

The project follows the standard Spring Boot architecture, with controllers handling HTTP requests, services for business logic, repositories for data persistence, and DTOs for data transfer.

### Folders Overview

- **controller**: Contains all the controllers handling HTTP requests for the authentication process.
    - `ChangePasswordController.java`: Manages password change requests.
    - `EditProfileController.java`: Handles profile edits for the user.
    - `EmailVerificationController.java`: Manages email verification.
    - `ForgotPasswordController.java`: Handles requests to reset forgotten passwords.
    - `HomeController.java`: Manages home page and navigation.
    - `LoginController.java`: Handles user login.
    - `RegistrationController.java`: Handles new user registration.
    - `ResetPasswordController.java`: Handles password reset requests.

- **dto**: Data Transfer Objects used for carrying data between different layers of the application.
    - `ChangePasswordDto.java`
    - `UserDto.java`

- **entity**: Represents database entities.
    - `RoleEntity.java`: Represents user roles.
    - `UserEntity.java`: Represents user details stored in the database.

- **exception**: Contains custom exceptions used throughout the application.
    - `EmailNotVerifiedException.java`: Custom exception for handling email verification failures.

- **repository**: Interfaces for interacting with the database.
    - `RoleRepository.java`
    - `UserRepository.java`

- **security**: Custom security configuration for handling Spring Security.
    - `CustomAuthenticationFailureHandler.java`: Handles authentication failures (like unverified email).
    - `CustomAuthenticationSuccessHandler.java`: Handles successful authentication.
    - `SpringSecurityCustomConfiguration.java`: Contains the Spring Security configuration.

- **service**: Contains service classes with business logic.
    - `EmailService.java`: Handles sending emails (verification, reset password, etc.).
    - `UserService.java`: Interface for user-related services.
    - `UserServiceImpl.java`: Implementation of user-related services.

- **util**: Contains utility classes.
    - `LoginModelMapper.java`: Maps between DTOs and entities.

- **resources**:
    - **templates**: Thymeleaf HTML templates used for the front-end views.
        - `change-password.html`
        - `edit-profile.html`
        - `error.html`
        - `forgot-password.html`
        - `home.html`
        - `login.html`
        - `register.html`
        - `resend-email-verification.html`
        - `reset-password.html`
        - `reset-password-link-error.html`
        - `verify-email.html`

## Backlog Features
- **Send Change Password Successfully Email**: Notify users when their password is successfully changed.
- **Change Password**: Users should be able to change their password from the profile page.
- **404 Error Page**: A custom 404 error page when users navigate to non-existent routes.
- **Change Email Address**: Allow users to update their email address with email verification.

## Technical Debt
- **Display messages without query parameters**: Implement a cleaner method to display success or error messages without relying on URL query parameters.
- **Improve the UI for Read-only Fields**: Make read-only fields (such as the email address in the profile) more visually distinct.
- **Fade out messages**: Implement a smoother fade-out effect for messages across forms.
- **Email Already Verified Screen**: Display a more user-friendly page when the user attempts to verify an already verified email.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repository/auth-web-app.git
   ```

2. Navigate to the project directory:
   ```bash
   cd auth-web-app
   ```

3. Install the dependencies and build the project using Maven:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Technology Stack
- **Java**: Backend language.
- **Spring Boot**: Backend framework for building the authentication system.
- **Spring Security**: Used for authentication and authorization.
- **Thymeleaf**: For rendering dynamic web pages.
- **MySQL**: The database used for storing user data.
- **Spring Data JPA**: For data access and persistence.
- **Bootstrap**: For front-end styling.