package backend;

public abstract class MenuItem implements AutoIncrement {
    private String id;
    private String name, description, type;
    private double price;
    private Boolean preferred;

    // Constructors
    public MenuItem() throws InvalidIndex {
        this("", "", "", 0.0, false, "", 0);
    }

    public MenuItem(String name, String description, String type, double price, Boolean preferred, String category, int index) throws InvalidIndex {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.preferred = preferred;
        this.incrementID(category, index);
    }

    // Getters and Setters
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Boolean getPreferred() {
        return preferred;
    }

    public void setPreferred(Boolean preferred) {
        this.preferred = preferred;
    }

    // Interface methods
    public void incrementID(String category, int index) throws InvalidIndex {
        switch (category) {
            case "F":
                if (index > FOOD_MAX_INDEX)
                    throw new InvalidIndex(FoodMaxIndexExceeded);
                break;
            case "D":
                if (index > DRINK_MAX_INDEX)
                    throw new InvalidIndex(DrinkMaxIndexExceeded);
                break;
            case "C":
                if (index > COMBO_MAX_INDEX)
                    throw new InvalidIndex(ComboMaxIndexExceeded);
                break;
        }

        // Increment the ID since no exception is thrown.
        this.id = category + index;
    }

    // User Defined Methods
    private void addPrice(double increase) {
        this.setPrice(this.getPrice() + increase);
    }

    private void setPreferred() {
        this.setPreferred(true);
    }

    private void changeDescription(String newDesc) {
        this.setDescription(newDesc);
    }

    @Override
    public abstract String toString();

    @Override
    public boolean equals(Object obj) {
        MenuItem menuItemToCheck = (MenuItem) obj;
        return this.id.equals(menuItemToCheck.getId());
    }

    // Exception to handle the user input time
    public class InvalidIndex extends Exception {
        public InvalidIndex(String message) {
            super(message);
        }
    }
}
