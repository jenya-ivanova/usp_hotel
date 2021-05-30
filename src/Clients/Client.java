package Clients;

public class Client {
    public int id_client;
    public String name_client;
    public String address_client;
    public String phone_client;

    public String name_cl;
    public String number_reservations;
    public String number_services;
    public String rating;

    public Client(){}

    public Client(int id_client, String name_client, String address_client, String phone_client) {
        this.id_client = id_client;
        this.name_client = name_client;
        this.address_client = address_client;
        this.phone_client = phone_client;
    }

    public int getId_client() {
        return this.id_client;
    }

    public String getName_client() {
        return this.name_client;
    }

    public String getAddress_client() {
        return this.address_client;
    }

    public String getPhone_client() {
        return this.phone_client;
    }

    public void setId_client(int id_client){
        if(id_client>=0)
            this.id_client=id_client;
        else
            throw new IllegalArgumentException("ID must be greater than or equal to 0");
    }
    public void setName_client(String name_client){
        if(name_client.length()>3)
            this.name_client=name_client;
        else
            throw new IllegalArgumentException("Name must at least 4 characters");
    }
    public void setAddress_client(String address_client){
        if(address_client.length()>3)
            this.address_client=address_client;
        else
            throw new IllegalArgumentException("Address must be at least 4 characters");
    }
    public void setPhone_client(String phone_client){
        if(phone_client.length()>7)
            this.phone_client=phone_client;
        else
            throw new IllegalArgumentException("Phone number must be at least 8 characters");
    }

    public Client(String n, String nr, String ns, String r) {
        this.name_cl = n;
        this.number_reservations = nr;
        this.number_services = ns;
        this.rating = r;
    }

    public String getName_cl() {
        return name_cl;
    }

    public String getNumber_reservations() {
        return number_reservations;
    }

    public String getNumber_services() {
        return number_services;
    }

    public String getRating() {
        return rating;
    }
}
