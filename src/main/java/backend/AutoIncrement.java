package backend;

public interface AutoIncrement {
    int FOOD_MAX_INDEX = 199;
    int DRINK_MAX_INDEX = 299;
    int COMBO_MAX_INDEX = 399;

    String FoodMaxIndexExceeded = "Food Index cannot go above " + FOOD_MAX_INDEX;
    String DrinkMaxIndexExceeded = "Drink Index cannot go above " + DRINK_MAX_INDEX;
    String ComboMaxIndexExceeded = "Combo Index cannot go above " + COMBO_MAX_INDEX;

    void incrementID(String category, int inex) throws MenuItem.InvalidIndex;
}
