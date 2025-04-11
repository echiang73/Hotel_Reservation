package ui;

import api.AdminResource;
import api.HotelResource;
import model.*;
import source.ReservationService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static model.AnsiiColor.*;
import static ui.MainMenu.clearScreen;
import static ui.MainMenu.input;

/**
 * The test data menu to add customers, rooms, and reservations.
 *
 * @author eddie.chiang
 * @version 1.0
 */
public class TestDataMenu {

    private static String testDataMenu =
            "\nAdd Test Data Menu\n" +
                    "-".repeat(20) + """

            1. Add Customers Test Data
            2. Add Rooms Test Data
            3. Add Reservations Test Data
            4. Back to Admin Menu
            """ +
                    "-".repeat(20) +
                    "\nPlease select a number for the menu option";

    protected static void getAddTestDataMenu() {
        System.out.println(testDataMenu);
        String selectedOption = input.nextLine();
        boolean repeat;

        do {
            repeat = false;
            clearScreen();
            switch (selectedOption) {
                case "1" -> addCustomersTestData();
                case "2" -> addRoomsTestData();
                case "3" -> addReservationTestData();
                case "4" -> AdminMenu.getAdminMenu();
                default -> {
                    System.out.println(testDataMenu);
                    System.out.println(RED + "Invalid, please enter a number from 1 to 4" + RESET);
                    selectedOption = input.nextLine();
                    repeat = true;
                }
            }
        } while (repeat);
    }

    private static final void addRoomsTestData() {
        addRoomsSampleData();
        System.out.println(GREEN + "Test Data: 4 Rooms added, Room number format " + RED + "T***S/D" + RESET);
        AdminMenu.displayAllRooms(true);
    }

    private static final void addRoomsSampleData() {
        List<IRoom> rooms = new ArrayList<>();

        Room room1 = new Room("T100$S", 199.0, RoomType.SINGLE);
        Room room2 = new Room("T101$D", 399.0, RoomType.DOUBLE);
        Room room3 = new FreeRoom("T102FS", RoomType.SINGLE);
        Room room4 = new FreeRoom("T103FD", RoomType.DOUBLE);
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        rooms.add(room4);

        AdminResource.addRoom(rooms);
    }

    private static final void addCustomersTestData() {
        addCustomersSampleData();
        System.out.println(GREEN + "Test Data: 4 Customers added" + RESET);
        AdminMenu.displayAllCustomers(true);
    }

    private static final void addCustomersSampleData() {
        HotelResource.createACustomer("eddie@domain.com", "Eddie", "Chiang");
        HotelResource.createACustomer("neo@matrix.com", "Thomas", "Anderson");
        HotelResource.createACustomer("james@oracle.com", "James", "Gosling");
        HotelResource.createACustomer("grace@usnavy.gov", "Grace", "Hopper");
    }

    private static final void addReservationTestData() {
        addReservationSampleData();
        System.out.println(GREEN + "Test Data: 5 Reservations added" + RESET);
        ReservationService.printAllReservation();
        TestDataMenu.getAddTestDataMenu();
    }

    private static final void addReservationSampleData() {
        addRoomsSampleData();
        IRoom room1 = HotelResource.getRoom("T100$S");
        IRoom room2 = HotelResource.getRoom("T101$D");
        IRoom room3 = HotelResource.getRoom("T102FS");
        IRoom room4 = HotelResource.getRoom("T103FD");

        addCustomersSampleData();

        Date date1a = new Date(2025-1900, Calendar.JANUARY,15);
        Date date1b = new Date(2025-1900, Calendar.JANUARY, 18);
        Date date2a = new Date(2025-1900, Calendar.MARCH, 15);
        Date date2b = new Date(2025-1900, Calendar.MARCH, 15);
        Date date3a = new Date(2025-1900, Calendar.MAY,1);
        Date date3b = new Date(2025-1900, Calendar.MAY,7);
        Date date4a = new Date(2025-1900, Calendar.JUNE,29);
        Date date4b = new Date(2025-1900, Calendar.JULY,5);

        HotelResource.bookARoom("eddie@domain.com", room1, date1a, date1b);
        HotelResource.bookARoom("neo@matrix.com", room2, date2a, date2b);
        HotelResource.bookARoom("james@oracle.com", room3, date3a, date3b);
        HotelResource.bookARoom("grace@usnavy.gov", room4, date4a, date4b);
        HotelResource.bookARoom("eddie@domain.com", room4, date3a, date3b);
    }
}
