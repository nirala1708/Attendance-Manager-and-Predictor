/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic75;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author HP
 */
public class Connect {
 public static Connection connect()
 {
     Connection con =null;
     try
     {
         Class.forName("oracle.jdbc.OracleDriver");
        // System.out.println("Class loaded scuccesfully");
         System.out.println("jdbc:oracle:thin:@//"+Basic75.DbLoginID+":1521/xe");
         System.out.println("pass: "+Basic75.DbPassword);
         con= DriverManager.getConnection("jdbc:oracle:thin:@//"+Basic75.DbLoginID+":1521/xe","system",Basic75.DbPassword);
         con.setReadOnly(false);
         
     }
     catch(ClassNotFoundException cnf)
     {
         System.out.println("Class could not be loaded, Connenct.java: "+cnf.getMessage());
     }
     catch(SQLException se)
     {
         System.out.println("Sql Exception Connect.java: "+se.getMessage());
     }
     finally
     {
         return con;
     }
 }
    
}
