package co.istad.mvm;

import co.istad.mvm.model.Movie;
import co.istad.mvm.model.User;
import co.istad.mvm.service.AuthService;
import co.istad.mvm.service.AuthServiceImpl;
import co.istad.mvm.service.MovieService;
import co.istad.mvm.service.MovieServiceImpl;
import co.istad.mvm.util.InputUtil;
import co.istad.mvm.util.ViewUtil;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

public class MovieProgram {
    private MovieService movieService;
    private AuthService authService;

    public MovieProgram() {
        this.movieService = new MovieServiceImpl();
        this.authService = new AuthServiceImpl();
    }

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
                case 0:
                    ViewUtil.printSuccessMessage("Thank you for using Movie Management System. Goodbye! 👋");
                    return;
                default:
                    ViewUtil.printErrorMessage("Invalid choice! Please try again.");
            }
        }
    }

    private void showMainMenu() {
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        table.setColumnWidth(0, 50, 100);

        table.addCell("🎬 MOVIE MANAGEMENT SYSTEM 🎬", cellStyle);
        table.addCell("1) 👑 Admin Login (Full Access)", cellStyle);
        table.addCell("2) 👤 User Login (Browse Only)", cellStyle);
        table.addCell("0) ❌ Exit", cellStyle);

        ViewUtil.print(table.render(), true);
    }

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
                attempts++;
                ViewUtil.printErrorMessage("Invalid credentials! Attempts left: " + (3 - attempts));
            }
        }

        ViewUtil.printErrorMessage("Too many failed attempts. Returning to main menu...");
        return false;
    }

    private void addNewUser() {
        ViewUtil.printHeader("➕ ADD NEW USER");

        String username = InputUtil.getString("Enter username: ");

        // Check if username already exists
        if (authService.userExists(username)) {
            ViewUtil.printErrorMessage("Username '" + username + "' already exists! Please choose a different username.");
            return;
        }

        String password = InputUtil.getString("Enter password: ");

        // Register the user (automatically as USER role)
        String result = authService.register(username, password);
        if (result.startsWith("SUCCESS")) {
            ViewUtil.printSuccessMessage(result);
        } else {
            ViewUtil.printErrorMessage(result);
        }
    }

    private void adminMode() {
        User currentUser = authService.getSession().getCurrentUser();
        ViewUtil.printSuccessMessage("🔧 ADMIN MODE - Welcome, " + currentUser.getUsername() + "!");

        while (true) {
            showAdminMenu();
            int choice = InputUtil.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    manageMovies();
                    break;
                case 2:
                    manageUsers();
                    break;
                case 3:
                    viewProfile();
                    break;
                case 0:
                    logout();
                    return;
                default:
                    ViewUtil.printErrorMessage("Invalid choice! Please try again.");
            }
        }
    }

    private void userMode() {
        User currentUser = authService.getSession().getCurrentUser();
        ViewUtil.printSuccessMessage("👤 USER MODE - Welcome, " + currentUser.getUsername() + "!");

        while (true) {
            showUserMenu();
            int choice = InputUtil.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    viewAllMovies();
                    break;
                case 2:
                    searchMovieById();
                    break;
                case 3:
                    searchMoviesByTitle();
                    break;
                case 4:
                    searchMoviesByGenre();
                    break;
                case 5:
                    searchMoviesByDirector();
                    break;
                case 6:
                    viewProfile();
                    break;
                case 0:
                    logout();
                    return;
                default:
                    ViewUtil.printErrorMessage("Invalid choice! Please try again.");
            }
        }
    }

    private void logout() {
        String username = authService.getSession().getCurrentUser().getUsername();
        authService.logout();
        ViewUtil.printSuccessMessage("User '" + username + "' logged out successfully.");
    }

    private void showAdminMenu() {
        User currentUser = authService.getSession().getCurrentUser();

        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        table.setColumnWidth(0, 50, 100);

        table.addCell("🔧 ADMIN PANEL - " + currentUser.getUsername(), cellStyle);
        table.addCell("1) 🎬 Manage Movies", cellStyle);
        table.addCell("2) 👥 Manage Users", cellStyle);
        table.addCell("3) 👤 View Profile", cellStyle);
        table.addCell("0) ↩️ Logout", cellStyle);

        ViewUtil.print(table.render(), true);
    }

    private void showUserMenu() {
        User currentUser = authService.getSession().getCurrentUser();

        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        table.setColumnWidth(0, 50, 100);

        table.addCell("👤 USER PANEL - " + currentUser.getUsername(), cellStyle);
        table.addCell("1) 📋 View All Movies", cellStyle);
        table.addCell("2) 🔍 Search by ID", cellStyle);
        table.addCell("3) 🔍 Search by Title", cellStyle);
        table.addCell("4) 🔍 Search by Genre", cellStyle);
        table.addCell("5) 🔍 Search by Director", cellStyle);
        table.addCell("6) 👤 View Profile", cellStyle);
        table.addCell("0) ↩️ Logout", cellStyle);

        ViewUtil.print(table.render(), true);
    }

    private void manageProfile() {
        while (true) {
            User currentUser = authService.getSession().getCurrentUser();
            ViewUtil.printHeader("👤 USER PROFILE");

            Table table = new Table(2, BorderStyle.UNICODE_ROUND_BOX_WIDE);
            table.setColumnWidth(0, 15, 20);
            table.setColumnWidth(1, 25, 30);

            table.addCell("Username");
            table.addCell(currentUser.getUsername());
            table.addCell("Role");
            table.addCell(currentUser.getRole());
            table.addCell("Permissions");
            table.addCell(currentUser.isAdmin() ? "Full Access" : "Read Only");

            ViewUtil.print(table.render(), true);

            Table optionsTable = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
            optionsTable.setColumnWidth(0, 40, 50);
            optionsTable.addCell("PROFILE OPTIONS", new CellStyle(CellStyle.HorizontalAlign.center));
            optionsTable.addCell("1) ✏️ Edit Profile", new CellStyle(CellStyle.HorizontalAlign.center));
            optionsTable.addCell("0) ↩️ Back", new CellStyle(CellStyle.HorizontalAlign.center));
            ViewUtil.print(optionsTable.render(), true);

            int choice = InputUtil.getInt("Choose option: ");
            switch (choice) {
                case 1:
                    editUserProfile();
                    break;
                case 0:
                    return;
                default:
                    ViewUtil.printErrorMessage("Invalid choice! Please try again.");
            }
        }
    }

    private void viewProfile() {
        manageProfile();
    }

    private void manageUsers() {
        ViewUtil.printHeader("👥 USER MANAGEMENT");
        List<User> users = authService.getUserDatabase().getAllUsers();

        // Use the updated ViewUtil method with showPasswords = true
        ViewUtil.printUserList(users, true);

        // Show admin count
        int adminCount = 0;
        for (User user : users) {
            if (user.isAdmin()) {
                adminCount++;
            }
        }

        ViewUtil.printSuccessMessage("Total users: " + users.size() + " (👑 Admins: " + adminCount + " | 👤 Users: " + (users.size() - adminCount) + ")");

        while (true) {
            Table optionsTable = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
            optionsTable.setColumnWidth(0, 40, 50);
            optionsTable.addCell("USER MANAGEMENT OPTIONS", new CellStyle(CellStyle.HorizontalAlign.center));
            optionsTable.addCell("1) ➕ Add New User", new CellStyle(CellStyle.HorizontalAlign.center));
            optionsTable.addCell("2) ❌ Delete User", new CellStyle(CellStyle.HorizontalAlign.center));
            optionsTable.addCell("3) 🔄 Refresh List", new CellStyle(CellStyle.HorizontalAlign.center));
            optionsTable.addCell("0) ↩️ Back to Admin Menu", new CellStyle(CellStyle.HorizontalAlign.center));
            ViewUtil.print(optionsTable.render(), true);

            int choice = InputUtil.getInt("Choose option: ");

            switch (choice) {
                case 1:
                    addNewUser();
                    break;
                case 2:
                    deleteUserAdmin(); // New method for admin to delete user
                    break;
                case 3:
                    // Refresh the user list
                    users = authService.getUserDatabase().getAllUsers();
                    ViewUtil.printHeader("👥 USER MANAGEMENT - REFRESHED");
                    ViewUtil.printUserList(users, true);
                    ViewUtil.printSuccessMessage("User list refreshed!");
                    break;
                case 0:
                    return;
                default:
                    ViewUtil.printErrorMessage("Invalid choice! Please try again.");
            }
        }
    }

    private void editUserProfile() {
        ViewUtil.printHeader("✏️ EDIT PROFILE");
        User currentUser = authService.getSession().getCurrentUser();

        ViewUtil.printSuccessMessage("Enter new details (press Enter to keep current value):");

        String newUsername = InputUtil.getString("Enter new username [" + currentUser.getUsername() + "]: ");
        String newPassword = InputUtil.getString("Enter new password (leave empty to keep current) : ");

        User updatedUser = new User();
        updatedUser.setId(currentUser.getId());
        updatedUser.setRole(currentUser.getRole()); // Role cannot be changed by user

        if (!newUsername.isEmpty()) {
            // Check if new username already exists and is not the current user's username
            if (!newUsername.equalsIgnoreCase(currentUser.getUsername()) && authService.userExists(newUsername)) {
                ViewUtil.printErrorMessage("Username '" + newUsername + "' already exists! Please choose a different username.");
                return;
            }
            updatedUser.setUsername(newUsername);
        } else {
            updatedUser.setUsername(currentUser.getUsername());
        }

        if (!newPassword.isEmpty()) {
            updatedUser.setPassword(newPassword);
        } else {
            updatedUser.setPassword(currentUser.getPassword());
        }

        if (authService.updateUser(updatedUser)) {
            // Update the session's current user to reflect changes
            authService.getSession().login(updatedUser);
            ViewUtil.printSuccessMessage("Profile updated successfully! ✅");
        } else {
            ViewUtil.printErrorMessage("Failed to update profile! ❌");
        }
    }

    private void deleteUserAdmin() {
        ViewUtil.printHeader("❌ DELETE USER");
        String userIdToDelete = InputUtil.getString("Enter User ID to delete: ");

        User currentUser = authService.getSession().getCurrentUser();
        if (currentUser != null && currentUser.getId().equals(userIdToDelete)) {
            ViewUtil.printErrorMessage("You cannot delete your own account !");
            return;
        }

        User userToDelete = authService.findUserById(userIdToDelete);

        if (userToDelete == null) {
            ViewUtil.printErrorMessage("User with ID '" + userIdToDelete + "' not found!");
            return;
        }

        ViewUtil.printHeader("👤 USER TO DELETE");
        Table userDetailTable = new Table(2, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        userDetailTable.setColumnWidth(0, 15, 20);
        userDetailTable.setColumnWidth(1, 25, 30);
        userDetailTable.addCell("ID");
        userDetailTable.addCell(userToDelete.getId());
        userDetailTable.addCell("Username");
        userDetailTable.addCell(userToDelete.getUsername());
        userDetailTable.addCell("Role");
        userDetailTable.addCell(userToDelete.getRole());
        ViewUtil.print(userDetailTable.render(), true);

        String confirm = InputUtil.getString("Are you sure you want to delete this user? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            ViewUtil.printSuccessMessage("User '" + userToDelete.getUsername() + "' deleted successfully! ✅");
        } else {
            ViewUtil.printSuccessMessage("Deletion cancelled. ↩️");
        }
    }

    private void manageMovies() {
        while (true) {
            Table table = getTable();
            ViewUtil.print(table.render(), true);

            int choice = InputUtil.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    viewAllMovies();
                    break;
                case 2:
                    addNewMovie();
                    break;
                case 3:
                    updateMovie();
                    break;
                case 4:
                    deleteMovie();
                    break;
                case 5:
                    searchMovieById();
                    break;
                case 6:
                    searchMoviesByTitle();
                    break;
                case 7:
                    searchMoviesByGenre();
                    break;
                case 8:
                    searchMoviesByDirector();
                    break;
                case 0:
                    return;
                default:
                    ViewUtil.printErrorMessage("Invalid choice! Please try again.");
            }
        }
    }

    private static Table getTable() {
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        table.setColumnWidth(0, 50, 100);
        table.addCell("🎬 MOVIE MANAGEMENT", cellStyle);
        table.addCell("1) 📋 List All  2) ➕ Add New  3) ✏️ Update  4) ❌ Delete", cellStyle);
        table.addCell("5) 🔍 Search by ID  6) 🔍 Search by Title  7) 🔍 Search by Genre  8) 🔍 Search by Director", cellStyle);
        table.addCell("0) ↩️ Back to Admin Menu", cellStyle);
        return table;
    }

    private void viewAllMovies() {
        ViewUtil.printHeader("🎬 ALL MOVIES");
        List<Movie> movies = movieService.getAllMovies().reversed();
        int pageSize = 5;
        int totalMovies = movies.size();
        int totalPages = (int) Math.ceil((double) totalMovies / pageSize);
        int currentPage = 1;

        if(totalMovies == 0) {
            ViewUtil.printWarningMessage("There are no movies to show");
            return;
        }

        while (true) {
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalMovies);
            List<Movie> subList = movies.subList(startIndex, endIndex);

            ViewUtil.printMovieList(subList);
            ViewUtil.printSuccessMessage("Page " + currentPage + " of " + totalPages + " | Total movies: " + totalMovies);

            Table options = new Table(1, BorderStyle.UNICODE_ROUND_BOX);
            options.setColumnWidth(0, 50, 100);
            options.addCell("[N] Next | [P] Previous | [G] Goto | [B] Back", new CellStyle(CellStyle.HorizontalAlign.center));
            ViewUtil.print(options.render(), true);

            String choice = InputUtil.getString("Choose an option: ").toLowerCase();

            switch (choice) {
                case "n":
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        ViewUtil.printWarningMessage("You are on the last page.");
                    }
                    break;
                case "p":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        ViewUtil.printWarningMessage("You are on the first page.");
                    }
                    break;
                case "g":
                    int page = InputUtil.getInt("Enter page number to go to: ");
                    if(page >=1 && page <= totalPages){
                        currentPage = page;
                    }else {
                        ViewUtil.printWarningMessage("There are no such page");
                    }
                    break;
                case "b":
                    return;
                default:
                    ViewUtil.printErrorMessage("Invalid choice! Please try again.");
            }
        }
    }

    private void addNewMovie() {
        ViewUtil.printHeader("➕ ADD NEW MOVIE");
        String title = InputUtil.getString("Enter Title: ");
        String director = InputUtil.getString("Enter Director: ");
        int releaseYear = InputUtil.getIntInRange("Enter Release Year: ", 1888, 2030);
        String genre = InputUtil.getString("Enter Genre: ");
        double rating = InputUtil.getDoubleInRange("Enter Rating (0-10): ", 0, 10);
        int duration = InputUtil.getInt("Enter Duration (minutes): ");

        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setReleaseYear(releaseYear);
        movie.setGenre(genre);
        movie.setRating(rating);
        movie.setDuration(duration);

        movieService.addMovie(movie);
        ViewUtil.printSuccessMessage("Movie added successfully! 🎉");
        ViewUtil.printMovieDetail(movie);
    }

    private void updateMovie() {
        ViewUtil.printHeader("✏️ UPDATE MOVIE");
        String id = InputUtil.getString("Enter Movie ID to update: ");

        Movie existingMovie = movieService.findMovieById(id);
        if (existingMovie == null) {
            ViewUtil.printErrorMessage("Movie not found! ❌");
            return;
        }

        ViewUtil.printHeader("📋 CURRENT MOVIE DETAILS");
        ViewUtil.printMovieDetail(existingMovie);

        ViewUtil.printSuccessMessage("Enter new details (press Enter to keep current value):");

        String title = InputUtil.getString("Enter Title [" + existingMovie.getTitle() + "]: ");
        String director = InputUtil.getString("Enter Director [" + existingMovie.getDirector() + "]: ");
        String releaseYearStr = InputUtil.getString("Enter Release Year [" + existingMovie.getReleaseYear() + "]: ");
        String genre = InputUtil.getString("Enter Genre [" + existingMovie.getGenre() + "]: ");
        String ratingStr = InputUtil.getString("Enter Rating [" + existingMovie.getRating() + "]: ");
        String durationStr = InputUtil.getString("Enter Duration [" + existingMovie.getDuration() + "]: ");

        if (!title.isEmpty()) existingMovie.setTitle(title);
        if (!director.isEmpty()) existingMovie.setDirector(director);
        if (!releaseYearStr.isEmpty()) existingMovie.setReleaseYear(Integer.parseInt(releaseYearStr));
        if (!genre.isEmpty()) existingMovie.setGenre(genre);
        if (!ratingStr.isEmpty()) existingMovie.setRating(Double.parseDouble(ratingStr));
        if (!durationStr.isEmpty()) existingMovie.setDuration(Integer.parseInt(durationStr));

        if (movieService.updateMovie(existingMovie)) {
            ViewUtil.printSuccessMessage("Movie updated successfully! ✅");
            ViewUtil.printMovieDetail(existingMovie);
        } else {
            ViewUtil.printErrorMessage("Failed to update movie! ❌");
        }
    }

    private void deleteMovie() {
        ViewUtil.printHeader("❌ DELETE MOVIE");
        String id = InputUtil.getString("Enter Movie ID to delete: ");

        Movie movie = movieService.findMovieById(id);
        if (movie == null) {
            ViewUtil.printErrorMessage("Movie not found! ❌");
            return;
        }

        ViewUtil.printHeader("🎬 MOVIE TO DELETE");
        ViewUtil.printMovieDetail(movie);

        String confirm = InputUtil.getString("Are you sure you want to delete this movie? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            if (movieService.deleteMovie(id)) {
                ViewUtil.printSuccessMessage("Movie deleted successfully! ✅");
            } else {
                ViewUtil.printErrorMessage("Failed to delete movie! ❌");
            }
        } else {
            ViewUtil.printSuccessMessage("Deletion cancelled. ↩️");
        }
    }

    private void searchMovieById() {
        ViewUtil.printHeader("🔍 SEARCH MOVIE BY ID");
        String id = InputUtil.getString("Enter Movie ID: ");
        Movie movie = movieService.findMovieById(id);
        ViewUtil.printMovieDetail(movie);
    }

    private void searchMoviesByTitle() {
        ViewUtil.printHeader("🔍 SEARCH MOVIES BY TITLE");
        String title = InputUtil.getString("Enter title keyword: ");
        List<Movie> movies = movieService.findMoviesByTitle(title);
        ViewUtil.printMovieList(movies);
        ViewUtil.printSuccessMessage("Found " + movies.size() + " movies");
    }

    private void searchMoviesByGenre() {
        ViewUtil.printHeader("🔍 SEARCH MOVIES BY GENRE");
        String genre = InputUtil.getString("Enter genre: ");
        List<Movie> movies = movieService.findMoviesByGenre(genre);
        ViewUtil.printMovieList(movies);
        ViewUtil.printSuccessMessage("Found " + movies.size() + " movies in genre: " + genre);
    }

    private void searchMoviesByDirector() {
        ViewUtil.printHeader("🔍 SEARCH MOVIES BY DIRECTOR");
        String director = InputUtil.getString("Enter director name: ");
        List<Movie> movies = movieService.findMoviesByDirector(director);
        ViewUtil.printMovieList(movies);
        ViewUtil.printSuccessMessage("Found " + movies.size() + " movies by director: " + director);
    }

    public static void main(String[] args) {
        MovieProgram program = new MovieProgram();
        program.start();
    }
}