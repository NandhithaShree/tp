package seedu.duke.expense;

import java.util.ArrayList;
import java.util.List;

import seedu.duke.messages.Messages;
import seedu.duke.storage.DataStorage;
import seedu.duke.summary.Categories;

//@@author mohammedhamdhan
/**
 * Manages a collection of expenses and provides operations on them.
 */
public class BudgetManager {
    Messages messages = new Messages();
    private List<Expense> expenses;

    /**
     * Constructs a BudgetManager with an empty list of expenses.
     */
    public BudgetManager() {
        this.expenses = DataStorage.loadExpenses();
    }

    /**
     * Adds an expense to the collection.
     *
     * @param expense the expense to add
     */
    public void addExpense(Expense expense) {
        if (expense == null || expense.getCategory() == null) {
            throw new IllegalArgumentException("Expense and category cannot be null");
        }
        expenses.add(expense);
        DataStorage.saveExpenses(expenses);
    }

    /**
     * Deletes an expense at the specified index.
     *
     * @param index the index of the expense to delete
     * @return the deleted expense
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Expense deleteExpense(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException(messages.invalidIndexMessage());
        }
        Expense deletedExpense = expenses.remove(index);
        DataStorage.saveExpenses(expenses);
        return deletedExpense;
    }

    /**
     * Edits an expense at the specified index.
     *
     * @param index  the index of the expense to edit
     * @param title   the new title (null to keep existing)
     * @param category the new category (null to keep existing)
     * @param date    the new date (null to keep existing)
     * @param amount  the new amount (negative to keep existing)
     * @return the edited expense
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Expense editExpense(int index, String title, Categories category, String date, double amount)
            throws IndexOutOfBoundsException {
        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException(messages.invalidIndexMessage());
        }
        Expense expense = expenses.get(index);
        
        // Only update fields that are not null (for title, category, date) or not negative (for amount)
        if (title != null) {
            expense.setTitle(title);
        }
        if (category != null) {
            expense.setCategory(category);
        }
        if (date != null) {
            expense.setDate(date);
        }
        if (amount >= 0) {
            expense.setAmount(amount);
        }
        
        DataStorage.saveExpenses(expenses);
        return expense;
    }

    /**
     * Gets all expenses.
     *
     * @return the list of expenses
     */
    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    /**
     * Gets the number of expenses.
     *
     * @return the number of expenses
     */
    public int getExpenseCount() {
        return expenses.size();
    }
    //@@author

    //@@author NandhithaShree
    /**
     * Returns the count of unsettled (pending) expenses.
     * @return the number of unsettled expenses
     * @throws AssertionError if the number of unsettled expenses is negative
     */
    public int getUnsettledExpenseCount() {
        int numberOfUnsettledExpenses = 0;

        for (Expense expense : expenses) {
            if (!expense.getDone()) {
                numberOfUnsettledExpenses++;
            }
        }
        assert numberOfUnsettledExpenses >= 0 : "number of unsettled expenses should not be negative";
        return numberOfUnsettledExpenses;
    }
    //@@author

    //@@author mohammedhamdhan
    /**
     * Calculates the total balance (sum of all unsettled expense amounts).
     *
     * @return the total balance
     */
    public double getTotalBalance() {
        double total = 0;

        for (Expense expense : expenses) {
            if(!expense.getDone()) {
                total += expense.getAmount();
            }
        }

        return total;
    }

    /**
     * Gets an expense at the specified index.
     *
     * @param index the index of the expense to get
     * @return the expense at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Expense getExpense(int index) throws IndexOutOfBoundsException {

        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException(messages.invalidIndexMessage());
        }

        return expenses.get(index);
    }
    //@@author

    //@@author NandhithaShree
    /**
     * Marks an expense as settled (done) based on its index.
     * <p>
     * The method checks if the provided index is valid. If the index is out of bounds, an
     * {@code IndexOutOfBoundsException} is thrown.
     * If the index is valid, the corresponding expense's status is updated to done, and all expenses are saved.
     * </p>
     *
     * @param index the index of the expense to mark as done
     * @throws IndexOutOfBoundsException if the provided index is invalid (out of range)
     */
    public void markExpense(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException(messages.invalidIndexMessage());
        }
        Expense expense = expenses.get(index);
        expense.setDone(true);
        DataStorage.saveExpenses(expenses);
    }

    /**
     * Unmarks an expense as unsettled (not done) based on its index.
     * <p>
     * The method checks if the provided index is valid. If the index is out of bounds, an
     * {@code IndexOutOfBoundsException} is thrown.
     * If the index is valid, the corresponding expense's status is updated to not done, and all expenses are saved.
     * </p>
     *
     * @param index the index of the expense to unmark as unsettled
     * @throws IndexOutOfBoundsException if the provided index is invalid (out of range)
     */
    public void unmarkExpense(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= expenses.size()) {
            throw new IndexOutOfBoundsException(messages.invalidIndexMessage());
        }
        Expense expense = expenses.get(index);
        expense.setDone(false);
        DataStorage.saveExpenses(expenses);
    }

    /**
     * Edits the currency of all expenses based on a given exchange rate.
     *
     * @param finalExchangeRate the exchange rate used to convert each expense amount
     */
    public void editExpenseCurrency(Double finalExchangeRate){

        if (getExpenseCount() == 0) {
            return;
        }

        for(int i = 0; i < getExpenseCount(); i++){
            Expense expense = expenses.get(i);
            editExpense(i, expense.getTitle(), expense.getCategory(), expense.getDate(),
                    expense.getAmount()*finalExchangeRate);
        }
    }
    //@@author

    /**
     * Saves all expenses to storage.
     */
    public void saveAllExpenses() {
        DataStorage.saveExpenses(expenses);
    }
}
//@@author

