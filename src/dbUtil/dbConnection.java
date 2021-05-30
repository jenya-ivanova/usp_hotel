package dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {
    public static dbConnection con;
    public static Connection getConnection(){
        String dbUser = "root";
        String dbPassword = "";
        String url = "jdbc:mysql://localhost:3306/hotel";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, dbUser, dbPassword);
        }catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }
}
