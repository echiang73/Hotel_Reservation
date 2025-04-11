import api.HotelResource;
import model.Customer;
import model.Room;
import model.RoomType;
import source.CustomerService;
import source.ReservationService;
import ui.MainMenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The driver page with developer's entry point to test parts of the hotel application
 *
 * @author eddie.chiang
 * @version 1.0
 */

public class Driver {
    public static void main(String[] args) {
////        Customer customer = new Customer("Eddie", "Chiang", "eddie@gmail.com");
////        System.out.println(customer);
////        CustomerService.addCustomer("eddie@gmail.com", "Eddie", "Chiang");
////        CustomerService.addCustomer("sara@gmail.com", "Sara", "Chiang");
////        CustomerService.addCustomer("zac@gmail.com", "Zac", "Chiang");
////        CustomerService.addCustomer("sara@gmail.com", "Sara", "Chiang");
////        System.out.println(CustomerService.getAllCustomers());
////        System.out.println(CustomerService.getCustomer("eddie@gmail.com"));
//
////        Room room = new Room("100", 0d, RoomType.SINGLE);
////        System.out.println(room);
//
//        Customer eddie = new Customer("Eddie", "Chiang", "eddie@gmail.com");
//        Customer sara = new Customer("Sara", "Chiang", "sara@gmail.com");
//
//        ReservationService.printAllReservation();
//        Room room101 = new Room("101", 149d, RoomType.SINGLE);
//        Room room102 = new Room("102", 0d, RoomType.DOUBLE);
////        Date checkInDate = new Date();
////        Date checkOutDate = new Date(checkInDate.getTime() + (1000 * 60 * 60 * 24));
//
////        Calendar calendar = Calendar.getInstance();
////        calendar.set(2024, 05, 30);
////        Date checkInDate = calendar.getTime();
////        calendar.set(2024, 05, 31);
////        Date checkOutDate = calendar.getTime();
//
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
//        calendar.setTime(new Date());
//        Date checkInDate = calendar.getTime();
//        calendar.add(Calendar.DATE,3);;
//        Date checkOutDate = calendar.getTime();
//
////        Calendar calendar = Calendar.getInstance();
////        calendar.setTime(new Date());
////        calendar.add(Calendar.DATE,1);
////        Date checkOutDate = calendar.getTime();
//
//        ReservationService.addRoom(room101);
//        ReservationService.addRoom(room102);
//        System.out.println("rooms = " + ReservationService.getRooms());
//
//
//        ReservationService.reserveARoom(eddie, room101, checkInDate, checkOutDate);
//        ReservationService.reserveARoom(sara, room102, checkInDate, checkOutDate);
//        ReservationService.printAllReservation();
//        System.out.println("----------------");
//        HotelResource.createACustomer("zac@gmail.com", "Zac", "Chiang");
//        System.out.println(HotelResource.getCustomer("eddie@gmail.com"));
//        System.out.println(HotelResource.getCustomer("zac@gmail.com"));
//        System.out.println(CustomerService.getAllCustomers());
//
//
//
//        System.out.println("findRooms = " + ReservationService.findRooms(checkInDate, checkOutDate));
//
//        ;
//        MainMenu.displayAppLogo();
//        MainMenu.getMenu();

        // -----------------------------------------
        Customer eddie = new Customer("Eddie", "Chiang", "eddie@gmail.com");
        Customer sara = new Customer("Sara", "Chiang", "sara@gmail.com");
        Customer zac = new Customer("Zac", "Chiang", "zac@gmail.com");

        ReservationService.printAllReservation();
        Room room101 = new Room("101", 149d, RoomType.SINGLE);
        Room room102 = new Room("102", 0d, RoomType.DOUBLE);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        calendar.setTime(new Date());
        Date checkInDate = calendar.getTime();
        calendar.add(Calendar.DATE,3);;
        Date checkOutDate = calendar.getTime();

        ReservationService.addRoom(room101);
        ReservationService.addRoom(room102);
        System.out.println("rooms = " + ReservationService.getRooms());


        ReservationService.reserveARoom(eddie, room101, checkInDate, checkOutDate);
        ReservationService.reserveARoom(sara, room102, checkInDate, checkOutDate);
//        ReservationService.reserveARoom(zac, room102, checkInDate, checkOutDate);

        ReservationService.printAllReservation();
        System.out.println("----------------");
        HotelResource.createACustomer("zac@gmail.com", "Zac", "Chiang");
        System.out.println(HotelResource.getCustomer("eddie@gmail.com"));
        System.out.println(HotelResource.getCustomer("zac@gmail.com"));
        System.out.println(CustomerService.getAllCustomers());


        System.out.println("findRooms = " + ReservationService.findRooms(checkInDate, checkOutDate));

        System.out.print("\033[H\033[2J");
        System.out.flush();

        MainMenu.startApp();


    }
}
