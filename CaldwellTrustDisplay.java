package ATM;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * CaldwellTrustDisplay represents the ATM logic, including account management,
 * deposits, withdrawals, transaction history, and file I/O for balances.
 */
public class CaldwellTrustDisplay {

    private Map<Integer, Double> accounts = new HashMap<>(); // Stores accountNumber -> balance
    private Map<Integer, Integer> pins = new HashMap<>();     // Stores accountNumber -> PIN
    private int currentAccountNumber;
    private int currentPIN;

    private final String TRANSACTIONS_FILE = "transactions.txt"; // Relative to JAR folder
    private final String BALANCES_FILE = "balances.txt";         // Relative to JAR folder

    public CaldwellTrustDisplay() {
        // Initialize example accounts and PINs
        accounts.put(1234, 1000.0);
        accounts.put(2341, 2000.0);
        accounts.put(3412, 3000.0);
        accounts.put(4123, 4000.0);

        pins.put(1234, 234);
        pins.put(2341, 341);
        pins.put(3412, 412);
        pins.put(4123, 123);

        // Load balances from file (if exists)
        loadBalancesFromFile();
    }

    // ---------------- Welcome Screen ----------------
    public void displayWelcomeScreen() {
        JOptionPane.showMessageDialog(null,
                "Welcome to Caldwell Bank & Trust\n" +
                        "--------------------------\n" +
                        "ATM Ready\n--------------------------\n" +
                        "Please Insert or Slide Card");
    }

    // ---------------- Account Verification ----------------
    public boolean slideCard(int accountNumber) {
        currentAccountNumber = accountNumber;
        return accounts.containsKey(accountNumber);
    }

    public boolean enterPIN(int pin) {
        currentPIN = pin;
        return pins.containsKey(currentAccountNumber) && pins.get(currentAccountNumber) == pin;
    }

    // ---------------- Main Menu ----------------
    public void showMainMenu() {
        char choice;

        do {
            String menu = "Main Menu:\n--------------------------\n" +
                    "A. Balance Inquiry\n" +
                    "B. Fast Cash ($100)\n" +
                    "C. Withdrawal\n" +
                    "D. Deposit\n" +
                    "E. View Transaction History\n" +
                    "F. Quit\n--------------------------";
            choice = JOptionPane.showInputDialog(menu).toLowerCase().charAt(0);
            mainMenuChoice(choice);
        } while (choice != 'f');
    }

    private void mainMenuChoice(char choice) {
        switch (choice) {
            case 'a': // Balance inquiry
                JOptionPane.showMessageDialog(null, "Balance: $" + getBalance());
                break;

            case 'b': // Fast Cash $100
                double fastCashAmount = 100.0;
                Withdrawal fastCash = new Withdrawal(accounts, currentAccountNumber);
                if (fastCash.withdraw(fastCashAmount)) {
                    JOptionPane.showMessageDialog(null, "Withdrawing $" + fastCashAmount);
                    recordTransaction("Fast Cash", fastCashAmount);
                }
                break;

            case 'c': // Withdrawal
                double withdrawalAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter withdrawal amount:"));
                Withdrawal withdrawal = new Withdrawal(accounts, currentAccountNumber);
                if (withdrawal.withdraw(withdrawalAmount)) {
                    JOptionPane.showMessageDialog(null, "Withdrawing $" + withdrawalAmount);
                    recordTransaction("Withdrawal", withdrawalAmount);
                }
                break;

            case 'd': // Deposit
                double depositAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount to deposit:"));
                Deposit deposit = new Deposit(accounts, currentAccountNumber);
                if (deposit.deposit(depositAmount)) {
                    JOptionPane.showMessageDialog(null,
                            "Depositing $" + depositAmount + "\nDeposit completed.");
                    recordTransaction("Deposit", depositAmount);
                }
                break;

            case 'e': // View transaction history
                showTransactionHistory();
                break;

            case 'f': // Quit
                boolean printReceipt = JOptionPane.showConfirmDialog(null, "Do you want a receipt?") == JOptionPane.YES_OPTION;
                endTransaction(printReceipt);
                break;

            default:
                JOptionPane.showMessageDialog(null, "Invalid choice.");
        }
    }

    // ---------------- Balance ----------------
    public double getBalance() {
        return accounts.get(currentAccountNumber);
    }

    // ---------------- Transaction History ----------------
    public void recordTransaction(String type, double amount) {
        try {
            File file = new File(TRANSACTIONS_FILE);
            if (!file.exists()) file.createNewFile(); // Create file if missing
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            String log = String.format("Account %d | %s: $%.2f | Balance: $%.2f | %s",
                    currentAccountNumber,
                    type,
                    amount,
                    getBalance(),
                    new java.util.Date());

            writer.write(log);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error recording transaction: " + e.getMessage());
        }
    }

    public void showTransactionHistory() {
        try {
            File file = new File(TRANSACTIONS_FILE);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No transaction history found.");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder history = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Account " + currentAccountNumber)) {
                    history.append(line).append("\n");
                }
            }
            reader.close();

            if (history.length() > 0) {
                JOptionPane.showMessageDialog(null, "Transaction History:\n" + history.toString());
            } else {
                JOptionPane.showMessageDialog(null, "No transaction history found for this account.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading transaction history: " + e.getMessage());
        }
    }

    // ---------------- End Transaction ----------------
    public void endTransaction(boolean printReceipt) {
        saveBalancesToFile();
        if (printReceipt) printReceipt();
        JOptionPane.showMessageDialog(null, "Thank you for using CBT ATM.\n----------------------------");
    }

    private void printReceipt() {
        JOptionPane.showMessageDialog(null,
                "Receipt\n-----------------\n" +
                        "Account: " + currentAccountNumber + "\n" +
                        "Balance: $" + getBalance() + "\n" +
                        "Date: " + new java.util.Date() + "\n-----------------");
    }

    // ---------------- File I/O ----------------
    public void loadBalancesFromFile() {
        try {
            File file = new File(BALANCES_FILE);
            if (!file.exists()) {
                file.createNewFile(); // create if missing
                saveBalancesToFile(); // save default balances
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int acc = Integer.parseInt(parts[0]);
                    double bal = Double.parseDouble(parts[1]);
                    accounts.put(acc, bal);
                }
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading balances: " + e.getMessage());
        }
    }

    public void saveBalancesToFile() {
        try {
            File file = new File(BALANCES_FILE);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<Integer, Double> entry : accounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving balances: " + e.getMessage());
        }
    }
}
