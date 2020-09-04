package backend;

import java.util.List;

public class Sale implements AutoIncrement {
    private static int index = 1;
    private String id;
    private Customer customer;
    private List<Order> orders;
    private Table table;
    private double subtotal, tax;

    // Constructors
    public Sale() {
        this(null);
    }

    public Sale(Customer customer) {
        this.incrementID("S", index);
        this.customer = customer;
        this.orders = customer.getOrders();
        this.table = customer.getTable();
        this.subtotal = customer.getAllSubtotal();
        this.tax = customer.getAllSST();
        index++;
    }

    public void incrementID(String category, int index) {
        this.id = String.format("S%03d", index);
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Table getTable() {
        return table;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTax() {
        return tax;
    }
}
