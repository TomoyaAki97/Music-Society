package da;

import domain.Programme;
import java.sql.*;
import javax.swing.*;

public class ProgrammeDA {
    private String host = "jdbc:derby://localhost:1527/collegedb";
    private String user = "nbuser";
    private String password = "nbuser";
    private String tableName = "Programme";
    private Connection conn;
    private PreparedStatement stmt;
    
    public ProgrammeDA() {
        createConnection();
    }
    
    public Programme getRecord(String code) {
        String queryStr = "SELECT * FROM " + tableName + " WHERE Code = ?";
        Programme programme = null;
        try {
            stmt = conn.prepareStatement(queryStr);
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                programme = new Programme(code, rs.getString("Name"), rs.getString("Faculty"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return programme;
    }
    
    public void addRecord(Programme programme){
        String insertStr = "INSERT INTO " + tableName + " VALUES(?, ?, ?)"; 
        try{
            stmt = conn.prepareStatement(insertStr);
            stmt.setString(1, programme.getCode());
            stmt.setString(2, programme.getName());
            stmt.setString(3, programme.getFaculty());
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }    
    
    public void updateRecord(Programme programme){
        String updateStr = "UPDATE " + tableName + " SET Name = ?, Faculty = ? WHERE Code = ?"; 
        try{
            stmt = conn.prepareStatement(updateStr);
            stmt.setString(3, programme.getCode());
            stmt.setString(1, programme.getName());
            stmt.setString(2, programme.getFaculty());
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteRecord(Programme programme){
        String updateStr = "DELETE FROM " + tableName + " WHERE Code = ?"; 
        try{
            stmt = conn.prepareStatement(updateStr);
            stmt.setString(1, programme.getCode());
            stmt.executeUpdate();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createConnection() {
        try {
            conn = DriverManager.getConnection(host, user, password);
            System.out.println("***TRACE: Connection established.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void shutDown() {
        if (conn != null)
            try {
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        ProgrammeDA da = new ProgrammeDA();
        Programme programme = da.getRecord("IA");
        System.out.println(programme);
    }

    
}
