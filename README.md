# Auth Evolution: Spring Security to AWS Cognito

This project demonstrates the architectural shift from a fully manual authentication system to a managed cloud identity provider. It serves as a codebase comparison between building authentication infrastructure from scratch versus offloading it to AWS.

## Technologies Used
* Spring Boot
* React
* MySQL

## What This Project Does
This repository is designed to be explored across different versions to illustrate the migration process:

* **Phase 1 (The Manual Way):** Implements traditional Spring Security. This includes custom JWT generation, storing users in a MySQL database, password hashing, and building a manual email OTP verification flow.
* **Phase 2 (The Managed Way):** Refactors the backend to strip out the custom security logic. It replaces the manual implementation with AWS Cognito for user management, OIDC standard token validation, and automated OTP delivery.