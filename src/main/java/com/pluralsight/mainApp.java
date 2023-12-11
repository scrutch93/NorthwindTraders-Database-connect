package com.pluralsight;

import java.sql.*;

public class mainApp {

        public static void main(String[] args) {

            //url, user, password go here

            try {
                Connection connection = DriverManager.getConnection(url, user, password);
                // Perform database operations here

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products ");


                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    // Get the 1st and 2nd fields returned from the query
                    // based on the SELECT statement
                    System.out.printf("Product Id: %d\nName: %s\nPrice: %.2f\nStock: %d\n------------------\n",
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getFloat(3),
                            resultSet.getInt(4));
                }


                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

