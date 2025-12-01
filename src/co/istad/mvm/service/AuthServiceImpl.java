package co.istad.mvm.service;

import co.istad.mvm.database.UserDatabase;
import co.istad.mvm.model.Session;
import co.istad.mvm.model.User;

import java.util.UUID;

public class AuthServiceImpl implements AuthService { // Changed class declaration
    private UserDatabase userDatabase;
    private Session session;

    public AuthServiceImpl() {
        this.userDatabase = new UserDatabase();
        this.session = new Session();
    }

    @Override // Added @Override
    public boolean login(String username, String password) {
        User user = userDatabase.authenticate(username, password);
        if (user != null) {
            session.login(user);
            return true;
        }
        return false;
    }

    @Override // Added @Override
    public void logout() {
        session.logout();
    }

    @Override // Added @Override
    public Session getSession() {
        return session;
    }

    @Override // Added @Override
    public UserDatabase getUserDatabase() {
        return userDatabase;
    }

    @Override // Added @Override
    public String register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return "ERROR: Username cannot be empty!";
        }

        if (password == null || password.trim().isEmpty()) {
            return "ERROR: Password cannot be empty!";
        }

        // Changed to use this.userExists as per the interface
        if (this.userExists(username)) {
            return "ERROR: Username '" + username + "' already exists!";
        }

        // Create new user using setters (no parameterized constructor)
        User newUser = new User();
        newUser.setId(generateUserId());
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole("USER");

        // Add to database
        if (userDatabase.registerUser(newUser)) {
            return "SUCCESS: User '" + username + "' registered successfully!";
        } else {
            return "ERROR: Failed to register user!";
        }
    }

    @Override // Added @Override
    public boolean userExists(String username) {
        return userDatabase.userExists(username);
    }

    private String generateUserId() {
        return "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}