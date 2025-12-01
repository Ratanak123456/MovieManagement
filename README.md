# Movie Management System

This is a simple command-line application for managing a collection of movies. It's a Java-based project that can be compiled and run from the command line.

## Features with Code Explanations

### 🔐 AUTHENTICATION & SECURITY FEATURES

#### 1. Role-Based Login (Admin vs. User)
- **Description:** The system provides separate login options for Admins and regular Users, directing them to different menus and functionalities.
- **Workflow:** The main menu prompts for a choice. Based on the choice, `adminLogin()` or `userLogin()` is called. Both methods then call a generic `login()` method, passing the required role ("ADMIN" or "USER") as an argument. The `login` method verifies credentials and checks if the authenticated user's role matches the required role.
- **Code:**
  ```java
  // From MovieProgram.java
  private void adminLogin() {
      ViewUtil.printHeader("🔐 ADMIN LOGIN");

      if (login("ADMIN")) {
          adminMode();
      }
  }

  private void userLogin() {
      ViewUtil.printHeader("👤 USER LOGIN");

      if (login("USER")) {
          userMode();
      }
  }

  private boolean login(String requiredRole) {
      int attempts = 0;
      while (attempts < 3) {
          String username = InputUtil.getString("Username: ");
          String password = InputUtil.getString("Password: ");

          if (authService.login(username, password)) {
              User user = authService.getSession().getCurrentUser();

              // Check if user has the required role
              if (requiredRole.equals("ADMIN") && !user.isAdmin()) {
                  ViewUtil.printErrorMessage("Access denied! Admin privileges required.");
                  authService.logout();
                  attempts++;
                  continue;
              }

              ViewUtil.printSuccessMessage("Login successful! Welcome " + user.getUsername() + " (" + user.getRole() + ")");
              return true;
          } else {
              // ... (error handling)
          }
      }
      // ... (error handling)
      return false;
  }
  ```

#### 2. Multiple Login Attempts
- **Description:** To prevent brute-force attacks, a user is allowed a maximum of three login attempts. After three failures, they are returned to the main menu.
- **Workflow:** The `login()` method in `MovieProgram.java` uses a `while` loop and an `attempts` counter. The loop continues as long as `attempts < 3`. If a login fails, the `attempts` counter is incremented, and an error message shows the number of attempts remaining.
- **Code:**
  ```java
  // From MovieProgram.java
  private boolean login(String requiredRole) {
      int attempts = 0;
      while (attempts < 3) {
          String username = InputUtil.getString("Username: ");
          String password = InputUtil.getString("Password: ");

          if (authService.login(username, password)) {
              // ... (success logic)
              return true;
          } else {
              attempts++;
              ViewUtil.printErrorMessage("Invalid credentials! Attempts left: " + (3 - attempts));
          }
      }

      ViewUtil.printErrorMessage("Too many failed attempts. Returning to main menu...");
      return false;
  }
  ```

#### 3. Session Management
- **Description:** The system tracks the currently logged-in user's state.
- **Workflow:** The `AuthService` contains a `Session` object. When a user successfully logs in (`authService.login()`), the `Session` object stores the `User` object. This `currentUser` can then be retrieved by any part of the application (like `adminMode()` or `userMode()`) to get the user's details.
- **Code:**
  ```java
  // From model/Session.java
  public class Session {
      private User currentUser;
      private boolean isLoggedIn;
      // ...
      public void login(User user) {
          this.currentUser = user;
          this.isLoggedIn = true;
      }
      // ...
      public User getCurrentUser() {
          return currentUser;
      }
  }

  // From service/AuthService.java
  public class AuthService {
      private UserDatabase userDatabase;
      private Session session;
      // ...
      public boolean login(String username, String password) {
          User user = userDatabase.authenticate(username, password);
          if (user != null) {
              session.login(user); // Session stores the logged-in user
              return true;
          }
          return false;
      }
  }
  ```

#### 4. Secure Logout
- **Description:** The system provides a secure logout mechanism that properly terminates the user's session.
- **Workflow:** When a user chooses to log out, the `logout()` method in `MovieProgram` is called. This method calls `authService.logout()`, which in turn calls `session.logout()`. The `Session` object then clears the `currentUser`, ensuring no part of the application can accidentally use the previous user's data.
- **Code:**
  ```java
  // From model/Session.java
  public class Session {
      // ...
      public void logout() {
          this.currentUser = null;
          this.isLoggedIn = false;
      }
  }

  // From service/AuthService.java
  public class AuthService {
      // ...
      public void logout() {
          session.logout();
      }
  }

  // From MovieProgram.java
  private void logout() {
      String username = authService.getSession().getCurrentUser().getUsername();
      authService.logout();
      ViewUtil.printSuccessMessage("User '" + username + "' logged out successfully.");
  }
  ```

#### 5. User Creation (Admin vs. Public)
- **Description:** The system differentiates between adding a user from the main menu (public registration) and adding a user from the admin panel. Both result in a "USER" role.
- **Workflow:** From the main menu, `addNewUser()` is called directly. From the admin panel, the `manageUsers()` menu also provides an option to call `addNewUser()`. The registration logic itself is handled by `AuthService`.
- **Code:**
  ```java
  // From MovieProgram.java
  private void addNewUser() {
      ViewUtil.printHeader("➕ ADD NEW USER");
      String username = InputUtil.getString("Enter username: ");
      // ... (check for duplicates) ...
      String password = InputUtil.getString("Enter password: ");
      String result = authService.register(username, password); // Delegates to AuthService
      // ... (print result) ...
  }
  ```

#### 6. Duplicate Username Prevention
- **Description:** The system prevents the creation of new users if the chosen username already exists.
- **Workflow:** Before attempting to register a new user, the `addNewUser()` method in `MovieProgram` calls `authService.getUserDatabase().userExists()`. This method checks the `UserDatabase` to see if any existing user has the same username (case-insensitively).
- **Code:**
  ```java
  // From MovieProgram.java
  private void addNewUser() {
      ViewUtil.printHeader("➕ ADD NEW USER");
      String username = InputUtil.getString("Enter username: ");

      // Check if username already exists
      if (authService.getUserDatabase().userExists(username)) {
          ViewUtil.printErrorMessage("Username '" + username + "' already exists! Please choose a different username.");
          return; // Stops the registration process
      }
      // ... (proceed with registration) ...
  }

  // From database/UserDatabase.java
  public boolean userExists(String username) {
      for (User user : users) {
          if (user.getUsername().equalsIgnoreCase(username)) {
              return true;
          }
      }
      return false;
  }
  ```

#### 7. Password Visibility
- **Description:** The admin has the ability to see the passwords of all users in the system.
- **Workflow:** In `MovieProgram.java`, the `manageUsers()` method calls `ViewUtil.printUserList()`, passing a boolean flag `showPasswords` as `true`. In `ViewUtil`, this flag is used to decide whether to print the actual password or a masked version (`********`).
- **Code:**
  ```java
  // From MovieProgram.java
  private void manageUsers() {
      ViewUtil.printHeader("👥 USER MANAGEMENT");
      List<User> users = authService.getUserDatabase().getAllUsers();

      // The 'true' flag here enables password visibility
      ViewUtil.printUserList(users, true);
      // ...
  }

  // From util/ViewUtil.java
  public static void printUserList(List<User> userList, boolean showPasswords) {
      // ...
      for (User user : userList) {
          table.addCell(user.getId());
          table.addCell(user.getUsername());

          // Show actual password if showPasswords is true, otherwise show ******
          if (showPasswords) {
              table.addCell(user.getPassword());
          } else {
              table.addCell("********");
          }
          // ...
      }
      // ...
  }
  ```

#### 8. Single Admin System
- **Description:** The system is designed with a pre-configured, single admin account. The registration feature only allows for the creation of regular users.
- **Workflow:** In the `UserDatabase`, the `initializeDefaultUsers()` method creates the "admin" user with the "ADMIN" role. The public-facing `register` method in `AuthService` hardcodes the role of any new user to "USER".
- **Code:**
  ```java
  // From database/UserDatabase.java
  private void initializeDefaultUsers() {
      // Admin account
      User admin = new User();
      admin.setId(generateUserId());
      admin.setUsername("admin");
      admin.setPassword("admin123");
      admin.setRole("ADMIN"); // Admin role is set here
      users.add(admin);

      // Regular user 1
      // ... (role is "USER")
  }

  // From service/AuthService.java
  public String register(String username, String password) {
      // ...
      User newUser = new User();
      newUser.setUsername(username);
      newUser.setPassword(password);
      newUser.setRole("USER"); // New users are always "USER"
      // ...
  }
  ```

---

## Project Structure and File Descriptions

The project is organized into several packages, each with a specific responsibility.

### `co.istad.mvm`

This is the root package for the project.

#### `MovieProgram.java`: The Application Controller

This is the main class that controls the application flow. It contains the `main` method, which is the entry point.

##### Class Structure
First, we define the fields for the services that the `MovieProgram` will use. By programming to interfaces (`MovieService`, `AuthService`), we make our program flexible.
```java
public class MovieProgram {
    private MovieService movieService;
    private AuthService authService;

    // ... constructor and methods
}
```

##### `main` Method
This is the static method that the Java Virtual Machine (JVM) calls to start the program.
- **How to write it:** Its only job is to create an instance of our `MovieProgram` class and call the `start()` method to begin the application.
```java
public static void main(String[] args) {
    MovieProgram program = new MovieProgram();
    program.start();
}
```

##### Constructor
The constructor initializes the service fields.
- **How to write it:** We instantiate the *implementations* of our services (`MovieServiceImpl`, `AuthServiceImpl`) and assign them to our interface fields.
```java
public MovieProgram() {
    this.movieService = new MovieServiceImpl();
    this.authService = new AuthServiceImpl();
}
```

##### `start()` Method
This method contains the main application loop.
- **How to write it:**
  1.  **Create an Infinite Loop:** Use `while (true)` to make the application run until the user explicitly exits.
  2.  **Show Menu:** Inside the loop, call a method to display the main menu options (`showMainMenu()`).
  3.  **Get Input:** Prompt the user for their choice using our `InputUtil.getInt()` helper.
  4.  **Handle Choice:** Use a `switch` statement to call the appropriate method based on the user's input (`adminLogin()`, `userLogin()`, etc.).
  5.  **Implement Exit:** `case 0` should print a goodbye message and use `return;` to exit the `start` method, thus ending the program.
```java
public void start() {
    ViewUtil.printHeader("🎬 WELCOME TO MOVIE MANAGEMENT SYSTEM 🎬");

    while (true) {
        showMainMenu();
        int choice = InputUtil.getInt("Choose an option: ");

        switch (choice) {
            case 1:
                adminLogin();
                break;
            case 2:
                userLogin();
                break;
            case 3:
                addNewUser();
                break;
            case 0:
                ViewUtil.printSuccessMessage("Thank you for using Movie Management System. Goodbye! 👋");
                return;
            default:
                ViewUtil.printErrorMessage("Invalid choice! Please try again.");
        }
    }
}
```

##### `login(String requiredRole)` Method
This method handles the logic for both admin and user login.
- **How to write it:**
  1.  **Allow Limited Attempts:** Use a `while` loop with a counter (`attempts < 3`) to limit login failures.
  2.  **Get Credentials:** Prompt for username and password.
  3.  **Authenticate:** Call `authService.login()`.
  4.  **Verify Role (If Login Succeeded):** If authentication is successful, get the user from the session. Check if the user's role matches the `requiredRole`. For admins, this means `!user.isAdmin()` would be false. If the role doesn't match, show an error, log them out, and continue the loop.
  5.  **Handle Failure:** If `authService.login()` returns false, increment the `attempts` counter and show an error.
  6.  **Handle Max Failures:** After the loop, if the user still hasn't logged in, show a "too many attempts" message.
```java
private boolean login(String requiredRole) {
    int attempts = 0;
    while (attempts < 3) {
        String username = InputUtil.getString("Username: ");
        String password = InputUtil.getString("Password: ");

        if (authService.login(username, password)) {
            User user = authService.getSession().getCurrentUser();

            if (requiredRole.equals("ADMIN") && !user.isAdmin()) {
                ViewUtil.printErrorMessage("Access denied! Admin privileges required.");
                authService.logout();
                attempts++;
                continue; // Ask for credentials again
            }

            ViewUtil.printSuccessMessage("Login successful! Welcome " + user.getUsername() + " (" + user.getRole() + ")");
            return true; // Login successful
        } else {
            attempts++;
            ViewUtil.printErrorMessage("Invalid credentials! Attempts left: " + (3 - attempts));
        }
    }
    ViewUtil.printErrorMessage("Too many failed attempts. Returning to main menu...");
    return false; // Login failed
}
```

##### `addNewUser()` Method
This method handles public user registration.
- **How to write it:**
  1.  **Get Username:** Prompt the user for a username.
  2.  **Check for Duplicates:** Before proceeding, call `authService.userExists()` to see if the username is already taken. If it is, print an error and `return` to stop the process.
  3.  **Get Password:** Prompt the user for a password.
  4.  **Register User:** Call `authService.register()` with the credentials. This method handles the actual user creation.
  5.  **Show Result:** Print the success or error message returned by the service.
```java
private void addNewUser() {
    ViewUtil.printHeader("➕ ADD NEW USER");
    String username = InputUtil.getString("Enter username: ");

    // Check if username already exists
    if (authService.userExists(username)) {
        ViewUtil.printErrorMessage("Username '" + username + "' already exists! Please choose a different username.");
        return;
    }

    String password = InputUtil.getString("Enter password: ");
    String result = authService.register(username, password);

    if (result.startsWith("SUCCESS")) {
        ViewUtil.printSuccessMessage(result);
    } else {
        ViewUtil.printErrorMessage(result);
    }
}
```

##### `viewAllMovies()` Method
This method displays the movie list with pagination.
- **How to write it:**
  1.  **Get All Movies:** Retrieve the full list of movies from `movieService.getAllMovies()`.
  2.  **Setup Pagination Variables:** Define `pageSize` (how many items per page), `totalPages`, and `currentPage`.
  3.  **Create Navigation Loop:** Use a `while(true)` loop to handle page navigation.
  4.  **Calculate Sublist:** On each iteration, calculate the `startIndex` and `endIndex` for the current page and use `movies.subList()` to get just the movies for that page.
  5.  **Display Content:** Print the sublist of movies and the current page info.
  6.  **Provide Navigation Options:** Prompt the user with options like `[N]ext`, `[P]revious`, `[G]oto`, and `[B]ack`.
  7.  **Handle Navigation Input:** Use a `switch` statement on the user's choice to change the `currentPage` number or to `return` from the method to go back.
```java
private void viewAllMovies() {
    ViewUtil.printHeader("🎬 ALL MOVIES");
    List<Movie> movies = movieService.getAllMovies();
    int pageSize = 5;
    int totalMovies = movies.size();
    int totalPages = (int) Math.ceil((double) totalMovies / pageSize);
    int currentPage = 1;

    // ... (check for empty list)

    while (true) {
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalMovies);
        List<Movie> subList = movies.subList(startIndex, endIndex);

        ViewUtil.printMovieList(subList);
        ViewUtil.printSuccessMessage("Page " + currentPage + " of " + totalPages + " | Total movies: " + totalMovies);

        // ... (display navigation options)

        String choice = InputUtil.getString("Choose an option: ").toLowerCase();

        switch (choice) {
            case "n": // Next page
                if (currentPage < totalPages) currentPage++;
                break;
            case "p": // Previous page
                if (currentPage > 1) currentPage--;
                break;
            case "g": // Goto page
                int page = InputUtil.getInt("Enter page number to go to: ");
                if (page >= 1 && page <= totalPages) currentPage = page;
                break;
            case "b": // Back
                return;
            default:
                ViewUtil.printErrorMessage("Invalid choice! Please try again.");
        }
    }
}
```

### `co.istad.mvm.database`

#### `MovieDatabase.java`: The In-Memory Movie Data Store

This class acts as our "database" for movies. It's an "in-memory" database, meaning it just holds the data in a list while the program is running. When the program stops, the data is gone.

##### Class Structure
The core of this class is a `List` of `Movie` objects.
- **How to write it:**
  1.  Declare a private list to hold the movies.
  2.  In the constructor, initialize this list as a new `ArrayList`.
  3.  Call a method like `initializeSampleData()` from the constructor to populate the database with some starting data.
```java
public class MovieDatabase {
    private List<Movie> movies;

    public MovieDatabase() {
        movies = new ArrayList<>();
        initializeSampleData(); // Populate with initial data
    }
    // ... other methods
}
```

##### `initializeSampleData()` Method
This method is used to pre-load the application with a set of movies.
- **How to write it:**
  1.  For each movie you want to add, create a new `Movie` object.
  2.  Use the setter methods (`.setTitle()`, `.setDirector()`, etc.) to set the details for that movie.
  3.  Crucially, assign a unique ID. We'll use a helper method `generateMovieId()` for this.
  4.  Add the fully-formed `Movie` object to the `movies` list.
```java
private void initializeSampleData() {
    // Example for one movie
    Movie movie1 = new Movie();
    movie1.setId(generateMovieId()); // Generate a unique ID
    movie1.setTitle("The Shawshank Redemption");
    movie1.setDirector("Frank Darabont");
    movie1.setReleaseYear(1994);
    movie1.setGenre("Drama");
    movie1.setRating(9.3);
    movie1.setDuration(142);
    movies.add(movie1);

    // ... repeat for other movies ...
}
```

##### `generateMovieId()` Method
This helper method creates a unique ID for each new movie.
- **How to write it:**
  1.  Use Java's built-in `UUID` class to generate a universally unique identifier.
  2.  `UUID.randomUUID().toString()` creates a long string like `123e4567-e89b-12d3-a456-426614174000`.
  3.  We can shorten this for readability. `.substring(0, 8)` takes just the first 8 characters.
  4.  `.toUpperCase()` makes it look consistent.
  5.  Prepend a prefix like `"MOV-"` to make it clear what kind of ID this is.
```java
private String generateMovieId() {
    return "MOV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
}
```

##### `getAllMovies()` Method
This method provides a safe way for other parts of the app to get the movie list.
- **How to write it:**
  -  Instead of returning the original `movies` list (which could be accidentally modified), we return a *new* `ArrayList` that contains all the elements of the original. This is a defensive copy that protects the integrity of our database.
```java
public List<Movie> getAllMovies() {
    return new ArrayList<>(movies); // Return a copy, not the original
}
```

##### `findMovieById(String id)` Method
This method searches for a single movie by its unique ID.
- **How to write it:**
  1.  Use an enhanced `for` loop to iterate through each `movie` in the `movies` list.
  2.  Inside the loop, check if the current `movie.getId()` is equal to the `id` being searched for. Use `.equals()` for string comparison.
  3.  If a match is found, immediately `return` that `movie` object.
  4.  If the loop finishes without finding a match, `return null` to indicate that no movie with that ID was found.
```java
public Movie findMovieById(String id) {
    for (Movie movie : movies) {
        if (movie.getId().equals(id)) {
            return movie;
        }
    }
    return null; // Not found
}
```

#### `UserDatabase.java`: The In-Memory User Data Store

This class is responsible for managing user data, including creating default users and authenticating them. Like `MovieDatabase`, it's an in-memory store.

##### Class Structure
The class holds a list of `User` objects and initializes them in the constructor.
- **How to write it:**
  1.  Declare a private `List<User> users`.
  2.  In the constructor, initialize the list and call a method to populate it with default users (`initializeDefaultUsers()`).
```java
public class UserDatabase {
    private List<User> users;

    public UserDatabase() {
        users = new ArrayList<>();
        initializeDefaultUsers();
    }
    // ... other methods
}
```

##### `initializeDefaultUsers()` Method
This method creates the essential user accounts needed for the application to function, such as the administrator.
- **How to write it:**
  1.  Create a new `User` object for the admin.
  2.  Set its properties: `Id`, `Username`, `Password`, and most importantly, `Role` to `"ADMIN"`.
  3.  Add the admin user to the `users` list.
  4.  Optionally, add some regular users for testing purposes, ensuring their `Role` is set to `"USER"`.
```java
private void initializeDefaultUsers() {
    // Admin account
    User admin = new User();
    admin.setId(generateUserId());
    admin.setUsername("admin");
    admin.setPassword("admin123");
    admin.setRole("ADMIN");
    users.add(admin);

    // Regular user 1
    User user1 = new User();
    user1.setId(generateUserId());
    user1.setUsername("user");
    user1.setPassword("user123");
    user1.setRole("USER");
    users.add(user1);
}
```

##### `authenticate(String username, String password)` Method
This is the core method for verifying a user's credentials.
- **How to write it:**
  1.  Iterate through the `users` list using an enhanced `for` loop.
  2.  Inside the loop, for each `user`, check two conditions:
      a. Does the username match? (`user.getUsername().equals(username)`)
      b. Does the password match? (`user.getPassword().equals(password)`)
  3.  If **both** conditions are true, you've found the correct user. `return` the `user` object immediately.
  4.  If the loop finishes without finding a match, `return null`.
```java
public User authenticate(String username, String password) {
    for (User user : users) {
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            return user;
        }
    }
    return null; // User not found or password incorrect
}
```

##### `userExists(String username)` Method
This method checks if a username is already taken.
- **How to write it:**
  1.  Iterate through the `users` list.
  2.  For each `user`, check if their username matches the one provided. It's good practice to use `.equalsIgnoreCase()` for this check so that "John" and "john" are considered the same username.
  3.  If a match is found, `return true`.
  4.  If the loop finishes, `return false`.
```java
public boolean userExists(String username) {
    for (User user : users) {
        if (user.getUsername().equalsIgnoreCase(username)) {
            return true;
        }
    }
    return false;
}
```

### `co.istad.mvm.model`

This package contains the "model" classes, also known as POJOs (Plain Old Java Objects) or data classes. Their primary purpose is to hold data in a structured way. They contain very little logic, consisting mainly of fields to store data and getter/setter methods to access that data.

#### `Movie.java`, `User.java`, `Session.java`: Data Blueprints

These classes act as blueprints for their respective objects.

##### How to write a Model Class (using `User.java` as an example)
1.  **Declare Private Fields:** Define the data points the class will hold as `private` fields. This encapsulates the data, meaning it can't be accessed directly from outside the class.
    ```java
    public class User {
        private String id;
        private String username;
        private String password;
        private String role;
        // ...
    }
    ```
2.  **Create a Constructor:** A default, no-argument constructor is good practice, especially for frameworks that might create objects automatically.
    ```java
    public User() {}
    ```
3.  **Generate Getters and Setters:** For each private field, create a public "getter" method to read its value and a public "setter" method to change its value. This provides controlled access to the data.
    - A **getter** for `username` would be `public String getUsername() { return this.username; }`.
    - A **setter** for `username` would be `public void setUsername(String username) { this.username = username; }`.
    ```java
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // ... getters and setters for all other fields ...
    ```
4.  **Add Helper Methods (Optional):** You can add simple methods for convenience. For example, `User.java` has an `isAdmin()` method that makes checking the user's role cleaner.
    ```java
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    ```
The same principles apply to `Movie.java` (with fields like `title`, `director`, etc.) and `Session.java` (with its `currentUser` field).

### `co.istad.mvm.service`

This package contains the business logic of the application, acting as a bridge between the user interface (`MovieProgram`) and the data layer (`database`). It uses an interface-based design for modularity.

#### `AuthService.java` (Interface)
*   **Description:** An interface that defines a contract for all authentication-related operations. It declares methods like `login()`, `logout()`, `register()`, and `userExists()`.

#### `AuthServiceImpl.java`: The Authentication Logic Handler

This class implements the `AuthService` interface and contains the core business logic for user authentication and registration. It acts as a middleman between the `MovieProgram` (the UI) and the `UserDatabase` (the data).

##### Class Structure
- **How to write it:**
  1.  Declare that the class `implements AuthService`.
  2.  Create private fields for its dependencies: `UserDatabase` and `Session`.
  3.  In the constructor, initialize these dependencies by creating new instances of them.
```java
public class AuthServiceImpl implements AuthService {
    private UserDatabase userDatabase;
    private Session session;

    public AuthServiceImpl() {
        this.userDatabase = new UserDatabase();
        this.session = new Session();
    }
    // ... other methods
}
```

##### `@Override login(String username, String password)` Method
This method handles the user login process.
- **How to write it:**
  1.  Add the `@Override` annotation to ensure it correctly implements the interface method.
  2.  Call the `userDatabase.authenticate()` method, passing along the `username` and `password`.
  3.  Check the result. If `authenticate()` returns a `User` object (i.e., not `null`), it means the credentials were correct.
  4.  If the user is not `null`, call `session.login(user)` to store the logged-in user in the session.
  5.  Return `true` for a successful login, and `false` otherwise.
```java
@Override
public boolean login(String username, String password) {
    User user = userDatabase.authenticate(username, password);
    if (user != null) {
        session.login(user);
        return true;
    }
    return false;
}
```

##### `@Override register(String username, String password)` Method
This method handles the creation of new user accounts.
- **How to write it:**
  1.  Add the `@Override` annotation.
  2.  **Perform validation:** Add guard clauses at the beginning to check for invalid input, such as an empty username or password. Return an error message if validation fails.
  3.  **Check for duplicates:** Call `this.userExists(username)` to ensure the username isn't already taken.
  4.  If all checks pass, create a new `User` object.
  5.  Set its properties: a generated ID, username, password, and hardcode the `role` to `"USER"`. This ensures public registration can't create admins.
  6.  Call `userDatabase.registerUser(newUser)` to save the new user.
  7.  Return a success or error message based on the result of the save operation.
```java
@Override
public String register(String username, String password) {
    if (username == null || username.trim().isEmpty()) {
        return "ERROR: Username cannot be empty!";
    }
    if (password == null || password.trim().isEmpty()) {
        return "ERROR: Password cannot be empty!";
    }
    if (this.userExists(username)) {
        return "ERROR: Username '" + username + "' already exists!";
    }

    User newUser = new User();
    newUser.setId(generateUserId()); // private helper method
    newUser.setUsername(username);
    newUser.setPassword(password);
    newUser.setRole("USER");

    if (userDatabase.registerUser(newUser)) {
        return "SUCCESS: User '" + username + "' registered successfully!";
    } else {
        return "ERROR: Failed to register user!";
    }
}
```

#### `MovieService.java` (Interface)
*   **Description:** An interface that defines a contract for all movie-related operations. It declares methods for CRUD (Create, Read, Update, Delete) and search operations.

#### `MovieServiceImpl.java`: The Movie Logic Handler

This class implements the `MovieService` interface. Its job is to connect the application's UI (`MovieProgram`) to the movie data layer (`MovieDatabase`). It contains the business logic for managing movies.

##### Class Structure
- **How to write it:**
  1.  Declare that the class `implements MovieService`.
  2.  Create a private field for its dependency, the `MovieDatabase`.
  3.  In the constructor, initialize the `database` field by creating a new `MovieDatabase` instance.
```java
public class MovieServiceImpl implements MovieService {
    private MovieDatabase database;

    public MovieServiceImpl() {
        this.database = new MovieDatabase();
    }
    // ... other methods
}
```

##### `@Override addMovie(Movie movie)` Method
This method handles the logic for adding a new movie.
- **How to write it:**
  1.  Add the `@Override` annotation.
  2.  The `Movie` object passed to this method from the UI won't have an ID yet. We need to generate one. We can create a private helper method or put the logic directly here to generate a unique ID (e.g., using `UUID`).
  3.  Set the generated ID on the `movie` object using `movie.setId()`.
  4.  Pass the completed `movie` object to the `database.addMovie()` method to be saved.
```java
@Override
public void addMovie(Movie movie) {
    // Generate a unique ID for the new movie.
    String uuid = "MOV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    movie.setId(uuid);
    database.addMovie(movie);
}
```

##### `@Override` Methods for Read, Update, Delete
Most other methods in this service are simple "pass-through" methods. They delegate the call directly to the corresponding method in the `MovieDatabase`.
- **How to write it:**
  1.  Add the `@Override` annotation for each method from the interface.
  2.  In the method body, simply call the equivalent method on the `database` object.
  3.  Return the result from the database method directly.
```java
@Override
public List<Movie> getAllMovies() {
    return database.getAllMovies();
}

@Override
public boolean updateMovie(Movie movie) {
    return database.updateMovie(movie);
}

@Override
public boolean deleteMovie(String id) {
    return database.deleteMovie(id);
}

@Override
public Movie findMovieById(String id) {
    return database.findMovieById(id);
}

// ... and so on for other find methods ...
```

### `co.istad.mvm.util`

This package contains utility classes. These classes are helpers that perform common, reusable tasks (like getting user input or displaying tables) so we don't have to repeat the same code in different parts of our application.

#### `InputUtil.java`: Handling User Input

This class is a toolkit for safely getting different types of input from the user via the console.

##### Class Structure
The class has a single `static Scanner` object. Using `static` means this one scanner is shared by all the methods in the class, so we don't need to create a new one every time we ask for input.
```java
public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);
    // ... methods
}
```

##### `getInt(String prompt)` Method
This method safely gets an integer from the user.
- **How to write it:**
  1.  **Create an Infinite Loop:** Use `while (true)` to keep asking the user for input until they provide a valid integer.
  2.  **Use a `try-catch` block:** This is crucial for handling errors. The `try` block attempts the "risky" operation of converting the user's text input into an integer.
  3.  **Prompt and Read:** Inside `try`, print the `prompt` and read the user's input using `scanner.nextLine()`.
  4.  **Parse the Integer:** Use `Integer.parseInt()` to convert the string to an `int`. If it succeeds, `return` the value. This will also break the loop.
  5.  **Catch the Error:** The `catch (NumberFormatException e)` block will execute if `parseInt()` fails (e.g., the user typed "abc"). Inside `catch`, print a helpful error message. The loop will then repeat, asking for input again.
```java
public static int getInt(String prompt) {
    while (true) {
        try {
            System.out.print(prompt);
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }
}
```
The same principle applies to `getDouble()`, but using `Double.parseDouble()`. The `getIntInRange()` method builds on this by adding another check to ensure the number is within a specific `min` and `max`.

#### `ViewUtil.java`: Displaying Formatted Output

This class handles all the presentation logic. It uses an external library, `text-table-formatter`, to display data in nicely formatted tables.

##### `printMovieList(List<Movie> movieList)` Method
This method displays a list of movies in a table.
- **How to write it:**
  1.  **Create a Table:** Instantiate a `Table` object from the library, defining the number of columns and the border style.
  2.  **Set Column Widths:** Use `table.setColumnWidth()` to control the layout and prevent columns from being too wide or too narrow.
  3.  **Add Headers:** Add the header titles for each column using `table.addCell()`.
  4.  **Loop Through Data:** Iterate over the `movieList` with an enhanced `for` loop.
  5.  **Populate Rows:** Inside the loop, for each `movie`, call `table.addCell()` for each piece of data (`movie.getId()`, `movie.getTitle()`, etc.), making sure to add them in the correct column order.
  6.  **Render the Table:** Finally, call `table.render()` to generate the formatted string and print it to the console.
```java
public static void printMovieList(List<Movie> movieList) {
    Table table = new Table(7, BorderStyle.UNICODE_ROUND_BOX_WIDE);

    // ... set column widths ...

    table.addCell("ID");
    table.addCell("TITLE");
    // ... add other headers ...

    for (Movie movie : movieList) {
        table.addCell(movie.getId());
        table.addCell(movie.getTitle());
        // ... add other movie data cells ...
    }

    print(table.render(), true); // A helper to print to console
}
```
The other methods like `printMovieDetail`, `printUserList`, and the message printers (`printSuccessMessage`, etc.) all follow this same fundamental pattern of creating a `Table`, configuring it, populating it with data, and rendering it.