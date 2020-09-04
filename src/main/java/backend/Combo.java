package backend;

import java.util.*;

public class Combo extends MenuItem {
    private final static HashMap<Character, String> SIZE_SELECTION = new HashMap<Character, String>() {
        {
            put('S', "Small");
            put('M', "Medium");
            put('L', "Large");
        }
    };
    private char size;
    private static int index = 300;

    // Constructors
    public Combo(String name, String description, String type, double price, Boolean preferred, String size) throws InvalidSize, InvalidIndex {
        super(name, description, type, price, preferred, "C", index);

        char sizeChar = Character.toUpperCase(size.charAt(0));

        // Check if size is either 'S', 'M', 'L'
        if (SIZE_SELECTION.containsKey(sizeChar)) {
            this.size = sizeChar;
        }
        else {
            throw new InvalidSize(String.format("Only these 3 sizes are available: %s, %s, %s", SIZE_SELECTION.get('S'), SIZE_SELECTION.get('M'), SIZE_SELECTION.get('L')));
        }

        index++;
    }

    // Getters and Setters
    public char getSize() {
        return size;
    }

    public void setSize(char size) {
        this.size = size;
    }

    public class InvalidSize extends Exception {
        public InvalidSize(String message) {
            super(message);
        }
    }

    // User defined methods

    @Override
    public String toString() {
        return String.format("| %s | %-25s | %-50s | %-6s | %-9s | RM%7.2f |    %-3s    |", this.getId(), this.getName(), this.getDescription(), this.getSize() == 'S' ? "Small" : this.getSize() == 'M' ? "Medium" : "Large", this.getType(), this.getPrice(), this.getPreferred() ? "Yes" : "No");
    }
}
