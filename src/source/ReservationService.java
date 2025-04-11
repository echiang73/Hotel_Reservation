package source;

import model.*;

import java.text.SimpleDateFormat;
import java.util.*;

import static model.AnsiiColor.*;

/**
 * The service layer of the hotel application storing Reservation information
 *
 * @author eddie.chiang
 * @version 1.0
 */
public class ReservationService {

    private static Set<Reservation> reservations = new HashSet<>();
    private static Map<String, Room> rooms = new HashMap<>();

    public static Map<String, Room> getRooms() {
        return rooms;
    }

    public static void addRoom(IRoom room) {
        String roomNum = room.getRoomNumber();
        double price = room.getRoomPrice();
        RoomType roomType = room.getRoomType();

        Room roomToAdd = price == 0 ? new FreeRoom(roomNum, roomType) : new Room(roomNum, price, roomType);
        rooms.put(room.getRoomNumber(), roomToAdd);
    }

    public static IRoom getARoom(String roomId) {
        for (IRoom room : rooms.values()) {
            if (room.getRoomNumber().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    public static Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        // ensures that a Customer is added to Customer list
        if (CustomerService.getCustomer(customer.getEmail()) == null) {
            CustomerService.addCustomer(customer.getEmail(), customer.getFirstName(), customer.getLastName());
        }
        IRoom selectedRoom = getARoom(room.getRoomNumber());
        Reservation reservation = new Reservation(customer, selectedRoom, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }


    private static List<String> convertDateToString(Date checkInDate, Date checkOutDate) {
        List<String> list = new ArrayList<>();
        long diff = checkOutDate.getTime() - checkInDate.getTime();
        long daysReserved = (diff / (1000*60*60*24));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);

        for (int i = 1; i <= daysReserved; i++) {
            String dateStr = dateFormat.format(calendar.getTime());
            list.add(dateStr);
            calendar.add(Calendar.DATE, 1);
        }

        return list;
    }

    public static Map<String, Room> findRooms(Date checkInDate, Date checkOutDate) {
        Map<String, Room> roomsAvailable = new HashMap<>(rooms);
        List<String> datesToReserve = convertDateToString(checkInDate, checkOutDate);

        for (Reservation reservation : reservations) {
            List<String> datesAlreadyReserved = convertDateToString(reservation.getCheckInDate(), reservation.getCheckOutDate());
            // boolean anyMatch = datesAlreadyReserved.stream().anyMatch(d -> datesToReserve.contains(d));
            boolean anyMatch = datesAlreadyReserved.stream().anyMatch(datesToReserve::contains);
            // boolean anyMatch = !Collections.disjoint(datesToReserve, datesAlreadyReserved);

            if (anyMatch) {
                if (reservation.getRoom() != null) {
                    roomsAvailable.remove(reservation.getRoom().getRoomNumber());
                }
            }
        }
        return roomsAvailable;
    }

    public static HashSet<Reservation> getCustomersReservation(Customer customer) {
        HashSet<Reservation> customerReservation = new HashSet<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer() != null && reservation.getCustomer().getEmail().equals(customer.getEmail())) {
                customerReservation.add(reservation);
            }
        }
        return customerReservation;
    }

    public static void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println(RED + "\nNo Reservations in the system." + RESET);
            return;
        }

        List<Reservation> list = new ArrayList<>(reservations);
        Collections.sort(list);
        System.out.println("\nList of Reservations");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i));
        }
    }
}
