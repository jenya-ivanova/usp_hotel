package AddReservation;

import Clients.AddClientController;
import Login.LoginController;
import dbUtil.SQL;
import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import static javafx.scene.layout.BackgroundPosition.CENTER;
import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;

public class AddReservationController {
    @FXML
    private ComboBox type_reservation_combobox;
    @FXML
    private ComboBox clients_combobox;
    @FXML
    private RadioButton existing_client_radiobutton;
    @FXML
    private RadioButton new_client_radiobutton;
    @FXML
    private Label message_label;
    @FXML
    private Label reservation_label;
    @FXML
    private Button view_rooms_button;
    @FXML
    private Button create_button;
    @FXML
    private Button save_button;
    @FXML
    private DatePicker date_reservation;
    @FXML
    private DatePicker arrival_date;
    @FXML
    private DatePicker departure_date;
    @FXML
    private ComboBox type_rooms_combobox;
    @FXML
    private ComboBox rooms_combobox;
    @FXML
    private AnchorPane ap;

    dbConnection connect = new dbConnection();
    Connection connection = connect.getConnection();

    public LocalDate date_res;
    public LocalDate arrival;
    public LocalDate departure;

    public ObservableList<Integer> rooms = FXCollections.observableArrayList();
    public ObservableList<String> room_types = FXCollections.observableArrayList();
    int id_reservation;
    public boolean ok=true;

    Alert alert = new Alert(Alert.AlertType.ERROR);
    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    public void initialize() throws IOException {
        Image image1 = new Image(new FileInputStream("C:/Users/HP/Pictures/hotel/add.jpg"));
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        ap.setBackground(new Background(new BackgroundImage(image1,NO_REPEAT, NO_REPEAT, CENTER, backgroundSize)));

        type_reservation_combobox.setPromptText("Choose");
        type_reservation_combobox.setItems(get_type_reservations());

        type_rooms_combobox.setPromptText("Choose:");
        type_rooms_combobox.setItems(get_room_types());
    }

    public void existing_client_radiobutton_on_action(ActionEvent event) {
        clients_combobox.setPromptText("Choose:");
        clients_combobox.setItems(get_existing_clients());
    }

    public void new_client_radiobutton_on_action(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Clients/add_client.fxml"));
            Stage registerStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            AddClientController addclientcontroller = (AddClientController) loader.getController();
            registerStage.setTitle("ADD CLIENT");
            registerStage.setScene(new Scene(root, 700, 500));
            registerStage.show();
            root.requestFocus();
        } catch (Exception ex) {
            System.out.println("Error loading FXML!");
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    public void type_rooms_combobox_on_action(ActionEvent event){
        String name_type_room = type_rooms_combobox.getValue().toString();
        int id_type_room = get_id_type_room(name_type_room);

        arrival = arrival_date.getValue();
        departure = departure_date.getValue();

        rooms_combobox.setPromptText("Choose:");
        rooms_combobox.setItems(get_rooms(id_type_room, arrival, departure));
    }

    public void create_button_on_action(ActionEvent event) throws SQLException{
        if(!existing_client_radiobutton.isSelected()){
            if(!new_client_radiobutton.isSelected()){
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required!");
                alert.showAndWait();
            }
        }
        if(existing_client_radiobutton.isSelected()){
            if(clients_combobox.getSelectionModel().isEmpty()) {
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required!");
                alert.showAndWait();
            }
        }
        if(date_reservation.getValue() == null || type_reservation_combobox.getSelectionModel().isEmpty()){
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields are required!");
            alert.showAndWait();
        }
        else {
            String name_type_reservation = type_reservation_combobox.getValue().toString();

            int id_type_reservation = get_id_type_reservation(name_type_reservation);
            int id_client = 0;

            if (existing_client_radiobutton.isSelected()) {
                String name_client = clients_combobox.getValue().toString();
                id_client = get_id_client(name_client);
            }
            if (new_client_radiobutton.isSelected())
                id_client = get_id_client(LoginController.client.getName_client());

            int user_id = LoginController.receptionist.getUser_id();
            date_res = date_reservation.getValue();

            insert_reservation(user_id, date_res, id_type_reservation, id_client);
        }
    }

    public void add_room_button_on_action(ActionEvent event) throws SQLException{
        int status = 1;

        insert_reservation_room(id_reservation, Integer.parseInt(rooms_combobox.getValue().toString()),arrival, departure, status);
    }

    public void save_button_on_action(ActionEvent event){
        if(ok)
            JOptionPane.showMessageDialog(null,"Changes saved successfully!");
        else
            JOptionPane.showMessageDialog(null,"An error occurred! Please try again!");

        Stage stage = (Stage) save_button.getScene().getWindow();
        stage.close();
    }


    public void insert_reservation(int user_id, LocalDate reservation_date, int id_type_reservation, int id_client) throws SQLException{
        String insertReservation="Insert into reservations(user_id, reservation_date, id_type_reservation, id_client) values ('" + LoginController.receptionist.getUser_id() + "','" + reservation_date + "','" + id_type_reservation + "','" + id_client + "')";

        if (SQL.insertSQL(insertReservation)) {
            alert2.setTitle("Reservation Information");
            alert2.setHeaderText(null);
            alert2.setContentText("Reservation created!");
            alert2.showAndWait();
            id_reservation = get_id_reservation(id_type_reservation, id_client, LoginController.receptionist.getUser_id(), date_res);
        }
        else{
            alert2.setTitle("Reservation Information");
            alert2.setHeaderText(null);
            alert2.setContentText("An error occurred! Please try again!");
            alert2.showAndWait();
        }
    }

    public void insert_reservation_room(int reservation_id, int room_id, LocalDate arrival, LocalDate departure, int reservation_status) throws SQLException{
        String insert_reservation_room ="Insert into room_reservation(reservation_id, room_id, date_arrival, date_departure, reservation_status_id) values ('" + reservation_id + "','" + room_id + "','" + arrival + "','" + departure + "','" + reservation_status + "')";

        if (SQL.insertSQL(insert_reservation_room)) {
            alert2.setTitle("Room Information");
            alert2.setHeaderText(null);
            alert2.setContentText("Room added");
            alert2.showAndWait();
            ok=true;
        }
        else {
            alert2.setTitle("Room Information");
            alert2.setHeaderText(null);
            alert2.setContentText("An error occurred! Please try again!");
            alert2.showAndWait();
            ok=false;
        }

        type_rooms_combobox.getSelectionModel().clearSelection();
        rooms.clear();
    }


    public ObservableList<String> get_type_reservations(){
        ObservableList<String> type_reservations = FXCollections.observableArrayList();

        Statement statement = null;
        ResultSet query = null;
        String get_type_reservations = "Select * from reservation_types";

        try {
            statement = connection.createStatement();
            query = statement.executeQuery(get_type_reservations);

            while (query.next()) {
                type_reservations.add(query.getString("name_type_reservation"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return type_reservations;
    }

    public ObservableList<String> get_existing_clients(){
        ObservableList<String> clients = FXCollections.observableArrayList();

        Statement statement = null;
        ResultSet query = null;
        String get_existing_clients = "Select * from clients";

        try {
            statement = connection.createStatement();
            query = statement.executeQuery(get_existing_clients);

            while (query.next()) {
                clients.add(query.getString("name_client"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return clients;
    }

    public ObservableList<String> get_room_types(){
        Statement statement = null;
        ResultSet query = null;
        String get_room_types = "Select * from room_types";

        try {
            statement = connection.createStatement();
            query = statement.executeQuery(get_room_types);

            while (query.next()) {
                room_types.add(query.getString("room_type_name"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return room_types;
    }

    public ObservableList<Integer> get_rooms(int id_type_room, LocalDate arrival, LocalDate departure){
        String getRooms = "SELECT * FROM rooms r join room_types rt on rt.room_type_id=r.room_type_id where r.room_type_id='" + id_type_room + "' AND r.room_id NOT IN(SELECT rr.room_id from room_reservation rr join rooms r on r.room_id=rr.room_id where rr.date_departure>='" + arrival + "' AND rr.date_arrival<='" + departure + "')";

        try {
            ResultSet rs = SQL.selectSQL(getRooms);

            while (rs.next()) {
                rooms.add(rs.getInt("room_id"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return rooms;
    }

    public int get_id_type_reservation(String name_type_reservation){
        int id_type_reservation = 0;

        Statement statement = null;
        ResultSet query = null;
        String get_id_type_reservation = "Select * from reservation_types where name_type_reservation='" + name_type_reservation + "'";

        try {
            statement = connection.createStatement();
            query = statement.executeQuery(get_id_type_reservation);

            if(query.next()) {
                id_type_reservation = query.getInt("id_type_reservation");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return id_type_reservation;
    }

    public int get_id_client(String name_client){
        int id_client = 0;

        Statement statement = null;
        ResultSet query = null;
        String get_id_client = "Select * from clients where name_client='" + name_client + "'";

        try {
            statement = connection.createStatement();
            query = statement.executeQuery(get_id_client);

            while (query.next()) {
                id_client = query.getInt("id_client");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return id_client;
    }

    public int get_id_type_room(String name_type_room){
        int id_type_room = 0;

        Statement statement = null;
        ResultSet query = null;
        String get_id_type_room = "Select * from room_types where room_type_name='" + name_type_room + "'";

        try {
            statement = connection.createStatement();
            query = statement.executeQuery(get_id_type_room);

            while (query.next()) {
                id_type_room = query.getInt("room_type_id");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return id_type_room;
    }

    public int get_id_reservation(int id_type_reservation, int id_client, int user_id, LocalDate date_res) {
        String getIDReservation = "SELECT * FROM reservations where id_type_reservation='" + id_type_reservation + "' AND id_client='" + id_client + "' AND user_id='" + user_id + "' AND reservation_date='" + date_res + "'";
        int id_reservation = 0;

        try {
            ResultSet rs = SQL.selectSQL(getIDReservation);

            if (rs.next()) {
                id_reservation = rs.getInt("reservation_id");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return id_reservation;
    }
}
