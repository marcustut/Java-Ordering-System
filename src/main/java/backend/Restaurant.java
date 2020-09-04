package backend;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    public static final int MAX_TABLE_COUNT = 30;
    private String name, address;
    private List<Table> tables;
    private List<Sale> sales;
    private List<Customer> customers;

    public Restaurant() {
        this("", "", new ArrayList<>());
    }

    public Restaurant(String name, String address, ArrayList<Table> tables) {
        this.name = name;
        this.address = address;
        this.tables = tables;
        this.sales = new ArrayList<Sale>();
        this.customers = new ArrayList<Customer>();
    }

    public Restaurant(String name, String address, ArrayList<Table> tables, ArrayList<Sale> sales, ArrayList<Customer> customers) {
        this.name = name;
        this.address = address;
        this.tables = tables;
        this.sales = sales;
        this.customers = customers;
    }

    public List<Customer> getAllCustomers() {
        return this.customers;
    }

    public List<Sale> getAllSales() {
        return this.sales;
    }

    public void addSale(Sale sale) {
        this.sales.add(sale);
    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }
}
