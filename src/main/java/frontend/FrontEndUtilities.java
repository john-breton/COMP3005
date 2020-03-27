package frontend;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

class FrontEndUtilities {
    static final ImageIcon WINDOW_ICON = new ImageIcon(new ImageIcon(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("logo.png"))).getImage());
    // JComboBox Arrays
    static final String[] resultFilterArr = {"Price", "A-Z", "Z-A", "Year"};
    static final String[] searchFilterArr = {"Title", "Author", "Genre", "ISBN", "Publisher", "Year"};
    static final String[] reportChoiceArr = {"--", "Sales v Expense", "Sales per Genre", "Sales per Author", "Sales per Publisher", "Sales per Month", "Sales per Year", "Expense per Month", "Expense per Year"};
    static final String[] provincesArr = {"--", "AB", "BC", "MB", "NB", "NL", "NS", "NT", "NU", "ON", "PE", "QC", "SK", "YT"};

    /* JTextFields -- Global in order for methods to access */
    /* JLabels -- Global to update users */
    static final JLabel loginSuccess = new JLabel("", JLabel.CENTER);
    static final JTextField usernameField = new JTextField(15);
    static final JPasswordField passwordField = new JPasswordField(15);

    /**
     * Confirms that the user wishes to logout
     */
    static void confirmLogout() {
        JButton logoutButton = new JButton("Logout");
        JButton cancelButton = new JButton("Cancel");
        logoutButton.setBackground(Color.WHITE);
        cancelButton.setBackground(Color.WHITE);

        Object[] options = {logoutButton, cancelButton};
        final JOptionPane areYouSure = new JOptionPane("Are you sure you want to logout?", JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[1]);
        final JDialog dialog = areYouSure.createDialog("Logout");
        dialog.setIconImage(FrontEndUtilities.WINDOW_ICON.getImage());
        dialog.setContentPane(areYouSure);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        logoutButton.addActionListener(e -> {
            new LoginScreen();
            dialog.setVisible(false);
            dialog.dispose();
        });
        cancelButton.addActionListener(e -> dialog.setVisible(false));

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
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
