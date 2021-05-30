package Reservations;

public class Reservation {
    public String reservation_id;
    public String type_reservation;
    public String name_client;
    public String name_user;
    public String room_id;
    public String reservation_date;
    public String date_arrival;
    public String date_departure;
    public String status;

    public Reservation(String reservation_id, String type_reservation, String name_client, String name_user, String room_id, String reservation_date, String date_arrival, String date_departure, String status) {
        this.reservation_id = reservation_id;
        this.type_reservation = type_reservation;
        this.name_client = name_client;
        this.name_user = name_user;
        this.room_id = room_id;
        this.reservation_date = reservation_date;
        this.date_arrival = date_arrival;
        this.date_departure = date_departure;
        this.status = status;
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getType_reservation() {
        return type_reservation;
    }

    public void setType_reservation(String type_reservation) {
        this.type_reservation = type_reservation;
    }

    public String getName_client() {
        return name_client;
    }

    public void setName_client(String name_client) {
        this.name_client = name_client;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getReservation_date() {
        return reservation_date;
    }

    public void setReservation_date(String reservation_date) {
        this.reservation_date = reservation_date;
    }

    public String getDate_arrival() {
        return date_arrival;
    }

    public void setDate_arrival(String date_arrival) {
        this.date_arrival = date_arrival;
    }

    public String getDate_departure() {
        return date_departure;
    }

    public void setDate_departure(String date_departure) {
        this.date_departure = date_departure;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
