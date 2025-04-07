//@@author matthewyeo1
package seedu.duke.summary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.duke.expense.Expense;

public class ExpenseClassifier {
    public Map<Categories, Double> calculateCategoryTotals(List<Expense> expenses) {
        Map<Categories, Double> categoryTotals = new HashMap<>();
        for (Categories category : Categories.values()) {
            categoryTotals.put(category, 0.0);
        }

        for (Expense expense : expenses) {
            Categories category = expense.getCategory();
            categoryTotals.put(category, categoryTotals.get(category) + expense.getAmount());
        }

        return categoryTotals;
    }

    public Map<Categories, Integer> calculateCategoryCounts(List<Expense> expenses) {
        Map<Categories, Integer> categoryCounts = new HashMap<>();
        for (Categories category : Categories.values()) {
            categoryCounts.put(category, 0);
        }

        for (Expense expense : expenses) {
            Categories category = expense.getCategory();
            categoryCounts.put(category, categoryCounts.get(category) + 1);
        }

        return categoryCounts;
    }
}
//@@author
