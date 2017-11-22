/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zion Tseu
 */
import java.sql.*;
public class SimpleSelect {
    public static void main(String[] args) {
        String host = "jdbc:derby://localhost:1527/collegedb";
        String user = "nbuser";
        String password = "nbuser";
        String sqlQuery = "SELECT * FROM Programme";
        try {
            Connection conn = DriverManager.getConnection(host, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
            System.out.println(rs.getString(1) + "\t" + rs.getString(2));
        }
        conn.close();
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
