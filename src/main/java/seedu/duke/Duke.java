//@@author matthewyeo1
package seedu.duke;

import java.util.Scanner;

import seedu.duke.commands.ExpenseCommand;
import seedu.duke.commands.FriendsCommands;
import seedu.duke.commands.SplitCommand;
import seedu.duke.commands.Commands;
import seedu.duke.currency.Currency;
import seedu.duke.expense.BudgetManager;
import seedu.duke.friends.GroupManager;
import seedu.duke.messages.Messages;
import seedu.duke.storage.DataStorage;
import seedu.duke.ui.UI;
import seedu.duke.summary.ExpenseClassifier;

/**
 * Main class of application.
 */
public class Duke {
    private final String storageFilePath;
    private final BudgetManager budgetManager;

    /**
     * Constructs a Duke instance with the specified storage file.
     *
     * @param fileName The file path where data is stored.
     */
    public Duke(String fileName) {
        this.storageFilePath = fileName;
        DataStorage.ensureFileExists();
        this.budgetManager = new BudgetManager();
    }

    /**
     * The entry point of the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Duke(DataStorage.dataFile).run();
    }

    /**
     * Runs the main loop of the application, handling user input.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Messages messages = new Messages();
        Commands commands = new Commands();

        GroupManager groupManager = new GroupManager();
        FriendsCommands friendsCommand = new FriendsCommands(groupManager);
        SplitCommand splitCommand = new SplitCommand(scanner, groupManager, friendsCommand);

        ExpenseClassifier expenseClassifier = new ExpenseClassifier();
        Currency currency = new Currency(scanner, budgetManager);
        ExpenseCommand expenseCommand = new ExpenseCommand(budgetManager, scanner, currency);

        UI ui = new UI(scanner,
            messages,
            storageFilePath,
            expenseCommand,
            commands,
            friendsCommand,
            splitCommand,
            currency,
            expenseClassifier
        );

        // Add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gracefully...");
            System.out.println("All expenses have been saved!");
        }));

        messages.displayWelcomeMessage();
        messages.displayCommandList();
        messages.setDivider();
        ui.handleUserInput();
    }
}
//@@author

