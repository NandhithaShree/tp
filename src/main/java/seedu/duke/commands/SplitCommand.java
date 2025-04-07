package seedu.duke.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

import seedu.duke.expense.Expense;
import seedu.duke.friends.Friend;
import seedu.duke.friends.GroupManager;
import seedu.duke.storage.DataStorage;
import java.util.Formatter;


public class SplitCommand {
    private Scanner scanner;
    private GroupManager groupManager;
    private FriendsCommands friendsCommands;

    public SplitCommand(Scanner scanner, GroupManager groupManager, FriendsCommands friendsCommands) {
        if (scanner == null || groupManager == null || friendsCommands == null) {
            throw new IllegalArgumentException("Scanner, GroupManager, and FriendsCommands must not be null.");
        }
        this.scanner = scanner;
        this.groupManager = groupManager;
        this.friendsCommands = friendsCommands;
    }

    /**
     * Executes the split command using the unified format:
     */
    public void executeSplit(String command) {
        try {
            if (command == null || command.trim().isEmpty()) {
                throw new IllegalArgumentException("Command cannot be empty.");
            }
            // Split command using "/" as delimiter.
            String[] parts = command.trim().split(" */");
            if (parts.length != 4) {
                throw new IllegalArgumentException(
                    "Invalid format. Usage: split/<equal|assign>/<expense index>/<group name>");
            }
            String commandWord = parts[0].trim();
            if (!commandWord.equalsIgnoreCase("split")) {
                throw new IllegalArgumentException("Invalid format." +
                    " Usage: split/<equal|assign>/<expense index>/<group name>");
            }
            String splitOption = parts[1].trim().toLowerCase();
            if (!splitOption.equals("equal") && !splitOption.equals("assign")) {
                throw new IllegalArgumentException("Invalid split option: '" + splitOption +
                    "'. Use 'equal' for an equal split or 'assign' for manual splitting.");
            }
            String expenseIndexStr = parts[2].trim();
            int expIndex;
            try {
                int index = Integer.parseInt(expenseIndexStr);
                if (index < 1) {
                    throw new IllegalArgumentException("Expense index must be a positive integer.");
                }
                expIndex = index - 1; // converting to 0-based index
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid expense index: '" + expenseIndexStr + "'");
            }
            String groupName = parts[3].trim();
            if (groupName.isEmpty()) {
                throw new IllegalArgumentException("Group name cannot be empty.");
            }
            List < Expense > expenses = DataStorage.loadExpenses();
            if (expenses.isEmpty()) {
                throw new IllegalArgumentException("No expenses available to split.");
            }
            if (expIndex >= expenses.size()) {
                throw new IllegalArgumentException("Expense index out of range: " + (expIndex + 1));
            }
            Expense selectedExpense = expenses.get(expIndex);
            double totalAmount = selectedExpense.getAmount();
            System.out.println("Selected expense amount: " + String.format("%.2f", totalAmount));

            if (!groupManager.groupExists(groupName)) {
                throw new IllegalArgumentException("Group '" + groupName + "' not found.");
            }
            List < Friend > members = groupManager.getGroupMembers(groupName);
            if (members == null || members.isEmpty()) {
                throw new IllegalArgumentException("No members in group '" +
                    groupName + "'. Cannot perform split.");
            }
            
            // Set the group name on the expense before checking for duplicate splits
            selectedExpense.setGroupName(groupName);

            // Prevent duplicate splits: 
            //check if the expense has already been split for this group.
            boolean alreadySplit = false;
            File owesFile = new File(OwesStorage.owesFile);
            if (owesFile.exists() && owesFile.length() > 0) {
                try {
                    List<String> lines = Files.readAllLines(owesFile.toPath());
                    for (String line : lines) {
                        if (line.contains("Expense: " + selectedExpense.getTitle()) &&
                            line.contains("Date: " + selectedExpense.getDate()) &&
                            line.contains("Group: " + groupName)) {
                            alreadySplit = true;
                            System.out.println("This expense has already been split for group '" + groupName + "'");
                            return;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error checking for duplicate splits: " + e.getMessage());
                    return;
                }
            }

            if (splitOption.equals("equal")) {
                int numMembers = members.size();
                double share = totalAmount / numMembers;
                System.out.println("Splitting " + totalAmount + " equally among " + numMembers +
                    " members of group \"" + groupName + "\":");
                for (Friend member: members) {
                    String assignment = createTransactionRecord(selectedExpense, groupName, member.getName(), share);
                    System.out.println(assignment);
                    OwesStorage.appendOwes(assignment);
                }
                System.out.println("Updated list of transactions!");
                friendsCommands.viewGroupDirect(groupName);
            } else { // "assign" option for manual split
                String method = "";
                while (true) {
                    System.out.print("Type '/a' for absolute amounts OR '/p' for percentages: ");
                    if (!scanner.hasNextLine()) {
                        System.out.println("No input available. Exiting split command.");
                        return;
                    }
                    method = scanner.nextLine().trim().toLowerCase();
                    if (method.equals("/a") || method.equals("/p")) {
                        break;
                    } else {
                        System.out.println("Invalid format. Usage: </a|/p>");
                    }
                }
                if (method.equals("/a")) {
                    double remaining = totalAmount;
                    System.out.println("Total expense amount to split: " + totalAmount);
                    System.out.println("You can assign up to " + remaining + " in total.");
                    for (Friend member: members) {
                        while (true) {
                            System.out.println("Remaining expense: " + String.format("%.2f", remaining));
                            System.out.print("Enter amount for " + member.getName() + ": ");
                            String amtStr = scanner.nextLine().trim();
                            double assigned;
                            try {
                                assigned = Double.parseDouble(amtStr);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid amount format for " + member.getName() +
                                    ". Please enter a numeric value.");
                                continue;
                            }
                            if (assigned < 0) {
                                System.out.println("Assigned amount for " + member.getName() +
                                    " cannot be negative. Try again.");
                            } else if (assigned > remaining) {
                                System.out.println("Assigned amount for " + member.getName() +
                                    " exceeds remaining amount of " + String.format("%.2f", remaining) +
                                    ". Try again.");
                            } else {
                                remaining -= assigned;
                                String assignment = createTransactionRecord(selectedExpense,
                                    groupName, member.getName(), assigned);
                                System.out.println(assignment);
                                OwesStorage.appendOwes(assignment);
                                break;
                            }
                        }
                    }
                    System.out.println("Updated list of transactions!");
                    // Call viewGroupDirect after manual absolute splitting.
                    friendsCommands.viewGroupDirect(groupName);
                } else { // method equals "/p" for percentage splitting
                    double remainingPercentage = 100.0;
                    System.out.println("Total expense is " + totalAmount +
                        ". You can assign up to 100% in total.");
                    for (Friend member: members) {
                        while (true) {
                            System.out.println("Remaining percentage: " +
                                String.format("%.2f", remainingPercentage) + "%");
                            System.out.print("Enter percentage for " + member.getName() + ": ");
                            String percStr = scanner.nextLine().trim();
                            double percent;
                            try {
                                percent = Double.parseDouble(percStr);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid percentage format for " + member.getName() +
                                    ". Please enter a numeric value.");
                                continue;
                            }
                            if (percent < 0 || percent > remainingPercentage) {
                                System.out.println("Percentage for " + member.getName() +
                                    " must be between 0 and " + String.format("%.2f", remainingPercentage) +
                                    ". Try again.");
                            } else {
                                double assignedAmount = totalAmount * (percent / 100.0);
                                String assignment = createTransactionRecord(selectedExpense,
                                    groupName, member.getName(), assignedAmount);
                                System.out.println(assignment);
                                OwesStorage.appendOwes(assignment);
                                remainingPercentage -= percent;
                                break;
                            }
                        }
                    }
                    System.out.println("Updated list of transactions!");
                    // Call viewGroupDirect after manual percentage splitting.
                    friendsCommands.viewGroupDirect(groupName);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String createTransactionRecord(Expense expense,
                                           String groupName, String memberName, double amount) {
        return "Transaction: Expense: " + expense.getTitle() +
                ", Date: " + expense.getDate() +
                ", Group: " + groupName +
                ", Member: " + memberName +
                " owes: " + String.format("%.2f", amount);
    }

    private void handleEqualSplit(Expense expense, List<Friend> members) {
        double totalAmount = expense.getAmount();
        double share = totalAmount / members.size();
        
        // Get the group name from the first member
        String groupName = members.isEmpty() ? null : members.get(0).getGroup();
        // Set group name on the expense
        expense.setGroupName(groupName);
        
        for (Friend member : members) {
            String transaction = String.format("Transaction: Expense: %s, Date: %s, Group: %s, Member: %s owes: %.2f",
                expense.getTitle(), expense.getDate(), expense.getGroupName(), member.getName(), share);
            OwesStorage.saveTransaction(transaction);
            System.out.println(transaction);
        }
    }

    private void handleManualSplit(Expense expense, List<Friend> members, Scanner scanner) {
        System.out.println("Enter '/a' for absolute amounts or '/p' for percentages:");
        String method = scanner.nextLine().trim();
        
        if (method.equals("/a")) {
            handleAbsoluteSplit(expense, members, scanner);
        } else if (method.equals("/p")) {
            handlePercentageSplit(expense, members, scanner);
        } else {
            System.out.println("Invalid method. Please enter '/a' for absolute amounts or '/p' for percentages.");
            handleManualSplit(expense, members, scanner);
        }
    }

    private void handleAbsoluteSplit(Expense expense, List<Friend> members, Scanner scanner) {
        double totalAmount = expense.getAmount();
        double remainingAmount = totalAmount;
        
        // Get the group name from the first member
        String groupName = members.isEmpty() ? null : members.get(0).getGroup();
        // Set group name on the expense
        expense.setGroupName(groupName);
        
        for (int i = 0; i < members.size() - 1; i++) {
            Friend member = members.get(i);
            System.out.println("Enter amount for " + member.getName() + ":");
            String input = scanner.nextLine().trim();
            
            try {
                double amount = Double.parseDouble(input);
                if (amount < 0 || amount > remainingAmount) {
                    System.out.println("Invalid amount. Please enter a value between 0 and " + remainingAmount);
                    i--; // Retry for this member
                    continue;
                }
                
                String transaction = String.format("Transaction: Expense: %s, Date: %s, Group: %s, " +
                                "Member: %s owes: %.2f",
                    expense.getTitle(), expense.getDate(), expense.getGroupName(), member.getName(), amount);
                OwesStorage.saveTransaction(transaction);
                System.out.println(transaction);
                
                remainingAmount -= amount;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format for " + member.getName());
                i--; // Retry for this member
            }
        }
        
        // Last member gets the remaining amount
        Friend lastMember = members.get(members.size() - 1);
        String transaction = String.format("Transaction: Expense: %s, Date: %s, Group: %s, Member: %s owes: %.2f",
            expense.getTitle(), expense.getDate(), expense.getGroupName(), lastMember.getName(), remainingAmount);
        OwesStorage.saveTransaction(transaction);
        System.out.println(transaction);
    }

    private void handlePercentageSplit(Expense expense, List<Friend> members, Scanner scanner) {
        double totalAmount = expense.getAmount();
        double remainingPercentage = 100.0;
        
        // Get the group name from the first member
        String groupName = members.isEmpty() ? null : members.get(0).getGroup();
        // Set group name on the expense
        expense.setGroupName(groupName);
        
        for (int i = 0; i < members.size() - 1; i++) {
            Friend member = members.get(i);
            System.out.println("Enter percentage for " + member.getName() + ":");
            String input = scanner.nextLine().trim();
            
            try {
                double percentage = Double.parseDouble(input);
                if (percentage < 0 || percentage > remainingPercentage) {
                    System.out.println("Invalid percentage. Please enter a value between 0 and " + remainingPercentage);
                    i--; // Retry for this member
                    continue;
                }
                
                double amount = totalAmount * (percentage / 100.0);
                String transaction = String.format("Transaction: Expense: %s," +
                                " Date: %s, Group: %s, Member: %s owes: %.2f",
                    expense.getTitle(), expense.getDate(), expense.getGroupName(), member.getName(), amount);
                OwesStorage.saveTransaction(transaction);
                System.out.println(transaction);
                
                remainingPercentage -= percentage;
            } catch (NumberFormatException e) {
                System.out.println("Invalid percentage format for " + member.getName());
                i--; // Retry for this member
            }
        }
        
        // Last member gets the remaining percentage
        Friend lastMember = members.get(members.size() - 1);
        double amount = totalAmount * (remainingPercentage / 100.0);
        String transaction = String.format("Transaction: Expense: %s, Date: %s, Group: %s, Member: %s owes: %.2f",
            expense.getTitle(), expense.getDate(), expense.getGroupName(), lastMember.getName(), amount);
        OwesStorage.saveTransaction(transaction);
        System.out.println(transaction);
    }

    /**
     * Nested class to handle owes file operations.
     */
    public class OwesStorage {
        public static String owesFile = "owedAmounts.txt";
        public static String checksumFile = "owedAmounts.chk";

        // Appends a transaction record to the owes file and updates the checksum.
        public static void appendOwes(String text) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(owesFile, true))) {
                writer.println(text);
            } catch (IOException e) {
                System.out.println("Error writing to owes file: " + e.getMessage());
            }
            updateChecksum();
        }

        // Recalculates and writes the checksum for the owes file.
        public static void updateChecksum() {
            try {
                byte[] fileBytes = Files.readAllBytes(new File(owesFile).toPath());
                String checksum = getSHA256Checksum(fileBytes);
                try (PrintWriter writer = new PrintWriter(new FileWriter(checksumFile, false))) {
                    writer.println(checksum);
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                System.out.println("Error updating checksum: " + e.getMessage());
            }
        }

        // Verifies the current checksum against the stored checksum.
        public static boolean verifyChecksum() {
            try {
                byte[] fileBytes = Files.readAllBytes(new File(owesFile).toPath());
                String currentChecksum = getSHA256Checksum(fileBytes);
                List < String > lines = Files.readAllLines(new File(checksumFile).toPath());
                if (lines.isEmpty()) {
                    return false;
                }
                String storedChecksum = lines.get(0).trim();
                return currentChecksum.equals(storedChecksum);
            } catch (IOException | NoSuchAlgorithmException e) {
                System.out.println("Error verifying checksum: " + e.getMessage());
                return false;
            }
        }

        // Utility method to compute SHA-256 checksum of the given data.
        private static String getSHA256Checksum(byte[] data) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(data);
            Formatter formatter = new Formatter();
            for (byte b: hashBytes) {
                formatter.format("%02x", b);
            }
            String hash = formatter.toString();
            formatter.close();
            return hash;
        }

        public static void saveTransaction(String transaction) {
            appendOwes(transaction);
        }
    }

    /**
     * Inner static class that lists owed transactions for a particular member in a group.
     * It gracefully handles any unexpected or malformed records in the owed amounts file.
     */
    public static class OwedTransactionsLister {


        /**
         * Lists all owed transactions for a particular member in a group.
         *
         * @param memberName The name of the member.
         * @param groupName  The name of the group.
         */
        public void listOwedTransactions(String memberName, String groupName) {
            File file = new File(OwesStorage.owesFile);
            if (!file.exists()) {
                System.out.println("No owed transactions found. The file does not exist.");
                return;
            }
            try {
                List < String > allLines = Files.readAllLines(file.toPath());
                boolean found = false;
                System.out.println("Owed transactions for member '" + memberName + "' in group '" + groupName + "':");
                for (String line: allLines) {
                    if (line == null || line.trim().isEmpty()) {
                        continue; // skip blank lines
                    }
                    // Check if the line follows the expected detailed format.
                    if (!line.startsWith("Transaction: Expense:")) {
                        System.out.println("Warning: Skipping unrecognized record: " + line);
                        continue;
                    }
                    // Check if the record pertains to the given member and group.
                    if (line.contains("Group: " + groupName) && line.contains("Member: " + memberName)) {
                        System.out.println(line);
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("No transactions found for member '" +
                        memberName + "' in group '" + groupName + "'.");
                }
            } catch (IOException e) {
                System.out.println("Error reading owed transactions: " + e.getMessage());
            }
        }
    }
}
