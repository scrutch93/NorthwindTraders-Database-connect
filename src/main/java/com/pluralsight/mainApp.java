package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class mainApp {
    static String url = "jdbc:mysql://localhost:3306/northwind";
    static  String user = "";
    static String password = "";

        public static void main(String[] args) throws SQLException {


            Scanner input = new Scanner(System.in);

            System.out.println("What would you like to do?");
            System.out.println(" 1) Display all products");
            System.out.println(" 2) Display all customers");
            System.out.println(" 3) Exit");
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
                    System.out.println("Later!");
                    System.exit(0);
            }



        }
    public static void getProducts() throws SQLException {

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            // Perform database operations here

            preparedStatement = connection.prepareStatement(
                    "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products ");


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

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            // Perform database operations here

            preparedStatement = connection.prepareStatement(
                    "SELECT ContactName, CompanyName, City, Country, Phone FROM customers ");


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




    }




