package co.istad.mvm.util;

import co.istad.mvm.model.Movie;
import co.istad.mvm.model.User;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

public class ViewUtil {

    public static void print(String text, boolean isNewLine) {
        if (isNewLine)
            System.out.println(text);
        else
            System.out.print(text);
    }

    public static void printHeader(String text) {
        Table table = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        table.addCell(text);
        print(table.render(), true);
    }

    public static void printMovieList(List<Movie> movieList) {
        Table table = new Table(7, BorderStyle.UNICODE_ROUND_BOX_WIDE);

        table.setColumnWidth(0, 12, 12);
        table.setColumnWidth(1, 20, 25);
        table.setColumnWidth(2, 15, 20);
        table.setColumnWidth(3, 8, 8);
        table.setColumnWidth(4, 12, 15);
        table.setColumnWidth(5, 8, 8);
        table.setColumnWidth(6, 10, 10);

        table.addCell("ID");
        table.addCell("TITLE");
        table.addCell("DIRECTOR");
        table.addCell("YEAR");
        table.addCell("GENRE");
        table.addCell("RATING");
        table.addCell("DURATION");

        for (Movie movie : movieList) {
            table.addCell(movie.getId());
            table.addCell(movie.getTitle());
            table.addCell(movie.getDirector());
            table.addCell(String.valueOf(movie.getReleaseYear()));
            table.addCell(movie.getGenre());
            table.addCell(String.format("%.1f", movie.getRating()));
            table.addCell(movie.getDuration() + " min");
        }

        print(table.render(), true);
    }

    public static void printMovieDetail(Movie movie) {
        if (movie == null) {
            printHeader("MOVIE NOT FOUND");
            return;
        }

        Table table = new Table(2, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        table.setColumnWidth(0, 15, 15);
        table.setColumnWidth(1, 35, 45);

        table.addCell("MOVIE ID");
        table.addCell(movie.getId());
        table.addCell("TITLE");
        table.addCell(movie.getTitle());
        table.addCell("DIRECTOR");
        table.addCell(movie.getDirector());
        table.addCell("RELEASE YEAR");
        table.addCell(String.valueOf(movie.getReleaseYear()));
        table.addCell("GENRE");
        table.addCell(movie.getGenre());
        table.addCell("RATING");
        table.addCell(String.format("%.1f/10", movie.getRating()));
        table.addCell("DURATION");
        table.addCell(movie.getDuration() + " minutes");

        print(table.render(), true);
    }

    public static void printUserList(List<User> userList, boolean showPasswords) {
        if (userList.isEmpty()) {
            printHeader("NO USERS FOUND");
            return;
        }

        Table table = new Table(4, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        table.setColumnWidth(0, 12, 15); // User ID
        table.setColumnWidth(1, 15, 20); // Username
        table.setColumnWidth(2, 15, 20); // Password
        table.setColumnWidth(3, 10, 15); // Role

        table.addCell("USER ID");
        table.addCell("USERNAME");
        table.addCell("PASSWORD");
        table.addCell("ROLE");

        for (User user : userList) {
            table.addCell(user.getId());
            table.addCell(user.getUsername());

            // Show actual password if showPasswords is true, otherwise show ******
            if (showPasswords) {
                table.addCell(user.getPassword());
            } else {
                table.addCell("********");
            }

            if (user.isAdmin()) {
                table.addCell("👑 " + user.getRole());
            } else {
                table.addCell("👤 " + user.getRole());
            }
        }

        print(table.render(), true);
    }

    public static void printSuccessMessage(String message) {
        Table table = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        CellStyle centerStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        table.addCell("SUCCESS: " + message, centerStyle);
        print(table.render(), true);
    }

    public static void printWarningMessage(String message) {
        Table table = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        CellStyle centerStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        table.addCell("WARNING: " + message, centerStyle);
        print(table.render(), true);
    }

    public static void printErrorMessage(String message) {
        Table table = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        CellStyle centerStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        table.addCell("ERROR: " + message, centerStyle);
        print(table.render(), true);
    }
}