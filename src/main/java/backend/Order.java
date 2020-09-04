package backend;

import java.util.*;

public class Order implements Taxable{
    private static int orderID;
    private List<MenuItem> orderItems;

    // Constructors
    public Order() {
        this(new ArrayList<MenuItem>());
    }

    public Order(List<MenuItem> orderItems) {
        Order.orderID++;
        this.orderItems = orderItems;
    }

    // Getters and Setters
    public int getOrderID() {
        return Order.orderID;
    }

    public void setOrderID(int orderID) {
        Order.orderID = orderID;
    }

    public List<MenuItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<MenuItem> orderItems) {
        this.orderItems = orderItems;
    }

    // User defined methods
    public void addFoods(List<Food> foods) {
        this.orderItems.addAll(foods);
    }

    public void addFood(Food food) {
        this.orderItems.add(food);
    }

    public void addDrinks(List<Drink> drinks) {
        this.orderItems.addAll(drinks);
    }

    public void addDrink(Drink drink) {
        this.orderItems.add(drink);
    }

    public void addCombos(List<Combo> combos) {
        this.orderItems.addAll(combos);
    }

    public void addCombo(Combo combo) {
        this.orderItems.add(combo);
    }

    @Override
    public String toString() {
        // Create a HashMap object to store ordered items
        HashMap<MenuItem, Integer> orderedItems = new HashMap<MenuItem, Integer>();

        // Add keys and values to the HashMap
        for (MenuItem menuItem : this.getOrderItems()) {
            if (orderedItems.containsKey(menuItem))
                orderedItems.put(menuItem, orderedItems.get(menuItem) + 1);
            else
                orderedItems.put(menuItem, 1);
        }

        StringBuilder ret = new StringBuilder();

        for (Map.Entry<MenuItem, Integer> orderedItem : orderedItems.entrySet()) {
            ret.append(String.format("  %-19s      %-6d RM%6.2f       RM%6.2f\n", orderedItem.getKey().getName(), orderedItem.getValue(), orderedItem.getKey().getPrice(), orderedItem.getKey().getPrice() * orderedItem.getValue()));
        }

        return ret.toString();
    }

    public double calcSubTotal() {
        double subtotal = 0;

        for (MenuItem orderItem : this.orderItems) {
            subtotal += orderItem.getPrice();
        }

        return subtotal;
    }

    public double calcSST() {
        return this.calcSubTotal() * SST_RATE;
    }

    public double calcTotal() {
        // Round to the nearest first decimal
        return Math.round((this.calcSubTotal() + this.calcSST()) * 10.0)  / 10.0;
    }
}
