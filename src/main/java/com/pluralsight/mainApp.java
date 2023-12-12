package com.pluralsight;

import java.sql.*;
import java.util.Scanner;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class mainApp {
//    static String url = "jdbc:mysql://localhost:3306/northwind";
//    static String user = "";
//    static String password = "";


    //These static variables are for checking for null possibilities in the try/catch/final approaches. These are not used in the try-with-resources. REMEMBER THIS!
    static ResultSet resultSet = null;
    static PreparedStatement preparedStatement = null;
    static Connection connection = null;
    static Scanner input = new Scanner(System.in);


    public static void main(String[] args) throws SQLException {

        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.hca.jdbc.UsingDriverManager <username> " +
                            "<password>");
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];

        BasicDataSource dataSource = new BasicDataSource ();

        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUsername(username);
        dataSource.setPassword(password);


        System.out.println("What would you like to do?");
        System.out.println(" 1) Display all products");
        System.out.println(" 2) Display all customers");
        System.out.println(" 3) Display all categories");
        System.out.println(" 4) Exit");
        System.out.println("Select an option: ");
        int selection = input.nextInt();


        switch (selection) {
            case 1:
                getProducts(dataSource);
                break;
            case 2:
                getCustomers(dataSource);
                break;
            case 3:
                getAllCategories(dataSource);
                getProductsFromCategory(dataSource);
                break;
            case 4:
                System.out.println("Later!");
                System.exit(0);
        }


    }

    public static void getProducts(DataSource dataSource) throws SQLException {


        try {
            //This connects to MySQL with login info
            connection = dataSource.getConnection();

            //This sets up the query prompt for SQL
            preparedStatement = connection.prepareStatement(
                    "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products ");

            //This takes the results from the executed query.
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                // Get the 1st and 2nd fields returned from the query
                // based on the SELECT statement
                System.out.printf("Product Id: %d\nName: %s\nPrice: %.2f\nStock: %d\n------------------\n",
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getFloat(3),
                        resultSet.getInt(4));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();

        }
    }

    public static void getCustomers(DataSource dataSource) throws SQLException {

        try {
            connection = dataSource.getConnection();
            // Perform database operations here

            preparedStatement = connection.prepareStatement(
                    "SELECT ContactName, CompanyName, City, Country, Phone FROM customers " +
                            "ORDER BY Country");


            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Get the 1st and 2nd fields returned from the query
                // based on the SELECT statement
                System.out.printf("Name: %s\n Company Name: %s\nCity: %s\nCountry: %s\nPhone: %s------------------\n",
                        resultSet.getString("ContactName"),
                        resultSet.getString("CompanyName"),
                        resultSet.getString("City"),
                        resultSet.getString("Country"),
                        resultSet.getString("Phone"));
            }


            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();

        }
    }


    //Below shows a try with resources approach
    public static void getAllCategories(DataSource dataSource) throws SQLException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT CategoryID, CategoryName FROM categories ORDER BY CategoryID");
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                System.out.printf("Category ID: %d  Company Name: %s\n",
                        resultSet.getInt(1),
                        resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void getProductsFromCategory(DataSource dataSource) {
        System.out.println("Enter a category ID to display all respective products:");
        int selection = input.nextInt();

        //This try with resources approach first uses try to insert objects that could fail
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM products WHERE CategoryID = ?")
        ) {
            preparedStatement.setInt(1, selection);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Showing all results from CategoryID " + selection + ":");
                System.out.println("--------------------");
                while (resultSet.next()) {
                    System.out.printf("Product Id: %d\nName: %s\nPrice: %.2f\nStock: %d\n------------------\n",
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getFloat(3),
                            resultSet.getInt(4));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}




