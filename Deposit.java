package ATM;

import javax.swing.*;
import java.util.Map;

public class Deposit {
    private Map<Integer, Double> accounts;
    private int currentAccountNumber;

    public Deposit(Map<Integer, Double> accounts, int currentAccountNumber) {
        this.accounts = accounts;
        this.currentAccountNumber = currentAccountNumber;
    }

    // Add amount to account balance
    public boolean deposit(double amount) {
        if (amount < 0) {
            JOptionPane.showMessageDialog(null, "Cannot deposit a negative amount.");
            return false;
        }
        accounts.put(currentAccountNumber, accounts.get(currentAccountNumber) + amount);
        return true;
    }
}
