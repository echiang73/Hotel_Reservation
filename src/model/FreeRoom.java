package model;

import static model.AnsiiColor.*;

public class FreeRoom extends Room {
    public FreeRoom(String roomNumber, RoomType enumeration) {
        super(roomNumber, 0d, enumeration);
        super.isFree = true;
    }

    @Override
    public String toString() {
        return "Room: " + GREEN + roomNumber + RESET + ", Price: " + GREEN + "free" + RESET + ", Room Type: " + GREEN + enumeration + RESET;
    }
}
