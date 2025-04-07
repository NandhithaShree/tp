//@@author matthewyeo1
package seedu.duke.ui;

import java.util.Scanner;

import seedu.duke.commands.Commands;
import seedu.duke.commands.ExpenseCommand;
import seedu.duke.commands.FriendsCommands;
import seedu.duke.commands.SplitCommand;
import seedu.duke.currency.Currency;
import seedu.duke.expense.BudgetManager;
import seedu.duke.messages.Messages;
import seedu.duke.summary.ExpenseClassifier;

/**
 * The UI class handles user interactions by processing input commands
 * and delegating tasks to respective command handlers.
 */
public class UI {
    private final Scanner scanner;
    private final Messages messages;
    private final String storageFilePath;
    private final ExpenseCommand expenseCommand;
    private final FriendsCommands friendsCommand;
    private final SplitCommand splitCommand;
    private Commands commands;
    private final BudgetManager budgetManager;
    private ExpenseClassifier expenseClassifier;
    private boolean isRunning;
    private final Currency currency;

    /**
     * Constructs a UI object for handling user input and interactions.
     *
     * @param scanner           Scanner for reading user input.
     * @param messages          Messages object for displaying messages.
     * @param storageFilePath   File path for data storage.
     * @param expenseCommand    Handles expense-related commands.
     * @param commands          Command constants.
     * @param friendsCommand    Handles friend group-related commands.
     * @param splitCommand      Handles expense splitting commands.
     * @param currency          Handles currency-related operations.
     * @param expenseClassifier Categorizes expenses for summary.
     */
    public UI(Scanner scanner, 
            Messages messages,
            String storageFilePath,
            ExpenseCommand expenseCommand, 
            Commands commands, 
            FriendsCommands friendsCommand, 
            SplitCommand splitCommand,
            Currency currency,
            ExpenseClassifier expenseClassifier) {
      
        this.scanner = scanner;
        this.messages = messages;
        this.storageFilePath = storageFilePath;
        this.expenseCommand = expenseCommand;
        this.commands = commands;
        this.budgetManager = expenseCommand.getBudgetManager();
        this.friendsCommand = friendsCommand;
        this.splitCommand = splitCommand;
        this.expenseClassifier = expenseClassifier;
        this.isRunning = true;
        this.currency = currency;
    }

    /**
     * Continuously listens for user input and processes commands accordingly.
     */
    public void handleUserInput() {
        while (isRunning) {
            messages.enterCommandMessage();
            
            // Check if input exists before reading
            if (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();
                processCommand(userInput);
            } else {
                // Handle the case where there is no input available
                messages.emptyInputMessage();
                isRunning = false;
                break;
            }
            
            if (isRunning) {
                messages.setDivider();
            }
        }
    }

    /**
     * Processes a user command and executes the corresponding action.
     *
     * @param userInput The command entered by the user.
     */
    public void processCommand(String userInput) {
        String command = userInput.trim().toLowerCase();

        if (command.startsWith(Commands.SORT_LIST)) {
            String[] parts = command.split("/", 2);
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                System.out.println("Invalid format. Usage: sort-list/<1,2,3,4>");
                return;
            }
            String sortOption = parts[1].trim();
            expenseCommand.sortExpenses(sortOption);
            return;
        }
        //@@author NandhithaShree
        if(command.equals(Commands.HELP)){
            messages.displayCommandList();
        } else if(command.equals(Commands.EXIT)){
            budgetManager.saveAllExpenses();
            messages.displayExitMessage();
            isRunning = false;
        } else if(command.startsWith(Commands.ADD_MEMBER)){
            friendsCommand.addMember(command);
        } else if(command.equals(Commands.LIST)){
            expenseCommand.displayAllExpenses();
        } else if(command.startsWith(Commands.DELETE)){
            expenseCommand.executeDeleteExpense(command);
        } else if(command.startsWith(Commands.EDIT)) {
            expenseCommand.executeEditExpense(command);
        } else if(command.equals(Commands.BALANCE)) {
            expenseCommand.showBalanceOverview();
        } else if (command.startsWith(Commands.MARK)){
            expenseCommand.executeMarkCommand(command);
        } else if (command.startsWith(Commands.UNMARK)) {
            expenseCommand.executeUnmarkCommand(command);
        } else if (command.equals(Commands.SETTLED_LIST)) {
            expenseCommand.displaySettledExpenses();
        } else if (command.equals(Commands.UNSETTLED_LIST)) {
            expenseCommand.displayUnsettledExpenses();
        } else if (command.startsWith(Commands.CREATE_GROUP)) {
            friendsCommand.createGroup(command);
        } else if (command.startsWith(Commands.VIEW_GROUP)) {
            friendsCommand.viewGroup(command);
        } else if (command.startsWith(Commands.VIEW_MEMBER)) {
            friendsCommand.viewMember(command);
        } else if (command.startsWith(Commands.ADD)) {
            expenseCommand.executeAddExpense(command);
        } else if (command.startsWith(Commands.REMOVE_MEMBER)) {
            friendsCommand.removeMember(command);
        } else if (command.startsWith(Commands.VIEW_ALL_GROUPS)) {
            friendsCommand.viewAllGroups();
        } else if(command.startsWith(Commands.SPLIT)) {
            splitCommand.executeSplit(command);
        } else if(command.startsWith(Commands.REMOVE_GROUP)) {
            friendsCommand.removeGroup(command);
        } else if(command.startsWith(Commands.CHANGE_CURRENCY)){
            currency.changeCurrency(command);
        } else if (command.startsWith(Commands.SUMMARY)){
            expenseCommand.showExpenseSummary(command);
        } else if (command.startsWith(Commands.EXPORT)){
            expenseCommand.exportExpenseSummary(command);
        } else if (command.startsWith(Commands.FIND)){
            expenseCommand.findExpense(command);
        } else {
            messages.displayInvalidCommandMessage();
        }
        //@@author
    }
}
//@@author

