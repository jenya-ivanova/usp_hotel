package dbUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
    static dbConnection connect = new dbConnection();
    static Connection connection;

    static {
        try {
            connection = connect.getConnection();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean result;

    public static boolean insertSQL(String sql) {
        try {
            Statement statement = connection.createStatement();
            int check = statement.executeUpdate(sql);
            if (check > 0)
                result = true;
            else
                result = false;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return result;
    }


    public static ResultSet selectSQL(String sql) {
        ResultSet query = null;

        try {
            Statement statement = connection.createStatement();
            query = statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return query;
    }

    public static boolean updateSQL(String sql) {
        try {
            Statement statement = connection.createStatement();
            int check = statement.executeUpdate(sql);
            if (check > 0)
                result = true;
            else
                result = false;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return result;
    }

    public static int checkPoints(int id_user) {
        ResultSet query = null;
        String checkPoints = "Select * from points where id_user = '" + id_user + "'";

        int points = 0;
        try {
            Statement statement = connection.createStatement();
            query = statement.executeQuery(checkPoints);
            while (query.next()) {
                points = query.getInt("points");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return points;
    }
    public static boolean setStatusGolden(int points,int id_user){
        Statement statement=null;
        ResultSet query=null;
        String statusGolden = "Update users set id_status=2 where id_user='"+id_user+"'";
        if(points>200){
            updateSQL(statusGolden);
            return true;
        }
        else{
            return false;
        }
    }
}
