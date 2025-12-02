package co.istad.mvm.service;

import co.istad.mvm.database.UserDatabase;
import co.istad.mvm.model.Session;
import co.istad.mvm.model.User;

public interface AuthService {
    boolean login(String username, String password);
    void logout();
    Session getSession();
    UserDatabase getUserDatabase();
    String register(String username, String password);
    boolean userExists(String username);
    boolean updateUser(User updatedUser);
}
