package Reservations;

import dbUtil.SQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewCancelledController {
    @FXML
    private TableView reservations_tableview;
    @FXML
    private Button undo_cancel_button;

    ObservableList<Integer> id_reservations = FXCollections.observableArrayList();
    ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    ObservableList<Integer> id_rooms = FXCollections.observableArrayList();

    Reservation reservation;

    Alert alert = new Alert(Alert.AlertType.ERROR);

    public void initialize(){
        load_reservations();
    }

    public void load_reservations(){
        id_reservations = get_cancelled_reservations();
        id_rooms = get_cancelled_rooms();

        String id_res="";
        String type_res = "";
        String name_cl = "";
        String name_rec;
        String id_ro="";
        String date_res = "";
        String arr = "";
        String dep = "";
        String stat;

        for(int i=0; i< id_reservations.size();i++){
            id_res = Integer.toString(id_reservations.get(i));
            type_res = get_type_reservation(id_reservations.get(i));
            name_cl = get_name_client(id_reservations.get(i));
            name_rec = get_name_receptionist(id_reservations.get(i));
            id_ro = Integer.toString(id_rooms.get(i));
            date_res = get_date_reservation(id_reservations.get(i));
            arr = get_arrival_date(id_reservations.get(i));
            dep = get_departure_date(id_reservations.get(i));
            stat = get_status(id_reservations.get(i), id_rooms.get(i));
            reservation = new Reservation(id_res, type_res,name_cl,name_rec,id_ro,date_res,arr,dep,stat);
            reservations.add(reservation);
        }

        TableColumn<Reservation,String> id_reservation_tablecolumn = new TableColumn("???");
        id_reservation_tablecolumn.setMinWidth(50);
        id_reservation_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("reservation_id"));

        TableColumn<Reservation,String> type_reservation_tablecolumn = new TableColumn("Type");
        type_reservation_tablecolumn.setMinWidth(100);
        type_reservation_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("type_reservation"));

        TableColumn<Reservation,String> name_client_tablecolumn = new TableColumn("Client");
        name_client_tablecolumn.setMinWidth(150);
        name_client_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("name_client"));

        TableColumn<Reservation,String> name_receptionist_tablecolumn = new TableColumn("Receptionist");
        name_receptionist_tablecolumn.setMinWidth(150);
        name_receptionist_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("name_user"));

        TableColumn<Reservation,String> id_room_tablecolumn = new TableColumn("Room");
        id_room_tablecolumn.setMinWidth(50);
        id_room_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("room_id"));

        TableColumn<Reservation,String> date_reservation_tablecolumn = new TableColumn("Date Reservation");
        date_reservation_tablecolumn.setMinWidth(130);
        date_reservation_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("reservation_date"));

        TableColumn<Reservation,String> arrival_date_tablecolumn = new TableColumn("Arrival Date");
        arrival_date_tablecolumn.setMinWidth(100);
        arrival_date_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("date_arrival"));

        TableColumn<Reservation,String> departure_date_tablecolumn = new TableColumn("Departure Date");
        departure_date_tablecolumn.setMinWidth(120);
        departure_date_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("date_departure"));

        TableColumn<Reservation,String> status_tablecolumn = new TableColumn("Status");
        status_tablecolumn.setMinWidth(100);
        status_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        reservations_tableview.getColumns().addAll(id_reservation_tablecolumn, type_reservation_tablecolumn, name_client_tablecolumn, name_receptionist_tablecolumn, id_room_tablecolumn, date_reservation_tablecolumn, arrival_date_tablecolumn, departure_date_tablecolumn,status_tablecolumn);
        reservations_tableview.setItems(reservations);
    }

    public void undo_cancel_button_on_action(ActionEvent event){
        if(reservations_tableview.getSelectionModel().isEmpty()){
            alert.setTitle("INPUT ERROR");
            alert.setHeaderText(null);
            alert.setContentText("You must choose reservation first!");
            alert.showAndWait();
        }else {
            Reservation row = (Reservation) reservations_tableview.getSelectionModel().getSelectedItem();
            int room = Integer.parseInt(row.getRoom_id());
            int res = Integer.parseInt(row.getReservation_id());

            boolean result = set_undo_cancel(res, room);
            if (result) {
                JOptionPane.showMessageDialog(null, "Cancelled removed!");
                id_reservations.remove(row.getReservation_id());
                id_rooms.remove(row.getRoom_id());
                reservations.remove(row);
            } else
                JOptionPane.showMessageDialog(null, "There was an error! Please try again!");

            Stage stage = (Stage) undo_cancel_button.getScene().getWindow();
            stage.close();
        }
    }

    public ObservableList<Integer> get_cancelled_reservations() {
        ObservableList<Integer> options = FXCollections.observableArrayList();

        String getReservations = "SELECT * FROM room_reservation where reservation_status_id=4";

        try {
            ResultSet rs = SQL.selectSQL(getReservations);

            while (rs.next()) {
                options.add(rs.getInt("reservation_id"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return options;
    }

    public ObservableList<Integer> get_cancelled_rooms() {
        ObservableList<Integer> options = FXCollections.observableArrayList();

        String getRooms = "SELECT * FROM room_reservation where reservation_status_id=4";

        try {
            ResultSet rs = SQL.selectSQL(getRooms);

            while (rs.next()) {
                options.add(rs.getInt("room_id"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return options;
    }

    public String get_type_reservation(int id_reservation) {
        String getID_typeres = "SELECT * FROM reservation_types tr join reservations res on res.id_type_reservation=tr.id_type_reservation where res.reservation_id='" + id_reservation + "'";
        String name_type_res = "";

        try {
            ResultSet rs = SQL.selectSQL(getID_typeres);

            if (rs.next()) {
                name_type_res = rs.getString("name_type_reservation");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return name_type_res;
    }

    public String get_name_client(int id_reservation) {
        String getName_client = "SELECT * FROM clients c join reservations res on res.id_client=c.id_client where res.reservation_id='" + id_reservation + "'";
        String name_client = "";

        try {
            ResultSet rs = SQL.selectSQL(getName_client);

            if (rs.next()) {
                name_client = rs.getString("name_client");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return name_client;
    }

    public String get_name_receptionist(int id_reservation) {
        String getName_receptionist = "SELECT * FROM users u join reservations res on res.user_id=u.user_id where res.reservation_id='" + id_reservation + "'";
        String name_receptionist = "";

        try {
            ResultSet rs = SQL.selectSQL(getName_receptionist);

            if (rs.next()) {
                name_receptionist = rs.getString("user_name");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return name_receptionist;
    }

    public String get_date_reservation(int id_reservation) {
        String getDateRes = "SELECT * FROM reservations where reservation_id='" + id_reservation + "'";
        String date_res = "";

        try {
            ResultSet rs = SQL.selectSQL(getDateRes);

            if (rs.next()) {
                date_res = rs.getString("reservation_date");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return date_res;
    }

    public String get_arrival_date(int id_reservation) {
        String getArrivalDate = "SELECT * FROM room_reservation where reservation_id='" + id_reservation + "'";
        String arrival = "";

        try {
            ResultSet rs = SQL.selectSQL(getArrivalDate);

            if (rs.next()) {
                arrival = rs.getString("date_arrival");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return arrival;
    }

    public String get_departure_date(int id_reservation) {
        String getDepartureDate = "SELECT * FROM room_reservation where reservation_id='" + id_reservation + "'";
        String departure = "";

        try {
            ResultSet rs = SQL.selectSQL(getDepartureDate);

            if (rs.next()) {
                departure = rs.getString("date_departure");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return departure;
    }

    public String get_status(int id_reservation, int id_room) {
        String getStatus = "SELECT * FROM reservation_status rs join room_reservation rr on rr.reservation_status_id=rs.reservation_status_id where rr.reservation_id='" + id_reservation + "' AND rr.room_id='" + id_room + "'";
        String status = "";

        try {
            ResultSet rs = SQL.selectSQL(getStatus);

            if (rs.next()) {
                status = rs.getString("reservation_status_name");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return status;
    }


    public boolean set_undo_cancel(int res, int room) {
        boolean result = false;

        String undoCheckinReservation = "UPDATE room_reservation SET reservation_status_id=1 where reservation_id='" + res + "' AND room_id='" + room + "'";

        try {
            if (SQL.updateSQL(undoCheckinReservation))
                result = true;
            else
                result = false;
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return result;
    }
}
