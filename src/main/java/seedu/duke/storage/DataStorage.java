//@@author matthewyeo1
package seedu.duke.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import seedu.duke.expense.Expense;
import seedu.duke.messages.Messages;
import seedu.duke.summary.Categories;

/**
 * Handles storage operations for expenses, including saving, loading,
 * ensuring file existence, and resetting data.
 */
public class DataStorage {
    public static String dataFile = "expenses.txt";
    private static final String SEPARATOR = "|";

    /**
     * Ensures that the data file exists.
     */
    public static void ensureFileExists() {
        File file = new File(dataFile);
        try {
            if (file.createNewFile()) {
                Messages.createNewFileMessage();
            }
        } catch (IOException e) {
            System.out.println(Messages.errorMessageTag() + " Error creating data file: " + e.getMessage());
        }
        assert file.exists() : "Data file should exist after ensuring";
    }

    /**
     * Saves expenses to the data file.
     *
     * @param expenses the list of expenses to save
     * @return true if saving was successful, false otherwise
     */
    public static boolean saveExpenses(List<Expense> expenses) {
        try {
            ensureFileExists();
            PrintWriter writer = new PrintWriter(dataFile);
            for (Expense expense : expenses) {
                writer.println(expense.getTitle() + "|" + 
                             expense.getCategory() + "|" + 
                             expense.getDate() + "|" + 
                             expense.getAmount() + "|" + 
                             expense.getDone() + "|" + 
                             expense.getGroupName());
            }
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Loads expenses from the data file.
     *
     * @return the list of loaded expenses
     */
    public static List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        try {
            ensureFileExists();
            Scanner scanner = new Scanner(new File(dataFile));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    String title = parts[0];
                    Categories category = Categories.valueOf(parts[1]);
                    String date = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    //boolean isDone = Boolean.parseBoolean(parts[4]);
                    //String groupName = parts[5];
                    
                    Expense expense = new Expense(title, category, date, amount);
                    //expense.setDone(isDone);
                    //expense.setGroupName(groupName);
                    expenses.add(expense);
                }
            }
            scanner.close();
        } catch (IOException e) {
            // Return empty list if file doesn't exist or can't be read
        }
        return expenses;
    }

    /**
     * Clears the contents of the data file (for testing purposes).
     */
    public static void resetExpenses() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            writer.write("");
        } catch (IOException e) {
            System.out.println(Messages.errorMessageTag() + " Error resetting expenses: " + e.getMessage());
        }
        assert new File(dataFile).length() == 0 : "Data file should be empty after reset";
    }
}
//@@author
