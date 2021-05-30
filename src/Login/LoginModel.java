package Login;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import dbUtil.dbConnection;

public class LoginModel {
    Connection connection;

    public LoginModel() {
        try {
            this.connection = dbConnection.getConnection();
        } catch (Exception ex) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (this.connection == null) {
            System.exit(1);
        }
    }

    public boolean isDatabaseConnected() {
        return this.connection != null;
    }
};

