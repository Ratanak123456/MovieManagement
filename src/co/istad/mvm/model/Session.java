package co.istad.mvm.model;

public class Session {
    private User currentUser;
    private boolean isLoggedIn;

    public Session() {
        this.isLoggedIn = false;
    }

    public void login(User user) {
        this.currentUser = user;
        this.isLoggedIn = true;
    }

    public void logout() {
        this.currentUser = null;
        this.isLoggedIn = false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

}