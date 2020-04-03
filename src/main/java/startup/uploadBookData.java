package startup;/*
 * Copyright © 3.2020. Ryan Godfrey, John Breton.
 * All rights reserved.
 */

import com.google.gson.*;
import backend.DatabaseQueries;

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

            // populate DB with users + addresses
            for(int i = 0; i < 1000; i++)
                addUsers();

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
            double royalty = (r.nextInt(99) + 1) / 1.0;
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
                statement.executeUpdate("INSERT INTO project.genre values ('" + g.toString().replaceAll("\"", "") + "');");
            } catch (SQLException e) {
                if(!e.toString().equals("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"genre_pkey\"\n" +
                        "  Detail: Key (name)=(" + g.toString().replaceAll("\"", "") + ") already exists.")){
                    System.out.println("Genre Error Code: " + e.getErrorCode());
                }
            }
        });
    }

    private void addHasGenre(BigInteger isbn, JsonArray genres){
        genres.forEach(g -> {
            try {
                statement.executeUpdate("INSERT INTO project.hasgenre values ('" + g.toString().replaceAll("\"", "") + "', '" + isbn + "');");
            } catch (SQLException e) {
                if(!e.toString().equals("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"hasgenre_pkey\"\n" +
                        "  Detail: Key (name, isbn)=(" + g.toString().replaceAll("\"", "") + ", " + isbn + ") already exists.")){
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
                        "  Detail: Key (auth_fn, auth_ln)=(" + firstName + ", " + lastName + ") already exists.")){
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

    private void addUsers(){
        Random r = new Random();
        String[] firstName = {"Adam", "Alex", "Aaron", "Ben", "Carl", "Dan", "David", "Edward", "Fred", "Frank", "George", "Hal", "Hank", "Ike", "John", "Jack", "Joe", "Larry", "Monte", "Matthew", "Mark", "Nathan", "Otto", "Paul", "Peter", "Roger", "Roger", "Steve", "Thomas", "Tim", "Ty", "Victor", "Walter", "Aiden", "Aidan", "Ayden", "Brad", "Bradley", "Connor", "Cathy", "Denver", "Daisere", "Evelynn", "Farah", "Gus", "Hale", "Iziac", "Jill", "Kyle", "Leah", "Monty", "Nathan", "Orion", "Penelope", "Quinn", "Ryan", "Stacey", "Tracy", "Umar", "Vern", "William", "Xia", "Yanny", "Zelda"};
        String[] lastName = {"Anderson", "Ashwoon", "Aikin", "Bateman", "Bongard", "Bowers", "Boyd", "Cannon", "Cast", "Deitz", "Dewalt", "Ebner", "Frick", "Hancock", "Haworth", "Hesch", "Hoffman", "Kassing", "Knutson", "Lawless", "Lawicki", "Mccord", "McCormack", "Miller", "Myers", "Nugent", "Ortiz", "Orwig", "Paiser", "Pettigrew", "Quinn", "Quizoz", "Ramachandran", "Resnick", "Sagar", "Schickowski", "Schiebel", "Sellon", "Severson", "Shaffer", "Solberg", "Soloman", "Sonderling", "Soukup", "Soulis", "Stahl", "Sweeney", "Tandy", "Trebil", "Trusela", "Trussel", "Turco", "Uddin", "Uflan", "Ulrich", "Upson", "Vader", "Vail", "Valente", "VanZandt", "Vanderpoel", "Ventotla", "Vogal", "Wagle", "Wagner", "Wakefield", "Weinstein", "Weiss", "Yang", "Yates", "Yocum", "Zeaser", "Zeller", "Ziegler", "Bauer", "Baxster", "Casal", "Cataldi", "Caswell", "Celedon", "Chambers", "Chapman", "Christensen", "Darnell", "Davidson", "Davis", "DeLorenzo", "Dinkins", "Doran", "Dugelman", "Dugan", "Duffman", "Eastman", "Ferro", "Ferry", "Fletcher", "Fietzer", "Hylan", "Hydinger", "Illingsworth", "Ingram", "Irwin", "Jagtap", "Jenson", "Johnson", "Johnsen", "Jones", "Jurgenson", "Kalleg", "Kaskel", "Keller", "Leisinger", "LePage", "Lewis", "Linde", "Lulloff", "Maki", "Martin", "McGinnis", "Mills", "Moody", "Moore", "Napier", "Nelson", "Norquist", "Nuttle", "Olson", "Ostrander", "Reamer", "Reardon", "Reyes", "Rice", "Ripka", "Roberts", "Rogers", "Root", "Sandstrom", "Sawyer", "Schlicht", "Schmitt", "Schwager", "Schutz", "Schuster", "Tapia", "Thompson", "Tiernan", "Tisler"};
        String[] streetID = {"Blvd", "Rd", "St", "Ave", "Cres"};
        String[] apartment = {"", "A", "B", "C", "D"};
        String[] cities = {"Ottawa", "Toronto", "Montreal", "Calgary", "Vancouver", "Moscow", "Kelowna", "Windsor", "Haliburton", "Edmonton", "Winnipeg", "Saskatoon", "St. Johns", "Halifax"};
        String[] provincesArr = {"AB", "BC", "MB", "NB", "NL", "NS", "NT", "NU", "ON", "PE", "QC", "SK", "YT"};
        String[] country = {"Canada", "USA", "Mexico", "Brazil", "Sweden", "Germany", "Russia", "China"};
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        boolean[] trueFalse = {true, false};
        // address deets
        String defaultEmail = "email@default.ca";
        String defaultStreetName = "Default ";
        StringBuilder postalCode = new StringBuilder();
        int streetNum = (int) (100L + r.nextFloat() * 900L); // 3 digit street num
        boolean isBilling = trueFalse[r.nextInt(2)];
        //generate postal code
        for(int i = 0; i < 3; i++){
            postalCode.append(letters[r.nextInt(letters.length)]).append(numbers[r.nextInt(numbers.length)]);
        }
        // user deets
        String userFirstName = firstName[r.nextInt(firstName.length)];
        String userLastName = lastName[r.nextInt(lastName.length)];
        String username = userFirstName.substring(0,1).toLowerCase() + userLastName.substring(0, 3).toLowerCase(); // username is 1st letter of first name + first 3 letters of last name
        Integer salary = null;
        int defaultSal = 30000;
        if(trueFalse[r.nextInt(2)] && (username.contains("r") || username.contains("R"))) // decide if the user is an admin
            salary = defaultSal;
        String streetid = streetID[r.nextInt(streetID.length)];
        String apt = apartment[r.nextInt(apartment.length)];
        String city = cities[r.nextInt(cities.length)];
        String prov = provincesArr[r.nextInt(provincesArr.length)];
        String count = country[r.nextInt(country.length)];

        try {
            statement.executeUpdate("Insert into project.user values ('" + username + "', '" + username + "', '" + userFirstName + "', '" + userLastName + "', '" + defaultEmail + "');" +
                    "insert into project.librarian values ('" + username + "'," + salary + ");");

            DatabaseQueries.addAddress(Integer.toString(streetNum), defaultStreetName + streetid, apt, city, prov, count, postalCode.toString());
            DatabaseQueries.addHasAdd(username, true, false);

            if(!isBilling) { // need another address
                streetNum = (int) (100L + r.nextFloat() * 900L);
                DatabaseQueries.addAddress(Integer.toString(streetNum), defaultStreetName + streetID[r.nextInt(streetID.length)], apartment[r.nextInt(apartment.length)], cities[r.nextInt(cities.length)], provincesArr[r.nextInt(provincesArr.length)], country[r.nextInt(country.length)], postalCode.toString());
            } else {
                DatabaseQueries.addAddress(Integer.toString(streetNum), defaultStreetName + streetid, apt, city, prov, count, postalCode.toString());
            }
            DatabaseQueries.addHasAdd(username, false, true);

        } catch (SQLException e) {
            if(e.toString().equals("org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint \"user_pkey\"\n" +
                    "  Detail: Key (user_name)=('" + username + "') already exists."))
            e.printStackTrace();
        }
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
