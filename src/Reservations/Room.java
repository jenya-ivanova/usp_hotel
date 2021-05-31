package Reservations;

public class Room {
    String room_id;
    String name_type_room;
    String usage;

    public Room(String room_id, String name_type_room, String usage) {
        this.room_id = room_id;
        this.name_type_room = name_type_room;
        this.usage = usage;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getName_type_room() {
        return name_type_room;
    }

    public void setName_type_room(String name_type_room) {
        this.name_type_room = name_type_room;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
