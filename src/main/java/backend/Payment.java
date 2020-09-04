package backend;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

public class Payment {
    public static final HashMap<Integer, String> PAYMENT_STATUS = new HashMap<Integer, String>() {
        {
            put(1, "Not paid");
            put(2, "Processing");
            put(3, "Paid");
        }
    };
    public static final HashMap<Integer, String> PAYMENT_METHOD = new HashMap<Integer, String>() {
        {
            put(1, "Cash");
            put(2, "E-Wallet");
            put(3, "Credit/Debit Card");
        }
    };
    private String status, paymentMethod;
    private double amountToPay;
    private double amountReceived;
    private Date paymentDate;

    // Constructors
    public Payment() throws InvalidStatus, InvalidPaymentMethod {
       this(1, 1, 0.0, new Date());
    }

    public Payment(double amountToPay) throws InvalidStatus, InvalidPaymentMethod {
        this(1, 1, amountToPay, new Date());
    }

    public Payment(int paymentMethod, double amountToPay) throws InvalidStatus, InvalidPaymentMethod {
        this(1, paymentMethod, amountToPay, new Date());
    }

    public Payment(int paymentMethod, double amountToPay, Date paymentDate) throws InvalidStatus, InvalidPaymentMethod {
        this(1, paymentMethod, amountToPay, paymentDate);
    }

    public Payment(int status, int paymentMethod, double amountToPay, Date paymentDate) throws InvalidStatus, InvalidPaymentMethod {
        if (status < 1 || status > 3)
            throw new InvalidStatus("The status provided is invalid. (Accepted are 1 - Not paid, 2 - Processing, 3 - Paid only)");

        if (paymentMethod < 1 || paymentMethod > 3)
            throw new InvalidPaymentMethod("The status provided is invalid. (Accepted are 1 - Not paid, 2 - Processing, 3 - Paid only)");

        this.status = PAYMENT_STATUS.get(status);
        this.paymentMethod = PAYMENT_METHOD.get(paymentMethod);
        this.amountToPay = amountToPay;
        this.paymentDate = paymentDate;
        this.amountReceived = 0;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(int status) throws InvalidStatus {
        if (status < 1 || status > 3)
            throw new InvalidStatus("The status provided is invalid. (Accepted are 1 - Not paid, 2 - Processing, 3 - Paid only)");

        this.status = PAYMENT_STATUS.get(status);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) throws InvalidPaymentMethod {
        if (paymentMethod < 1 || paymentMethod > 3)
            throw new InvalidPaymentMethod("The status provided is invalid. (Accepted are 1 - Not paid, 2 - Processing, 3 - Paid only)");

        this.paymentMethod = PAYMENT_METHOD.get(paymentMethod);
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    // Exception class
    public class InvalidStatus extends Exception {
        public InvalidStatus(String message) {
            super(message);
        }
    }

    public class InvalidPaymentMethod extends Exception {
        public InvalidPaymentMethod(String message) {
            super(message);
        }
    }

    // User defined methods
    public void selectPaymentMethod() {

    }

    public double pay(double amountPaid) {
        this.amountReceived = amountPaid;
        return amountPaid - this.amountToPay;
    }

    public static boolean showQRCode() throws IOException, InterruptedException {
        // JFrame to display Image
        JFrame frame = new JFrame("Touch N Go E-Wallet");
        URL imageURL = new URL("https://i.ibb.co/dBG5bpC/Tn-G-QRCode.png");
        Image image = ImageIO.read(imageURL);
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

        return true;
    }
}
