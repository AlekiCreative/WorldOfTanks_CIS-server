/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ciswotserver;

import java.sql.*;

/**
 *
 * @author alikp
 */
public class DatabaseController {
    
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final String DB_NAME = "cis";
    private static final String DB_SERVER = "localhost";
    private static final String DB_PORT = "3306";
    
    public String read(String table,String name_id, int id, String columm){
    
        try{  
            
            Class.forName("com.mysql.cj.jdbc.Driver");  
            Connection con = DriverManager.getConnection("jdbc:mysql://"+DB_SERVER+":"+DB_PORT+"/"+DB_NAME,DB_USER,DB_PASS);
            
            
            //here sonoo is database name, root is username and password  
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("SELECT "+columm+" FROM " + table + " WHERE "+name_id+"=" + id);  
            while(rs.next()){  
            return rs.getString(1);
            }
            con.close(); 
            
         }catch(Exception e){ 
             System.out.println(e);
         }
        
        return "Chyba DB";
    }
    

    public String update(String table, String id_columm, int id, String columm, String value){
        Connection con = null;
        PreparedStatement ps = null;
        
        try{

            con = DriverManager.getConnection("jdbc:mysql://"+DB_SERVER+":"+DB_PORT+"/"+DB_NAME,DB_USER,DB_PASS);
            String query = "update "+table+" set "+columm+"=? WHERE `"+id_columm+"`=? ";
            //String query = "update DemoTable set FirstName=? where Id=? ";
            ps = con.prepareStatement(query);
            ps.setString(1, value);
            ps.setInt(2, id);
            ps.executeUpdate();
            

            
            con.close(); 
            
         }catch(Exception e){ 
             System.out.println(e);
         }
        
        return "Update DB: " + table + " : " +columm+ " : " + value ;
    }
    
    public int getCOUNT(String table){
    
        try{  
            
            Class.forName("com.mysql.cj.jdbc.Driver");  
            Connection con = DriverManager.getConnection("jdbc:mysql://"+DB_SERVER+":"+DB_PORT+"/"+DB_NAME,DB_USER,DB_PASS);
            
            //here sonoo is database name, root is username and password  
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("SELECT COUNT(*) FROM " + table);  
            rs.next();            
            
            
            return rs.getInt("count(*)");
            
            
            
         }catch(Exception e){ 
             System.out.println(e);
         }
        
        return 666;
    }
    
    
    
    
    public String GET_CLAN_ID(int id){
    
        String return_data = read("clans","ID", id, "clan_id");
        
    return return_data;
    }
    
    public String GET_ACCESS_TOKEN(int id){
    
        String return_data = read("clans","ID", id, "access_token");
        
    return return_data;
    }
    
    
    }
