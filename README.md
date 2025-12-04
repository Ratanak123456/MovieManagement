# Java Movie Management System

A console-based application for managing a movie library, built with Java.

## Description

This project is a simple, command-line-driven movie management system. It provides different functionalities based on user roles (Admin vs. User). The application uses an in-memory database, meaning all data is reset when the application closes.

## Features

### General
- User authentication (Login/Logout)

### User Role
- View all movies in a paginated, formatted table.
- Search for movies by title.

### Admin Role
- Includes all `User` role features.
- **Movie Management (CRUD):**
    - Add a new movie.
    - Update an existing movie's details.
    - Delete a movie.
- **User Management (CRUD):**
    - View all registered users.
    - Add a new user (with Admin or User roles).
    - Update a user's details.
    - Delete a user.

## Project Structure

The project follows a layered architecture to separate concerns:

```
src/
└── co/
    └── istad/
        └── mvm/
            ├── MovieProgram.java       # Main entry point & presentation logic
            ├── database/
            │   ├── MovieDatabase.java  # In-memory movie data store
            │   └── UserDatabase.java   # In-memory user data store
            ├── model/
            │   ├── Movie.java          # Movie data model
            │   └── User.java           # User data model
            ├── service/
            │   ├── AuthServiceImpl.java # Authentication & user business logic
            │   └── MovieServiceImpl.java # Movie business logic
            └── util/
                ├── InputUtil.java      # Handles user console input
                └── ViewUtil.java       # Renders views and menus
```

## How to Run

1.  **Compile the code:**
    Open a terminal in the project's root directory and compile all `.java` files. You need to include the `text-table-formatter` library in the classpath.

    ```bash
    javac -d out -cp "path/to/text-table-formatter-1.1.2.jar" $(find src -name "*.java")
    ```
    *(Note: You may need to create the `out` directory first: `mkdir out`)*

2.  **Run the application:**
    Execute the `main` method from the compiled output directory.

    ```bash
    java -cp "out:path/to/text-table-formatter-1.1.2.jar" co.istad.mvm.MovieProgram
    ```

3.  **Login Credentials:**
    - **Admin:**
        - **Username:** `admin`
        - **Password:** `admin123`
    - **User:**
        - **Username:** `user`
        - **Password:** `user123`

## Dependencies

-   [Text Table Formatter](https://github.com/iNamC/text-table-formatter) v1.1.2 - For displaying data in a clean, tabular format in the console.
