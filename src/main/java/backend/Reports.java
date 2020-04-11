/*
 * Copyright © 4.2020. Ryan Godfrey, John Breton.
 * All rights reserved.
 */

package backend;

import javax.swing.*;
import java.sql.*;
import java.util.Vector;

public class Reports {
    // Just putting this here so we can change it when we test.
    private static final String USER = "postgres";
    private static final String DATABASE = "lookinnabook";
    private static Connection connection;
    private static Statement statement;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE, USER, "");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Produces a table of information with sales by genre, including the genre,
     * the quantity of books sold from that genre and the
     * revenue, cost, profit from that genre
     *
     * @param timeInter the interval of time in the past from now
     * @return a JTable of data
     */
    public static JTable genreReport(String timeInter, String[] sortOption) {
        final String column1 = "Genre";
        final String column2 = "Quantity";
        final String column3 = "Total Revenue";
        final String column4 = "Total Cost";
        final String column5 = "Total Profit";
        Vector<Vector<java.io.Serializable>> data = new Vector<Vector<java.io.Serializable>>();

        switch (timeInter) {
            case "Year" -> timeInter = "1 year";
            case "Month" -> timeInter = "1 month";
            case "Week" -> timeInter = "1 week";
            case "Day" -> timeInter = "1 day";
        }
        try {
            String query = String.format("SELECT name, " +
                    "count(name) as quantity, " +
                    "sum(quantity*price) as revenue, " +
                    "sum(quantity*price*royalty/100) as cost, s" +
                    "um(quantity*price)-sum(quantity*price*royalty/100) as profit " +
                    "from (project.order natural join project.checkout natural join project.bask_item natural join project.book natural join project.hasgenre) " +
                    "WHERE date_placed > now() - interval '%s' " +
                    "group by name " +
                    "order by %s %s;", timeInter, sortOption[0], sortOption[1]);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            Vector<java.io.Serializable> columnNames = new Vector<>();
            columnNames.add(column1);
            columnNames.add(column2);
            columnNames.add(column3);
            columnNames.add(column4);
            columnNames.add(column5);

            while (rs.next()) {
                Vector<java.io.Serializable> row = new Vector<java.io.Serializable>();
                row.add(rs.getString("name"));
                row.add(rs.getString("quantity"));
                row.add(rs.getDouble("revenue"));
                row.add("-" + rs.getDouble("cost"));
                row.add(rs.getDouble("profit"));
                data.add(row);
            }
            return new JTable(data, columnNames);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Produces a table of information with sales by author, including the author name,
     * the quantity of books sold from that author and the
     * revenue, cost, profit from that author
     *
     * @param timeInter the interval of time in the past from now
     * @return a JTable of data
     */
    public static JTable authorReport(String timeInter, String[] sortOption) {
        final String column1 = "Author";
        final String column2 = "Quantity";
        final String column3 = "Total Revenue";
        final String column4 = "Total Cost";
        final String column5 = "Total Profit";
        Vector<Vector<java.io.Serializable>> data = new Vector<Vector<java.io.Serializable>>();

        switch (timeInter) {
            case "Year" -> timeInter = "1 year";
            case "Month" -> timeInter = "1 month";
            case "Week" -> timeInter = "1 week";
            case "Day" -> timeInter = "1 day";
        }
        try {
            String query = String.format("SELECT coalesce(auth_fn, '') ||' '|| coalesce(auth_ln, '') as name, " +
                    "sum(quantity) as quantity, " +
                    "sum(quantity*price) as revenue, " +
                    "sum(quantity*price*royalty/100) as cost, " +
                    "sum(quantity*price)-sum(quantity*price*royalty/100) as profit " +
                    "from (project.order natural join project.checkout natural join project.bask_item natural join project.book natural join project.writes) " +
                    "WHERE date_placed > now() - interval '%s' " +
                    "group by auth_fn, auth_ln " +
                    "order by %s %s;", timeInter, sortOption[0], sortOption[1]);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            Vector<java.io.Serializable> columnNames = new Vector<>();
            columnNames.add(column1);
            columnNames.add(column2);
            columnNames.add(column3);
            columnNames.add(column4);
            columnNames.add(column5);

            while (rs.next()) {
                Vector<java.io.Serializable> row = new Vector<java.io.Serializable>();
                row.add(rs.getString("name"));
                row.add(rs.getString("quantity"));
                row.add(rs.getDouble("revenue"));
                row.add("-" + rs.getDouble("cost"));
                row.add(rs.getDouble("profit"));
                data.add(row);
            }
            return new JTable(data, columnNames);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Produces a table of information with sales by publisher, including the publisher name,
     * the quantity of books sold from that publisher and the
     * revenue, cost, profit from that publisher
     *
     * @param timeInter the interval of time in the past from now
     * @return a JTable of data
     */
    public static JTable publisherReport(String timeInter, String[] sortOption) {
        final String column1 = "Publisher";
        final String column2 = "Quantity";
        final String column3 = "Total Revenue";
        final String column4 = "Total Cost";
        final String column5 = "Total Profit";
        Vector<Vector<java.io.Serializable>> data = new Vector<Vector<java.io.Serializable>>();

        switch (timeInter) {
            case "Year" -> timeInter = "1 year";
            case "Month" -> timeInter = "1 month";
            case "Week" -> timeInter = "1 week";
            case "Day" -> timeInter = "1 day";
        }
        try {
            String query = String.format("SELECT pub_name as name, " +
                    "sum(quantity) as quantity, " +
                    "sum(quantity*price) as revenue, " +
                    "sum(quantity*price*royalty/100) as cost, " +
                    "sum(quantity*price)-sum(quantity*price*royalty/100) as profit " +
                    "from (project.order natural join project.checkout natural join project.bask_item natural join project.book natural join project.publishes) " +
                    "WHERE date_placed > now() - interval '%s' " +
                    "group by pub_name " +
                    "order by %s %s;", timeInter, sortOption[0], sortOption[1]);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            Vector<java.io.Serializable> columnNames = new Vector<>();
            columnNames.add(column1);
            columnNames.add(column2);
            columnNames.add(column3);
            columnNames.add(column4);
            columnNames.add(column5);

            while (rs.next()) {
                Vector<java.io.Serializable> row = new Vector<java.io.Serializable>();
                row.add(rs.getString("name"));
                row.add(rs.getString("quantity"));
                row.add(rs.getDouble("revenue"));
                row.add("-" + rs.getDouble("cost"));
                row.add(rs.getDouble("profit"));
                data.add(row);
            }
            return new JTable(data, columnNames);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Produces a table of information with sales by time frame, including the time frame,
     * the quantity of books sold in that time and the
     * revenue, cost, profit during that period.
     *
     * @param timeInter the interval of time to separate the data
     *                  (ie Month would separate the data into each month in the past until the beginning of time)
     * @return a JTable of data
     */
    public static JTable expenseReport(String timeInter, String[] sortOption) {
        final String column1 = "Date";
        final String column2 = "Quantity";
        final String column3 = "Total Revenue";
        final String column4 = "Total Cost";
        final String column5 = "Total Profit";
        Vector<Vector<java.io.Serializable>> data = new Vector<Vector<java.io.Serializable>>();
        String sortDate;
        int datePart = 0;

        if (sortOption[0].equals("date")) {
            sortDate = String.format("date_trunc('%s', date_placed)", timeInter);
        } else sortDate = sortOption[0];

        switch (timeInter) {
            case "Year" -> datePart = 6;
            case "Month" -> datePart = 3;
            case "Day", "Week" -> {
            }
        }

        try {
            String query = String.format("SELECT date_trunc('%s', date_placed) as time, " +
                    "sum(quantity) as quantity, " +
                    "sum(quantity*price) as revenue, " +
                    "sum(quantity*price*royalty/100) as cost, " +
                    "sum(quantity*price)-sum(quantity*price*royalty/100) as profit " +
                    "from (project.order natural join project.checkout natural join project.bask_item natural join project.book) " +
                    "WHERE date_placed < now() " +
                    "group by date_trunc('%s', date_placed) " +
                    "ORDER BY %s %s;", timeInter, timeInter, sortDate, sortOption[1]);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            Vector<java.io.Serializable> columnNames = new Vector<>();
            columnNames.add(column1);
            columnNames.add(column2);
            columnNames.add(column3);
            columnNames.add(column4);
            columnNames.add(column5);

            while (rs.next()) {
                Vector<java.io.Serializable> row = new Vector<java.io.Serializable>();
                row.add(rs.getString("time").substring(0, 10 - datePart));
                row.add(rs.getString("quantity"));
                row.add(rs.getDouble("revenue"));
                row.add("-" + rs.getDouble("cost"));
                row.add(rs.getDouble("profit"));
                data.add(row);
            }
            return new JTable(data, columnNames);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Return the quantity of a specific book sold in the previous time interval
     *
     * @param isbn   the book to look for
     * @param option the time interval to check (ex "1 month")
     * @return the quantity of the book that was sold in the previous time interval
     */
    public static int booksSold(String isbn, String option) {
        int quantity = 0;
        try {
            String query = String.format("SELECT isbn, sum(quantity) as quantity " +
                    "from (project.order natural join project.checkout natural join project.bask_item natural join project.book) " +
                    "WHERE date_placed > now() - interval '%s' " +
                    "AND isbn = %s" +
                    "group by isbn;", option, isbn);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                quantity = rs.getInt("quantity");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return quantity;
    }
}
