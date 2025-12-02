package co.istad.mvm.database;

import co.istad.mvm.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDatabase {
    private List<User> users;

    public UserDatabase() {
        users = new ArrayList<>();
        initializeDefaultUsers();
    }

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

        // Regular user 2
        User user2 = new User();
        user2.setId(generateUserId());
        user2.setUsername("john");
        user2.setPassword("john123");
        user2.setRole("USER");
        users.add(user2);
    }

    // Add this method to generate user IDs
    private String generateUserId() {
        return "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public boolean userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    // Update this method to accept User object instead of individual parameters
    public boolean registerUser(User newUser) {
        if (userExists(newUser.getUsername())) {
            return false; // Username already exists
        }

        if (newUser.getUsername().trim().isEmpty() || newUser.getPassword().trim().isEmpty()) {
            return false; // Empty username or password
        }

        newUser.setId(generateUserId()); // Assign ID here
        users.add(newUser);
        return true;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // NEW: Check if admin exists
    public boolean adminExists() {
        for (User user : users) {
            if (user.isAdmin()) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String userId) {
        return users.removeIf(user -> user.getId().equals(userId));
    }

    public boolean updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                return true;
            }
        }
        return false;
    }
}