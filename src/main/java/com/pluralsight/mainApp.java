package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class mainApp {
    static String url = "jdbc:mysql://localhost:3306/northwind";
    static  String user = "";
    static String password = "";

    static ResultSet resultSet = null;
    static PreparedStatement preparedStatement = null;
    static Connection connection = null;
    static Scanner input = new Scanner(System.in);



        public static void main(String[] args) throws SQLException {


            System.out.println("What would you like to do?");
            System.out.println(" 1) Display all products");
            System.out.println(" 2) Display all customers");
            System.out.println(" 3) Display all categories");
            System.out.println(" 4) Exit");
            System.out.println("Select an option: ");
            int selection = input.nextInt();


            switch (selection) {
                case 1:
                    getProducts();
                    break;
                case 2:
                    getCustomers();
                    break;
                case 3:
                    getAllCategories();
                    getProductsFromCategory();
                    break;
                case 4:
                    System.out.println("Later!");
                    System.exit(0);
            }



        }
    public static void getProducts() throws SQLException {

        try {
            //This connects to MySQL with login info
            connection = DriverManager.getConnection(url, user, password);

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

    public static void getCustomers() throws SQLException {

        try {
            connection = DriverManager.getConnection(url, user, password);
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
    public static void getAllCategories() throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
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


    public static void getProductsFromCategory() {
        System.out.println("Enter a category ID to display all respective products:");
        int selection = input.nextInt();

        //This try with resources approach first uses try to insert objects that could fail
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
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




