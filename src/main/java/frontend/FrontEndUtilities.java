package frontend;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FrontEndUtilities {
    public static final ImageIcon WINDOW_ICON = new ImageIcon(new ImageIcon(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("logo.png"))).getImage());
    // JComboBox Arrays
    static final String[] resultFilterArr = {"Price: High to low", "Price: Low to high", "Title: A-Z", "Title: Z-A", "Year: High to low", "Year: Low to high"};
    static final String[] searchFilterArr = {"Title", "Author", "Genre", "ISBN", "Publisher", "Year"};
    static final String[] reportChoiceArr = {"--", "Sales v Expense", "Sales per Genre", "Sales per Author", "Sales per Publisher"};
    static final String[] reportTimeFrameArr = {"Year", "Month", "Week", "Day"};
    static final String[] reportSortArr = {"Name: A-Z", "Name: Z-A", "Date: High to Low", "Date: Low to High", "Quantity: High to Low", "Quantity: Low to High", "Revenue: High to Low", "Revenue: Low to High", "Cost: High to Low", "Cost: Low to High", "Profit: High to Low", "Profit: Low to High"};
    static final String[] provincesArr = {"--", "AB", "BC", "MB", "NB", "NL", "NS", "NT", "NU", "ON", "PE", "QC", "SK", "YT"};
    static final String[] statusArr = {"--", "Processing", "Ready for Shipment", "On the Way!", "Delivered"};

    /* JTextFields -- Global in order for methods to access */
    /* JLabels -- Global to update users */
    static final JLabel loginSuccess = new JLabel("", JLabel.CENTER);
    static final JTextField usernameField = new JTextField(15);
    static final JPasswordField passwordField = new JPasswordField(15);

    /**
     * Confirms that the user wishes to logout
     */
    static void confirmLogout(JFrame frame) {
        JButton logoutButton = formatButton("Logout");
        logoutButton.setToolTipText("Logout and return to the login page");
        JButton cancelButton = formatButton("Cancel");
        cancelButton.setToolTipText("Cancel the logout and remain logged in");

        Object[] options = {logoutButton, cancelButton};
        final JOptionPane areYouSure = new JOptionPane("Are you sure you want to logout?", JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[1]);
        final JDialog dialog = areYouSure.createDialog("Logout");
        dialog.setIconImage(FrontEndUtilities.WINDOW_ICON.getImage());
        dialog.setContentPane(areYouSure);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        logoutButton.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();
            dialog.setVisible(false);
            dialog.dispose();
            new LoginScreen();
        });
        cancelButton.addActionListener(e -> dialog.setVisible(false));

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Configure a JFrame for display.
     *
     * @param frame The JFrame to be configured.
     */
    static void configureFrame(JFrame frame) {
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(FrontEndUtilities.WINDOW_ICON.getImage());
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setTitle("LookInnaBook");
        frame.setVisible(true);
    }

    /**
     * Prepare a JButton with default formatting options.
     *
     * @param name The name of the JButton.
     * @return The newly created and correctly formatted JButton.
     */
    public static JButton formatButton(String name) {
        JButton button = new JButton(name);
        button.setBackground(Color.WHITE);
        return button;
    }

    /**
     * Checks if a String contains only unicode letters.
     *
     * @param s The String to be checked
     * @return True if the String contains only unicode letters, false otherwise.
     */
    static boolean check(String s) {
        if (s == null) {
            return true;
        }

        int len = s.length();
        for (int i = 0; i < len; i++) {
            if ((!Character.isLetter(s.charAt(i)))) {
                return true;
            }
        }
        return false;
    }

}
