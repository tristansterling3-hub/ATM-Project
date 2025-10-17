package ATM;

import javax.swing.*;
import java.util.Map;

public class Withdrawal {
    private Map<Integer, Double> accounts;
    private int currentAccountNumber;

    public Withdrawal(Map<Integer, Double> accounts, int currentAccountNumber) {
        this.accounts = accounts;
        this.currentAccountNumber = currentAccountNumber;
    }

    // Subtract amount from account balance if sufficient funds
    public boolean withdraw(double amount) {
        double currentBalance = accounts.get(currentAccountNumber);
        if (currentBalance >= amount) {
            accounts.put(currentAccountNumber, currentBalance - amount);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient funds.\n----------------------------");
            return false;
        }
    }
}
