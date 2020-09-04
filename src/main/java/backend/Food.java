package backend;

public class Food extends MenuItem {
    private static int index = 100;

    // Constructors
    public Food(String name, String description, String type, double price, Boolean preferred) throws InvalidIndex {
        super(name, description, type, price, preferred, "F", index);
        index++;
    }

    // User defined methods
    @Override
    public String toString() {
        return String.format("| %s | %-25s | %-50s | %-8s | RM%7.2f |    %-3s    |", this.getId(), this.getName(), this.getDescription(), this.getType(), this.getPrice(), this.getPreferred() ? "Yes" : "No");

    }
}
