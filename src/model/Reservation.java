package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static model.AnsiiColor.*;

public class Reservation implements Comparable<Reservation> {
    private final Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public void setRoom(IRoom room) {
        this.room = room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        String checkIn = dateFormat.format(calendar.getTime());
        calendar.setTime(checkOutDate);
        String checkOut = dateFormat.format(calendar.getTime());
        return customer + ", " + room +
                ", Check in: " + GREEN + checkIn + RESET +
                ", Check out: " + GREEN + checkOut + RESET;
    }

    @Override
    public int compareTo(Reservation reservation) {
        return customer.getEmail().compareTo(reservation.getCustomer().getEmail());
    }
}
