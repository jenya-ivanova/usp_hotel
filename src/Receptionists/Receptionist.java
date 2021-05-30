package Receptionists;

public class Receptionist {
    int user_id;
    String username;
    String password;
    String user_name;
    String user_phone;

    public Receptionist(){}

    public Receptionist(int user_id, String username, String password, String user_name, String user_phone) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.user_name = user_name;
        this.user_phone = user_phone;
    }

    public Receptionist(int user_id, String user_name, String user_phone) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
}
