/*
 * Copyright © 3.2020. Ryan Godfrey, John Breton.
 * All rights reserved.
 */

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.stream.Stream;

/**
 * The type Upload book data.
 */
@SuppressWarnings("JavaDoc")
public class uploadBookData {

    private static Statement statement = null;
    private static final String DATABASE = "LookInnaBook";
    private static final String USER = "ryan";
    static String titleObject;
    static BigInteger isbnObject;
    static JsonArray authorsObject;
    static int yearObject;
    static int pgCntObject;
    static JsonArray genresObject;
    static JsonArray publishersObject;


    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE, USER, "");
            statement = connection.createStatement();

            // emptyDB
            StringBuilder resetDB = new StringBuilder();
            Stream<String> stream = Files.lines(Paths.get("documentation/DDL/COMP 3005 - project - DDL file.txt"), StandardCharsets.UTF_8);
            stream.forEach(s -> resetDB.append(s).append("\n"));
            statement.executeUpdate(resetDB.toString());

            // add admin
            statement.executeUpdate("Insert into project.user (user_name, password) values ('" + USER + "', '" + USER + "');" +
                    "insert into project.librarian values ('" + USER + "', 300.00);");

        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        new uploadBookData();
        System.out.println("Database has been updated");
    }

    public uploadBookData(){
        try{
            FileReader file = new FileReader("documentation/bookdata.json");
            JsonArray arr = JsonParser.parseReader(file).getAsJsonArray();

            arr.forEach(book -> {
                parseBookObject((JsonObject) book);

                if(titleObject != null && isbnObject != null && pgCntObject != -1 && yearObject != -1 && authorsObject != null &&
                    publishersObject != null && genresObject != null)
                {
                    addBook(isbnObject, titleObject, pgCntObject);
                    addGenre(genresObject);
                    addAuthor(authorsObject, isbnObject);
                    addPublisher(publishersObject);
                    addHasGenre(isbnObject, genresObject);
                    addWrites(authorsObject, isbnObject);
                    addPublishes(publishersObject, yearObject, isbnObject);
                }
            });
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * adds a new book to the database
     * @param isbn
     * @param title
     * @param pgCnt
     */
    private void addBook(BigInteger isbn, String title, int pgCnt) {
        try {
            int version = 1;
            Random r = new Random();
            int price = r.nextInt(998) + 1;
            double royalty = r.nextDouble();
            int stock = r.nextInt(1000) + 1;

            statement.executeUpdate("INSERT INTO project.book values(" + isbn +",'" + title + "'," + version + ", " + pgCnt + ", " + price + ", " + royalty + ", " + stock + ")");
        } catch (SQLException e) {
            if(!e.toString().equals("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"book_pkey\"\n" +
                    "  Detail: Key (isbn)=(" + isbn + ") already exists.")){
                System.out.println("Book Error Code: " + e.getErrorCode());
            }
        }
    }

    /**
     * Adds a new genre to the database
     * @param genres
     */
    private void addGenre(JsonArray genres) {
        genres.forEach(g -> {
            try {
                statement.executeUpdate("INSERT INTO project.genre values ('" + g + "');");
            } catch (SQLException e) {
                if(!e.toString().equals("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"genre_pkey\"\n" +
                        "  Detail: Key (name)=(" + g + ") already exists.")){
                    System.out.println("Genre Error Code: " + e.getErrorCode());
                }
            }
        });
    }

    private void addHasGenre(BigInteger isbn, JsonArray genres){
        genres.forEach(g -> {
            try {
                statement.executeUpdate("INSERT INTO project.hasgenre values ('" + g + "', '" + isbn + "');");
            } catch (SQLException e) {
                if(!e.toString().equals("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"hasgenre_pkey\"\n" +
                        "  Detail: Key (name, isbn)=(" + g + ", " + isbn + ") already exists.")){
                    System.out.println("HasGenre Error Code: " + e.getErrorCode());
                }
            }
        });
    }

    /**
     * Adds a new publisher to the database
     * @param publishers
     */
    private void addPublisher(JsonArray publishers) {
        Random r = new Random();
        String defaultEmail = "default@carleton.ca";
        long phoneNum = (long) (1000000000L + r.nextFloat() * 9000000000L);
        long bankAcc = (long) (100000000000000L + r.nextFloat() * 900000000000000L);

        publishers.forEach(p -> {
            String newP = p.getAsString().replaceAll("'","");
            try {
                statement.executeUpdate("INSERT INTO project.publisher values ('" + newP + "','" + defaultEmail + "', " + phoneNum + ", " + bankAcc + ");");
            } catch (SQLException e) {
                if(!e.toString().equals("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"publisher_pkey\"\n" +
                        "  Detail: Key (name)=(" + newP + ") already exists.")){
                    System.out.println("Publisher Error Code: " + e.getErrorCode());
                }
            }
        });
    }

    private void addPublishes(JsonArray publishers, int year, BigInteger isbn){
        publishers.forEach(p -> {
            String newP = p.getAsString().replaceAll("'","");
            try {
                statement.executeUpdate("INSERT INTO project.publishes values ('" + newP + "', '" + isbn + "', '" + year + "');");
            } catch (SQLException e) {
                if(!e.toString().equals("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"publishes_pkey\"\n" +
                        "  Detail: Key (pub_name, isbn)=(" + newP + ", " + isbn + ") already exists.")){
                    System.out.println("Publishes Error Code: " + e.getErrorCode());
                }
            }
        });
    }

    /**
     * Adds a new author to the database
     * @param authors
     */
    private void addAuthor(JsonArray authors, BigInteger isbn) {
        authors.forEach(a -> {
            String newA = a.getAsString().replaceAll("'","");
            String[] names = newA.split(" ");
            String firstName = names[0];
            String lastName = names[names.length-1];
            try {
                statement.executeUpdate("INSERT INTO project.author values ('" + firstName + "', '" + lastName + "');" +
                        "INSERT INTO project.writes values ('" + firstName + "', '" + lastName + "', '" + isbn + "');");
            } catch (SQLException e) {
                if(!e.toString().equals("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"author_pkey\"\n" +
                        "  Detail: Key (first_name, last_name)=(" + firstName + ", " + lastName + ") already exists.")){
                    System.out.println("Author Error Code: " + e.getErrorCode());
                }
            }
        });

    }

    private void addWrites(JsonArray authors, BigInteger isbn){
        authors.forEach(a -> {
            String newA = a.getAsString().replaceAll("'","");
            String[] names = newA.split(" ");
            String firstName = names[0];
            String lastName = names[names.length-1];
            try {
                statement.executeUpdate("INSERT INTO project.writes values ('" + firstName + "', '" + lastName + "', '" + isbn + "');");
            } catch (SQLException e) {
                if(!e.toString().equals("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"writes_pkey\"\n" +
                        "  Detail: Key (auth_fn, auth_ln, isbn)=(" + firstName + ", " + lastName + ", " + isbn + ") already exists.")){
                    System.out.println("Writes Error Code: " + e.getErrorCode());
                }
            }
        });
    }

    private static void parseBookObject(JsonObject book){
        if(!(book.get("title") instanceof JsonNull) &&
                !(book.get("isbn13") instanceof JsonNull) &&
                !(book.get("authors") instanceof JsonNull) &&
                !(book.get("year") instanceof JsonNull) &&
                !(book.get("page_count") instanceof JsonNull) &&
                !(book.get("tags") instanceof JsonNull) &&
                !(book.get("publishers") instanceof JsonNull) &&
                book.get("title") != null &&
                book.get("isbn13") != null &&
                book.get("authors") != null &&
                book.get("year") != null &&
                book.get("page_count") != null &&
                book.get("tags") != null &&
                book.get("publishers") != null &&
                !book.get("title").getAsString().equals(" ")
        ) {
            titleObject = book.get("title").getAsString();
            titleObject = titleObject.replaceAll("'","");
            isbnObject = book.get("isbn13").getAsBigInteger();
            authorsObject = (JsonArray) book.get("authors");
            try{ yearObject = book.get("year").getAsInt(); }catch(Exception ignored){}
            pgCntObject = book.get("page_count").getAsInt();
            genresObject = (JsonArray) book.get("tags");
            publishersObject = (JsonArray) book.get("publishers");
        }else {
            titleObject = null;
            isbnObject = null;
            authorsObject = null;
            yearObject = -1;
            pgCntObject = -1;
            genresObject = null;
            publishersObject = null;
        }
    }
}
