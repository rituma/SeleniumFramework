package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public interface databaseConnection{
	public static final String DB_URL = "jdbc:mysql://localhost:3306/employee";
	public static final String username = "root";
	public static final String password = "ritu1995"; 
    public static final List<List<Object>> list = new ArrayList<>();
    public static List<List<Object>> dbConnection(){
    try (Connection conn = DriverManager.getConnection(DB_URL, username, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employee")) { //
          
            int count=0;
           // Process the result set
           while (rs.next() && count<100) {
        	   List<Object> hp = new ArrayList<>();
        	   hp.add(String.valueOf(rs.getInt("emp_no")));
        	   hp.add(rs.getString("first_name"));
        	   count++;
        	   list.add(hp);
              }
           System.out.println(list);
           System.out.println("Connection successful!");

       } catch (SQLException e) {
    	   
           // Handle exceptions (e.g., connection failure, SQL errors)
           System.err.println("Database connection failed:");
           e.printStackTrace();
       }
	return list;
    }
}
