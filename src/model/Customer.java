package model;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.AnsiiColor.*;

public class Customer implements Comparator<Customer> {
    private String firstName;
    private String lastName;
    private final String email;

    public Customer(String firstName, String lastName, String email) throws IllegalArgumentException {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("Error, Names can't be null");
        }
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Error, Invalid email");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null) {
            throw new IllegalArgumentException("Error, First name can't be null");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null) {
            throw new IllegalArgumentException("Error, Last names can't be null");
        }
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public static boolean validateEmail(String email) {
        // TODO expand regEx to include more than just .com
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public String toString() {
        return "Name: " + GREEN + firstName + " " + lastName + RESET + ", Email: " + GREEN + email + RESET;
    }

    @Override
    public int compare(Customer c1, Customer c2) {
        return c1.email.compareTo(c2.email);
    }
}
