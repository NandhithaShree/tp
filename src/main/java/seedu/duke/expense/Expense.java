//@@author mohammedhamdhan
package seedu.duke.expense;

import seedu.duke.summary.Categories;

/**
 * Represents an individual expense with title, description, and amount.
 */
public class Expense {
    private String title;
    private String date;
    private double amount;
    private Boolean isDone;
    private String groupName;
    private Categories category;

    /**
     * Constructs an Expense object with the given title, category, date, and amount.
     *
     * @param title       A short name or summary of the expense
     * @param category    The category of the expense
     * @param date        Date of expense
     * @param amount      The monetary value of the expense
     */
    public Expense(String title, Categories category, String date, double amount) {
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.isDone = false;
        this.category = category;
    }

    /**
     * Returns the title of the expense.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the expense.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the date of the expense.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the expense.
     *
     * @param date the new date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the amount of the expense.
     *
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the expense.
     *
     * @param amount the new amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns the category of the expense.
     *
     * @return the category
     */
    public Categories getCategory() {
        return category;
    }

    /**
     * Sets the category of the expense.
     *
     * @param category the new category
     */
    public void setCategory(Categories category) {
        this.category = category;
    }

    /**
     * Returns a string representation of the expense.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "Title: " + title + "\n"
                + "Category: " + category + "\n"
                + "Date: " + date + "\n"
                + "Amount: " + String.format("%.2f", amount);
    }

    /**
     * Returns whether the expense is marked as completed.
     *
     * @return {@code true} if the expense is completed, {@code false} otherwise.
     */
    public Boolean getDone() {
        return isDone;
    }

    /**
     * Sets the completion status of the expense.
     *
     * @param isDone {@code true} to mark the expense as completed, {@code false} otherwise.
     */
    public void setDone(Boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Gets the group name associated with the expense.
     *
     * @return the group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the group name associated with the expense.
     *
     * @param groupName the group name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
