/*
 * Copyright © 4.2020. Ryan Godfrey, John Breton.
 * All rights reserved.
 */

package backend;

import frontend.AdminScreen;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Reports {
    // Just putting this here so we can change it when we test.
    private static final String USER = "ryan";
    private static final String DATABASE = "LookInnaBook";
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
     * Produces a string of information with sales by genre, including the genre,
     * the quantity of books sold from that genre and the
     * revenue, cost, profit from that genre
     *
     * @param timeInter the interval of time in the past from now
     * @return a string of data
     */
    public static String genreReport(String timeInter, String[] sortOption) {
        final String column1 = "Name";
        final String column2 = "Quantity";
        final String column3 = "Total Revenue";
        final String column4 = "Total Cost";
        final String column5 = "Total Profit";

        switch(timeInter){
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

            String data = "";
            data += String.format("%-35s ", column1);
            data += String.format("%-20s", column2);
            data += String.format("%-20s", column3);
            data += String.format("%-20s", column4);
            data += String.format("%10s%n", column5);

            while (rs.next()) {
                //Print one row
                data += String.format("%-35s", rs.getString("name"));
                data += String.format("%-20s", rs.getString("quantity"));
                data += String.format("$%-20.2f", rs.getDouble("revenue"));
                data += String.format("-$%-20.2f", rs.getDouble("cost"));
                data += String.format("$%-20.2f%n", rs.getDouble("profit"));
            }
            return data;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Produces a string of information with sales by author, including the author name,
     * the quantity of books sold from that author and the
     * revenue, cost, profit from that author
     *
     * @param timeInter the interval of time in the past from now
     * @return a string of data
     */
    public static String authorReport(String timeInter, String[] sortOption) {
        final String column1 = "Name";
        final String column2 = "Quantity";
        final String column3 = "Total Revenue";
        final String column4 = "Total Cost";
        final String column5 = "Total Profit";

        switch(timeInter){
            case "Year" -> timeInter = "1 year";
            case "Month" -> timeInter = "1 month";
            case "Week" -> timeInter = "1 week";
            case "Day" -> timeInter = "1 day";
        }
        try {
            String query = String.format("SELECT auth_fn as name, auth_ln, " +
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

            String data = "";
            data += String.format("%10s ", column1);
            data += String.format("%30s", column2);
            data += String.format("%30s", column3);
            data += String.format("%30s", column4);
            data += String.format("%20s%n", column5);

            while (rs.next()) {
                //Print one row
                String name =  rs.getString("name") + " " + rs.getString("auth_ln");
                data += String.format("%-35s", name);
                data += String.format("%-20s", rs.getString("quantity"));
                data += String.format("$%-20.2f", rs.getDouble("revenue"));
                data += String.format("-$%-20.2f", rs.getDouble("cost"));
                data += String.format("$%-20.2f%n", rs.getDouble("profit"));
            }
            return data;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Produces a string of information with sales by publisher, including the publisher name,
     * the quantity of books sold from that publisher and the
     * revenue, cost, profit from that publisher
     *
     * @param timeInter the interval of time in the past from now
     * @return a string of data
     */
    public static String publisherReport(String timeInter, String[] sortOption) {
        final String column1 = "Name";
        final String column2 = "Quantity";
        final String column3 = "Total Revenue";
        final String column4 = "Total Cost";
        final String column5 = "Total Profit";

        switch(timeInter){
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

            String data = "";
            data += String.format("%15s ", column1);
            data += String.format("%90s", column2);
            data += String.format("%20s", column3);
            data += String.format("%20s", column4);
            data += String.format("%20s%n", column5);

            while (rs.next()) {
                //Print one row
                data += String.format("%-100s", rs.getString("name"));
                data += String.format("%-20s", rs.getString("quantity"));
                data += String.format("$%-20.2f", rs.getDouble("revenue"));
                data += String.format("-$%-20.2f", rs.getDouble("cost"));
                data += String.format("$%-10.2f%n", rs.getDouble("profit"));
            }
            return data;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Produces a string of information with sales by time frame, including the time frame,
     * the quantity of books sold in that time and the
     * revenue, cost, profit during that period.
     *
     * @param timeInter the interval of time to seperate the data
     *                  (ie Month would seperate the data into each month in the past until the beginning of time)
     * @return a string of data
     */
    public static String expenseReport(String timeInter, String[] sortOption) {
        final String column1 = timeInter;
        final String column2 = "Quantity";
        final String column3 = "Total Revenue";
        final String column4 = "Total Cost";
        final String column5 = "Total Profit";
        String sortDate = "";
        int datePart = 0;

        if(sortOption[0].equals("date")){
            sortDate = String.format("date_trunc('%s', date_placed)", timeInter);
        } else sortDate = sortOption[0];

        switch (timeInter){
            case "Year" -> datePart = 6;
            case "Month" -> datePart = 3;
            case "Day", "Week" -> datePart = 0;
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

            String data = "";
            data += String.format("%10s ", column1);
            data += String.format("%20s", column2);
            data += String.format("%20s", column3);
            data += String.format("%20s", column4);
            data += String.format("%20s%n", column5);

            while (rs.next()) {
                //Print one row
                data += String.format("%-20s", rs.getString("time").substring(0, 10 - datePart));
                data += String.format("%-20s", rs.getString("quantity"));
                data += String.format("$%-20.2f", rs.getDouble("revenue"));
                data += String.format("-$%-20.2f", rs.getDouble("cost"));
                data += String.format("$%-10.2f%n", rs.getDouble("profit"));
            }
            return data;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
