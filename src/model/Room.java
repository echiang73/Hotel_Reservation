package model;

import static model.AnsiiColor.GREEN;
import static model.AnsiiColor.RESET;

public class Room implements IRoom, Comparable<Room> {
    protected String roomNumber;
    protected Double price;
    protected RoomType enumeration;
    protected boolean isFree = false;

    public Room(String roomNumber, Double price, RoomType enumeration) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }

    @Override
    public final String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public final Double getRoomPrice() {
        return price;
    }

    @Override
    public final RoomType getRoomType() {
        return enumeration;
    }

    @Override
    public final boolean isFree() {
        return isFree;
    }

    @Override
    public String toString() {
        return "Room: " + GREEN + roomNumber + RESET +
                ", Price: " + GREEN + (isFree ? "free" : "$" + price) + RESET +
                ", Room Type: " + GREEN + enumeration + RESET;
    }

    @Override
    public final int compareTo(Room room) {
        return roomNumber.compareTo(room.roomNumber);
    }
}
