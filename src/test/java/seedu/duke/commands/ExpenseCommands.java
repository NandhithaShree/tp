//@@author matthewyeo1
package seedu.duke.commands;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.currency.Currency;
import seedu.duke.expense.BudgetManager;
import seedu.duke.expense.Expense;
import seedu.duke.summary.Categories;

class ExpenseCommandTest {
    private ExpenseCommand expenseCommand;
    private BudgetManager budgetManager;
    private ByteArrayOutputStream outContent;
    private Currency currency;
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        budgetManager = new BudgetManager();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        currency = new Currency(new Scanner(System.in), budgetManager);
        expenseCommand = new ExpenseCommand(budgetManager, new Scanner(System.in), currency);
    }
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);

        try (PrintWriter writer = new PrintWriter("expenses.txt")) {
            writer.print("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File("./currentCurrency");
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File monthlySummaryFile = new File("monthly_summary.txt");
        if (monthlySummaryFile.exists()) {
            try (PrintWriter writer = new PrintWriter(monthlySummaryFile)) {
                writer.print("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Clear the contents of the "category_summary.txt" file
        File categorySummaryFile = new File("category_summary.txt");
        if (categorySummaryFile.exists()) {
            try (PrintWriter writer = new PrintWriter(categorySummaryFile)) {
                writer.print("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        Scanner scanner = new Scanner(System.in);
        currency = new Currency(scanner, budgetManager);
        expenseCommand = new ExpenseCommand(budgetManager, scanner, currency);
    }

    @Test
    void testExecuteAddExpenseValidInput() {
        String input = "add/Lunch/Food/01-01-2025/10.50";
        expenseCommand.executeAddExpense(input);
        Expense addedExpense = budgetManager.getExpense(0);
        assertEquals("Lunch", addedExpense.getTitle());
        assertEquals(Categories.Food, addedExpense.getCategory());
        assertEquals("01-01-2025", addedExpense.getDate());
        assertEquals(10.50, addedExpense.getAmount());
        assertEquals(1, budgetManager.getExpenseCount());
    }

    @Test
    void testExecuteAddExpenseInvalidCategory() {
        String input = "add/Lunch/InvalidCategory/01-01-2025/10.50";
        expenseCommand.executeAddExpense(input);
        
        assertEquals(0, budgetManager.getExpenseCount());
        String output = outContent.toString();
        assertTrue(output.contains("Invalid category." +
                " Please use one of: Food, Shopping, Travel, Entertainment, Miscellaneous"));
    }

    @Test
    void testExecuteEditExpenseValidInput() {
        // First add an expense
        expenseCommand.executeAddExpense("add/Original/Food/01-01-2025/10.50");
        
        // Then edit it
        String input = "edit/1/NewTitle/Shopping/02-02-2025/20.50";
        expenseCommand.executeEditExpense(input);
        
        Expense editedExpense = budgetManager.getExpense(0);
        assertEquals("NewTitle", editedExpense.getTitle());
        assertEquals(Categories.Shopping, editedExpense.getCategory());
        assertEquals("02-02-2025", editedExpense.getDate());
        assertEquals(20.50, editedExpense.getAmount());
    }

    @Test
    void testExecuteEditExpenseInvalidCategory() {
        // First add an expense
        expenseCommand.executeAddExpense("add/Original/Food/01-01-2025/10.50");
        
        // Try to edit with invalid category
        String input = "edit/1/NewTitle/InvalidCategory/02-02-2025/20.50";
        expenseCommand.executeEditExpense(input);
        
        // Check that the expense wasn't changed
        Expense expense = budgetManager.getExpense(0);
        assertEquals("Original", expense.getTitle());
        assertEquals(Categories.Food, expense.getCategory());
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid category. Please use one of:" +
                " Food, Shopping, Travel, Entertainment, Miscellaneous"));
    }

    @Test
    void testExecuteEditExpenseKeepExisting() {
        // First add an expense
        expenseCommand.executeAddExpense("add/Original/Food/01-01-2025/10.50");
        
        // Edit only the title and amount, keep other fields unchanged
        String input = "edit/1/NewTitle/x/x/20.50";
        expenseCommand.executeEditExpense(input);
        
        Expense editedExpense = budgetManager.getExpense(0);
        assertEquals("NewTitle", editedExpense.getTitle());
        assertEquals(Categories.Food, editedExpense.getCategory());
        assertEquals("01-01-2025", editedExpense.getDate());
        assertEquals(20.50, editedExpense.getAmount());
    }
    //@@author
    //@@author mohammedhamdhan
    @Test
    void testExecuteEditExpenseInvalidExpenseNumber() {
        // First add an expense
        expenseCommand.executeAddExpense("add/Original/Food/01-01-2025/10.50");
        
        // Try to edit non-existent expense
        String input = "edit/999/NewTitle/Shopping/02-02-2025/20.50";
        expenseCommand.executeEditExpense(input);
        
        String output = outContent.toString();
        assertTrue(output.contains("Please enter a valid expense number"));
    }

    @Test
    void testExportCategorySummary() {
        // Add expenses from different categories
        expenseCommand.executeAddExpense("add/Taxi/Travel/05-01-2025/45.00");
        expenseCommand.executeAddExpense("add/Dinner/Food/06-01-2025/55.00");

        provideInput("");

        // Run the export command
        expenseCommand.exportCategorySummary();

        // Verify the file was created with correct message
        String output = outContent.toString();
        assertTrue(output.contains("Category summary exported to category_summary.txt"));

        // Optional: Read and verify file contents
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("category_summary.txt");
            String fileContent = new String(java.nio.file.Files.readAllBytes(path));

            assertTrue(fileContent.contains("Category-wise Expense Summary"));
            assertTrue(fileContent.contains("Travel:"));
            assertTrue(fileContent.contains("Food:"));
        } catch (IOException e) {
            // Handle exception or fail test
            assertTrue(false, "Failed to read exported file: " + e.getMessage());
        }
    }

    @Test
    void testExportMonthlySummary() {
        // Add expenses from different months
        expenseCommand.executeAddExpense("add/March Expense/Food/15-03-2025/300.00");
        expenseCommand.executeAddExpense("add/April Expense/Food/15-04-2025/400.00");

        provideInput("");

        // Run the export command
        expenseCommand.exportMonthlySummary();

        // Verify the file was created with correct message
        String output = outContent.toString();
        assertTrue(output.contains("Monthly summary exported to monthly_summary.txt"));

        // Optional: Read and verify file contents
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("monthly_summary.txt");
            String fileContent = new String(java.nio.file.Files.readAllBytes(path));

            assertTrue(fileContent.contains("Monthly Expense Summary"));
            assertTrue(fileContent.contains("03-2025")); // March
            assertTrue(fileContent.contains("04-2025")); // April
            assertTrue(fileContent.contains("$300.00")); // March amount
            assertTrue(fileContent.contains("$400.00")); // April amount
        } catch (IOException e) {
            // Handle exception or fail test
            assertTrue(false, "Failed to read exported file: " + e.getMessage());
        }
    }
    //@@author
    //@@author NandhithaShree
    @Test
    void testExecuteMarkCommand() {
        // Add an expense first
        expenseCommand.executeAddExpense("add/Test Expense/Food/31-12-2025/100.0");
        
        String input = "mark/1";
        expenseCommand.executeMarkCommand(input);
        
        Expense expense = budgetManager.getExpense(0);
        assertEquals(true, expense.getDone());
        assertEquals("Test Expense", expense.getTitle());
        assertEquals(Categories.Food, expense.getCategory());
        assertEquals("31-12-2025", expense.getDate());
        assertEquals(100.0, expense.getAmount());
    }

    @Test
    void testExecuteUnmarkCommand() {
        // Add an expense first
        expenseCommand.executeAddExpense("add/Test Expense/Food/31-12-2025/100.0");
        
        // Mark it first
        expenseCommand.executeMarkCommand("mark/1");
        
        // Then unmark it
        String input = "unmark/1";
        expenseCommand.executeUnmarkCommand(input);
        
        Expense expense = budgetManager.getExpense(0);
        assertEquals(false, expense.getDone());
        assertEquals("Test Expense", expense.getTitle());
        assertEquals(Categories.Food, expense.getCategory());
        assertEquals("31-12-2025", expense.getDate());
        assertEquals(100.0, expense.getAmount());
    }

    @Test
    void testExecuteMarkCommandInvalidInputs() {
        // Add an expense first
        expenseCommand.executeAddExpense("add/Test Expense/Food/31-12-2025/100.0");
        
        String input = "mark/2"; // Invalid expense number
        expenseCommand.executeMarkCommand(input);
        
        String output = outContent.toString();
        assertTrue(output.contains("Please enter a valid expense number."));
    }

    @Test
    void testExecuteAddExpenseEmptyTitle() {
        String input = "add//Food/01-01-2025/10.50";
        expenseCommand.executeAddExpense(input);
        
        assertEquals(0, budgetManager.getExpenseCount());
        String output = outContent.toString();
        assertTrue(output.contains("Invalid format. None of the fields can be empty."));
    }
    //@@author
    //@@author mohammedhamdhan
    @Test
    void testExecuteAddExpenseEmptyCategory() {
        String input = "add/Lunch//01-01-2025/10.50";
        expenseCommand.executeAddExpense(input);
        
        assertEquals(0, budgetManager.getExpenseCount());
        String output = outContent.toString();
        assertTrue(output.contains("Invalid format. None of the fields can be empty."));
    }

    @Test
    void testExecuteAddExpenseEmptyDate() {
        String input = "add/Lunch/Food//10.50";
        expenseCommand.executeAddExpense(input);
        
        assertEquals(0, budgetManager.getExpenseCount());
        String output = outContent.toString();
        assertTrue(output.contains("Invalid format. None of the fields can be empty."));
    }

    @Test
    void testExecuteAddExpenseInvalidDateFormat() {
        String input = "add/Lunch/Food/2025-01-01/10.50";
        expenseCommand.executeAddExpense(input);
        
        assertEquals(0, budgetManager.getExpenseCount());
        String output = outContent.toString();
        assertTrue(output.contains("Invalid date. Please enter a valid date in DD-MM-YYYY format."));
    }

    @Test
    void testExecuteAddExpenseInvalidDate() {
        String input = "add/Lunch/Food/31-02-2025/10.50";
        expenseCommand.executeAddExpense(input);
        
        assertEquals(0, budgetManager.getExpenseCount());
        String output = outContent.toString();
        assertTrue(output.contains("Invalid date. Please enter a valid date in DD-MM-YYYY format."));
    }

    @Test
    void testExecuteAddExpenseNegativeAmount() {
        String input = "add/Lunch/Food/01-01-2025/-10.50";
        expenseCommand.executeAddExpense(input);
        
        assertEquals(0, budgetManager.getExpenseCount());
        String output = outContent.toString();
        assertTrue(output.contains("Amount cannot be negative."));
    }

    @Test
    void testExecuteAddExpenseZeroAmount() {
        String input = "add/Lunch/Food/01-01-2025/0";
        expenseCommand.executeAddExpense(input);
        
        assertEquals(0, budgetManager.getExpenseCount());
        String output = outContent.toString();
        assertTrue(output.contains("Amount cannot be zero."));
    }

    @Test
    void testExecuteAddExpenseInvalidAmountFormat() {
        String input = "add/Lunch/Food/01-01-2025/abc";
        expenseCommand.executeAddExpense(input);
        
        assertEquals(0, budgetManager.getExpenseCount());
        String output = outContent.toString();
        assertTrue(output.contains("Invalid amount format. Please enter a valid number."));
    }

    @Test
    void testExecuteEditExpenseEmptyExpenseId() {
        // First add an expense
        expenseCommand.executeAddExpense("add/Original/Food/01-01-2025/10.50");
        
        // Try to edit with empty expense ID
        String input = "edit//NewTitle/Shopping/02-02-2025/20.50";
        expenseCommand.executeEditExpense(input);
        
        String output = outContent.toString();
        assertTrue(output.contains("Expense ID cannot be empty."));
    }

    @Test
    void testExecuteEditExpenseInvalidDateFormat() {
        // First add an expense
        expenseCommand.executeAddExpense("add/Original/Food/01-01-2025/10.50");
        
        // Try to edit with invalid date format
        String input = "edit/1/NewTitle/Food/2025-01-01/20.50";
        expenseCommand.executeEditExpense(input);
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid date format. Please enter a valid date in DD-MM-YYYY format."));
    }

    @Test
    void testExecuteEditExpenseNegativeAmount() {
        // First add an expense
        expenseCommand.executeAddExpense("add/Original/Food/01-01-2025/10.50");
        
        // Try to edit with negative amount
        String input = "edit/1/NewTitle/Food/01-01-2025/-20.50";
        expenseCommand.executeEditExpense(input);
        
        String output = outContent.toString();
        assertTrue(output.contains("Amount cannot be negative."));
    }

    @Test
    void testExecuteEditExpenseZeroAmount() {
        // First add an expense
        expenseCommand.executeAddExpense("add/Original/Food/01-01-2025/10.50");
        
        // Try to edit with zero amount
        String input = "edit/1/NewTitle/Food/01-01-2025/0";
        expenseCommand.executeEditExpense(input);
        
        String output = outContent.toString();
        assertTrue(output.contains("Amount cannot be zero."));
    }

    @Test
    void testExecuteEditExpenseInvalidAmountFormat() {
        // First add an expense
        expenseCommand.executeAddExpense("add/Original/Food/01-01-2025/10.50");
        
        // Try to edit with invalid amount format
        String input = "edit/1/NewTitle/Food/01-01-2025/abc";
        expenseCommand.executeEditExpense(input);
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid input format. Please enter a valid number."));
    }

    @Test
    void testShowExpenseSummaryInvalidFormat() {
        // Test with invalid format (missing parameters)
        expenseCommand.showExpenseSummary("summary/BY-CATEGORY");
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid format. Usage: summary/BY-MONTH/N or BY-CATEGORY/Y or N"));
    }

    @Test
    void testShowExpenseSummaryInvalidViewType() {
        // Test with invalid view type
        expenseCommand.showExpenseSummary("summary/INVALID/N");
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid view type. Please use BY-MONTH or BY-CATEGORY."));
    }

    @Test
    void testShowExpenseSummaryInvalidVisualizationChoice() {
        // Test with invalid visualization choice
        expenseCommand.showExpenseSummary("summary/BY-CATEGORY/X");
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid visualization choice. Please enter Y or N."));
    }

    @Test
    void testShowExpenseSummaryByMonthWithVisualization() {
        // Test BY-MONTH with Y (which is not allowed)
        expenseCommand.showExpenseSummary("summary/BY-MONTH/Y");
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid format. BY-MONTH view only supports N option (no visualization)."));
    }

    @Test
    void testShowExpenseSummaryEmptyExpenses() {
        // Test summary with no expenses
        expenseCommand.showExpenseSummary("summary/BY-CATEGORY/N");
        
        String output = outContent.toString();
        assertTrue(output.contains("No expenses found."));
    }

    @Test
    void testShowMonthlySummaryMultipleMonths() {
        // Add expenses from different months
        expenseCommand.executeAddExpense("add/January Expense/Food/15-01-2025/100.00");
        expenseCommand.executeAddExpense("add/February Expense/Food/15-02-2025/200.00");
        expenseCommand.executeAddExpense("add/March Expense/Food/15-03-2025/300.00");
        
        // Clear previous output
        outContent.reset();
        
        // Call the monthly summary method directly
        expenseCommand.showMonthlySummary();
        
        String output = outContent.toString();
        assertTrue(output.contains("01-2025: $100.00"));
        assertTrue(output.contains("02-2025: $200.00"));
        assertTrue(output.contains("03-2025: $300.00"));
    }

    @Test
    void testShowCategorySummaryMultipleCategories() {
        // Add expenses from different categories
        expenseCommand.executeAddExpense("add/Food Expense/Food/15-01-2025/100.00");
        expenseCommand.executeAddExpense("add/Shopping Expense/Shopping/15-01-2025/200.00");
        expenseCommand.executeAddExpense("add/Travel Expense/Travel/15-01-2025/300.00");
        
        // Clear previous output
        outContent.reset();
        
        // Call the category summary method directly (without visualization)
        expenseCommand.showCategorySummary(false);
        
        String output = outContent.toString();
        assertTrue(output.contains("Food: $100.00"));
        assertTrue(output.contains("Shopping: $200.00"));
        assertTrue(output.contains("Travel: $300.00"));
    }
}
//@@author
