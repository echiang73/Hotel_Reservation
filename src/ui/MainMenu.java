package ui;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import source.CustomerService;
import source.ReservationService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.AnsiiColor.*;
import static model.Customer.validateEmail;

/**
 * The main menu to navigate through the hotel reservation application to book rooms
 *
 * @author eddie.chiang
 * @version 1.0
 */

public class MainMenu {
    protected static final Scanner input = new Scanner(System.in);
    private static final String welcome = GREEN + " ".repeat(9) + "Welcome to Hotel Oracle Application" + RESET;
    private static final String invalidYN = RED + "Invalid. Please enter Y (Yes) or N (No)." + RESET;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final String logo = GREEN + """
             _   _       _       _    ___                 _     \s
            | | | | ___ | |_ ___| |  / _ \\ _ __ __ _  ___| | ___\s
            | |_| |/ _ \\| __/ _ \\ | | | | | '__/ _` |/ __| |/ _ \\
            |  _  | (_) | ||  __/ | | |_| | | | (_| | (__| |  __/
            |_| |_|\\___/ \\__\\___|_|  \\___/|_|  \\__,_|\\___|_|\\___|
            """ + RESET;
    private static final String menu = "\nMain Menu\n" +
            "-".repeat(20) + """

            1. Find and reserve a room
            2. See my reservations
            3. Create an account
            4. Administration
            5. Exit
            """ +
            "-".repeat(20) +
            "\nPlease select a number for the menu option";

    public static void startApp() {
        displayAppLogo();
        System.out.println(welcome);
        getMenu();
    }
    public static void displayAppLogo() {
        System.out.println(logo);
    }

    public static void getMenu() {
        System.out.println(menu);
        String selectedOption = input.nextLine();
        boolean repeat;

        do {
            repeat = false;
            clearScreen();
            switch (selectedOption) {
                case "1" -> findAndReserveRoom();
                case "2" -> displayReservation();
                case "3" -> createAccountAndGetNewCustomer(null);
                case "4" -> AdminMenu.getAdminMenu();
                case "5" -> {
                    input.close();
                    System.out.println("Goodbye, have a nice day!\n");
                }
                default -> {
                    System.out.println(welcome);
                    System.out.println(menu);
                    System.out.println(RED + "Invalid, please enter a number from 1 to 5" + RESET);
                    selectedOption = input.nextLine();
                    repeat = true;
                }
            }
        } while (repeat);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        displayAppLogo();
    }

    private static void findAndReserveRoom() {
        String asterisk = "*".repeat(15);
        String title = GREEN + "\n" + asterisk + " Search Room to Reserve " + asterisk + RESET;
        System.out.println(title);

        Date checkInDate = getValidatedDate("check-in", getTodayStartOfDay());
        Date checkOutDate = getValidatedDate("check-out", getOneDayLater(checkInDate));
        Date selectedCheckInDate = checkInDate;
        Date selectedCheckOutDate = checkOutDate;
        Date checkInAWeekLater;
        Date checkOutAWeekLater;
        String noRoomMsg = RED + "\nNOTICE: There are no rooms available. Check a few days out? Y/N " + RESET;
        String daysLaterMsg = "How many days later to check for room availability, i.e. 7 days out? ";
        String daysLaterErrorMsg = RED + "\nInvalid. " + daysLaterMsg + RESET;
        boolean repeat;

        Map<String, Room> rooms = ReservationService.findRooms(checkInDate, checkOutDate);
        List<Room> listRooms = new ArrayList<>(rooms.values());

        if (listRooms.isEmpty()) {
            do {
                clearScreen();
                System.out.println(title);
                System.out.print(noRoomMsg);
                String yesOrNo = input.nextLine().toLowerCase().trim();
                int daysLater = 7;
                if (yesOrNo.equals("y")) {
                    do {
                        System.out.print(daysLaterMsg);
                        String searchDaysLater = input.nextLine().trim();
                        repeat = false;
                        try {
                            daysLater = Integer.parseInt(searchDaysLater);
                        } catch (NumberFormatException ex) {
                            System.out.println(daysLaterErrorMsg);
                            repeat = true;
                        }
                    } while (repeat);
                    checkInAWeekLater = getXDaysLater(checkInDate, daysLater);
                    checkOutAWeekLater = getXDaysLater(checkOutDate, daysLater);
                    selectedCheckInDate = checkInAWeekLater;
                    selectedCheckOutDate = checkOutAWeekLater;

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(checkInAWeekLater);
                    String checkInAWeekLaterStr = dateFormat.format(calendar.getTime());
                    calendar.setTime(checkOutAWeekLater);
                    String checkOutAWeekLaterStr = dateFormat.format(calendar.getTime());

                    System.out.println("Searching " + GREEN + daysLater + RESET + " days later, check-in: " + GREEN
                            + checkInAWeekLaterStr + RESET + ", check-out: " + GREEN + checkOutAWeekLaterStr + RESET);
                    rooms = ReservationService.findRooms(checkInAWeekLater, checkOutAWeekLater);
                    listRooms = new ArrayList<>(rooms.values());

                    repeat = false;
                } else if (yesOrNo.equals("n")) {
                    repeat = false;
                } else {
                    System.out.println(invalidYN);
                    repeat = true;
                }
            } while (repeat);
        }

        if (rooms.isEmpty()) {
            System.out.println(RED + "Sorry, no rooms are available still." + RESET);
        } else {
            Collections.sort(listRooms);
            clearScreen();
            System.out.println(title);
            System.out.println("Availability:");
            printRooms(listRooms);
            bookRoom(listRooms, selectedCheckInDate, selectedCheckOutDate);
        }
        MainMenu.getMenu();
    }

    private static void bookRoom(List<Room> listRooms, Date checkInDate, Date checkOutDate) {
        boolean repeat = false;
        IRoom roomSelected = null;
        do {
            System.out.println("\nWould you like to book a room? ");
            String yesOrNo = input.nextLine().toLowerCase().trim();
            if (yesOrNo.equals("y")) {
                Customer customer = inquireAccountGetCustomer();

                if (customer != null) {
                    do {
                        System.out.println("What room would you like to reserve?");
                        String roomNumber = input.nextLine().trim();
                        boolean isRoomOnList = listRooms.stream().anyMatch(r -> r.getRoomNumber().equals(roomNumber));

                        if (isRoomOnList) {
                            roomSelected = ReservationService.getARoom(roomNumber);
                            repeat = false;
                        } else {
                            System.out.println(RED + roomNumber + " is not one of the available rooms.  Please try again." + RESET);
                            repeat = true;
                        }
                    } while (repeat);

                    Reservation bookedReservation = ReservationService.reserveARoom(customer, roomSelected, checkInDate, checkOutDate);
                    clearScreen();
                    System.out.println("Your room has been booked!");
                    System.out.println(bookedReservation);
                }
            } else if (yesOrNo.equals("n")) {
                repeat = false;
            } else {
                System.out.println(invalidYN);
                repeat = true;
            }
        } while (repeat);
    }

    public static Customer inquireAccountGetCustomer() {
        String email;
        Customer customer = null;
        boolean repeat;

        do {
            System.out.println("Do you have an account?");
            String yesOrNo = input.nextLine().toLowerCase().trim();
            if (yesOrNo.equals("y")) {
                email = getValidEmail();
                customer = verifyAndCreateAccount(email);
                repeat = false;
            } else if (yesOrNo.equals("n")) {
                System.out.println("You need to create an account first in order to book a room");
                email = getValidEmail();
                customer = createAccountAndGetNewCustomer(email);
                repeat = false;
            } else {
                System.out.println(invalidYN);
                repeat = true;
            }
        } while (repeat);
        return customer;
    }

    private static String getValidEmail() {
        String email;
        String emailMsg = "Enter email, format name@domain.com";
        String emailErrorMsg = RED + "Invalid. " + emailMsg + RESET;
        boolean repeat;
        do {
            System.out.println(emailMsg);
            email = input.nextLine().trim();
            if (validateEmail(email)) {
                repeat = false;
            } else {
                System.out.println(emailErrorMsg);
                repeat = true;
            }
        }  while (repeat);
        return email;
    }

    public static Customer verifyAndCreateAccount(String email) {
        Customer customer;
        boolean repeat;
        do {
            repeat = false;
            customer = CustomerService.getCustomer(email);
            if (customer == null) {
                System.out.print("Sorry, that email doesn't appear to be in our system. ");
                do {
                    System.out.println("Do you want to create an account?");
                    String yesOrNo = input.nextLine().toLowerCase().trim();
                    if (yesOrNo.equals("y")) {
                        addCustomer(email);
                        customer = CustomerService.getCustomer(email);
                        repeat = false;
                    } else if (yesOrNo.equals("n")) {
                        repeat = false;
                    } else {
                        System.out.println(invalidYN);
                        repeat = true;
                    }
                } while (repeat);
            }
        } while (repeat);

        return customer;
    }

    private static void addCustomer(String email) {
        System.out.println("First name: ");
        String firstName = input.nextLine();
        System.out.println("Last name: ");
        String lastName = input.nextLine();
        CustomerService.addCustomer(email, firstName, lastName);
    }

    private static Date getTodayStartOfDay() {
        Calendar day = Calendar.getInstance();
        day.set(Calendar.MILLISECOND, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.HOUR_OF_DAY, 0);
        return day.getTime();
    }

    private static Date getOneDayLater(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        return calendar.getTime();
    }

    private static Date getXDaysLater(Date date, int daysLater) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,daysLater);
        return calendar.getTime();
    }

    private static Date getValidatedDate(String typeDate, Date refDate) {
        boolean repeat = false;
        String enterMsg = "Please enter " + typeDate + " date month/day/year, example 06/05/2024.";
        Date date = null;

        do {
            System.out.println("\n" + enterMsg);
            String dateStr = input.nextLine().trim();
            String afterTodayMsg = RED + "Date enter " + dateStr + " must be today or later." + RESET;
            String oneDayAfterMsg = RED + "Date entered, " + dateStr + ", must be at least 1 day after check-in" + RESET;

            boolean isValidRegEx = validateRegExDate(dateStr);

            if (isValidRegEx) {
                date = convertToDate(dateStr);
                Comparator<Date> comparator = (d1, d2) -> {
                    if (d1.getYear() != d2.getYear()) return d1.getYear() - d2.getYear();
                    if (d1.getMonth() != d2.getMonth()) return d1.getMonth() - d2.getMonth();
                    return (int) (d1.getDate() - d2.getDate());
                };
                int comp = comparator.compare(date, refDate);
                if (comp < 0) {
                    System.out.println(typeDate.equals("check-in") ? afterTodayMsg : oneDayAfterMsg);
                    repeat = true;
                } else {
                    repeat = false;
                }
            } else {
                System.out.println(RED + "Invalid. " + enterMsg + RESET);
                repeat = true;
            }
        } while (repeat);

        return date;
    }

    private static Date convertToDate(String dateStr) {
        String[] splitDate = dateStr.split("/");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(splitDate[2]), (Integer.parseInt(splitDate[0]) - 1), Integer.parseInt(splitDate[1]));
        return calendar.getTime();
    }

    private static void printRooms(List<Room> listRooms) {
        for (Room room : listRooms) {
            System.out.println(room);
        }
    }

    private static void displayReservation() {
        String asterisk = "*".repeat(17);
        String title = GREEN + "\n" + asterisk + " Reservation Search " + asterisk + RESET;
        System.out.println(title);
        System.out.println("\nPlease enter the email of the Customer to see their reservations.");

        String email = getValidEmail();
        List<Reservation> reservations = HotelResource.getCustomerReservations(email);
        if (reservations.isEmpty()) {
            System.out.println("There are no reservations under that email");
        } else {
            Collections.sort(reservations);
            System.out.println("\nList of Reservations");
            for (int i = 0; i < reservations.size(); i++) {
                System.out.println((i + 1) + ". " + reservations.get(i));
            }
        }
        MainMenu.getMenu();
    }

    private static Customer createAccountAndGetNewCustomer(String email) {
        boolean isMainPage = email == null;
        Customer customer;
        if (isMainPage) {
            String asterisk = "*".repeat(17);
            String title = GREEN + "\n" + asterisk + " Create an Account " + asterisk + RESET;
            String emailMsg = "\nPlease enter email using the format " + GREEN + "me@email.com" + RESET;
            System.out.println(title);
            System.out.println(emailMsg);
        }

        boolean repeat;
        do {
            if (isMainPage) {
                email = getValidEmail();
            }
            customer = CustomerService.getCustomer(email);

            if (customer == null) {
                repeat = false;
            } else {
                System.out.println("Oh oh, " + RED + email + RESET + " already exist in the system," +
                        " please use a different email address.");
                email = getValidEmail();
                repeat = true;
            }
        } while (repeat);

        System.out.println("First name: ");
        String firstName = input.nextLine();
        System.out.println("Last name: ");
        String lastName = input.nextLine();
        CustomerService.addCustomer(email, firstName, lastName);
        Customer newCustomer = CustomerService.getCustomer(email);

        if (isMainPage) {
            clearScreen();
            MainMenu.getMenu();
        }
        return newCustomer;
    }

    public static boolean validateRegExDate(String date) {
        String dateRegex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }
}
