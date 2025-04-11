package source;

import model.Customer;

import java.util.HashMap;
import java.util.Map;

/**
 * The service layer of the hotel application storing Customer information
 *
 * @author eddie.chiang
 * @version 1.0
 */
public class CustomerService {
    private static Map<String, Customer> allCustomers = new HashMap<>();
    public static void addCustomer(String email, String firstName, String lastName) {
        allCustomers.put(email, new Customer(firstName, lastName, email));
    }

    public static Customer getCustomer(String customerEmail) {
        for (Customer customer : allCustomers.values()) {
            if (customer.getEmail().equals(customerEmail)) {
                return customer;
            }
        }
        return null;
    }

    public static Map<String, Customer> getAllCustomers() {
        return allCustomers;
    }
}
