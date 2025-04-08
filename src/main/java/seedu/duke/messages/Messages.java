//@@author matthewyeo1
package seedu.duke.messages;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class Messages {

    private static final List<JFrame> activeChartWindows = new ArrayList<>();

    /**
     * System message upon program start-up.
     */
    public void displayWelcomeMessage() {
        setDivider();
        System.out.println("Welcome to O$P$ expense-tracker! How can I help you?");
    }

    /**
     * User input indicator.
     */
    public void enterCommandMessage() {
        System.out.print("Enter command: ");
    }

    /**
     * System message for NULL input.
     */
    public void emptyInputMessage() {
        System.out.println("No input detected. Exiting program...");
    }

    /**
     * Displays a broken line separating each user input command.
     */
    public static void setDivider() {
        System.out.println("\n" + "_".repeat(80) + "\n");
    }

    /**
     * ERROR tag for various types of errors.
     */
    public static String errorMessageTag() {
        return "ERROR: ";
    }

    /**
     * Displays an error message when the user inputs an invalid index.
     */
    public String invalidIndexMessage() {
        return "Invalid expense index.";
    }

    /**
     * Displays the exit message when the user exits the program.
     */
    public void displayExitMessage() {
        System.out.println("Thank you for using the Expense Manager. Goodbye!");
    }

    /**
     * Displays an error message when the user inputs an invalid command.
     */
    public void displayInvalidCommandMessage() {
        System.out.println("Invalid command.");
    }

    /**
     * Displays message upon data file creation.
     */
    public static void createNewFileMessage() {
        System.out.println("Created a new file!");
    }

    /**
     * Displays message for a group with no members.
     */
    public String displayEmptyGroupMessage() {
        return "No members in this group.";
    }

    /**
     * Displays message for missing groups.txt file.
     */
    public void displayMissingFileExceptionMessage() {
        System.out.println("Owed amounts file not found. No amounts to display.");
    }

    /**
     * Displays message for invalid amounts.
     */
    public void displayInvalidAmountExceptionMessage() {
        System.out.println("Error parsing owed amounts. Some amounts may not be displayed.");
    }

    /**
     * Displays message for missing group.
     */
    public void displayMissingGroupMessage() {
        System.out.println("Group not found.");
    }

    /**
     * Displays the list of available commands.
     */
    public void displayCommandList() {
        System.out.println("""
            AVAILABLE COMMANDS:
            ------------------
            help
              Description: Displays this help message
              Usage: help

            add
              Description: Add a new expense
              Usage: add/<title>/<category>/<date>/<amount>
              Format:
                - Title: Short name for the expense
                - Category: Input one of the following:
                            - Food
                            - Travel
                            - Entertainment
                            - Shopping
                            - Miscellaneous
                - Date: In DD-MM-YYYY format
                - Amount: The monetary value (must be a positive number)
              You will then be prompted to enter a description (optional)

            mark
              Description: Mark an expense as settled
              Usage: mark/<expense number>

            unmark
              Description: Unmark an expense to become an unsettled expense
              Usage: unmark/<expense number>

            list
              Description: List all expenses
              Usage: list

            list-settled
              Description: List all settled expenses
              Usage: list-settled

            list-unsettled
              Description: List all unsettled expenses
              Usage: list-unsettled

            delete
              Description: Delete an existing expense
              Usage: delete/<expense ID>

            edit
              Description: Edit an existing expense
              Usage: edit/<expense number>/<new title>/<new category>/<new date>/<new amount>
              Note: Use 'x' to keep existing values for title, date, amount or category

            balance
              Description: Show the balance overview (total expenses and amount owed)
              Usage: balance

            create-group
              Description: Create a new group and add members to it
              Usage: create-group
              You will be prompted to:
                - Enter group name
                - Enter members to add one by one
                - Enter done to create and save the group

            view-group
              Description: View the members of a specific group
              Usage: view-group
              You will be prompted to enter:
                - Enter group name

            add-member
              Description: Add a member to an existing group/ create a new group and add
              Usage: add-member /<member name> /<group-name>
                -If the group exists, adds to group. Else prompts the user to create a new group first

            remove-group
              Description: Remove a member from a group
              Usage: remove-group
              You will be prompted to enter:
                - Enter name of member to remove
                - Enter group to remove member from

            my-groups
              Description: View all groups and their members
              Usage: my-groups

            split
              Description: Split an expense between the members of an existing group
              Usage: split/<equal | assign>/<expense index>/<group name>
                -If assign, you will be prompted for 

            summary
              Description: View expense summaries in different formats
              Usage: summary/<BY-MONTH|BY-CATEGORY>/<Y|N>
              Format:
                - First parameter must be either BY-MONTH or BY-CATEGORY
                - Second parameter must be Y or N for visualization
                Note: BY-MONTH only supports N option (no visualization)

            export
              Description: Export expense summaries to text files
              Usage: export/<monthly | category wise>
              

            change-currency
              Description: Change all your expenses to a different currency
              Usage: change-currency/<method>/<currency>[/<rate>]
              Format:
                Method 1: change-currency/1/<currency>/<exchange rate>
                Method 2: change-currency/2/<currency>
              Note: Currency must be in ISO 4217 format (e.g., SGD, USD, JPY)

            sort-list
              Description: Sort expenses for viewing
              Usage: sort-list/<option>
              Options:
                1: Sort by title (ascending alphabetically)
                2: Sort by title (descending alphabetically)
                3: Sort by amount (ascending)
                4: Sort by amount (descending)

            find
              Description: Search for expenses by keyword
              Usage: find
              You will be prompted to enter a search keyword

            exit
              Description: Exit the program
              Usage: exit
            """);
    }

    private static void closeAllChartWindows() {
        for (JFrame window : activeChartWindows) {
            if (window != null && window.isDisplayable()) {
                window.dispose();
            }
        }
        activeChartWindows.clear();
    }

    private void initializeVisualizationCleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closeAllChartWindows();
        }));
    }
}
//@@author
