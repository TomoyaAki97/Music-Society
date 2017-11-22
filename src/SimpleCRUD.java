/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zion Tseu
 */
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class SimpleCRUD extends JFrame {
    private JLabel jlblProgCode= new JLabel("Programme Code");
    private JTextField jtfProgCode = new JTextField(10);
    private JLabel jlblProgName= new JLabel("Programme Name");
    private JTextField jtfProgName = new JTextField(15);
    private JLabel jlblFaculty= new JLabel("Faculty");
    private JTextField jtfFaculty = new JTextField(10);
    private JButton jbtCreate = new JButton("Create");
    private JButton jbtRetrieve = new JButton("Retrieve");
    private JButton jbtUpdate = new JButton("Update");
    private JButton jbtDelete = new JButton("Delete");
    private JButton jbtReset = new JButton("Reset");
    private String host = "jdbc:derby://localhost:1527/collegedb";
    private String user = "nbuser";
    private String password = "nbuser";
    private String tableName = "PROGRAMME"; 
    private Connection conn ;
    
    public SimpleCRUD() {
        JPanel jpanel = new JPanel( new GridLayout(3,2));
        JPanel jpanel2 = new JPanel( new FlowLayout(FlowLayout.CENTER));
        Font myFont = new Font("SansSerif ", Font.BOLD, 16);
        
        
        jlblProgCode.setFont(myFont);
        jlblProgName.setFont(myFont);
        jlblFaculty.setFont(myFont);
        jbtCreate.setFont(myFont);
        jbtRetrieve.setFont(myFont);
        jbtUpdate.setFont(myFont);
        jbtDelete.setFont(myFont);
        jbtReset.setFont(myFont);
        
        jpanel.add(jlblProgCode);
        jpanel.add(jtfProgCode);
        jpanel.add(jlblProgName);
        jpanel.add(jtfProgName);
        jpanel.add(jlblFaculty);
        jpanel.add(jtfFaculty);
        
        jpanel2.add(jbtCreate);
        jpanel2.add(jbtRetrieve);
        jpanel2.add(jbtUpdate);
        jpanel2.add(jbtDelete);
        jpanel2.add(jbtReset);
        
        add(jpanel, BorderLayout.CENTER);
        add(jpanel2, BorderLayout.SOUTH);
        
        setTitle("Simple CRUD");
        setSize(600,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        jtfProgCode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {   
                jtfProgName.requestFocusInWindow();
            }
        });
        
        jtfProgName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {   
                jtfFaculty.requestFocusInWindow();
            }
        });
        
        jbtReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {   
                clearText();
            }
        });
                        
        jbtCreate.addActionListener(new ButtonListener());
        jbtRetrieve.addActionListener(new RetrieveListener());
        jbtUpdate.addActionListener(new ButtonListener()); 
        jbtDelete.addActionListener(new ButtonListener()); 
          
    }       

    private class RetrieveListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String code = jtfProgCode.getText();
            ResultSet rs = selectRecord(code);
            try {
                if (rs.next()) {
                    jtfProgName.setText(rs.getString("Name"));
                    jtfFaculty.setText(rs.getString("Faculty"));
                } else {
                    JOptionPane.showMessageDialog(null,"No such programme code.");
                }
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    public ResultSet selectRecord(String code) {
            
        String queryStr = "SELECT * FROM " + tableName + " WHERE Code = ?";
        ResultSet rs = null;
        try {     
            createConnection();
            PreparedStatement stmt = conn.prepareStatement(queryStr);
            stmt.setString(1, code);
            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
        }
        return rs;
    }
    
    private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String code = jtfProgCode.getText();
            String name = jtfProgName.getText();
            String faculty = jtfFaculty.getText();
            ResultSet rs = selectRecord(code);                       
            try {
                createConnection();
                if (e.getSource() == jbtCreate){ 
                    if(rs.next()){
                        JOptionPane.showMessageDialog(null, "The programme code already exist.", "Duplicate Record", JOptionPane.ERROR_MESSAGE);                       
                    }
                    else{
                        PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tableName + " VALUES(?, ?, ?)");
                        stmt.setString(1, code);
                        stmt.setString(2, name);
                        stmt.setString(3, faculty);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "New Programme added.","Successful Record Insertion",JOptionPane.INFORMATION_MESSAGE);                       
                        clearText();
                    }
                }
                else if(e.getSource() == jbtUpdate){
                    if(rs.next()){
                        
                        PreparedStatement stmt = conn.prepareStatement("UPDATE " + tableName + " SET Name = ?, Faculty = ? WHERE Code = ?");
                        stmt.setString(1, name);
                        stmt.setString(2, faculty);
                        stmt.setString(3, code);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record updated.","Successful Record Update",JOptionPane.INFORMATION_MESSAGE);                       
                        clearText();
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "No such Programme code.","Record Not Found",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(e.getSource() == jbtDelete){
                    if(rs.next()){
                        int option = JOptionPane.showConfirmDialog(null, "Are you sure?", "Select an Option", JOptionPane.YES_NO_CANCEL_OPTION);
                        if(option == 0){
                            PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE Code = ?");
                            stmt.setString(1, code);
                            stmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Record deleted.","Successful Record Deletion",JOptionPane.INFORMATION_MESSAGE);                       
                            clearText();
                        }
                    }
                    else
                        clearText();
                }       
            }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }

    }
    public static void main(String[] args) {
        new SimpleCRUD();
    }
    
    private void createConnection(){
        try{
            conn = DriverManager.getConnection(host,user,password);
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void clearText(){
        jtfProgCode.setText("");
        jtfProgName.setText("");
        jtfFaculty.setText("");
    }

}
