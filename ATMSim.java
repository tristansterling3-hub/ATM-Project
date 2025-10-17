package ATM;

import javax.swing.JOptionPane;

public class ATMSim {
    public static void main(String[] args) {
        // Create ATM object
        CaldwellTrustDisplay atm = new CaldwellTrustDisplay();
        atm.displayWelcomeScreen(); // Show welcome screen

        int accountNumber;
        int attempts = 0;

        // Prompt user for account number with max 3 attempts
        do {
            accountNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter account number:"));
            attempts++;
        } while (!atm.slideCard(accountNumber) && attempts < 3);

        if (attempts >= 3) {
            JOptionPane.showMessageDialog(null, "Maximum attempts reached. Contact your bank.");
        } else {
            int pin;
            attempts = 0;

            // Prompt user for PIN with max 3 attempts
            do {
                pin = Integer.parseInt(JOptionPane.showInputDialog("Enter PIN:"));
                attempts++;
            } while (!atm.enterPIN(pin) && attempts < 3);

            if (attempts >= 3) {
                JOptionPane.showMessageDialog(null, "Maximum attempts reached. Contact your bank.");
            } else {
                // Show main menu after successful login
                atm.showMainMenu();
            }
        }
    }
}
