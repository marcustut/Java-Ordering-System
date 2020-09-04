package backend;

import java.util.*;

public class Drink extends MenuItem {
    private final static HashMap<Character, String> TEMP_SELECTION = new HashMap<Character, String>(){
        {
            put('H', "Hot");
            put('C', "Cold");
        }
    };
    private char drinkTemp;
    private static int index = 200;

    // Constructors
    public Drink(String name, String description, String type, double price, Boolean preferred, String drinkTemp) throws InvalidDrinkTemp, InvalidIndex {
        super(name, description, type, price, preferred, "D", index);

        char drinkTempChar = Character.toUpperCase(drinkTemp.charAt(0));

        // Check if drinkTemp is either 'H', 'C'
        if (TEMP_SELECTION.containsKey(drinkTempChar)) {
            this.drinkTemp = drinkTempChar;
        }
        else {
            throw new InvalidDrinkTemp(String.format("Only these 2 options of temperature are available: %s/%s", TEMP_SELECTION.get('H'), TEMP_SELECTION.get('C')));
        }

        index++;
    }

    // Getters and Setters
    public char getDrinkTemp() {
        return drinkTemp;
    }

    public void setDrinkTemp(char drinkTemp) {
        this.drinkTemp = drinkTemp;
    }

    // Exception class
    public class InvalidDrinkTemp extends Exception {
        public InvalidDrinkTemp(String message) {
           super(message);
        }
    }

    // User defined methods
    @Override
    public String toString() {
        return String.format("| %s | %-25s | %-50s | %-4s | %-10s | RM%7.2f |    %-3s    |", this.getId(), this.getName(), this.getDescription(), this.getDrinkTemp() == 'H' ? "Hot" : this.getDrinkTemp() == 'C' ? "Cold" : "Warm", this.getType(), this.getPrice(), this.getPreferred() ? "Yes" : "No");
    }
}