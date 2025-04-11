package api;

import model.Customer;
import model.IRoom;
import model.Room;
import source.CustomerService;
import source.ReservationService;

import java.util.List;
import java.util.Map;

import static source.ReservationService.*;

public class AdminResource {

    public static Customer getCustomer(String email) {
        return CustomerService.getCustomer(email);
    }

    public static void addRoom(List<IRoom> rooms) {
        for (IRoom room : rooms) {
            ReservationService.addRoom(room);
        }
    }

    public static Map<String, Room> getAllRooms() {
        return ReservationService.getRooms();
    }

    public static void displayAllReservations() {
        printAllReservation();
    }
}
