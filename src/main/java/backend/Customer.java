package backend;

import java.util.ArrayList;
import java.util.List;

public class Customer implements AutoIncrement {
    private static int index = 1;
    private String id;
    private Table table;
    private List<Order> orders;
    private Payment payment;

    // Constructor
    public Customer() {
        this(null, null, null);
    }

    public Customer(int tableNo) {
        this(new Table(tableNo), new ArrayList<Order>(), null);
    }

    public Customer(Table table, ArrayList<Order> orders, Payment payment) {
        this.incrementID("C", index);
        this.table = table;
        this.table.setCustomer(this);
        this.orders = orders;
        this.orders.add(new Order());
        this.payment = payment;
        index++;
    }

    // Getter and Setters
    public String getId() {
        return this.id;
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    // User defined methods
    public void incrementID(String category, int index) {
        this.id = String.format("%s%03d", category, index);
    }

    public void addOrder(List<Order> orders) {
        this.orders.addAll(orders);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void placeOrders(List<Order> customerOrders) {
        this.addOrder(customerOrders);
    }

    public void placeOrder(Order customerOrder) {
        this.addOrder(customerOrder);
    }

    public double getAllSubtotal() {
        double allSubtotal = 0.0;

        for (Order order : this.getOrders()) {
            allSubtotal += order.calcSubTotal();
        }

        return allSubtotal;
    }

    public double getAllSST() {
        double allSST = 0.0;

        for (Order order : this.getOrders()) {
            allSST += order.calcSST();
        }

        return allSST;
    }

    public double getAllTotal() {
        double allTotal = 0.0;

        for (Order order : this.getOrders()) {
            allTotal += order.calcTotal();
        }

        return allTotal;
    }
}
