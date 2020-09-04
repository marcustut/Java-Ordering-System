package frontend;

import backend.*;
import backend.Menu;
import backend.MenuItem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class UserInterface {
    public final static String SEPARATOR = String.format("%75s\n", " ").replace(' ', '-');
    public final static String SHORT_SEPARATOR = String.format("%31s", " ").replace(' ', '-');
    public final static Scanner scanner = new Scanner(System.in);
    public static char inputPrefix;
    private SimpleDateFormat dateFormat;

    public UserInterface() {
        this('>', new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy(E)"));
    }

    public UserInterface(char prefix, SimpleDateFormat dateFormat) {
        UserInterface.inputPrefix = prefix;
        this.dateFormat = dateFormat;
    }

    public void welcomeScreen() {
        String logo = "\n" +
                "                               ,---._                    ____                                                    \n" +
                "    ,---,. ,-.----.          .-- -.' \\                 ,'  , `.                      ____                   ,-.  \n" +
                "  ,'  .'  \\\\    /  \\         |    |   :             ,-+-,.' _ |                    ,'  , `.             ,--/ /|  \n" +
                ",---.' .' |;   :    \\        :    ;   |          ,-+-. ;   , ||                 ,-+-,.' _ |           ,--. :/ |  \n" +
                "|   |  |: ||   | .\\ :        :        |         ,--.'|'   |  ;|              ,-+-. ;   , ||           :  : ' /   \n" +
                ":   :  :  /.   : |: |        |    :   :        |   |  ,', |  ':  ,--.--.    ,--.'|'   |  || ,--.--.   |  '  /    \n" +
                ":   |    ; |   |  \\ :        :                 |   | /  | |  || /       \\  |   |  ,', |  |,/       \\  '  |  :    \n" +
                "|   :     \\|   : .  /        |    ;   |        '   | :  | :  |,.--.  .-. | |   | /  | |--'.--.  .-. | |  |   \\   \n" +
                "|   |   . |;   | |  \\    ___ l                 ;   . |  ; |--'  \\__\\/: . . |   : |  | ,    \\__\\/: . . '  : |. \\  \n" +
                "'   :  '; ||   | ;\\  \\ /    /\\    J   :        |   : |  | ,     ,\" .--.; | |   : |  |/     ,\" .--.; | |  | ' \\ \\ \n" +
                "|   |  | ; :   ' | \\.'/  ../  `..-    ,        |   : '  |/     /  /  ,.  | |   | |`-'     /  /  ,.  | '  : |--'  \n" +
                "|   :   /  :   : :-'  \\    \\         ;         ;   | |`-'     ;  :   .'   \\|   ;/        ;  :   .'   \\;  |,'     \n" +
                "|   | ,'   |   |.'     \\    \\      ,'          |   ;/         |  ,     .-./'---'         |  ,     .-./'--'       \n" +
                "`----'     `---'        \"---....--'            '---'           `--`---'                   `--`---'               \n" +
                "                                                                                                                 \n" +
                "                                                                                                                 \n";
        String welcome = "Welcome to BRJ Mamak, we're happy to serve!\uD83D\uDE03";

        System.out.println(logo + welcome);
    }

    public int mainScreen(int userSelection) {
        String instruction = "\nYou can navigate through the system by entering numbers listed below:\n";
        String selection = "1. Main Menu \uD83D\uDD22\n" +
                           "2. Order Food \uD83C\uDF5B\n" +
                           "3. Staff Login\uD83D\uDC68\u200D\uD83C\uDF73\n";
        String mainScreen = instruction + selection + SEPARATOR + inputPrefix;

        boolean continueInput = true;

        // Validate user input
        do {
            try {
                // Render the mainScreen
                System.out.print(mainScreen);

                userSelection = scanner.nextInt();

                while (userSelection < 1 || userSelection > 4) {
                    // Render the mainScreen
                    System.out.println("\nYou can only enter selection from '1 - 4', try again.\n");
                    System.out.print(mainScreen);

                    // Take input from user
                    userSelection = scanner.nextInt();
                }

                continueInput = false;
            } catch (InputMismatchException ex) {
                System.out.println(this.errorMessage(ex) + '\n');
                scanner.nextLine();
            }
        } while (continueInput);

        return userSelection;
    }

    public boolean orderScreen(Menu menu, Customer customer, int numOrders) {
        String indicateMenuAvailable = String.format("\n%47s The menu available now is <%s>\n\n", "", menu.getMenuName());
        String menuTitle = "=======================================================================================================================================\n" +
                           String.format("%77s %55s\n", menu.getMenuName() + " \uD83D\uDCC4", "⏲️ " + dateFormat.format(new Date())) +
                           "=======================================================================================================================================\n\n";

        // Food
        String foodTitle = "-----------------------------------------------------------Food\uD83C\uDF5C-----------------------------------------------------------\n";
        String foodAttributes = "+------+---------------------------+----------------------------------------------------+----------+-----------+-----------+\n" +
                                "|  ID  | Name                      | Description                                        |   Type   |   Price   | Preferred |";
        System.out.println(indicateMenuAvailable + menuTitle + foodTitle + foodAttributes);
        for (Food food : menu.getFoodInMenu()) {
            String foodDetails = "+------+---------------------------+----------------------------------------------------+----------+-----------+-----------+\n" + food.toString();
            System.out.println(foodDetails);
        }
        System.out.println("+------+---------------------------+----------------------------------------------------+----------+-----------+-----------+\n");

        // Drinks
        String drinksTitle = "---------------------------------------------------------------Drink☕---------------------------------------------------------------\n";
        String drinksAttributes = "+------+---------------------------+----------------------------------------------------+------+------------+-----------+-----------+\n" +
                "|  ID  | Name                      | Description                                        | Temp |    Type    |   Price   | Preferred |";
        System.out.println(drinksTitle + drinksAttributes);
        for (Drink drink : menu.getDrinksInMenu()) {
            String drinkDetails = "+------+---------------------------+----------------------------------------------------+------+------------+-----------+-----------+\n" + drink.toString();
            System.out.println(drinkDetails);
        }
        System.out.println("+------+---------------------------+----------------------------------------------------+------+------------+-----------+-----------+\n");

        // Combos
        String combosTitle = "---------------------------------------------------------------Combo\uD83C\uDF5A\uD83C\uDF75--------------------------------------------------------------\n";
        String combosAttributes = "+------+---------------------------+----------------------------------------------------+--------+-----------+-----------+-----------+\n" +
                "|  ID  | Name                      | Description                                        |  Size  |    Type   |   Price   | Preferred |";
        System.out.println(combosTitle + combosAttributes);
        for (Combo combo : menu.getCombosInMenu()) {
            String comboDetails = "+------+---------------------------+----------------------------------------------------+--------+-----------+-----------+-----------+\n" + combo.toString();
            System.out.println(comboDetails);
        }
        System.out.println("+------+---------------------------+----------------------------------------------------+--------+-----------+-----------+-----------+");

        // User Input
        boolean continueInput = true;
        int numFood, numDrinks, numCombos;
        numFood = numDrinks = numCombos = 0;

        List<Food> selectedFoods;
        List<Drink> selectedDrinks;
        List<Combo> selectedCombos;
        selectedFoods = new ArrayList<Food>();
        selectedDrinks = new ArrayList<Drink>();
        selectedCombos = new ArrayList<Combo>();

        // Validate user input
        do {
            try {

                // Taking orders
                for (int i=0; i<numOrders; i++) {
                    System.out.printf("\n%s\n| %s%3d |\n%s", "+" + String.format("%13s", " ").replace(" ", "-") + "+", "\uD83D\uDCCB Order", (i+1), "+" + String.format("%13s", " ").replace(" ", "-") + "+");

                    // Taking food
                    System.out.print("\nHow many food do you want? \uD83C\uDF5C\n" + inputPrefix);
                    numFood = scanner.nextInt();

                    while (!(numFood >= 0)) {
                        System.out.println("\nYou can only enter zero or positive numbers, try again.");
                        System.out.print("How many food do you want? \uD83C\uDF5c\n" + inputPrefix);
                        numFood = scanner.nextInt();
                    }

                    // Consume extra new line in STDIN
                    if (scanner.hasNextLine()) scanner.nextLine();

                    // Top separator if there's a food
                    if (numFood != 0) System.out.println(SHORT_SEPARATOR);

                    // Taking selectedFoodIDs
                    for (int j=0; j<numFood; j++) {
                        System.out.printf("Enter the ID for Food %d: F1", (j+1));
                        String userInputFoodID = scanner.nextLine().trim();
                        for (Food food : menu.getFoodInMenu()) {
                            if (food.getId().equals("F1" + userInputFoodID))
                                selectedFoods.add(food);
                        }
                    }

                    // Bottom separator if there's a food
                    if (numFood != 0) System.out.println(SHORT_SEPARATOR);

                    // Taking drinks
                    System.out.print("\nHow many drinks do you want? ☕\n" + inputPrefix);
                    numDrinks = scanner.nextInt();

                    while (!(numDrinks >= 0)) {
                        System.out.println("\nYou can only enter zero or positive numbers, try again.");
                        System.out.print("How many drinks do you want? ☕\n" + inputPrefix);
                        numDrinks = scanner.nextInt();
                    }

                    // Consume extra new line in STDIN
                    if (scanner.hasNextLine()) scanner.nextLine();

                    // Top separator if there's a drink
                    if (numDrinks != 0) System.out.println(SHORT_SEPARATOR);

                    // Taking selectedDrinksIDs
                    for (int j=0; j<numDrinks; j++) {
                        System.out.printf("Enter the ID for Drink %d: D2", (j+1));
                        String userInputDrinkID = scanner.nextLine().trim();
                        for (Drink drink : menu.getDrinksInMenu()) {
                            if (drink.getId().equals("D2" + userInputDrinkID))
                                selectedDrinks.add(drink);
                        }
                    }

                    // Bottom separator if there's a drink
                    if (numDrinks != 0) System.out.println(SHORT_SEPARATOR);

                    // Taking combos
                    System.out.print("\nHow many combos do you want? \uD83C\uDF5A\uD83C\uDF75\n" + inputPrefix);
                    numCombos = scanner.nextInt();

                    while (!(numCombos >= 0)) {
                        System.out.println("\nYou can only enter zero or positive numbers, try again.");
                        System.out.print("How many combos do you want? \uD83C\uDF5A\uD83C\uDF75\n" + inputPrefix);
                        numCombos = scanner.nextInt();
                    }

                    // Consume extra new line in STDIN
                    if (scanner.hasNextLine()) scanner.nextLine();

                    // Top separator if there's a combo
                    if (numCombos != 0) System.out.println(SHORT_SEPARATOR);

                    // Taking selectedCombosIDs
                    for (int j=0; j<numCombos; j++) {
                        System.out.printf("Enter the ID for Combo %d: C3", (j+1));
                        String userInputComboID = scanner.nextLine().trim();
                        for (Combo combo : menu.getCombosInMenu())
                           if (combo.getId().equals("C3" + userInputComboID))
                               selectedCombos.add(combo);
                    }

                    // Bottom separator if there's a combo
                    if (numCombos != 0) System.out.println(SHORT_SEPARATOR);
                }

                continueInput = false;
            } catch (InputMismatchException ex) {
                System.out.println(this.errorMessage(ex) + '\n');
                if (scanner.hasNextLine()) scanner.nextLine();
            }
        } while (continueInput);

        System.out.println();

        return confirmOrder(customer, selectedFoods, selectedDrinks, selectedCombos);
    }

    public boolean confirmOrder(Customer customer, List<Food> foods, List<Drink> drinks, List<Combo> combos) {
        String confirmTitle = "----------------------------------------\uD83D\uDC47Please confirm your order\uD83D\uDC47----------------------------------------\n";
        double totalPrice = 0;

        System.out.println(confirmTitle + "\nFoods\uD83C\uDF5C");

        for (int i=0; i<foods.size(); i++) {
            System.out.printf("%d. %s (RM %.2f)\n", (i+1), foods.get(i).getName(), foods.get(i).getPrice());
            totalPrice += foods.get(i).getPrice();
        }

        System.out.println("\nDrinks☕");

        for (int i=0; i<drinks.size(); i++) {
            System.out.printf("%d. %s (RM %.2f)\n", (i+1), drinks.get(i).getName(), drinks.get(i).getPrice());
            totalPrice += drinks.get(i).getPrice();
        }

        System.out.println("\nCombos\uD83C\uDF5A\uD83C\uDF75");

        for (int i=0; i<combos.size(); i++) {
            System.out.printf("%d. %s (RM %.2f)\n", (i+1), combos.get(i).getName(), combos.get(i).getPrice());
            totalPrice += combos.get(i).getPrice();
        }

        System.out.println("\n+----------------------------+");
        System.out.printf("| \uD83D\uDCB0 Total Price - RM %6.2f |\n", totalPrice);
        System.out.println("+----------------------------+");

        System.out.println("\nIs this your order? (Y/N)");
        System.out.print(inputPrefix);
        String orderConfirmedStr = scanner.nextLine().trim();
        boolean orderConfirmed = orderConfirmedStr.equalsIgnoreCase("y");

        // Adding confirmed items to customer order.
        if (orderConfirmed) {
            List<MenuItem> orderedItems = new ArrayList<MenuItem>() {
                {
                    addAll(foods);
                    addAll(drinks);
                    addAll(combos);
                }
            };
            Order confirmedOrder = new Order(orderedItems);
            customer.placeOrder(confirmedOrder);
        }

        return orderConfirmed;
    }

    public double paymentScreen(Payment payment) throws Payment.InvalidPaymentMethod, Payment.InvalidStatus, IOException, InterruptedException {
        String paymentTitle = "\n-------------------------------------------------------Payment\uD83D\uDCB5------------------------------------------------------\n";
        System.out.println(paymentTitle + "\nSelect your payment method");

        // Looping through the available payment methods.
        for (int i : Payment.PAYMENT_METHOD.keySet()) {
            System.out.printf("%d. %s\n", i, Payment.PAYMENT_METHOD.get(i));
        }

        System.out.print(inputPrefix);

        int paymentMethod = 1;

        do {
            if (paymentMethod < 1 || paymentMethod > 3) {
                System.out.println("Invalid Payment Method. Only 1, 2, 3 are accepted. Try again");
                System.out.print(inputPrefix);
            }

            paymentMethod = scanner.nextInt();
        } while (paymentMethod < 1 || paymentMethod > 3);

        // Set payment method
        payment.setPaymentMethod(paymentMethod);

        double amountPaid, balance;
        amountPaid = balance = 0;
        boolean payAgain = true;

        while (!payment.getStatus().equals("Paid") && payAgain) {
            // Prompt user for payment
            if (payment.getPaymentMethod().equals("Cash")) {
                System.out.printf("\nYour amount to pay is RM %.2f \uD83D\uDCB5\n", payment.getAmountToPay());
                System.out.print(inputPrefix + "RM ");

                amountPaid = scanner.nextDouble();
            } else if (payment.getPaymentMethod().equals("E-Wallet")) {
                boolean paymentDone;

                do {
                    paymentDone = Payment.showQRCode();
                } while (!paymentDone);

                amountPaid = payment.getAmountToPay();
            } else {
                // Tell user how much to pay
                System.out.printf("\nYour amount to pay is RM %.2f \uD83D\uDCB5\n", payment.getAmountToPay());

                // Prompt user for card information
                String cardNumber, expiryDate;
                int cardCVV;

                System.out.println("\n+" + String.format("%28s", " ").replace(" ", "-") + "+\n" + "| \uD83D\uDCB3 Credit Card Information |\n" + "+" + String.format("%28s", " ").replace(" ", "-") + "+");
                System.out.print("Card Number (eg. 4283-3220-1234-3214): ");
                cardNumber = scanner.next().trim();

                System.out.print("Expiry Date (eg. 05/23): ");
                expiryDate = scanner.next().trim();

                System.out.print("CVV (eg. 123): ");
                cardCVV = scanner.nextInt();

                amountPaid = payment.getAmountToPay();
            }

            balance = payment.pay(amountPaid);

            if (balance >= 0) {
                // Set payment status to "Paid"
                payment.setStatus(3);

                return amountPaid;
            } else {
                // Set payment status to "Not paid"
                payment.setStatus(1);
                System.out.printf("\nRM %.2f is not enough to pay.\uD83D\uDE22\n", amountPaid);
                System.out.println("Try again? (Y/N)");
                System.out.print(inputPrefix);

                // Consume extra new line in STDIN
                if (scanner.hasNextLine()) scanner.nextLine();

                String tryAgain = scanner.nextLine();

                if (tryAgain.contains("n") || tryAgain.contains("N"))
                    payAgain = false;
            }
        }

        return -1;
    }

    public void printReceipt(Customer customer, double amountPaid) {
        String receiptTitle = "\n------------------------------------------------------Receipt\uD83E\uDDFE-------------------------------------------------------\n";
        System.out.println(receiptTitle);

        String cashierName = "Mohammad Khalid Bin Abdullah";

        System.out.println("  BRJ MAMAK PUCHONG");
        System.out.println("  MAIN BRANCH");
        System.out.println("  2, Jalan Puchong Maju, Puchong Entrepreneur Park,\n  58200 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur.\n");
        System.out.printf("  TABLE: %49s\n", customer.getTable().getTableNo());
        System.out.printf("  PHONE: %49s\n", "03-74980475");
        System.out.printf("  DATE: %50s\n", dateFormat.format(new Date()));
        System.out.printf("  CASHIER: %47s\n", cashierName);
        System.out.println("\n============================================================");
        System.out.println("           ITEM           QTY       PRICE      TOTAL PRICE");
        System.out.println("------------------------------------------------------------");
        for (Order order : customer.getOrders()) {
            System.out.print(order.toString());
        }
        System.out.println("------------------------------------------------------------");
        System.out.printf("  %-46s RM%6.2f\n", "SUBTOTAL             : ", customer.getAllSubtotal());
        System.out.printf("  %-46s RM%6.2f\n", "SST (6%)             : ", customer.getAllSST());
        System.out.printf("  %-46s RM%6.2f\n", "TOTAL (TAX INCLUDED) : ", customer.getAllTotal());
        System.out.println("------------------------------------------------------------");
        System.out.printf("  %-46s RM%6.2f\n", "AMOUNT PAID          : ", amountPaid);
        System.out.printf("  %-46s RM%6.2f\n", "BALANCE              : ", amountPaid - customer.getAllTotal());
        System.out.println("============================================================\n");
        System.out.printf("%14s %s\n", " ", "THANK YOU! COME AGAIN NEXT TIME");
    }

    public void staffLogin(String correctUsername, String correctPassword) {
        System.out.println("\n--------------------------------------Staff Login\uD83D\uDC68\u200D\uD83D\uDCBC--------------------------------------");
        System.out.print("Username: ");
        String username = scanner.next().trim();
        System.out.print("Password: ");
        String password = scanner.next().trim();

        boolean accessGranted = username.equalsIgnoreCase(correctUsername) && password.equalsIgnoreCase(correctPassword);

        while(!accessGranted) {
            System.out.println("\n⚠️ Username or Password is incorrect ⚠️");
            System.out.println("------------------------------------------------");
            System.out.println("Do you want to continue logging in? (Y/N)");
            System.out.print(inputPrefix);

            String continueLoginStr = scanner.next().trim();
            boolean continueLogin = continueLoginStr.contains("y") || continueLoginStr.contains("Y");

            if (!continueLogin) return;

            System.out.print("Username: ");
            username = scanner.next().trim();
            System.out.print("Password: ");
            password = scanner.next().trim();

            accessGranted = username.equalsIgnoreCase(correctPassword) && password.equalsIgnoreCase(correctPassword);
        }

        if (accessGranted) {
            System.out.println("\nWelcome back " + correctUsername + "!");
            System.out.println("Interact with our Telegram Bot to manage the restaurant! \uD83D\uDCF1");
            System.out.println("The link to @BRJMamakBot - http://t.me/BRJMamakBot \uD83D\uDD17");

            System.out.println("\nDo you want the QR Code? (Y/N)");
            System.out.print(inputPrefix);
            String promptQRStr = scanner.next().trim();
            boolean promptQR= promptQRStr.contains("y") || promptQRStr.contains("Y");

            try {
                if (promptQR) this.showBotQR();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showBotQR() throws InterruptedException, IOException {
        // JFrame to display Image
        JFrame frame = new JFrame("BRJ Mamak Telegram Bot");
        URL imageURL = null;
        Image image = null;
        imageURL = new URL("https://i.ibb.co/Gcmvdfd/frame.png");
        image = ImageIO.read(imageURL);
        ImageIcon qrCode = new ImageIcon(image);
        JLabel label = new JLabel(qrCode);

        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);

        // Don't return when the QR Code is still opened.
        Thread.sleep(3500);

        while (frame.isActive()) {
        }
    }

    public String errorMessage(Exception ex) {
        String errorMsg = null;

        if (ex.toString().contains("InputMismatchException")) {
            errorMsg = "\nYou're not inputting an integer, try again.";
        }
        else {
            errorMsg = "\nYour input is incorrect. Try again.";
        }

        return errorMsg;
    }
}
