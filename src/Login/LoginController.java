package Login;

import Clients.Client;
import Receptionists.Receptionist;
import Receptionists.ReceptionistController;
import dbUtil.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static javafx.scene.layout.BackgroundPosition.CENTER;
import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;

public class LoginController {
    LoginModel loginModel = new LoginModel();

    @FXML
    private Label dbstatus;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private AnchorPane ap;

    public static int id_user;
    public static Client client = new Client();
    public static Receptionist receptionist = new Receptionist();

    dbConnection connect = new dbConnection();
    Connection connection = connect.getConnection();

    public void initialize() throws IOException{
        Image image = new Image(new FileInputStream("C:/Users/HP/Pictures/hotel/dubai.jpg"));
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        ap.setBackground(new Background(new BackgroundImage(image,NO_REPEAT, NO_REPEAT, CENTER, backgroundSize)));
    }

    public void Login(ActionEvent event) throws SQLException {
        if (username.getText().isEmpty() || password.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "Please enter username and password");
        else
            validateLogin();
    }

    public void validateLogin() throws SQLException{

        try{
            if(get_user(username.getText(), password.getText())){
                int id = receptionist.getUser_id();
                String user = receptionist.getUsername();
                String pass = receptionist.getPassword();
                String name = receptionist.getUser_name();
                String phone = receptionist.getUser_phone();

                id_user = receptionist.getUser_id();

                if(user.equals(username.getText()) && pass.equals(password.getText())){
                    Stage stage = (Stage) this.loginButton.getScene().getWindow();
                    stage.close();

                    receptionist = new Receptionist(id, user, pass, name, phone);
                    ReceptionistStage();
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
                username.clear();
                password.clear();
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void ReceptionistStage() {
        try {
            Stage userStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = (Pane) loader.load(getClass().getResource("/Receptionists/receptionists.fxml").openStream());
            ReceptionistController receptionistcontroller = (ReceptionistController) loader.getController();
            Scene scene = new Scene(root);
            userStage.setScene(scene);
            userStage.setTitle("Receptionist Menu");
            userStage.setResizable(false);
            userStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean get_user(String username, String password) throws SQLException{
        boolean exist = false;

        Statement statement = null;
        ResultSet query = null;

        String get_user = "Select * from users where username='" + username + "' AND password='" + password + "'";

        try {
            statement = connection.createStatement();
            query = statement.executeQuery(get_user);

            if (query.next()) {
                receptionist.setUser_id(query.getInt("user_id"));
                receptionist.setUsername(query.getString("username"));
                receptionist.setPassword(query.getString("password"));
                receptionist.setUser_name(query.getString("user_name"));
                receptionist.setUser_phone(query.getString("user_phone"));
                exist = true;
            }
            else {
                exist = false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return exist;
    }
}