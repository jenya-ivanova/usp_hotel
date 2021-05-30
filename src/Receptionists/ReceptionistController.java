package Receptionists;

import AddReservation.AddReservationController;
import Clients.AddClientController;
import ExtraServices.AddExtraServiceController;
import Reservations.*;
import dbUtil.SQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import static javafx.scene.layout.BackgroundPosition.CENTER;
import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;

public class ReceptionistController {
    @FXML
    private Button add_reservation_button;
    @FXML
    private Button add_client_button;
    @FXML
    private Button add_extra_service_button;
    @FXML
    private Button view_reservations_button;
    @FXML
    private Button view_arrivals_button;
    @FXML
    private Button view_departures_button;
    @FXML
    private Button view_departed_button;
    @FXML
    private Button view_cancelled_button;
    @FXML
    private Button logout_button;
    @FXML
    private ComboBox months_combobox;
    @FXML
    private ImageView imageView;
    @FXML
    private TableView tableView;
    @FXML
    private AnchorPane ap;

    Reservation reservation;
    Room room;

    ObservableList<Integer> id_reservations = FXCollections.observableArrayList();
    ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    ObservableList<Integer> id_rooms = FXCollections.observableArrayList();

    ObservableList<Integer> id_rooms_usage = FXCollections.observableArrayList();
    ObservableList<Room> rooms_usage = FXCollections.observableArrayList();

    public void initialize() throws IOException {
        Image image = new Image(new FileInputStream("C:/Users/HP/Pictures/hotel/ocean.png"));
        imageView.setImage(image);

        Image image1 = new Image(new FileInputStream("C:/Users/HP/Pictures/hotel/blue.jpg"));
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        ap.setBackground(new Background(new BackgroundImage(image1,NO_REPEAT, NO_REPEAT, CENTER, backgroundSize)));

        ObservableList<String> months = FXCollections.observableArrayList("January","February","March","April","May","June","July","August","September","October","November","December");
        months_combobox.setItems(months);
    }
    public void clear_tableview(){
        tableView.getItems().clear();
        tableView.getColumns().clear();
        id_reservations.clear();
        reservations.clear();
        id_rooms.clear();
    }

    public void addReservationButton(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AddReservation/addReservation.fxml"));
            Stage registerStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            AddReservationController addReservationController = (AddReservationController) loader.getController();
            registerStage.setTitle("Add Reservation");
            registerStage.setScene(new Scene(root, 700, 600));
            registerStage.show();
            root.requestFocus();
        } catch (Exception ex) {
            System.out.println("Error loading FXML!");
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    public void add_client_button_on_action(ActionEvent event){
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

    public void add_extra_service_button_on_action(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ExtraServices/add_extra_service.fxml"));
            Stage registerStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            AddExtraServiceController addExtraServiceController = (AddExtraServiceController) loader.getController();
            registerStage.setTitle("Add Extra Service");
            registerStage.setScene(new Scene(root, 600, 400));
            registerStage.show();
            root.requestFocus();
        } catch (Exception ex) {
            System.out.println("Error loading FXML!");
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    public void view_reservations_button_on_action(ActionEvent event){
        clear_tableview();

        id_reservations = get_all_reservations();
        id_rooms = get_all_rooms();

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
            stat = get_status(id_reservations.get(i));
            reservation = new Reservation(id_res, type_res,name_cl,name_rec,id_ro,date_res,arr,dep,stat);
            reservations.add(reservation);
        }

        TableColumn<Reservation,String> id_reservation_tablecolumn = new TableColumn("№");
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

        tableView.getColumns().addAll(id_reservation_tablecolumn, type_reservation_tablecolumn, name_client_tablecolumn, name_receptionist_tablecolumn, id_room_tablecolumn, date_reservation_tablecolumn, arrival_date_tablecolumn, departure_date_tablecolumn,status_tablecolumn);
        tableView.setItems(reservations);
    }

    public void view_arrivals_button_on_action(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Reservations/view_arrivals.fxml"));
            Stage registerStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            ViewArrivalsController viewArrivalsController = (ViewArrivalsController) loader.getController();
            registerStage.setTitle("Arrivals");
            registerStage.setScene(new Scene(root, 1000, 700));
            registerStage.show();
            root.requestFocus();
        } catch (Exception ex) {
            System.out.println("Error loading FXML!");
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    public void view_departures_button_on_action(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Reservations/view_departures.fxml"));
            Stage registerStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            ViewDeparturesController viewDeparturesController = (ViewDeparturesController) loader.getController();
            registerStage.setTitle("Departures");
            registerStage.setScene(new Scene(root, 1000, 700));
            registerStage.show();
            root.requestFocus();
        } catch (Exception ex) {
            System.out.println("Error loading FXML!");
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    public void view_departed_button_on_action(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Reservations/view_departed.fxml"));
            Stage registerStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            ViewDepartedController viewDepartedController = (ViewDepartedController) loader.getController();
            registerStage.setTitle("Departed");
            registerStage.setScene(new Scene(root, 1000, 700));
            registerStage.show();
            root.requestFocus();
        } catch (Exception ex) {
            System.out.println("Error loading FXML!");
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    public void view_cancelled_button_on_action(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Reservations/view_cancelled.fxml"));
            Stage registerStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            ViewCancelledController viewCancelledController = (ViewCancelledController) loader.getController();
            registerStage.setTitle("Cancelled");
            registerStage.setScene(new Scene(root, 1000, 700));
            registerStage.show();
            root.requestFocus();
        } catch (Exception ex) {
            System.out.println("Error loading FXML!");
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    public void view_button_on_action(ActionEvent event) {
        clear_tableview();

        String get_room = "";
        String month = months_combobox.getValue().toString();

        switch (month) {
            case "January":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-01-01'AND '2021-01-31') group by room_id";
                break;
            case "February":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-02-01'AND '2021-02-28') group by room_id";
                break;
            case "March":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-03-01'AND '2021-03-31') group by room_id";
                break;
            case "April":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-04-01'AND '2021-04-30') group by room_id";
                break;
            case "May":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-05-01'AND '2021-05-31') group by room_id";
                break;
            case "June":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-06-01'AND '2021-06-30') group by room_id";
                break;
            case "July":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-07-01'AND '2021-07-31') group by room_id";
                break;
            case "August":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-08-01'AND '2021-08-31') group by room_id";
                break;
            case "September":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-09-01'AND '2021-09-30') group by room_id";
                break;
            case "October":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-10-01'AND '2021-10-31') group by room_id";
                break;
            case "November":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-11-01'AND '2021-11-30') group by room_id";
                break;
            case "December":
                get_room = "SELECT * from room_reservation where (date_arrival BETWEEN '2021-12-01'AND '2021-12-31') group by room_id";
                break;
        }

        try {
            ResultSet query = SQL.selectSQL(get_room);

            while(query.next()){
                if(id_rooms_usage.contains(query.getInt("room_id"))){

                }
                else{
                    id_rooms_usage.add(query.getInt("room_id"));
                }
            }
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }

        show_rooms_usage();
    }

    public ObservableList<Integer> get_all_reservations() {
        ObservableList<Integer> options = FXCollections.observableArrayList();

        String getReservations = "SELECT * FROM room_reservation";

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
    public ObservableList<Integer> get_all_rooms() {
        ObservableList<Integer> options = FXCollections.observableArrayList();

        String getRooms = "SELECT * FROM room_reservation";

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
    public String get_status(int id_reservation) {
        String getStatus = "SELECT * FROM reservation_status rs join room_reservation rr on rr.reservation_status_id=rs.reservation_status_id where rr.reservation_id='" + id_reservation + "'";
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


    public int get_count(int room_id){
        String get_count = "";
        String month = months_combobox.getValue().toString();

        switch (month) {
            case "January":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-01-01'AND '2021-01-31') AND room_id='" + room_id + "'";
                break;
            case "February":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-02-01'AND '2021-02-28') AND room_id='" + room_id + "'";
                break;
            case "March":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-03-01'AND '2021-03-31') AND room_id='" + room_id + "'";
                break;
            case "April":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-04-01'AND '2021-04-30') AND room_id='" + room_id + "'";
                break;
            case "May":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-05-01'AND '2021-05-31') AND room_id='" + room_id + "'";
                break;
            case "June":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-06-01'AND '2021-06-30') AND room_id='" + room_id + "'";
                break;
            case "July":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-07-01'AND '2021-07-31') AND room_id='" + room_id + "'";
                break;
            case "August":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-08-01'AND '2021-08-31') AND room_id='" + room_id + "'";
                break;
            case "September":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-09-01'AND '2021-09-30') AND room_id='" + room_id + "'";
                break;
            case "October":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-10-01'AND '2021-10-31') AND room_id='" + room_id + "'";
                break;
            case "November":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-11-01'AND '2021-11-30') AND room_id='" + room_id + "'";
                break;
            case "December":
                get_count = "SELECT count(room_id) as total from room_reservation where (date_arrival BETWEEN '2021-12-01'AND '2021-12-31') AND room_id='" + room_id + "'";
                break;
        }

        int count = 0;
        try {
            ResultSet query = SQL.selectSQL(get_count);

            if(query.next()){
                count = query.getInt("total");
            }
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return count;
    }

    public String get_name_type_room(int room_id){
        String name_type_room = "";

        String get_name_type_room = "Select * from rooms r join room_types rt on rt.room_type_id=r.room_type_id where r.room_id='" + room_id + "'";

        try {
            ResultSet query = SQL.selectSQL(get_name_type_room);

            if(query.next()) {
                name_type_room = query.getString("room_type_name");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
            e.getCause();
        }
        return name_type_room;
    }

    public void show_rooms_usage(){
        String id_ro = "";
        String type_ro = "";
        String count = "";

        for(int i=0; i< id_rooms_usage.size();i++){
            id_ro = Integer.toString(id_rooms_usage.get(i));
            type_ro = get_name_type_room(id_rooms_usage.get(i));
            count = Integer.toString(get_count(id_rooms_usage.get(i)));

            room = new Room(id_ro, type_ro, count);
            rooms_usage.add(room);
        }

        TableColumn<Reservation,String> id_room_tablecolumn = new TableColumn("Room №");
        id_room_tablecolumn.setMinWidth(70);
        id_room_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("room_id"));

        TableColumn<Reservation,String> type_room_tablecolumn = new TableColumn("Room Type");
        type_room_tablecolumn.setMinWidth(120);
        type_room_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("name_type_room"));

        TableColumn<Reservation,String> usage_tablecolumn = new TableColumn("Usage");
        usage_tablecolumn.setMinWidth(100);
        usage_tablecolumn.setCellValueFactory(
                new PropertyValueFactory<>("usage"));

        tableView.getColumns().addAll(id_room_tablecolumn, type_room_tablecolumn, usage_tablecolumn);
        tableView.setItems(rooms_usage);
    }

    public void logout_button_on_action(ActionEvent event)throws IOException {
        Stage stage1 = (Stage) logout_button.getScene().getWindow();
        stage1.close();
        Parent root = FXMLLoader.load(getClass().getResource("/Login/login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
