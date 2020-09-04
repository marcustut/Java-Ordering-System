package backend;

import frontend.Main;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.toggle.CustomToggle;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Bot extends AbilityBot {
    // Turn off unwanted default commands provided by Telegram
    private static final CustomToggle toggle = new CustomToggle()
            .turnOff("ban")
            .turnOff("unban")
            .turnOff("demote")
            .turnOff("promote")
            .turnOff("stats")
            .turnOff("claim")
            .turnOff("backup")
            .turnOff("recover")
            .turnOff("report")
            .toggle("commands", "help");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mma");
    private static String BOT_TOKEN, BOT_USERNAME;
    private static int CREATOR_ID;

    /**
     * Constructor used to instantiate the Telegram Bot object
     *
     * @param token    The bot's token for communicating with the Telegram server.
     * @param username The bot's name, which was specified during registration.
     */
    public Bot(String token, String username, int creatorId) {
        super(token, username, toggle);
        BOT_TOKEN = token;
        BOT_USERNAME = username;
        CREATOR_ID = creatorId;
    }

    /**
     * The bot needs to know its master since it has sensitive commands that only the master can use.
     *
     * @return Restaurant Manager's Telegram ID.
     */
    @Override
    public int creatorId() {
        return CREATOR_ID;
    }

    private Predicate<Update> hasMessageWith(String msg) {
        return update -> update.getMessage().getText().equalsIgnoreCase(msg);
    }

    private Predicate<Update> hasCallbackQueryWith(String cb) {
        return update -> update.getCallbackQuery().getData().equalsIgnoreCase(cb);
    }

    private ReplyFlow changeActiveHoursFlow(Menu menu) {
        return ReplyFlow
                .builder(db)
                .action(update -> {
                    // Responding to the callback query sent by buttons
                    long messageId = update.getCallbackQuery().getMessage().getMessageId();
                    long chatId = update.getCallbackQuery().getMessage().getChatId();

                    String breakfastActiveHoursText = String.format("<strong>⌚ Current active hours: </strong><code>%s - %s</code>\n\nSend me the new active hours (eg. 05:00PM - 07:00PM)", menu.getStartTime().format(dateTimeFormatter), menu.getEndTime().plusMinutes(1).format(dateTimeFormatter));

                    EditMessageText newMessage = new EditMessageText()
                            .setChatId(chatId)
                            .setMessageId((int) messageId)
                            .enableHtml(true)
                            .setText(breakfastActiveHoursText);

                    // Try editing the message as response
                    try {
                        execute(newMessage);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                })
                .onlyIf(hasCallbackQueryWith("changeActiveHours"))
                .next(Reply.of(update -> {
                    // Responding to the callback query sent by buttons
                    long chatId = update.getMessage().getChatId();

                    try {
                        LocalTime newStartTime = LocalTime.parse(update.getMessage().getText().substring(0, 7), dateTimeFormatter);
                        LocalTime newEndTime = LocalTime.parse(update.getMessage().getText().substring(10, 17), dateTimeFormatter);

                        menu.setStartTime(newStartTime);
                        menu.setEndTime(newEndTime);

                        String confirmText = String.format("<strong>Active Hours for %s is successfully changed to</strong> <code>%s - %s</code> ✔️", menu.getMenuName(), menu.getStartTime().format(dateTimeFormatter), menu.getEndTime().format(dateTimeFormatter));

                        SendMessage newMessage = new SendMessage()
                                .setChatId(chatId)
                                .enableHtml(true)
                                .setText(confirmText);

                        execute(newMessage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, Update::hasMessage))
                .build();
    }

    private ReplyFlow changeMenuName(Menu menu) {
        return ReplyFlow
                .builder(db)
                .action(update -> {
                    // Responding to the callback query sent by buttons
                    long messageId = update.getCallbackQuery().getMessage().getMessageId();
                    long chatId = update.getCallbackQuery().getMessage().getChatId();

                    String currentMenuName = String.format("📋 Current menu name: <strong>%s</strong>\n\nSend me the new menu name (eg. Lobster Royale)", menu.getMenuName());

                    EditMessageText newMessage = new EditMessageText()
                            .setChatId(chatId)
                            .setMessageId((int) messageId)
                            .enableHtml(true)
                            .setText(currentMenuName);

                    // Try editing the message as response
                    try {
                        execute(newMessage);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                })
                .onlyIf(hasCallbackQueryWith("changeMenuName"))
                .next(Reply.of(update -> {
                    // Responding to the callback query sent by buttons
                    long chatId = update.getMessage().getChatId();

                    try {
                        String newMenuName = update.getMessage().getText();

                        menu.setMenuName(newMenuName);

                        String confirmText = String.format("Menu name is successfully changed to <strong>%s</strong> ✔️", menu.getMenuName());

                        SendMessage newMessage = new SendMessage()
                                .setChatId(chatId)
                                .enableHtml(true)
                                .setText(confirmText);

                        execute(newMessage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, Update::hasMessage))
                .build();
    }

    private ReplyFlow addFood(Menu menu) {
        return ReplyFlow
                .builder(db)
                .action(update -> {
                    // Responding to the callback query sent by buttons
                    long messageId = update.getCallbackQuery().getMessage().getMessageId();
                    long chatId = update.getCallbackQuery().getMessage().getChatId();

                    String templateText = "\n<strong>Name:</strong> " +
                            "\n<strong>Description:</strong> " +
                            "\n<strong>Type:</strong> " +
                            "\n<strong>Price: RM</strong>" +
                            "\n<strong>Preferred:</strong> " +
                            "\n\n<code>Copy and paste this template to add new food 👆</code>";

                    EditMessageText newMessage = new EditMessageText()
                            .setChatId(chatId)
                            .setMessageId((int) messageId)
                            .enableHtml(true)
                            .setText(templateText);

                    try {
                        execute(newMessage);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                })
                .onlyIf(hasCallbackQueryWith("addFood"))
                .next(Reply.of(update -> {
                    // Responding to the callback query sent by buttons
                    long chatId = update.getMessage().getChatId();

                    try {
                        String[] args = update.getMessage().getText().split("\n");

                        String foodName = args[0].substring(6).trim();
                        String description = args[1].substring(13).trim();
                        String type = args[2].substring(6).trim();
                        double price = Double.parseDouble(args[3].substring(9));
                        boolean preferred = args[4].substring(11).contains("y") || args[4].substring(11).contains("Y");

                        Food newFood = new Food(foodName, description, type, price, preferred);
                        menu.addFood(newFood);

                        String successMessage = String.format("<strong>%s</strong> is successfully added to %s with id <code>%s</code> ✔️", foodName, menu.getMenuName(), newFood.getId());

                        SendMessage newMessage = new SendMessage()
                                .setChatId(chatId)
                                .enableHtml(true)
                                .setText(successMessage);

                        execute(newMessage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, Update::hasMessage))
                .build();
    }

    private ReplyFlow addDrink(Menu menu) {
        return ReplyFlow
                .builder(db)
                .action(update -> {
                    // Responding to the callback query sent by buttons
                    long messageId = update.getCallbackQuery().getMessage().getMessageId();
                    long chatId = update.getCallbackQuery().getMessage().getChatId();

                    String templateText = "\n<strong>Name:</strong> " +
                            "\n<strong>Description:</strong> " +
                            "\n<strong>Type:</strong> " +
                            "\n<strong>Temperature:</strong>" +
                            "\n<strong>Price: RM</strong>" +
                            "\n<strong>Preferred:</strong> " +
                            "\n\n<code>Copy and paste this template to add new drink 👆</code>";

                    EditMessageText newMessage = new EditMessageText()
                            .setChatId(chatId)
                            .setMessageId((int) messageId)
                            .enableHtml(true)
                            .setText(templateText);

                    try {
                        execute(newMessage);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                })
                .onlyIf(hasCallbackQueryWith("addDrink"))
                .next(Reply.of(update -> {
                    String[] args = update.getMessage().getText().split("\n");
                    String drinkName = args[0].substring(6);
                    String description = args[1].substring(13);
                    String type = args[2].substring(6);
                    String temperature = args[3].substring(13);
                    double price = Double.parseDouble(args[4].substring(9));
                    boolean preferred = args[5].substring(11).contains("y") || args[4].substring(11).contains("Y");

                    try {
                        Drink newDrink = new Drink(drinkName, description, type, price, preferred, temperature);
                        menu.addDrink(newDrink);

                        String successMessage = String.format("<strong>%s</strong> is successfully added to %s with id <code>%s</code> ✔️", drinkName, menu.getMenuName(), newDrink.getId());

                        SendMessage newMessage = new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .enableHtml(true)
                                .setText(successMessage);

                        execute(newMessage);
                    } catch (MenuItem.InvalidIndex | Drink.InvalidDrinkTemp | TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                }, Update::hasMessage))
                .build();
    }

    private ReplyFlow addCombo(Menu menu) {
        return ReplyFlow
                .builder(db)
                .action(update -> {
                    // Responding to the callback query sent by buttons
                    long messageId = update.getCallbackQuery().getMessage().getMessageId();
                    long chatId = update.getCallbackQuery().getMessage().getChatId();

                    String templateText = "\n<strong>Name:</strong> " +
                            "\n<strong>Description:</strong> " +
                            "\n<strong>Type:</strong> " +
                            "\n<strong>Size:</strong> " +
                            "\n<strong>Price: RM</strong>" +
                            "\n<strong>Preferred:</strong> " +
                            "\n\n<code>Copy and paste this template to add new Combo 👆</code>";

                    EditMessageText newMessage = new EditMessageText()
                            .setChatId(chatId)
                            .setMessageId((int) messageId)
                            .enableHtml(true)
                            .setText(templateText);

                    try {
                        execute(newMessage);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                })
                .onlyIf(hasCallbackQueryWith("addCombo"))
                .next(Reply.of(update -> {
                    String[] args = update.getMessage().getText().split("\n");
                    String comboName = args[0].substring(6);
                    String description = args[1].substring(13);
                    String type = args[2].substring(6);
                    String size = args[3].substring(6);
                    double price = Double.parseDouble(args[4].substring(9));
                    boolean preferred = args[5].substring(11).contains("y") || args[4].substring(11).contains("Y");

                    try {
                        Combo newCombo = new Combo(comboName, description, type, price, preferred, size);
                        menu.addCombo(newCombo);

                        String successMessage = String.format("<strong>%s</strong> is successfully added to %s with id <code>%s</code> ✔️", comboName, menu.getMenuName(), newCombo.getId());

                        SendMessage newMessage = new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .enableHtml(true)
                                .setText(successMessage);

                        execute(newMessage);
                    } catch (MenuItem.InvalidIndex | Combo.InvalidSize | TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                }, Update::hasMessage))
                .build();
    }

    public ReplyFlow menu() {
        String askMenu = "<strong>Select one of the menus \uD83D\uDC47</strong>";

        // Buttons for selecting menu
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        rowInline.add(new InlineKeyboardButton().setText("Breakfast").setCallbackData("breakfastMenu"));
        rowInline.add(new InlineKeyboardButton().setText("Lunch").setCallbackData("lunchMenu"));
        rowInline.add(new InlineKeyboardButton().setText("Dinner").setCallbackData("dinnerMenu"));

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        // Buttons for editing menu
        InlineKeyboardMarkup markupInline2 = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        rowInline2.add(new InlineKeyboardButton().setText("Change Active Hours ⏳").setCallbackData("changeActiveHours"));
        rowInline2.add(new InlineKeyboardButton().setText("Change Menu Name 📋").setCallbackData("changeMenuName"));
        rowsInline2.add(rowInline2);

        rowInline2 = new ArrayList<>();

        rowInline2.add(new InlineKeyboardButton().setText("Add Food 🍲").setCallbackData("addFood"));
        rowInline2.add(new InlineKeyboardButton().setText("Add Drink 🧃").setCallbackData("addDrink"));
        rowInline2.add(new InlineKeyboardButton().setText("Add Combo 🍱").setCallbackData("addCombo"));

        rowsInline2.add(rowInline2);
        markupInline2.setKeyboard(rowsInline2);

        return ReplyFlow
                .builder(db)
                .onlyIf(hasMessageWith("/menu"))
                .action(update -> silent.execute(new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText(askMenu)
                                .enableHtml(true)
                                .setReplyMarkup(markupInline)
                        )
                )
                .next(ReplyFlow
                        .builder(db)
                        .action(update -> {
                                    // Responding to the callback query sent by buttons
                                    long messageId = update.getCallbackQuery().getMessage().getMessageId();
                                    long chatId = update.getCallbackQuery().getMessage().getChatId();

                                    Menu breakfastMenu = Main.getMenu(Main.BREAKFAST);
                                    String breakfastMenuText = breakfastMenu.toString();

                                    EditMessageText newMessage = new EditMessageText()
                                            .setChatId(chatId)
                                            .setMessageId((int) messageId)
                                            .enableHtml(true)
                                            .setText(breakfastMenuText)
                                            .setReplyMarkup(markupInline2);

                                    // Try editing the message as response
                                    try {
                                        execute(newMessage);
                                    } catch (TelegramApiException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                        )
                        .onlyIf(hasCallbackQueryWith("breakfastMenu"))
                        .next(changeActiveHoursFlow(Main.getMenu(Main.BREAKFAST)))
                        .next(changeMenuName(Main.getMenu(Main.BREAKFAST)))
                        .next(addFood(Main.getMenu(Main.BREAKFAST)))
                        .next(addDrink(Main.getMenu(Main.BREAKFAST)))
                        .next(addCombo(Main.getMenu(Main.BREAKFAST)))
                        .build()
                )
                .next(ReplyFlow
                        .builder(db)
                        .action(update -> {
                                    // Responding to the callback query sent by buttons
                                    long messageId = update.getCallbackQuery().getMessage().getMessageId();
                                    long chatId = update.getCallbackQuery().getMessage().getChatId();

                                    Menu lunchMenu = Main.getMenu(Main.LUNCH);
                                    String lunchMenuText = lunchMenu.toString();

                                    EditMessageText newMessage = new EditMessageText()
                                            .setChatId(chatId)
                                            .setMessageId((int) messageId)
                                            .enableHtml(true)
                                            .setText(lunchMenuText)
                                            .setReplyMarkup(markupInline2);

                                    // Try editing the message as response
                                    try {
                                        execute(newMessage);
                                    } catch (TelegramApiException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                        )
                        .onlyIf(hasCallbackQueryWith("lunchMenu"))
                        .next(changeActiveHoursFlow(Main.getMenu(Main.LUNCH)))
                        .next(changeMenuName(Main.getMenu(Main.LUNCH)))
                        .next(addFood(Main.getMenu(Main.LUNCH)))
                        .next(addDrink(Main.getMenu(Main.LUNCH)))
                        .next(addCombo(Main.getMenu(Main.LUNCH)))
                        .build()
                )
                .next(ReplyFlow
                        .builder(db)
                        .action(update -> {
                                    // Responding to the callback query sent by buttons
                                    long messageId = update.getCallbackQuery().getMessage().getMessageId();
                                    long chatId = update.getCallbackQuery().getMessage().getChatId();

                                    Menu dinnerMenu = Main.getMenu(Main.DINNER);
                                    String dinnerMenuText = dinnerMenu.toString();

                                    EditMessageText newMessage = new EditMessageText()
                                            .setChatId(chatId)
                                            .setMessageId((int) messageId)
                                            .enableHtml(true)
                                            .setText(dinnerMenuText)
                                            .setReplyMarkup(markupInline2);

                                    // Try editing the message as response
                                    try {
                                        execute(newMessage);
                                    } catch (TelegramApiException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                        )
                        .onlyIf(hasCallbackQueryWith("dinnerMenu"))
                        .next(changeActiveHoursFlow(Main.getMenu(Main.DINNER)))
                        .next(changeMenuName(Main.getMenu(Main.DINNER)))
                        .next(addFood(Main.getMenu(Main.DINNER)))
                        .next(addDrink(Main.getMenu(Main.DINNER)))
                        .next(addCombo(Main.getMenu(Main.DINNER)))
                        .build()
                )
                .build();
    }

    public ReplyFlow currentMenu() {
        return ReplyFlow
                .builder(db)
                .onlyIf(hasMessageWith("/currentmenu"))
                .action(update -> {
                    Menu currentServingMenu = Main.getCurrentServingMenu();
                    String message = currentServingMenu.toString() + "\n\n<u>Above show the current serving menu</u>";

                    silent.execute(new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(message)
                            .enableHtml(true)
                    );
                })
                .build();
    }

    public ReplyFlow currentSales() {

        return ReplyFlow
                .builder(db)
                .onlyIf(hasMessageWith("/sales"))
                .action(update -> {
                    Restaurant restaurant = Main.getRestaurant();
                    List<Sale> sales = restaurant.getAllSales();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ssa");
                    StringBuilder salesReport = new StringBuilder(String.format("<strong>Sales Report \uD83D\uDCC8</strong>\n<em>%s</em>", LocalTime.now().format(formatter)));

                    for (Sale sale : sales) {
                        salesReport.append(String.format("\n\n<strong>Sale ID:</strong> %27s\n<strong>Customer ID:</strong> %16s\n<strong>Table No.:</strong>                        %02d\n<strong>Subtotal:</strong>             RM%7.2f\n<strong>SST:</strong>                        RM%7.2f\n<strong>Total</strong> 💰:             <strong><em>RM%7.2f</em></strong>", sale.getId(), sale.getCustomer().getId(), sale.getTable().getTableNo(), sale.getCustomer().getAllSubtotal(), sale.getCustomer().getAllSST(), sale.getCustomer().getAllTotal()));
                    }

                    silent.execute(new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(salesReport.toString())
                            .enableHtml(true)
                    );
                })
                .build();
    }

    public ReplyFlow totalSales() {

        return ReplyFlow
                .builder(db)
                .onlyIf(hasMessageWith("/totalsales"))
                .action(update -> {
                    Restaurant restaurant = Main.getRestaurant();
                    List<Sale> sales = restaurant.getAllSales();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ssa");
                    String totalSalesNow = String.format("<strong>Total Sales - %d \uD83D\uDCC8</strong>\n<em>as of %s</em>", sales.size(), LocalTime.now().format(formatter));

                    silent.execute(new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(totalSalesNow)
                            .enableHtml(true)
                    );
                })
                .build();
    }
}
