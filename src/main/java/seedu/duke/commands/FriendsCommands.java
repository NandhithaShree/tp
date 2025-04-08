//@@author nandhananm7
package seedu.duke.commands;
import seedu.duke.friends.Group;
import seedu.duke.friends.GroupManager;
import seedu.duke.commands.SplitCommand.OwesStorage;
import seedu.duke.friends.Friend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class FriendsCommands {
    private GroupManager groupManager;
    private Scanner scanner;

    public FriendsCommands(GroupManager groupManager) {
        this.groupManager = groupManager;
        this.scanner = new Scanner(System.in);
    }

    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // This regex allows letters, numbers, and spaces.
        return name.matches("[A-Za-z0-9 ]+");
    }

    /**
     * Creates a group from the given command in the format:
     * create-group /group name.
     * Prompts for members and saves the group if valid.
     *
     * @param command the command string.
     */
    public void createGroup(String command) {
        String[] parts = command.trim().split(" */", 2);
        if (parts.length < 2 || !parts[0].equals("create-group")) {
            System.out.println("Invalid format. Usage: create-group/<group-name>");
            return;
        }

        String groupName = parts[1].trim().toLowerCase();
        if (groupName.isEmpty() || !isValidName(groupName)) {
            System.out.println("Invalid group name. Name cannot be empty or contain special characters.");
            return;
        }

        if (groupManager.groupExists(groupName)) {
            System.out.println("Group '" + groupName + "' already exists.");
            System.out.println("Add more members to " + groupName + " using the add-member command.");
            return;
        }

        System.out.println("Who would you like to add to the group? (Type 'done' to finish)");
        boolean hasMembers = false;

        while (true) {
            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim().toLowerCase();
            if (name.equalsIgnoreCase("done")) {
                break;
            }

            if (!isValidName(name)) {
                System.out.println("Invalid name. It cannot be empty or contain special characters. Try again.");
                continue;
            }

            if (groupManager.isMemberInGroup(groupName, name)) {
                System.out.println("Member '" + name + "' already exists in the group.");
                continue;
            }

            groupManager.addFriendToGroup(groupName, new Friend(name, groupName));
            hasMembers = true;
        }

        if (!hasMembers) {
            System.out.println("Operation cancelled. Group must have at least one member.");
            return;
        }

        groupManager.saveGroups();
        System.out.println("Group created successfully!");
    }

    //@@author

    //@@author Ashertan256
    /**
     * Displays detailed owed transactions for a specific member in a group.
     */
    public void viewMember(String command) {
        if (command == null || command.trim().isEmpty()) {
            System.out.println("Error: Command cannot be empty.");
            return;
        }
        if (!OwesStorage.verifyChecksum()) {
            System.out.println("Error: The owedAmounts file has likely been tampered with.");
            System.out.println("Clearing contents of the file");

            // clear the file
            try (PrintWriter writer = new PrintWriter("owedAmounts.txt")) {
                writer.print("");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (PrintWriter writer = new PrintWriter("owedAmounts.chk")) {
                writer.print("");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // Expected format: view-member/<groupname>/<member name>
        String[] parts = command.trim().split("/");
        if (parts.length != 3) {
            System.out.println("Invalid format. Usage: view-member/<group name>/<member name>");
            return;
        }

        String commandWord = parts[0].trim();
        if (!commandWord.equalsIgnoreCase("view-member")) {
            System.out.println("Invalid command. Expected command to start with " +
                "'view-member'.");
            return;
        }

        String groupName = parts[1].trim();
        String memberName = parts[2].trim();
        if (groupName.isEmpty() || memberName.isEmpty()) {
            System.out.println("Error: Group name and member name cannot be empty.");
            return;
        }
        File file = new File("owedAmounts.txt");
        if (!file.exists()) {
            System.out.println("Error: Owed transactions file does not exist. No data available.");
            return;
        }
        List < String > allLines;
        try {
            allLines = java.nio.file.Files.readAllLines(file.toPath());
        } catch (IOException e) {
            System.out.println("Error reading owed transactions file: " + e.getMessage());
            return;
        }
        if (allLines.isEmpty()) {
            System.out.println("No transactions found in the file.");
            return;
        }
        boolean found = false;
        double totalAmount = 0.0;
        System.out.println("Transactions for member '" + memberName +
            "' in group '" + groupName + "':");
        for (String line: allLines) {
            if (line == null || line.trim().isEmpty()) {
                continue; // Skip blank lines
            }
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("Transaction: Expense:")) {
                System.out.println("Warning: Skipping unrecognized record: " + trimmedLine);
                continue;
            }
            if (trimmedLine.contains("Group: " + groupName) &&
                trimmedLine.contains("Member: " + memberName)) {
                System.out.println(trimmedLine);
                found = true;
                int owesIndex = trimmedLine.lastIndexOf(" owes:");
                if (owesIndex != -1) {
                    String amountStr = trimmedLine.substring(owesIndex + " owes:".length())
                        .trim();
                    try {
                        double amount = Double.parseDouble(amountStr);
                        totalAmount += amount;
                    } catch (NumberFormatException nfe) {
                        System.out.println("Warning: Unable to parse amount in record: " +
                            trimmedLine);
                    }
                }
            }
        }
        if (found) {
            System.out.println("Total Amount: " + String.format("%.2f", totalAmount));
        } else {
            System.out.println("No transactions found for member '" + memberName +
                "' in group '" + groupName + "'.");
        }
    }

    /**
     * Displays the details of a specified group based on the command.
     * Command format: view-group /group name.
     * Retrieves and shows group members and their total owed expenses.
     *
     * @param command the command string containing the group name.
     */
    public void viewGroup(String command) {
        String[] parts = command.trim().split(" */", 2);
        if (parts.length < 2 || !parts[0].equals("view-group")) {
            System.out.println("Invalid format. Usage: view-group/<group name>");
            return;
        }

        String groupName = parts[1].trim();
        if (groupName.isEmpty() || !isValidName(groupName)) {
            System.out.println("Invalid group name. Name cannot be empty or contain special characters.");
            return;
        }

        if (!groupManager.groupExists(groupName)) {
            System.out.println("Group not found");
            return;
        }
        System.out.println("Group: " + groupName);

        Map < String, Double > owedAmounts = new HashMap < > ();
        File file = new File("owedAmounts.txt");
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("- ")) {
                    String[] lineParts = line.split(" owes: ");
                    if (lineParts.length == 2) {
                        String name = lineParts[0].substring(2).trim();
                        try {
                            double amount = Double.parseDouble(lineParts[1].trim());
                            double existing = owedAmounts.getOrDefault(name, 0.0);
                            owedAmounts.put(name, existing + amount);
                        } catch (NumberFormatException e) {
                            System.out.println("Warning: Unable to parse amount in record: " + line);
                        }
                    }
                } else if (line.startsWith("Transaction: Expense:")) {
                    try {
                        int memberIdx = line.indexOf("Member: ");
                        int owesIdx = line.indexOf(" owes:");
                        if (memberIdx == -1 || owesIdx == -1 || owesIdx <= memberIdx) {
                            System.out.println("Warning: Skipping incorrectly formatted record: " + line);
                            continue;
                        }
                        String memberName = line.substring(memberIdx + "Member: ".length(), owesIdx).trim();
                        String amountStr = line.substring(owesIdx + " owes:".length()).trim();
                        double amount = Double.parseDouble(amountStr);
                        double existing = owedAmounts.getOrDefault(memberName, 0.0);
                        owedAmounts.put(memberName, existing + amount);
                    } catch (Exception e) {
                        System.out.println("Warning: Skipping incorrectly formatted record: " + line);
                    }
                } else {
                    System.out.println("Warning: Unrecognized record format: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Owed amounts file not found. No expense data available.");
        }

        List < Friend > members = groupManager.getGroupMembers(groupName);
        if (members.isEmpty()) {
            System.out.println("No members in this group.");
        } else {
            System.out.println("Members:");
            int memberCount = 1;
            for (Friend friend: members) {
                String friendName = friend.getName();
                System.out.println(memberCount + ". " + friendName);
                memberCount++;
            }
        }

    }


    /**
     * Displays the members and their total owed amounts for a given group.
     * This method is called directly without requiring user input.
     * Used internally when the group name is already known.
     *
     * @param groupName the name of the group to display.
     */
    public void viewGroupDirect(String groupName) {
        Map < String, Double > owedAmounts = new HashMap < > ();
        File file = new File("owedAmounts.txt");
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                // Handle the old format (e.g., "- Alice owes: 50.00")
                if (line.startsWith("- ")) {
                    String[] lineParts = line.split(" owes: ");
                    if (lineParts.length == 2) {
                        String name = lineParts[0].substring(2).trim(); // Remove "- "
                        try {
                            double amount = Double.parseDouble(lineParts[1].trim());
                            double existing = owedAmounts.getOrDefault(name, 0.0);
                            owedAmounts.put(name, existing + amount);
                        } catch (NumberFormatException e) {
                            System.out.println("Warning: Unable to parse amount in record: " + line);
                        }
                    }
                } else if (line.startsWith("Transaction: Expense:")) {
                    try {
                        int memberIdx = line.indexOf("Member: ");
                        int owesIdx = line.indexOf(" owes:");
                        if (memberIdx == -1 || owesIdx == -1 || owesIdx <= memberIdx) {
                            System.out.println("Warning: Skipping incorrectly formatted record: " + line);
                            continue;
                        }
                        String memberName = line.substring(memberIdx + "Member: ".length(), owesIdx).trim();
                        String amountStr = line.substring(owesIdx + " owes:".length()).trim();
                        double amount = Double.parseDouble(amountStr);
                        double existing = owedAmounts.getOrDefault(memberName, 0.0);
                        owedAmounts.put(memberName, existing + amount);
                    } catch (Exception e) {
                        System.out.println("Warning: Skipping incorrectly formatted record: " + line);
                    }
                } else {
                    System.out.println("Warning: Unrecognized record format: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Owed amounts file not found. No expense data available.");
        }

        // Retrieve group members and display their total owed amounts.
        List < Friend > members = groupManager.getGroupMembers(groupName);
        if (members.isEmpty()) {
            System.out.println("No members in this group.");
        } else {
            System.out.println("Members:");
            for (Friend friend: members) {
                String name = friend.getName();
                double totalOwed = owedAmounts.getOrDefault(name, 0.0);
                System.out.println(name + " - Expense: $" + String.format("%.2f", totalOwed));
            }
        }
    }

    //@@author

    //@@author nandhananm7
    /**
     * Displays all existing groups.
     * If there are no groups, informs the user.
     */
    public void viewAllGroups() {
        if (groupManager.getGroups().isEmpty()) {
            System.out.println("You have no groups.");
        } else {
            for (Group group: groupManager.getGroups()) {
                System.out.println(group);
            }
        }
    }

    /**
     * Adds a member to an existing group or creates a new group if it doesn't exist.
     * Command format: add-member /member name /group name.
     * Prompts for confirmation if the group does not exist.
     *
     * @param command the command string containing member name and group name.
     */
    public void addMember(String command) {
        String[] parts = command.trim().split(" */", 3);
        if (parts.length < 3 || !parts[0].equals("add-member")) {
            System.out.println("Invalid fromat. Usage: add-member/<member name>/<group-name>");
            return;
        }

        String name = parts[1].trim();
        String groupName = parts[2].trim();

        if (name.isEmpty() || !isValidName(name)) {
            System.out.println("Invalid member name. Name cannot be empty or contain special characters.");
            return;
        }

        if (groupName.isEmpty() || !isValidName(groupName)) {
            System.out.println("Invalid group name. Name cannot be empty or contain special characters.");
            return;
        }

        if (groupManager.groupExists(groupName)) {
            if (groupManager.isMemberInGroup(groupName, name)) {
                System.out.println("Member '" + name + "' already exists in group '" + groupName + "'.");
                return;
            }
            groupManager.addFriendToGroup(groupName, new Friend(name, groupName));
            groupManager.saveGroups();
            System.out.println(name + " has been added to " + groupName);
        } else {
            System.out.print("Group does not exist. Would you like to create this group first? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("y")) {
                groupManager.addFriendToGroup(groupName, new Friend(name, groupName));
                groupManager.saveGroups();
                System.out.println("Group " + groupName + " has been created and " + name + " has been added.");
            } else {
                System.out.println("Operation cancelled. " + name + " was not added.");
            }
        }
    }

    /**
     * Removes a member from a specified group.
     * Command format: remove-member /member name /group name
     * Prompts for confirmation before removal.
     *
     * @param command the command string containing member name and group name.
     */
    public void removeMember(String command) {
        String[] parts = command.trim().split(" */", 3);
        if (parts.length < 3 || !parts[0].equals("remove-member")) {
            System.out.println("Invalid format. Usage: remove-member/<member name>/<group-name>");
            return;
        }

        String memberName = parts[1].trim();
        String groupName = parts[2].trim();

        if (memberName.isEmpty() || !isValidName(memberName)) {
            System.out.println("Invalid member name. Name cannot be empty or contain special characters.");
            return;
        }

        if (groupName.isEmpty() || !isValidName(groupName)) {
            System.out.println("Invalid group name. Name cannot be empty or contain special characters.");
            return;
        }

        boolean memberExists = false;
        for (Group group: groupManager.getGroups()) {
            if (group.getName().equals(groupName) && group.isMemberInGroup(memberName)) {
                memberExists = true;
                break;
            }
        }

        if (!memberExists) {
            System.out.println(memberName + " is not in " + groupName);
            return;
        }

        System.out.print("Are you sure you want to remove " + memberName + " from " + groupName + "? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Operation cancelled. " + memberName + " was not removed from " + groupName);
            return;
        }

        boolean removed = false;
        for (Group group: groupManager.getGroups()) {
            if (group.getName().equals(groupName)) {
                removed = group.removeFriend(memberName);
                break;
            }
        }

        if (removed) {
            groupManager.saveGroups();
            System.out.println(memberName + " has been removed from " + groupName);
        } else {
            System.out.println(memberName + " is not in " + groupName);
        }
    }

    /**
     * Removes a specified group after user confirmation.
     * Command format: remove-group /group name
     * If the group exists, prompts the user before removal.
     *
     * @param command the command string containing the group name.
     */
    public void removeGroup(String command) {
        String[] parts = command.trim().split(" */", 2);
        if (parts.length < 2 || !parts[0].equals("remove-group")) {
            System.out.println("Invalid format. Usage: remove-group/<group-name>");
            return;
        }

        String groupName = parts[1].trim();

        if (!groupManager.groupExists(groupName)) {
            System.out.println("Group: " + groupName + " does not exist.");
            return;
        }

        System.out.print("Are you sure you want to remove the group " + groupName + "? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Operation cancelled. Group " + groupName + " was not removed.");
            return;
        }

        groupManager.removeGroup(groupName);
        groupManager.saveGroups();
        System.out.println("Group " + groupName + " has been removed.");
    }
}
//@@author
