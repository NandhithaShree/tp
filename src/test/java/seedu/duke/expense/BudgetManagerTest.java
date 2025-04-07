//@@author matthewyeo1
package seedu.duke.expense;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.storage.DataStorage;
import seedu.duke.summary.Categories;


public class BudgetManagerTest {

    private BudgetManager budgetManager;
    private final String testTitle = "Test Expense";
    private final String testDate = "01-01-2025";
    private final double testAmount = 100.0;

    @BeforeEach
    void setUp() {
        DataStorage.resetExpenses();
        budgetManager = new BudgetManager();
    }

    @AfterEach
    void tearDown() {
        DataStorage.resetExpenses();
    }

    @Test
    void testAddExpense() {
        Expense expense = new Expense(testTitle, Categories.Food, testDate, testAmount);

        budgetManager.addExpense(expense);

        assertEquals(1, budgetManager.getExpenseCount());
        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testDate, budgetManager.getExpense(0).getDate());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
    }

    @Test
    void testDeleteExpense() {
        Expense expense1 = new Expense("Expense 1", Categories.Food, "01-01-2025", 50.0);
        Expense expense2 = new Expense("Expense 2", Categories.Shopping, "31-12-2025", 30.0);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);

        Expense deletedExpense = budgetManager.deleteExpense(0);

        assertEquals(1, budgetManager.getExpenseCount());
        assertEquals(expense1, deletedExpense);
        assertEquals("Expense 2", budgetManager.getExpense(0).getTitle());
    }

    @Test
    void testEditExpense() {
        Expense expense = new Expense(testTitle, Categories.Food, testDate, testAmount);
        budgetManager.addExpense(expense);

        Expense editedExpense = budgetManager.editExpense(0, "Updated Title", Categories.Shopping,
                "02-01-2025", 200.0);

        assertEquals("Updated Title", editedExpense.getTitle());
        assertEquals(Categories.Shopping, editedExpense.getCategory());
        assertEquals(200.0, editedExpense.getAmount());
    }

    @Test
    void testGetTotalBalance() {
        Expense expense1 = new Expense("Expense 1", Categories.Food, "01-01-2025", 50.0);
        Expense expense2 = new Expense("Expense 2", Categories.Shopping, "31-12-2025", 100.0);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);

        double totalBalance = budgetManager.getTotalBalance();

        assertEquals(150.0, totalBalance);
    }

    @Test
    void testGetAllExpenses() {
        Expense expense1 = new Expense("Expense 1", Categories.Food, "01-01-2025", 50.0);
        Expense expense2 = new Expense("Expense 2", Categories.Shopping, "31-12-2025", 100.0);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);

        List<Expense> expenses = budgetManager.getAllExpenses();

        assertEquals(2, expenses.size());
        assertTrue(expenses.contains(expense1));
        assertTrue(expenses.contains(expense2));
    }

    @Test
    void testDeleteExpenseOutOfRange() {
        Expense expense = new Expense(testTitle, Categories.Food, testDate, testAmount);
        budgetManager.addExpense(expense);

        assertThrows(IndexOutOfBoundsException.class, () -> budgetManager.deleteExpense(1));
    }

    @Test
    void testEditExpenseOutOfRange() {
        assertThrows(IndexOutOfBoundsException.class,
                () -> budgetManager.editExpense(1,
                        "New Title",
                        Categories.Food,
                        "02-01-2025",
                        200.0));
    }

    @Test
    void testGetExpenseOutOfRange() {
        assertThrows(IndexOutOfBoundsException.class, () -> budgetManager.getExpense(1));
    }
    //@@author

    //@@author NandhithaShree
    @Test
    void testMark(){
        Expense expense = new Expense(testTitle, Categories.Food, testDate, testAmount);
        assertEquals(expense.getDone(), false);
        budgetManager.addExpense(expense);
        int testIndex = 0;

        budgetManager.markExpense(testIndex);
        assertEquals(expense.getDone(), true);
        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
        assertEquals(testDate, budgetManager.getExpense(0).getDate());
    }

    @Test
    void testUnmark(){
        Expense expense = new Expense(testTitle, Categories.Food, testDate, testAmount);
        budgetManager.addExpense(expense);
        int testIndex = 0;
        assertEquals(expense.getDone(), false);

        budgetManager.markExpense(testIndex);
        assertEquals(expense.getDone(), true);

        budgetManager.unmarkExpense(testIndex);
        assertEquals(expense.getDone(), false);
        assertEquals(testTitle, budgetManager.getExpense(0).getTitle());
        assertEquals(testAmount, budgetManager.getExpense(0).getAmount());
        assertEquals(testDate, budgetManager.getExpense(0).getDate());
    }

    @Test
    void testMarkExpenseOutOfRange(){
        Expense expense = new Expense(testTitle, Categories.Food, testDate, testAmount);
        budgetManager.addExpense(expense);

        assertThrows(IndexOutOfBoundsException.class, () -> budgetManager.markExpense(1));
    }

    @Test
    void testUnmarkExpenseOutOfRange(){
        Expense expense = new Expense(testTitle, Categories.Food, testDate, testAmount);
        budgetManager.addExpense(expense);

        assertThrows(IndexOutOfBoundsException.class, () -> budgetManager.unmarkExpense(1));
    }

    @Test
    void testGetUnsettledExpensesCountZeroUnsettled(){
        int numberOfUnsettledExpenses = 0;

        assertEquals(budgetManager.getUnsettledExpenseCount(), numberOfUnsettledExpenses);
    }

    @Test
    void testGetUnsettledExpensesCountOneUnsettled(){
        Expense expense = new Expense(testTitle, Categories.Food, testDate, testAmount);
        budgetManager.addExpense(expense);
        int numberOfUnsettledExpenses = 1;

        assertEquals(budgetManager.getUnsettledExpenseCount(), numberOfUnsettledExpenses);
    }

    @Test
    void testGetUnsettledExpensesCountMultipleUnsettled(){
        Expense expense1 = new Expense(testTitle, Categories.Food, testDate, testAmount);
        Expense expense2 = new Expense(testTitle, Categories.Shopping, testDate, testAmount);
        budgetManager.addExpense(expense1);
        budgetManager.addExpense(expense2);
        int numberOfUnsettledExpenses = 2;

        assertEquals(budgetManager.getUnsettledExpenseCount(), numberOfUnsettledExpenses);
    }
    //@@author
}


