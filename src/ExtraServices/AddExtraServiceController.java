package ExtraServices;

import dbUtil.SQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import static javafx.scene.layout.BackgroundPosition.CENTER;
import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;

public class AddExtraServiceController {
    @FXML
    private Button add_button;
    @FXML
    private ComboBox clients_combobox;
    @FXML
    private ComboBox reservations_combobox;
    @FXML
    private ComboBox extra_services_combobox;
    @FXML
    private AnchorPane ap;

    Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    public void initialize() throws IOException {
        Image image1 = new Image(new FileInputStream("C:/Users/HP/Pictures/hotel/tree.jpeg"));
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        ap.setBackground(new Background(new BackgroundImage(image1,NO_REPEAT, NO_REPEAT, CENTER, backgroundSize)));

        clients_combobox.setPromptText("Choose:");
        clients_combobox.setItems(get_clients());
    }

    public void clients_combobox_on_action(ActionEvent event){
        String name_client = clients_combobox.getValue().toString();
        int id_client = get_id_client(name_client);

        reservations_combobox.setPromptText("Choose:");
        reservations_combobox.setItems(get_reservations(id_client));
    }

    public void reservations_combobox_on_action(ActionEvent event){
        extra_services_combobox.setPromptText("Choose:");
        extra_services_combobox.setItems(get_extra_services());
    }

    public void add_button_on_action(ActionEvent event){
        if(clients_combobox.getSelectionModel().isEmpty() || extra_services_combobox.getSelectionModel().isEmpty() || reservations_combobox.getSelectionModel().isEmpty()) {
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled!");
            alert.showAndWait();
        }
        else {
            String name_client = clients_combobox.getValue().toString();
            String name_service = extra_services_combobox.getValue().toString();
            int id_client = get_id_client(name_client);
            int id_reservation = Integer.parseInt(reservations_combobox.getValue().toString());
            int id_service = get_id_service(name_service);

            String insert_client_service ="Insert into reservation_services(reservation_id, service_id, id_client) values ('" + id_reservation + "','" + id_service + "','" + id_client + "')";

            if (SQL.insertSQL(insert_client_service)) {
                JOptionPane.showMessageDialog(null, "Service added successfully!");
            }
            else{
                JOptionPane.showMessageDialog(null, "There was an error! Please try again!");
            }

            Stage stage = (Stage) add_button.getScene().getWindow();
            stage.close();
        }
    }


    public ObservableList<String> get_clients() {
        ObservableList<String> options = FXCollections.observableArrayList();

        String getClients = "SELECT * FROM clients";

        try {
            ResultSet rs = SQL.selectSQL(getClients);

            while (rs.next()) {
                options.add(rs.getString("name_client"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return options;
    }

    public int get_id_client(String name_client) {
        String getIDclient = "SELECT * FROM clients where name_client='" + name_client + "'";
        int id_client = 0;

        try {
            ResultSet rs = SQL.selectSQL(getIDclient);

            while (rs.next()) {
                id_client = rs.getInt("id_client");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return id_client;
    }

    public ObservableList<String> get_reservations(int id_client) {
        ObservableList<String> options = FXCollections.observableArrayList();

        String getIDReservation = "SELECT * FROM reservations where id_client='" + id_client + "'";

        try {
            ResultSet rs = SQL.selectSQL(getIDReservation);

            while (rs.next()) {
                options.add(rs.getString("reservation_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return options;
    }

    public ObservableList<String> get_extra_services() {
        ObservableList<String> options = FXCollections.observableArrayList();

        String getExtraService = "SELECT * FROM extra_services";

        try {
            ResultSet rs = SQL.selectSQL(getExtraService);

            while (rs.next()) {
                options.add(rs.getString("service_name"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return options;
    }

    public int get_id_service(String name_service) {
        String getExtraService = "SELECT * FROM extra_services where service_name='" + name_service + "'";
        int id_service = 0;

        try {
            ResultSet rs = SQL.selectSQL(getExtraService);

            if (rs.next()) {
                id_service = rs.getInt("service_id");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return id_service;
    }
}
