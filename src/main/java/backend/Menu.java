package backend;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Menu {
    private String menuName;
    private LocalTime startTime, endTime;
    private List<Food> foodInMenu = new ArrayList<>();
    private List<Drink> drinksInMenu = new ArrayList<>();
    private List<Combo> combosInMenu = new ArrayList<>();

    // Constructors
    public Menu() {
        this("", LocalTime.parse("00:00:00"), LocalTime.parse("00:00:00"), null, null, null);
    }

    public Menu(String menuName, LocalTime startTime, LocalTime endTime, Food[] foodInMenu, Drink[] drinkInMenu, Combo[] comboInMenu) {
        this.menuName = menuName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.foodInMenu.addAll(Arrays.asList(foodInMenu));
        this.drinksInMenu.addAll(Arrays.asList(drinkInMenu));
        this.combosInMenu.addAll(Arrays.asList(comboInMenu));
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<Food> getFoodInMenu() {
        return foodInMenu;
    }

    public void setFoodInMenu(List<Food> foodInMenu) {
        this.foodInMenu = foodInMenu;
    }

    public List<Drink> getDrinksInMenu() {
        return drinksInMenu;
    }

    public void setDrinksInMenu(List<Drink> drinkInMenu) {
        this.drinksInMenu = drinkInMenu;
    }

    public List<Combo> getCombosInMenu() {
        return combosInMenu;
    }

    public void setCombosInMenu(List<Combo> comboInMenu) {
        this.combosInMenu = comboInMenu;
    }

    // User Defined Methods
    public void addFood(Food foodToAdd) {
        foodInMenu.add(foodToAdd);
    }

    public void removeFood(Food foodToRemove) {
        foodInMenu.remove(foodToRemove);
    }

    public void addDrink(Drink drinkToAdd) {
        drinksInMenu.add(drinkToAdd);
    }

    public void removeDrink(Drink drinkToRemove) {
        drinksInMenu.remove(drinkToRemove);
    }

    public void addCombo(Combo comboToAdd) {
        combosInMenu.add(comboToAdd);
    }

    public void removeCombo(Combo comboToRemove) {
        combosInMenu.remove(comboToRemove);
    }

    public String showItemsInMenu() {
        String result = "";
        return result;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma");
        StringBuilder menuMessage = new StringBuilder(String.format("<strong>%s \uD83D\uDCCB</strong>\n<em>%s - %s</em>\n", this.menuName, this.startTime.format(formatter), this.endTime.plusMinutes(1).format(formatter)));

        menuMessage.append("\n<strong>Foods \uD83C\uDF72</strong>");

        for (int i=0; i<foodInMenu.size(); i++)
            menuMessage.append(String.format("\n%s - %s (RM%.2f)", foodInMenu.get(i).getId(), foodInMenu.get(i).getName(), foodInMenu.get(i).getPrice()));

        menuMessage.append("\n\n<strong>Drinks \uD83E\uDD64</strong>");

        for (int i=0; i<drinksInMenu.size(); i++)
            menuMessage.append(String.format("\n%s - %s (RM%.2f)", drinksInMenu.get(i).getId(), drinksInMenu.get(i).getName(), drinksInMenu.get(i).getPrice()));

        menuMessage.append("\n\n<strong>Combos \uD83C\uDF5B☕</strong>");

        for (int i=0; i<combosInMenu.size(); i++)
            menuMessage.append(String.format("\n %s - %s (RM%.2f)", combosInMenu.get(i).getId(), combosInMenu.get(i).getName(), combosInMenu.get(i).getPrice()));

        return menuMessage.toString();
    }
}
