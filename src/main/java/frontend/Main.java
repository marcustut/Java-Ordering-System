package frontend;

import backend.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Main {
    public final static int BREAKFAST = 0;
    public final static int LUNCH = 1;
    public final static int DINNER = 2;
    private static final String STAFF_USERNAME = "marcus";
    private static final String STAFF_PASSWORD = "brj123";
    private static Food[][] foods;
    private static Drink[][] drinks;
    private static Combo[][] combos;
    private static Menu[] menus;
    private static UserInterface UI = new UserInterface();
    private static List<Table> tables = new ArrayList<>();
    private static Restaurant restaurant = new Restaurant();

    private static void createMenuItems() throws MenuItem.InvalidIndex, Drink.InvalidDrinkTemp, Combo.InvalidSize {
        Food[] morningFood = {
                new Food("Nasi Lemak", "Typical Malaysian breakfast.", "Nasi", 2.5, true),
                new Food("Roti Bakar", "A beloved breakfast in the morning.", "Roti", 2.0, true),
                new Food("Nasi Goreng Kampung", "A spicy flavour of traditional Nasi Goreng.", "Nasi", 5.0, false)
        };
        Food[] afternoonFood = {
                new Food("Roti Telur", "A symbolic food in Malaysia", "Roti", 1.5, true),
                new Food("Roti Tisu", "A crispy and sweet variation of roti canai", "Roti", 2.0, true),
                new Food("Maggie Goreng", "The most ordered dish in the shop.", "Maggie", 4.5, false)
        };
        Food[] dinnerFood = {
                new Food("Roti Telur", "A symbolic food in Malaysia", "Roti", 1.5, true),
                new Food("Roti Tisu", "A crispy and sweet variation of roti canai", "Roti", 2.0, true),
                new Food("Maggie Goreng", "The most ordered dish in the shop.", "Maggie", 4.5, false)
        };

        foods = new Food[][]{
                morningFood, afternoonFood, dinnerFood
        };

        Drink[] morningDrinks = {
                new Drink("Teh Ice", "Traditional Malaysian tea.", "Teh", 1.5, false, "Cold"),
                new Drink("Milo Panas", "A very welcomed Malaysian drink.", "Malt", 1.5, true, "Hot"),
                new Drink("Neslo Panas", "A mixture of Nescafe and Milo", "Malt", 2.5, false, "Hot")
        };
        Drink[] afternoonDrinks = {
                new Drink("Limau Ice", "A drink made with Lemons", "Fruit", 1.2, true, "Cold"),
                new Drink("100 Plus", "A good soft drink to quench thirst.", "Soft Drink", 2.0, false, "Cold"),
                new Drink("Teh O Panas", "Another variant of Teh.", "Teh", 2.0, false, "Hot")
        };
        Drink[] dinnerDrinks = {
                new Drink("Limau Ice", "A drink made with Lemons", "Fruit", 1.2, true, "Cold"),
                new Drink("100 Plus", "A good soft drink to quench thirst.", "Soft Drink", 2.0, false, "Cold"),
                new Drink("Teh O Panas", "Another variant of Teh.", "Teh", 2.0, false, "Hot")
        };

        drinks = new Drink[][]{
                morningDrinks, afternoonDrinks, dinnerDrinks
        };

        Combo[] morningCombo = {
                new Combo("Breakfast Combo 1", "A combo with a mixed of food", "Breakfast", 7.5, true, "Medium"),
                new Combo("Breakfast Combo 2", "A combo with a mixed of food", "Breakfast", 6.5, true, "Small"),
                new Combo("Breakfast Combo 3", "A combo with a mixed of food", "Breakfast", 9.5, true, "Large"),
        };
        Combo[] afternoonCombo = {
                new Combo("Lunch Combo 1", "A combo with a mixed of food", "Lunch", 7.5, true, "Medium"),
                new Combo("Lunch Combo 2", "A combo with a mixed of food", "Lunch", 6.5, true, "Small"),
                new Combo("Lunch Combo 3", "A combo with a mixed of food", "Lunch", 9.5, true, "Large"),
        };
        Combo[] dinnerCombo = {
                new Combo("Dinner Combo 1", "A combo with a mixed of food", "Lunch", 7.5, true, "Medium"),
                new Combo("Dinner Combo 2", "A combo with a mixed of food", "Lunch", 6.5, true, "Small"),
                new Combo("Dinner Combo 3", "A combo with a mixed of food", "Lunch", 9.5, true, "Large"),
        };

        combos = new Combo[][]{
                morningCombo, afternoonCombo, dinnerCombo
        };
    }

    private static void createMenus() {
        menus = new Menu[]{
                new Menu("Breakfast Menu", LocalTime.parse("04:00:00"), LocalTime.parse("11:59:59"), foods[BREAKFAST], drinks[BREAKFAST], combos[BREAKFAST]),
                new Menu("Lunch Menu", LocalTime.parse("12:00:00"), LocalTime.parse("18:59:59"), foods[LUNCH], drinks[LUNCH], combos[LUNCH]),
                new Menu("Dinner Menu", LocalTime.parse("19:00:00"), LocalTime.parse("03:59:59"), foods[DINNER], drinks[DINNER], combos[DINNER])
        };
    }

    private static void initTelegramBot() {
        ApiContextInitializer.init(); // Initialize API Context
        TelegramBotsApi botsApi = new TelegramBotsApi(); // Instantiate Telegram Bots API

        // Register the Bot
        try {
            botsApi.registerBot(new Bot("1034303490:AAHXrohNaZBOBUX2-VbZqx8oOxPJF5bMTag", "BRJMamakBot", 797584575));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static void init() {
        try {
            // Create Food, Drinks, Combos
            createMenuItems();
            // Create Menus
            createMenus();
            // Initialize Telegram Bot
            initTelegramBot();
            // Create list of tables
            for (int i = 0; i < Restaurant.MAX_TABLE_COUNT; i++)
                tables.add(new Table(i + 1));
            // Create Restaurant Object
            restaurant = new Restaurant(
                    "BRJ Mamak",
                    "2, Jalan Puchong Maju, Puchong Entrepreneur Park, 58200 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur.",
                    (ArrayList<Table>) tables
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void clearScreen() {
        // Clears Screen in Java
        try {
            // If Windows then run 'cls' in cmd
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static Menu getCurrentServingMenu() {
        // Check time now and give different menus accordingly
        LocalTime breakfastStart = menus[BREAKFAST].getStartTime();
        LocalTime lunchStart = menus[LUNCH].getStartTime();
        LocalTime dinnerStart = menus[DINNER].getStartTime();
        LocalTime timeNow = LocalTime.now();

        return timeNow.isAfter(breakfastStart) && timeNow.isBefore(lunchStart) ? menus[BREAKFAST]
                : timeNow.isAfter(lunchStart) && timeNow.isBefore(dinnerStart) ? menus[LUNCH] : menus[DINNER];
    }

    public static Restaurant getRestaurant() {
        return restaurant;
    }

    public static Menu getMenu(int time) {
        return menus[time];
    }

    public static void main(String[] args) {
        // Initialize objects and Telegram Bot
        init();

        // Forever loop for the program to exit
        while (true) {
            // variables
            int userSelection = 1;

            // Render the Welcome Screen
            clearScreen();
            UI.welcomeScreen();

            // Render the main menu
            while (userSelection == 1) userSelection = UI.mainScreen(userSelection);

            if (userSelection == 2) {
                // Take tableNo from customer
                int tableNo = 1;
                boolean continueTableInput = true;

                do {
                    try {
                        System.out.print("\nWhat is your table no? (1 - 30) \uD83E\uDE91\n" + UserInterface.inputPrefix);
                        tableNo = UserInterface.scanner.nextInt();

                        while (tableNo < 1 || tableNo > 30) {
                            System.out.println("You can only enter selection from '1 - 30', try again.\n");
                            System.out.print("What is your table no? (1 - 30) \uD83E\uDE91\n" + UserInterface.inputPrefix);
                            tableNo = UserInterface.scanner.nextInt();
                        }

                        continueTableInput = false;
                    } catch (InputMismatchException ex) {
                        System.out.println(UI.errorMessage(ex));
                        UserInterface.scanner.nextLine();
                    }
                } while (continueTableInput);

                // Create a customer object with the tableNo given
                Customer customer = new Customer(tableNo);
                boolean orderDone = false;

                // Taking numOrders
                System.out.print("\nHow many orders do you want to make? \uD83E\uDD14\n" + UserInterface.inputPrefix);
                int numOrders = UserInterface.scanner.nextInt();

                while (!(numOrders > 0)) {
                    System.out.println("\nYou can only enter positive numbers, try again.");
                    System.out.print("How many orders do you want to make? \uD83E\uDD14\n" + UserInterface.inputPrefix);
                    // Take input from user
                    numOrders = UserInterface.scanner.nextInt();
                }

                // Consume extra new line in STDIN
                if (UserInterface.scanner.hasNextLine()) UserInterface.scanner.nextLine();

                while (!orderDone) {
                    // Clear the screen before showing menu
                    clearScreen();

                    boolean continueToPayment = UI.orderScreen(getCurrentServingMenu(), customer, numOrders);
                    double amountPaid = 0;

                    if (continueToPayment) {
                        try {
                            Payment customerPayment = new Payment(customer.getAllTotal());
                            customer.setPayment(customerPayment);

                            amountPaid = UI.paymentScreen(customerPayment);
                            orderDone = amountPaid != -1;
                        } catch (Payment.InvalidStatus | Payment.InvalidPaymentMethod | IOException | InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.out.println("\nDo you want to order again? (Y/N)");
                        System.out.print(UserInterface.inputPrefix);

                        // Consume extra new line in STDIN
                        if (UserInterface.scanner.hasNextLine()) UserInterface.scanner.nextLine();

                        String orderDoneStr = UserInterface.scanner.nextLine().trim();
                        orderDone = orderDoneStr.contains("n") || orderDoneStr.contains("N");

                        if (orderDone) {
                            clearScreen();
                            break;
                        }
                    }

                    if (orderDone) {
                        // Record the sale
                        Sale newSale = new Sale(customer);

                        restaurant.addSale(newSale);
                        restaurant.addCustomer(customer);

                        UI.printReceipt(customer, amountPaid);
                    }

                    boolean nextCustomer;
                    String nextCustomerStr;

                    System.out.print("\nNext customer? \uD83D\uDC68\n" + UserInterface.inputPrefix);
                    nextCustomerStr = UserInterface.scanner.next();
                    nextCustomer = nextCustomerStr.contains("y");

                    while (!nextCustomer) {
                        System.out.println("\nYou can only enter yes/no");
                        System.out.print("Next customer? \uD83D\uDC68\n" + UserInterface.inputPrefix);
                        nextCustomerStr = UserInterface.scanner.next();
                        nextCustomer = nextCustomerStr.contains("y");
                    }
                }
                clearScreen();
            } else {
                UI.staffLogin(STAFF_USERNAME, STAFF_PASSWORD);
            }
        }
    }
}
