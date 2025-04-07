package seedu.duke.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.duke.expense.Expense;
import seedu.duke.summary.Categories;

//@@author matthewyeo1
class DataStorageTest {
    public static final String TEST_DATA_FILE = "test_expenses.txt";

    @BeforeEach
    void setUp() {
        DataStorage.dataFile = TEST_DATA_FILE;
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_DATA_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testEnsureFileExists() {
        DataStorage.ensureFileExists();
        File file = new File(TEST_DATA_FILE);
        assertTrue(file.exists(), "File should be created");
    }

    @Test
    void testSaveAndLoadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("Lunch", Categories.Food, "01-01-2025", 10.50));
        expenses.add(new Expense("Transport", Categories.Travel, "31-12-2025", 2.00));

        assertTrue(DataStorage.saveExpenses(expenses), "Saving should be successful");

        List<Expense> loadedExpenses = DataStorage.loadExpenses();
        assertEquals(2, loadedExpenses.size(), "Should load 2 expenses");

        assertEquals("Lunch", loadedExpenses.get(0).getTitle());
        assertEquals(Categories.Food, loadedExpenses.get(0).getCategory());
        assertEquals("01-01-2025", loadedExpenses.get(0).getDate());
        assertEquals(10.50, loadedExpenses.get(0).getAmount());

        assertEquals("Transport", loadedExpenses.get(1).getTitle());
        assertEquals(Categories.Travel, loadedExpenses.get(1).getCategory());
        assertEquals("31-12-2025", loadedExpenses.get(1).getDate());
        assertEquals(2.00, loadedExpenses.get(1).getAmount());
    }

    @Test
    void testLoadExpensesFromNonExistingFile() {
        File file = new File(TEST_DATA_FILE);
        if (file.exists()) {
            file.delete();
        }

        List<Expense> loadedExpenses = DataStorage.loadExpenses();
        assertTrue(loadedExpenses.isEmpty(), "Should return an empty list");
    }

    @Test
    void testSaveAndLoadExpensesWithMarkedStatus() {
        List<Expense> expenses = new ArrayList<>();
        Expense expense1 = new Expense("Lunch", Categories.Food, "01-01-2025", 10.50);
        Expense expense2 = new Expense("Transport", Categories.Travel, "31-12-2025", 2.00);
        expense1.setDone(true); // Mark the first expense as done
        expenses.add(expense1);
        expenses.add(expense2);

        assertTrue(DataStorage.saveExpenses(expenses), "Saving should be successful");

        List<Expense> loadedExpenses = DataStorage.loadExpenses();
        assertEquals(2, loadedExpenses.size(), "Should load 2 expenses");

        assertTrue(loadedExpenses.get(0).getDone(), "First expense should be marked as done");
        assertFalse(loadedExpenses.get(1).getDone(), "Second expense should not be marked as done");
    }

    @Test
    void testSaveAndLoadExpensesWithDifferentCategories() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("Lunch", Categories.Food, "01-01-2025", 10.50));
        expenses.add(new Expense("Movie", Categories.Entertainment, "02-01-2025", 15.00));
        expenses.add(new Expense("Shopping", Categories.Shopping, "03-01-2025", 100.00));
        expenses.add(new Expense("Taxi", Categories.Travel, "04-01-2025", 20.00));
        expenses.add(new Expense("Other", Categories.Miscellaneous, "05-01-2025", 5.00));

        assertTrue(DataStorage.saveExpenses(expenses), "Saving should be successful");

        List<Expense> loadedExpenses = DataStorage.loadExpenses();
        assertEquals(5, loadedExpenses.size(), "Should load all 5 expenses");

        assertEquals(Categories.Food, loadedExpenses.get(0).getCategory());
        assertEquals(Categories.Entertainment, loadedExpenses.get(1).getCategory());
        assertEquals(Categories.Shopping, loadedExpenses.get(2).getCategory());
        assertEquals(Categories.Travel, loadedExpenses.get(3).getCategory());
        assertEquals(Categories.Miscellaneous, loadedExpenses.get(4).getCategory());
    }

    @Test
    void testSaveAndLoadExpensesWithZeroAmount() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("Free Event", Categories.Entertainment, "01-01-2025", 0.00));

        assertTrue(DataStorage.saveExpenses(expenses), "Saving should be successful");

        List<Expense> loadedExpenses = DataStorage.loadExpenses();
        assertEquals(1, loadedExpenses.size(), "Should load 1 expense");
        assertEquals(0.00, loadedExpenses.get(0).getAmount(), "Amount should be 0.00");
    }

    @Test
    void testSaveAndLoadExpensesWithLargeAmount() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("Big Purchase", Categories.Shopping, "01-01-2025", 50000.00));

        assertTrue(DataStorage.saveExpenses(expenses), "Saving should be successful");

        List<Expense> loadedExpenses = DataStorage.loadExpenses();
        assertEquals(1, loadedExpenses.size(), "Should load 1 expense");
        assertEquals(50000.00, loadedExpenses.get(0).getAmount(), "Amount should be 50000.00");
    }
}
//@@author
