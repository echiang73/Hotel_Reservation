package ui;

import api.AdminResource;
import model.*;
import source.CustomerService;

import java.util.*;

import static model.AnsiiColor.*;
import static ui.MainMenu.clearScreen;
import static ui.MainMenu.input;

/**
 * The administration menu to navigate through the maintenance operations for the hotel reservation application
 *
 * @author eddie.chiang
 * @version 1.0
 */
public class AdminMenu {

    private static String adminMenu =
            "\nAdministration Menu\n" +
                    "-".repeat(20) + """

                    1. See all Customers
                    2. See all Rooms
                    3. See all Reservations
                    4. Add a Room
                    5. Add Test Data
                    6. Back to Main Menu
                    """ +
                    "-".repeat(20) +
                    "\nPlease select a number for the menu option";

    public static void getAdminMenu() {
        System.out.println(adminMenu);
        String selectedOption = input.nextLine();
        boolean repeat;

        do {
            repeat = false;
            clearScreen();
            switch (selectedOption) {
                case "1" -> displayAllCustomers();
                case "2" -> displayAllRooms();
                case "3" -> displayAllReservations();
                case "4" -> addRoom();
                case "5" -> TestDataMenu.getAddTestDataMenu();
                case "6" -> MainMenu.getMenu();
                default -> {
                    System.out.println(adminMenu);
                    System.out.println(RED + "Invalid, please enter a number from 1 to 6" + RESET);
                    selectedOption = input.nextLine();
                    repeat = true;
                }
            }
        } while (repeat);
    }

    protected static void displayAllCustomers() {
        displayAllCustomers(false);
    }
    protected static void displayAllCustomers(boolean isTestData) {
        String asterisk = "*".repeat(15);
        String title = GREEN + "\n" + asterisk + " List of All Customers " + asterisk + "\n" + RESET;
        System.out.println(title);

        Map<String, Customer> customers = CustomerService.getAllCustomers();
        List<Customer> customersList = new ArrayList<>(customers.values());
        customersList.sort(Comparator.comparing(Customer::getLastName).thenComparing(Customer::getFirstName));

        if (customersList.isEmpty()) {
            System.out.println(RED + "\nNo Customers in the system." + RESET);
        } else {
            int i = 1;

            for (Customer customer : customersList) {
                System.out.println(i + ". " + customer);
                i++;
            }
        }
        if (isTestData) {
            TestDataMenu.getAddTestDataMenu();
        } else {
            AdminMenu.getAdminMenu();
        }
    }

    protected static void displayAllRooms() {
        displayAllRooms(false);
    }

    protected static void displayAllRooms(boolean isTestData) {
        String asterisk = "*".repeat(17);
        String title = GREEN + "\n" + asterisk + " List of All Rooms " + asterisk + "\n" + RESET;
        System.out.println(title);

        Map<String, Room> rooms = AdminResource.getAllRooms();
        List<Room> roomsList = new ArrayList<>(rooms.values());
        roomsList.sort(Comparator.comparing(Room::getRoomNumber));
        if (roomsList.isEmpty()) {
            System.out.println(RED + "\nNo Rooms in the system." + RESET);
        } else {
            int i = 1;

            for (Room room : roomsList) {
                System.out.println(i + ". " + room);
                i++;
            }
        }

        if (isTestData) {
            TestDataMenu.getAddTestDataMenu();
        } else {
            AdminMenu.getAdminMenu();
        }
    }

    private static void displayAllReservations() {
        displayAllReservations(false);
    }
    private static void displayAllReservations(boolean isTestData) {
        String asterisk = "*".repeat(14);
        String title = GREEN + "\n" + asterisk + " List of All Reservations " + asterisk + "\n" + RESET;
        System.out.println(title);
        AdminResource.displayAllReservations();
        if (isTestData) {
            TestDataMenu.getAddTestDataMenu();
        } else {
            AdminMenu.getAdminMenu();
        }
    }

    private static void addRoom() {
        String asterisk = "*".repeat(21);
        String title = GREEN + "\n" + asterisk + " Add a Room " + asterisk + "\n" + RESET;
        System.out.println(title);

        final String ROOM_NUM_MSG = "Enter room number: ";
        final String PRICE_MSG = "Enter price per night: ";
        final String PRICE_ERROR_MSG = RED + "Invalid number. " + PRICE_MSG + RESET + "$";
        final String PRICE_NEG_ERROR_MSG = RED + "Invalid, negative number. " + PRICE_MSG + RESET + "$";
        final String ROOM_TYPE_MSG = "Enter room type: 1 for single bed, 2 for double bed: ";
        final String ROOM_TYPE_ERROR_MSG = RED + "Invalid. " + ROOM_TYPE_MSG + RESET;
        final String REPEAT_ADD_ROOM_MSG = "\nWould you like to add another room? Y/N ";
        final String REPEAT_ADD_ROOM_ERROR_MSG = RED + "Invalid. Would you like to add another room? Y/N " + RESET;

        List<IRoom> rooms = new ArrayList<>();
        boolean repeat;
        boolean repeatAddRoom = false;
        String roomNum;
        double price = 0;
        RoomType roomType = null;
        IRoom room;

        do {
            System.out.print("\n" + ROOM_NUM_MSG);
            roomNum = input.nextLine().trim();

            System.out.print(PRICE_MSG + "$");
            do {
                repeat = false;
                try {
                    price = Double.parseDouble(input.nextLine().trim());
                    if (price < 0) {
                        System.out.print(PRICE_NEG_ERROR_MSG);
                        repeat = true;
                    }
                } catch (NumberFormatException ex) {
                    System.out.print(PRICE_ERROR_MSG);
                    repeat = true;
                }
            } while (repeat);

            System.out.print(ROOM_TYPE_MSG);
            do {
                repeat = false;
                String roomTypeStr = input.nextLine().trim();
                if (roomTypeStr.equals("1")) {
                    roomType = RoomType.SINGLE;
                } else if (roomTypeStr.equals("2")) {
                    roomType = RoomType.DOUBLE;
                } else {
                    System.out.print(ROOM_TYPE_ERROR_MSG);
                    repeat = true;
                }
            } while (repeat);

            room = (price == 0) ? new FreeRoom(roomNum, roomType) : new Room(roomNum, price, roomType);
            rooms.add(room);
            System.out.println("\nRoom added: " + room);

            System.out.println(REPEAT_ADD_ROOM_MSG);
            do {
                repeatAddRoom = false;
                repeat = false;
                String yesOrNo = input.nextLine().toLowerCase().trim();
                if (yesOrNo.equals("y")) {
                    repeatAddRoom = true;
                } else if (yesOrNo.equals("n")) {
                    clearScreen();
                    AdminResource.addRoom(rooms);
                } else {
                    System.out.print(REPEAT_ADD_ROOM_ERROR_MSG);
                    repeat = true;
                }
            } while (repeat);
        } while (repeatAddRoom);

        AdminMenu.getAdminMenu();
    }
}
