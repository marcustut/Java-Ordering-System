package backend;

public class Table {
    private int tableNo;
    private boolean isOccupied;
    private Customer customer;

    // Constructors
    public Table(int tableNo) {
        this(tableNo, false, null);
    }

    public Table(int tableNo, Customer customer) {
        this(tableNo, true, customer);
    }

    public Table(int tableNo, boolean isOccupied, Customer customer) {
        this.tableNo = tableNo;
        this.isOccupied = isOccupied;
        this.customer = customer;
    }

    // Getters and Setters
    public int getTableNo() {
        return this.tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // User defined methods
    public boolean isOccupied() {
        return this.isOccupied;
    }

    public void bookTable(Customer customer) {
        this.setCustomer(customer);
        this.setOccupied(true);
    }

    public void releaseTable() {
        this.setOccupied(false);
        this.setCustomer(null);
    }
}
