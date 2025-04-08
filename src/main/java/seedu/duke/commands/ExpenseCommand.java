//@@author mohammedhamdhan
package seedu.duke.commands;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.knowm.xchart.AnnotationTextPanel;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.PieStyler.LabelType;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;

import seedu.duke.currency.Currency;
import seedu.duke.expense.BudgetManager;
import seedu.duke.expense.Expense;
import seedu.duke.friends.Friend;
import seedu.duke.friends.GroupManager;
import seedu.duke.summary.Categories;
import seedu.duke.summary.ExpenseClassifier;

/**
 * Handles expense-related commands entered by the user.
 */
public class ExpenseCommand {
    private static final List<JFrame> activeChartWindows = new ArrayList<>();

    private BudgetManager budgetManager;
    private Scanner scanner;
    private GroupManager groupManager = new GroupManager();
    private Currency currency;

    /**
     * Constructs an ExpenseCommand with the given BudgetManager and Scanner.
     *
     * @param budgetManager the budget manager to use
     * @param scanner       the scanner for user input
     */
    public ExpenseCommand(BudgetManager budgetManager, Scanner scanner, Currency currency) {
        assert budgetManager != null : "BudgetManager cannot be null";
        assert scanner != null : "Scanner cannot be null";
        this.budgetManager = budgetManager;
        this.scanner = scanner;
        this.currency = currency;

        initializeVisualizationCleanup();
    }

    /**
     * Closes all active chart windows.
     */
    private static void closeAllChartWindows() {
        for (JFrame window : activeChartWindows) {
            if (window != null && window.isDisplayable()) {
                window.dispose();
            }
        }
        activeChartWindows.clear();
    }

    /**
     * Initializes the automatic cleanup of visualization windows.
     * This should be called when the ExpenseCommand is constructed.
     */
    private void initializeVisualizationCleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closeAllChartWindows();
        }));
    }

    //@@author

    //@@author matthewyeo1

    public static boolean isAddCommandValid(String[] parts) {
        return parts.length >= 5 && !parts[1].trim().isEmpty() &&
                !parts[2].trim().isEmpty() && !parts[3].trim().isEmpty() && !parts[4].trim().isEmpty();
    }

    public static boolean isDeleteCommandValid(String[] parts) {
        return parts.length >= 2 && !parts[1].trim().isEmpty();
    }

    public static boolean isEditCommandValid(String[] parts) {
        return parts.length >= 6 && !parts[1].trim().isEmpty() && !parts[5].trim().isEmpty();
    }

    public static boolean isValidDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }

        // Check format DD-MM-YYYY
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return false;
        }

        String[] parts = date.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        // Validate year (assuming expenses can't be from before 2000)
        if (year < 2000 || year > 2100) {
            return false;
        }

        // Validate month
        if (month < 1 || month > 12) {
            return false;
        }

        // Validate day based on month
        int maxDays;
        if (month == 2) {
            // Check for leap year
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                maxDays = 29;
            } else {
                maxDays = 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            maxDays = 30;
        } else {
            maxDays = 31;
        }

        return day >= 1 && day <= maxDays;
    }

    public static boolean isValidDescription(String description) {
        return description.length() <= 200;
    }

    public static boolean isUniqueTitle(String title, List<Expense> expenses) {
        for (Expense expense : expenses) {
            if (expense.getTitle().equals(title)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isUniqueDate(String date, List<Expense> expenses) {
        for (Expense expense : expenses) {
            if (expense.getDate().equals(date)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAmountBelowCap(Double amount, Currency currency) {
        double maxSGDAmount = 50000.0;
        double sgdEquivalentAmount;
        String currentCurrency = currency.getCurrentCurrency();

        if (currentCurrency.equals("SGD")) {
            Double exchangeRate = currency.getExchangeRate(currentCurrency);
            if (exchangeRate == null) {
                System.out.println("Error: Unable to retrieve exchange rate for " + currentCurrency);
                return false;
            }
            sgdEquivalentAmount = amount / exchangeRate;
        } else {
            sgdEquivalentAmount = amount;
        }

        if (sgdEquivalentAmount > maxSGDAmount) {
            System.out.println("The entered amount exceeds the maximum " +
                    "allowed limit of 50,000 SGD (or its equivalent).");
            return false;
        }
        return true;
    }
    //@@author

    //@@author mohammedhamdhan
    /**
     * Executes the add expense command.
     */
    public void executeAddExpense(String userInput) {
        // Split and trim each part to handle multiple spaces
        String[] parts = userInput.split("/");
        if (parts.length < 5) {
            System.out.println("Invalid format. Usage: add/<title>/<category>/<date>/<amount>");
            return;
        }

        // Trim each part to handle multiple spaces
        String title = parts[1].trim();
        String categoryStr = parts[2].trim();
        String date = parts[3].trim();
        String amountStr = parts[4].trim();

        // Edge case: Check if any required field is empty after trimming
        if (title.isEmpty() || categoryStr.isEmpty() || date.isEmpty() || amountStr.isEmpty()) {
            System.out.println("Invalid format. None of the fields can be empty. " +
                    "Usage: add/<title>/<category>/<date>/<amount>");
            return;
        }

        try {
            // Validate title uniqueness
            List<Expense> expenses = budgetManager.getAllExpenses();
            if (!isUniqueTitle(title, expenses) && !isUniqueDate(date, expenses)) {
                System.out.println("Expense with the same title and date already exists.");
                return;
            }

            // Validate category (case-insensitive)
            Categories category;
            try {
                // Convert to proper case format (first letter uppercase, rest lowercase)
                categoryStr = categoryStr.toLowerCase();
                categoryStr = categoryStr.substring(0, 1).toUpperCase() + categoryStr.substring(1);
                category = Categories.valueOf(categoryStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category. Please use one of: Food, Shopping, Travel," +
                        " Entertainment, Miscellaneous");
                return;
            }

            // Validate date format and values
            if (!isValidDate(date)) {
                System.out.println("Invalid date. Please enter a valid date in DD-MM-YYYY format.");
                return;
            }

            // Validate amount
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format. Please enter a valid number.");
                return;
            }

            if (amount < 0) {
                System.out.println("Amount cannot be negative.");
                return;
            }

            if (amount == 0) {
                System.out.println("Amount cannot be zero.");
                return;
            }

            if (!isAmountBelowCap(amount, currency)) {
                return;
            }

            // Create and add the expense
            Expense expense = new Expense(title, category, date, amount);
            //budgetManager.addExpense(expense);
            String transaction = String.format("%s|%s|%s|%.2f", title, categoryStr, date, amount);
            ExpenseStorage.saveTransaction(transaction);
            System.out.println("Expense added successfully:");
            System.out.println(expense);

        } catch (Exception e) {
            System.out.println("Error adding expense: " + e.getMessage());
        }
    }
    //@@author

    //@@author matthewyeo1
    /**
     * Executes the delete expense command by setting the expense amount to 0.0.
     */
    public void executeDeleteExpense(String userInput) {
        try {
            // Split into command and expense ID
            String[] parts = userInput.split("/", 2);
            if (!isDeleteCommandValid(parts)) {
                System.out.println("Invalid format. Usage: delete/<expense ID>");
                return;
            }

            int index = Integer.parseInt(parts[1].trim()) - 1; // Convert to 0-based index

            // Load expenses from the file with checksum verification
            List<String> rawExpenses = ExpenseStorage.loadExpenses();
            if (rawExpenses.isEmpty()) {
                System.out.println("No expenses found. The file may have been tampered with and cleared.");
                return;
            }

            // Validate the index
            if (index < 0 || index >= rawExpenses.size()) {
                System.out.println("Please enter a valid expense number.");
                return;
            }

            // Parse the expense to be deleted
            String rawExpense = rawExpenses.get(index);
            String[] expenseParts = rawExpense.split("\\|");
            if (expenseParts.length < 4) { // Ensure the record has all required fields
                System.out.println("Malformed expense record detected at index " + index);
                return;
            }

            // Extract details for confirmation
            String title = expenseParts[0];
            String category = expenseParts[1];
            String date = expenseParts[2];
            double amount = Double.parseDouble(expenseParts[3]);

            System.out.println("Are you sure you want to delete this expense? (y/n)");
            System.out.println("Title: " + title);
            System.out.println("Category: " + category);
            System.out.println("Date: " + date);
            System.out.println("Amount: " + String.format("%.2f", amount));

            String confirmation = scanner.nextLine().trim().toLowerCase();
            if (!confirmation.equals("y")) {
                System.out.println("Deletion aborted.");
                return;
            }

            // Remove the expense from the list
            rawExpenses.remove(index);

            // Save the updated list back to the file and update the checksum
            try (PrintWriter writer = new PrintWriter(new FileWriter(ExpenseStorage.expensesFile))) {
                for (String record : rawExpenses) {
                    writer.println(record);
                }
            } catch (IOException e) {
                System.out.println("Error writing to expenses file: " + e.getMessage());
                return;
            }

            // Update the checksum
            ExpenseStorage.updateChecksum();

            System.out.println("Expense deleted successfully:");
            System.out.println("Title: " + title);
            System.out.println("Category: " + category);
            System.out.println("Date: " + date);
            System.out.println("Amount: " + String.format("%.2f", amount));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter a valid expense ID.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please enter a valid expense number.");
        } catch (Exception e) {
            System.out.println("Error deleting expense: " + e.getMessage());
        }
    }
    //@@author

    //@@author mohammedhamdhan
    /**
     * Executes the edit expense command.
     */
    private int getExpenseCountFromFile() {
        List<String> rawExpenses = ExpenseStorage.loadExpenses();
        int validExpenseCount = 0;

        for (String rawExpense : rawExpenses) {
            if (rawExpense == null || rawExpense.trim().isEmpty()) {
                continue; // Skip empty or malformed lines
            }

            // Ensure the line has all required fields
            String[] parts = rawExpense.split("\\|");
            if (parts.length >= 4) {
                validExpenseCount++;
            }
        }

        return validExpenseCount;
    }

    public void executeEditExpense(String userInput) {
        try {
            // Split by forward slash and handle multiple spaces
            String[] parts = userInput.split("/");
            if (parts.length < 6) {
                System.out.println("Invalid format. Usage: edit/<expense ID>/<new title>/<new category>/" +
                        "<new date>/<new amount>");
                return;
            }

            // Trim each part to handle multiple spaces
            String expenseIdStr = parts[1].trim();
            String title = parts[2].trim();
            String categoryStr = parts[3].trim();
            String date = parts[4].trim();
            String amountStr = parts[5].trim();

            // Edge case: Check if expense ID is empty after trimming
            if (expenseIdStr.isEmpty()) {
                System.out.println("Expense ID cannot be empty." +
                        " Usage: edit/<expense ID>/<new title>/<new category>/<new date>/<new amount>");
                return;
            }

            int index = Integer.parseInt(expenseIdStr) - 1; // Convert to 0-based index
            if (index < 0 || index >= getExpenseCountFromFile()) {
                System.out.println("Please enter a valid expense number.");
                return;
            }

            // Load expenses from the file with checksum verification
            List<String> rawExpenses = ExpenseStorage.loadExpenses();
            if (rawExpenses.isEmpty()) {
                System.out.println("No expenses found. The file may have been tampered with and cleared.");
                return;
            }

            // Parse the existing expense record
            String existingRecord = rawExpenses.get(index);
            String[] existingParts = existingRecord.split("\\|");
            if (existingParts.length < 4) {
                System.out.println("Malformed expense record detected at index " + index);
                return;
            }

            // Extract existing values
            String existingTitle = existingParts[0];
            String existingCategory = existingParts[1];
            String existingDate = existingParts[2];
            double existingAmount = Double.parseDouble(existingParts[3]);
            //boolean existingDone = Boolean.parseBoolean(existingParts[4]);
            //String existingGroupName = existingParts[5];

            // Handle 'x' values for fields to keep unchanged
            title = title.equalsIgnoreCase("x") ? existingTitle : title;
            categoryStr = categoryStr.equalsIgnoreCase("x") ? existingCategory : categoryStr;
            date = date.equalsIgnoreCase("x") ? existingDate : date;
            double amount = amountStr.equalsIgnoreCase("x") ? existingAmount : Double.parseDouble(amountStr);

            // Validate category if provided
            Categories category = null;
            if (!categoryStr.equals(existingCategory)) {
                try {
                    // Convert to proper case format (first letter uppercase, rest lowercase)
                    categoryStr = categoryStr.toLowerCase();
                    categoryStr = categoryStr.substring(0, 1).toUpperCase() + categoryStr.substring(1);
                    category = Categories.valueOf(categoryStr);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid category. Please use one of:" +
                            " Food, Shopping, Travel, Entertainment, Miscellaneous");
                    return;
                }
            }

            // Validate date if provided
            if (!date.equals(existingDate) && !isValidDate(date)) {
                System.out.println("Invalid date format. Please enter a valid date in DD-MM-YYYY format.");
                return;
            }

            // Validate amount if provided
            if (amount != existingAmount) {
                if (amount < 0) {
                    System.out.println("Amount cannot be negative.");
                    return;
                }
                if (amount == 0) {
                    System.out.println("Amount cannot be zero.");
                    return;
                }
                if (!isAmountBelowCap(amount, currency)) {
                    return;
                }
            }

            // Create the updated expense record
            String updatedRecord = String.format("%s|%s|%s|%.2f",
                    title, categoryStr, date, amount);

            // Replace the old record with the updated record
            rawExpenses.set(index, updatedRecord);

            // Save the updated list back to the file and update the checksum
            try (PrintWriter writer = new PrintWriter(new FileWriter(ExpenseStorage.expensesFile))) {
                for (String record : rawExpenses) {
                    writer.println(record);
                }
            } catch (IOException e) {
                System.out.println("Error writing to expenses file: " + e.getMessage());
                return;
            }

            // Update the checksum
            ExpenseStorage.updateChecksum();

            System.out.println("Expense edited successfully:");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter a valid number.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please enter a valid expense number.");
        } catch (Exception e) {
            System.out.println("Error editing expense: " + e.getMessage());
        }
    }
    /**
     * Displays all expenses in chronological order (most recent first).
     */
    public void displayAllExpenses() {
        File expensesFile = new File(ExpenseStorage.expensesFile);
        if (!expensesFile.exists()) {
            System.out.println("Expense file not found.");
            System.out.println("Use the add command.");
            return;
        }

        // Load expenses directly from the file with checksum verification
        List<String> rawExpenses = ExpenseStorage.loadExpenses();

        // Handle inherently empty file or tampered file
        if (rawExpenses.isEmpty()) {
            System.out.println("No expenses found in the list.");
            return;
        }

        System.out.println("All expenses are in " + currency.getCurrentCurrency());
        System.out.println("List of Expenses:");

        int expenseNumber = 1;
        for (String rawExpense : rawExpenses) {
            if (rawExpense == null || rawExpense.trim().isEmpty()) {
                continue; // Skip empty or malformed lines
            }

            // Parse and display the expense
            String[] parts = rawExpense.split("\\|");
            if (parts.length == 4) { // Ensure the line has all required fields
                String title = parts[0];
                String category = parts[1];
                String date = parts[2];
                double amount = Double.parseDouble(parts[3]);
                //boolean isDone = Boolean.parseBoolean(parts[4]);
                //String groupName = parts[5];


                System.out.println("Expense #" + expenseNumber);
                System.out.println("Title: " + title);
                System.out.println("Category: " + category);
                System.out.println("Date: " + date);
                System.out.println("Amount: " + String.format("%.2f", amount));
                //System.out.println("Settled: " + (isDone ? "Yes" : "No"));
                //System.out.println("Group: " + groupName);


                System.out.println();
            } else {
                System.out.println("Warning: Skipping malformed expense record: " + rawExpense);
            }
            expenseNumber++;
        }

        if (expenseNumber == 1) {
            System.out.println("No valid expenses found in the file.");
        }
    }
    //@@author

    //@@author NandhithaShree
    /**
     * Displays all settled (completed) expenses sorted by date in descending order.
     */
    public void displaySettledExpenses(){
        List<Expense> expenses = budgetManager.getAllExpenses();
        int numberOfExpensesPrinted = 0;

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("All expenses are in " + currency.getCurrentCurrency());

        for (int i = 0; i < expenses.size(); i++) {
            while(i < expenses.size() && !expenses.get(i).getDone()) {
                i++;
            }

            if(i >= expenses.size()) {
                break;
            }

            numberOfExpensesPrinted++;
            System.out.println("Expense #" + (i + 1));
            System.out.println(expenses.get(i));
            System.out.println();
        }

        String pluralOrSingular = (numberOfExpensesPrinted != 1 ? "expenses" : "expense");
        System.out.println("You have " + numberOfExpensesPrinted + " settled " + pluralOrSingular);
    }

    /**
     * Displays all unsettled (pending) expenses sorted by date in descending order.
     */
    public void displayUnsettledExpenses() {
        List<Expense> expenses = budgetManager.getAllExpenses();
        int numberOfExpensesPrinted = 0;

        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        System.out.println("All expenses are in " + currency.getCurrentCurrency());

        for (int i = 0; i < expenses.size(); i++) {
            while (i < expenses.size() && expenses.get(i).getDone()) {
                i++;
            }

            if (i >= expenses.size()) {
                break;
            }

            numberOfExpensesPrinted++;
            System.out.println("Expense #" + (i + 1));
            System.out.println(expenses.get(i));
            System.out.println();
        }

        String pluralOrSingular = (numberOfExpensesPrinted != 1 ? "expenses" : "expense");
        System.out.println("You have " + numberOfExpensesPrinted + " unsettled " + pluralOrSingular);
    }
    //@@author

    //@@author mohammedhamdhan
    /**
     * Shows the balance overview.
     */
    public void showBalanceOverview() {
        double totalBalance = budgetManager.getTotalBalance();
        System.out.println("Balance Overview");
        System.out.println("----------------");
        System.out.println("Total number of unsettled expenses: " + budgetManager.getUnsettledExpenseCount());
        System.out.println("Total unsettled amount: " + String.format("%.2f", totalBalance));
    }
    //@@author

    //@@author NandhithaShree
    /**
     * Marks an expense as settled based on user input.
     *
     * @throws NumberFormatException if the input is not a valid number
     * @throws IndexOutOfBoundsException if the input is out of range
     */
    public void executeMarkCommand(String command) {
        try{
            String[] splitInput = command.trim().split("\\s*/\\s*", 2);
            if(splitInput.length != 2){
                System.out.println("Invalid format. Usage: mark/<expense ID>");
                return;
            }
            String expenseNumberToMark = splitInput[1].trim();
            int indexToMark = Integer.parseInt(expenseNumberToMark) - 1;
            if(budgetManager.getExpense(indexToMark).getDone()){
                System.out.println("Expense was already marked before!");
                return;
            }

            budgetManager.markExpense(indexToMark);
            System.out.println("Expense " + expenseNumberToMark + " successfully marked!");

        } catch(IndexOutOfBoundsException e){
            System.out.println("Please enter a valid expense number.");

        } catch(NumberFormatException e){
            System.out.println("Please enter a number.");
        }
    }

    /**
     * Unmarks an expense as unsettled based on user input.
     *
     * @throws NumberFormatException if the input is not a valid number
     * @throws IndexOutOfBoundsException if the input is out of range
     */
    public void executeUnmarkCommand(String command) {
        try {
            String[] splitInput = command.trim().split("\\s*/\\s*", 2);
            if(splitInput.length != 2){
                System.out.println("Invalid format. Usage: unmark/<expense ID>");
                return;
            }
            String expenseNumberToUnmark = splitInput[1].trim();
            int indexToUnmark = Integer.parseInt(expenseNumberToUnmark) - 1;
            if(!budgetManager.getExpense(indexToUnmark).getDone()){
                System.out.println("Expense was already unmarked!");
                return;
            }

            budgetManager.unmarkExpense(indexToUnmark);
            System.out.println("Expense " + expenseNumberToUnmark + " successfully unmarked!");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please enter a valid expense number.");
        } catch(NumberFormatException e){
            System.out.println("Please enter a number.");
        }
    }
    //@@author

    //@@author mohammedhamdhan
    /**
     * Shows the expense summary based on the provided input format.
     * Format: summary/BY-MONTH/N or BY-CATEGORY/Y or N
     *
     * @param userInput The command input from the user
     */
    public void showExpenseSummary(String userInput) {
        try {
            String[] parts = userInput.split("/", 3);
            
            if (parts.length < 3 || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
                System.out.println("Invalid format. Usage: summary/BY-MONTH/N or BY-CATEGORY/Y or N");
                return;
            }

            String viewType = parts[1].trim().toUpperCase();
            String showChart = parts[2].trim().toUpperCase();

            // Special handling for BY-MONTH - only allow N
            if (viewType.equals("BY-MONTH") && !showChart.equals("N")) {
                System.out.println("Invalid format. BY-MONTH view only supports N option (no visualization).");
                return;
            }

            if (!showChart.equals("Y") && !showChart.equals("N")) {
                System.out.println("Invalid visualization choice. Please enter Y or N.");
                return;
            }

            switch (viewType) {
            case "BY-MONTH":
                showMonthlySummary();
                break;
            case "BY-CATEGORY":
                showCategorySummary(showChart.equals("Y"));
                break;
            default:
                System.out.println("Invalid view type. Please use BY-MONTH or BY-CATEGORY.");
                break;
            }
        } catch (Exception e) {
            System.out.println("Error processing summary command: " + e.getMessage());
        }
    }

    /**
     * Shows a monthly summary of expenses.
     */
    public void showMonthlySummary() {
        List<Expense> expenses = budgetManager.getAllExpenses();
        assert expenses != null : "Expenses list should not be null";
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        // Group expenses by month
        Map<String, List<Expense>> monthlyExpenses = new HashMap<>();
        Map<String, Double> monthlyTotals = new HashMap<>();
        
        for (Expense expense : expenses) {
            String month = expense.getDate().substring(3, 10); // Get MM-YYYY
            
            // Add expense to the month's list
            if (!monthlyExpenses.containsKey(month)) {
                monthlyExpenses.put(month, new ArrayList<>());
            }
            monthlyExpenses.get(month).add(expense);
            
            // Update month's total amount
            monthlyTotals.merge(month, expense.getAmount(), Double::sum);
        }

        System.out.println("\nMonthly Expense Summary:");
        System.out.println("----------------------");
        for (Map.Entry<String, List<Expense>> entry : monthlyExpenses.entrySet()) {
            String month = entry.getKey();
            List<Expense> monthExpenses = entry.getValue();
            double total = monthlyTotals.get(month);
            
            System.out.printf("%s: $%.2f (%d expenses)%n", month, total, monthExpenses.size());
            
            // List all expense titles for this month
            System.out.println("  Expenses:");
            for (Expense expense : monthExpenses) {
                System.out.printf("  - %s (%s): $%.2f%n", 
                        expense.getTitle(), 
                        expense.getDate(), 
                        expense.getAmount());
            }
            System.out.println();
        }
    }

    /**
     * Shows a category-wise summary of expenses with optional visualization.
     * 
     * @param showVisualization Whether to show the pie chart visualization
     */
    public void showCategorySummary(boolean showVisualization) {
        List<Expense> expenses = budgetManager.getAllExpenses();
        assert expenses != null : "Expenses list should not be null";
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        // Use ExpenseClassifier to calculate category proportions
        ExpenseClassifier classifier = new ExpenseClassifier();
        Map<Categories, Double> categoryTotals = classifier.calculateCategoryTotals(expenses);
        Map<Categories, Integer> categoryCounts = classifier.calculateCategoryCounts(expenses);

        // Display the summary
        System.out.println("\nCategory-wise Expense Summary:");
        System.out.println("----------------------------");
        
        // Prepare data for pie chart
        List<String> categoryNames = new ArrayList<>();
        List<Number> categoryValues = new ArrayList<>();
        
        for (Categories category : Categories.values()) {
            double total = categoryTotals.get(category);
            int count = categoryCounts.get(category);
            if (count > 0) {  // Only show categories with expenses
                System.out.printf("%s: $%.2f (%d expenses)%n", 
                        category.toString(), total, count);
                
                // Add to chart data
                categoryNames.add(category.toString());
                categoryValues.add(total);
            }
        }
        
        if (showVisualization && !categoryNames.isEmpty()) {
            showPieChart(categoryNames, categoryValues);
        }
    }

    /**
     * Displays a pie chart of expenses by category using XChart.
     */
    private void showPieChart(List<String> categoryNames, List<Number> categoryValues) {
        assert categoryNames != null && categoryValues != null : "Category data cannot be null";
        assert categoryNames.size() == categoryValues.size() : "Category names and values must have same size";
        assert !categoryNames.isEmpty() : "Cannot create pie chart with no data";

        // Close any existing chart windows
        closeAllChartWindows();

        // Create Chart with specific dimensions
        PieChart chart = new PieChartBuilder()
                .width(1024)
                .height(768)
                .title("Expenses by Category")
                .theme(ChartTheme.GGPlot2)
                .build();

        // Calculate total for percentage calculations
        double total = categoryValues.stream()
                .mapToDouble(num -> num.doubleValue())
                .sum();
        assert total > 0 : "Total expenses must be greater than 0";

        // Customize Chart styling
        chart.getStyler()
                .setLegendPosition(Styler.LegendPosition.OutsideE)
                .setLegendVisible(true)
                .setAnnotationTextPanelPadding(8)
                .setPlotContentSize(0.7)  // Ensure pie chart is a complete circle
                .setDecimalPattern("#,###.##")
                .setPlotBackgroundColor(Color.WHITE)
                .setChartBackgroundColor(Color.WHITE)
                .setToolTipsEnabled(true)  // Enable tooltips on hover
                .setToolTipsAlwaysVisible(false)
                .setAntiAlias(true)
                .setLegendBorderColor(Color.BLACK);

        chart.getStyler().setLabelType(LabelType.Percentage);
        chart.getStyler().setLabelsFontColor(Color.BLACK);
        chart.getStyler().setLabelsDistance(1.15);
        // Add an annotation panel with summary info
        chart.addAnnotation(new AnnotationTextPanel(
            String.format("Total Expenses: $%.2f", total), 40, 40, true));

        // Create custom series names with formatted amounts and percentages
        for (int i = 0; i < categoryNames.size(); i++) {
            String name = categoryNames.get(i);
            double value = categoryValues.get(i).doubleValue();
            double percentage = (value / total) * 100;

            // Format with amount and percentage
            String formattedName = String.format("%s: $%.2f (%.1f%%)", 
                name, value, percentage);

            // Add series with custom colors based on index
            Color seriesColor = getColorForIndex(i);
            chart.addSeries(formattedName, value)
                .setFillColor(seriesColor);
        }

        // Display the chart in a custom frame
        JFrame frame = new JFrame("Expense Summary");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Add the chart panel
        XChartPanel<PieChart> chartPanel = new XChartPanel<>(chart);
        frame.add(chartPanel, BorderLayout.CENTER);

        // Add instruction label
        JLabel label = new JLabel("Close this window to return to the application", SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(label, BorderLayout.SOUTH);

        // Set frame properties
        frame.pack();
        frame.setLocationRelativeTo(null);  // Center on screen

        // Add window listener to handle closing and cleanup
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                activeChartWindows.remove(frame);
                frame.dispose();
            }
        });

        // Track this window
        activeChartWindows.add(frame);
        frame.setVisible(true);

        System.out.println("Displaying pie chart.");
    }

    /**
     * Returns a color for the given index to ensure consistent coloring.
     * @param index The index of the category
     * @return A Color object for the pie chart segment
     */
    private Color getColorForIndex(int index) {
        Color[] colors = {
            new Color(70, 130, 180),   // Steel Blue
            new Color(255, 99, 71),    // Tomato
            new Color(50, 205, 50),    // Lime Green
            new Color(255, 215, 0),    // Gold
            new Color(147, 112, 219),  // Medium Purple
            new Color(60, 179, 113),   // Medium Sea Green
            new Color(238, 130, 238),  // Violet
            new Color(30, 144, 255),   // Dodge Blue
            new Color(255, 165, 0),    // Orange
            new Color(106, 90, 205)    // Slate Blue
        };
        return colors[index % colors.length];
    }

    /**
     * Exports the expense summary to a file.
     */
    public void exportExpenseSummary(String userInput) {
        // Split and trim to handle multiple spaces
        String[] parts = userInput.split("/", 2);
        
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            System.out.println("Invalid format. Usage: export/<monthly | category wise>");
            return;
        }
        
        String exportType = parts[1].trim().toLowerCase();
        
        if (exportType.equals("monthly")) {
            exportMonthlySummary();
        } else if (exportType.equals("category wise")) {
            exportCategorySummary();
        } else {
            System.out.println("Invalid export type. Please use 'monthly' or 'category wise'.");
        }
    }

    /**
     * Exports monthly summary to a file.
     */
    public void exportMonthlySummary() {
        try (FileWriter writer = new FileWriter("monthly_summary.txt")) {
            List<Expense> expenses = budgetManager.getAllExpenses();
            assert expenses != null : "Expenses list should not be null";
            
            if (expenses.isEmpty()) {
                writer.write("No expenses found.");
                return;
            }

            // Group expenses by month
            Map<String, List<Expense>> monthlyExpenses = new HashMap<>();
            Map<String, Double> monthlyTotals = new HashMap<>();
            
            for (Expense expense : expenses) {
                String month = expense.getDate().substring(3, 10);
                
                // Add expense to the month's list
                if (!monthlyExpenses.containsKey(month)) {
                    monthlyExpenses.put(month, new ArrayList<>());
                }
                monthlyExpenses.get(month).add(expense);
                
                // Update month's total amount
                monthlyTotals.merge(month, expense.getAmount(), Double::sum);
            }

            writer.write("Monthly Expense Summary\n");
            writer.write("----------------------\n");
            for (Map.Entry<String, List<Expense>> entry : monthlyExpenses.entrySet()) {
                String month = entry.getKey();
                List<Expense> monthExpenses = entry.getValue();
                double total = monthlyTotals.get(month);
                
                writer.write(String.format("%s: $%.2f (%d expenses)%n", 
                        month, total, monthExpenses.size()));
                
                // List all expense titles for this month
                writer.write("  Expenses:\n");
                for (Expense expense : monthExpenses) {
                    writer.write(String.format("  - %s (%s): $%.2f%n", 
                            expense.getTitle(), 
                            expense.getDate(), 
                            expense.getAmount()));
                }
                writer.write("\n");
            }
            System.out.println("Monthly summary exported to monthly_summary.txt");
        } catch (IOException e) {
            System.out.println("Error exporting monthly summary: " + e.getMessage());
        }
    }

    /**
     * Exports category-wise summary to a file.
     */
    public void exportCategorySummary() {
        try (FileWriter writer = new FileWriter("category_summary.txt")) {
            List<Expense> expenses = budgetManager.getAllExpenses();
            assert expenses != null : "Expenses list should not be null";
            
            if (expenses.isEmpty()) {
                writer.write("No expenses found.");
                return;
            }

            // Use ExpenseClassifier to calculate category proportions
            ExpenseClassifier classifier = new ExpenseClassifier();
            Map<Categories, Double> categoryTotals = classifier.calculateCategoryTotals(expenses);
            Map<Categories, Integer> categoryCounts = classifier.calculateCategoryCounts(expenses);

            writer.write("Category-wise Expense Summary\n");
            writer.write("----------------------------\n");
            for (Categories category : Categories.values()) {
                double total = categoryTotals.get(category);
                int count = categoryCounts.get(category);
                if (count > 0) {  // Only show categories with expenses
                    writer.write(String.format("%s: $%.2f (%d expenses)%n", 
                            category.toString(), total, count));
                }
            }
            System.out.println("Category summary exported to category_summary.txt");
        } catch (IOException e) {
            System.out.println("Error exporting category summary: " + e.getMessage());
        }
    }
    //@@author

    //@@author matthewyeo1
    /**
     * Gets the budget manager.
     *
     * @return the budget manager
     */
    public BudgetManager getBudgetManager() {
        return budgetManager;
    }

    /**
     * Updates the owesData.txt file for the deleted expense.
     *
     * @param deletedExpense the expense being deleted
     */
    private void updateOwesDataFile(Expense deletedExpense) {
        String owesFile = "owedAmounts.txt";
        File file = new File(owesFile);

        // Temporary map to store updated owed amounts
        Map<String, Double> updatedOwes = new HashMap<>();

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.startsWith("- ")) { // Lines with owed amounts start with "- "
                    String[] parts = line.split(" owes: ");
                    if (parts.length == 2) {
                        String name = parts[0].substring(2).trim(); // Extract the member's name
                        double amount = Double.parseDouble(parts[1].trim()); // Extract the owed amount
                        updatedOwes.put(name, amount); // Store in the map
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Owed amounts file not found. No amounts to update.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Error parsing owed amounts. Some amounts may not be updated.");
            return;
        }

        // Adjust owed amounts for the deleted expense
        List<Friend> groupMembers = getGroupMembersForExpense(deletedExpense);
        if (groupMembers != null && !groupMembers.isEmpty()) {
            double totalAmount = deletedExpense.getAmount();
            int numMembers = groupMembers.size();
            double sharePerMember = totalAmount / numMembers;

            for (Friend member : groupMembers) {
                String name = member.getName();
                double currentOwed = updatedOwes.getOrDefault(name, 0.0);
                double newOwed = Math.max(currentOwed - sharePerMember, 0.0); // Reduce owed amount
                updatedOwes.put(name, newOwed);
            }
        }

        // Clear the existing file content before appending updated data
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(""); // Clear the file
        } catch (IOException e) {
            System.out.println("Error clearing owed amounts file: " + e.getMessage());
            return;
        }

        System.out.println("Updated owed amounts written to file successfully.");
    }

    /**
     * Retrieves the group members associated with the given expense.
     *
     * @param expense the expense to find group members for
     * @return the list of group members, or null if none are found
     */
    private List<Friend> getGroupMembersForExpense(Expense expense) {
        // Assuming the expense has a reference to its associated group
        String groupName = expense.getGroupName(); // Add this method to your Expense class
        if (groupName == null || groupName.isEmpty()) {
            return null;
        }
        return groupManager.getGroupMembers(groupName);
    }
    //@@author

    //@@author nandhananm7
    /**
     * Searches for expenses containing the given keyword in the title or description.
     */
    public void findExpense(String command) {
        String[] parts = command.trim().split(" */", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            System.out.println("Invalid command. Please use the format: find /<keyword>");
            return;
        }

        String keyword = parts[1].trim();

        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty.");
            return;
        }

        List<Expense> expenses = budgetManager.getAllExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }

        List<Expense> matchingExpenses = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Expense expense : expenses) {
            if (expense.getTitle() != null && expense.getTitle().toLowerCase().contains(lowerKeyword)) {
                matchingExpenses.add(expense);
            }
        }

        if (matchingExpenses.isEmpty()) {
            System.out.println("No matching expenses found for keyword: " + keyword);
        } else {
            System.out.println("Found " + matchingExpenses.size() + " matching expense(s):");
            for (Expense expense : matchingExpenses) {
                System.out.println(expense);
                System.out.println();
            }
        }
    }
    //@@author

    //@@author matthewyeo1
    public void sortExpenses(String option) {
        List<Expense> expenses = budgetManager.getAllExpenses();

        if (expenses.isEmpty()) {
            System.out.println("No expenses to sort.");
            return;
        }

        switch (option) {
        case "1":
            expenses.sort(Comparator.comparing(Expense::getTitle));
            System.out.println("Expenses sorted by title (ascending):");
            break;
        case "2":
            expenses.sort(Comparator.comparing(Expense::getTitle).reversed());
            System.out.println("Expenses sorted by title (descending):");
            break;
        case "3":
            expenses.sort(Comparator.comparing(Expense::getAmount));
            System.out.println("Expenses sorted by amount (ascending):");
            break;
        case "4":
            expenses.sort(Comparator.comparing(Expense::getAmount).reversed());
            System.out.println("Expenses sorted by amount (descending):");
            break;
        default:
            System.out.println("Invalid option.");
            return;
        }

        for (Expense expense : expenses) {
            System.out.println();
            System.out.println(expense);
        }
    }
    //@@author
}
//@@author

