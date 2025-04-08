## Quick Start

1.  Ensure you have Java 17 or above installed on your PC. **[Version 17 is preferred]**
2.  You may download [here](https://se-education.org/guides/tutorials/javaInstallationMac.html) for Mac users and [here](https://www.oracle.com/sg/java/technologies/downloads/) for Windows users.
3.  If you have it installed already, you may check it by running `java -version` in your terminal.
4.  Download the latest `.jar` file from here(https://github.com/AY2425S2-CS2113-F11-2/tp/releases/tag/v2.0).
5.  Copy the file to the folder you want to use as the home folder for your **O\$P$ budget tracking app** 🙂.
6.  Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar tp.jar` command to run the application.
7.  Type the command in the command box and press **Enter** to execute it.
8.  **Example:** Typing `help` and pressing **Enter** will open a mini window showing a list of all possible commands.
9.  Refer to the [features](https://docs.google.com/document/d/125Cg7wzuc4XFo3wsziwL2f64KN1uUfvFL5dIm6IQrSk/edit?tab=t.xl7ogrtj0a5q#heading=h.61o02m6y9xrc) section below for details on all commands and functionalities.

---

## Feature List

These are all the available commands that the user can input while navigating through the O\$P\$ application.

### Admin

#### Viewing help: `help`

Lists the commands available to the user.

- **Usage:** `help`
- **Expected output:** _[To insert list of commands that will be returned]_

---

#### Exiting the program: `exit`

Terminates the program and saves the user's data, such as their payee list, groups, balance, and transaction information to a `.csv` file.

- **Usage:** `exit`
- **Expected output:** _[Save aforementioned data into file and produce an exit message]_

---

### Manage Expenses

#### Add an expense: `add`

- **Format:** `add/<title>/<category>/<date>/<amount>`

Add an expense with a title, description, date, amount. You have to first enter
the `add` keyword, followed by the `title`,`category`, `date`, `amount`, each separated by a `/`.

The `date` field has to follow the DD-MM-YYYY format. You can choose the date to be in the past (earliest 2000),
present and future, as long as it is a legitimate date (e.g. NOT 99-99-9999).

Once the expense has been added, it is unsettled. Typing `list-unsettled` will allow you to view
your newly added expense, just like `list`.

The `category` field has to be one of these categories (Food/Travel/Entertainment/Shopping/Miscellaneous).

The `amount` can be entered as a whole number or floating-point number. The result will be rounded off to 2 decimal
places. The `amount` is capped at 50,000SGD or its equivalent if your expenses are in another currency.

- **Example Usage:**

```
add/Chicken Rice/Food/25-12-2025/100
```

```
Expense added successfully:
Title: chicken rice
Category: Food
Date: 25-12-2025
Amount: 100.00
```

The entry will automatically be tagged with a unique expense ID.

You are not allowed to add an expense containing a `title` of an expense that already exists.

---

#### Edit an expense: `edit`

Edit an existing expense. Follows the same constraints and parameter-filling procedure as the `add` command. 'X' can be used to keep existing values.

- **Format:** `edit/<expense ID>/<new title>/<new category>/<new date>/<new amount>`

- **Example Usage:**

```
edit/1/chicken rice/food/20-08-2004/20
```

- **Output:**
  If you choose to change the amount:

```
Expense edited successfully:
Title: chicken rice
Category: Food
Date: 20-08-2004
Amount: 100.00
```

If you choose to change the category:

```
Expense edited successfully:
Title: chicken rice
Category: Micellaneous
Date: 20-08-2004
Amount: 20.00
```

If you choose to keep the current category:

```
Expense edited successfully:
Title: chicken rice
Category: Food
Date: 20-08-2004
Amount: 20.00
```

---

#### Delete an expense: `delete`

Delete an expense followed by the desired expense index to remove it from the `list`.
You will see the title, description, date and amount of the to-be-deleted expense and
be prompted to confirm the deletion.

Expense ID is 1-index, so to delete your second expense, type `delete/2`.

- **Format:** `delete/<expense ID>`

- **Example Usage:**

```
delete/1
```

- **Output:**

```
Are you sure you want to delete this expense? (y/n)
Title: breakfast
Description: Technoedge Canteen
Date: 23-08-2002
Amount: 10.00
```

Typing 'n' (both lowercase and uppercase accepted) aborts the function.

```
Deletion aborted.
```

Typing 'y' (both lowercase and uppercase accepted) will display the title, description, date and amount of the to-be-deleted expense
and execute the delete function.

```
Updated owed amounts written to file successfully.
Expense deleted successfully:
Title: breakfast
Description: Technoedge Canteen
Date: 23-08-2002
Amount: 10.00
```

---

#### View all expenses: `list`

View all the expenses.

- **Format:** `list`
- **Usage:** `list`
- **Example:**
  ```
  List of Expenses:
  Expense #1
  Title: test expense
  Description: testing testing
  Date: 01-01-2025
  Amount: $50.00
  ```

---

#### View unsettled expenses: `list-unsettled`

View all expenses that are unmarked

- **Format and Example Usage:** `list-unsettled`
- **Output:**

  ```
  All expenses are in SGD
  Expense #1
  Title: test expense
  Description: testing testing
  Date: 20-08-2004
  Amount: 50.00

  You have 1 unsettled expense
  ```

---

#### View settled expenses: `list-settled`

View all expenses that are marked

- **Format and Example Usage:** `list-settled`
- **Output:**

  ```
  All expenses are in SGD
  Expense #1
  Title: test expense
  Description: testing testing
  Date: 20-08-2004
  Amount: 50.00

  You have 1 settled expense
  ```

---

#### Mark settled expenses: `mark`

Once a transaction is made, the user can mark it as paid.

- **Format** `mark/<expense ID>`
- **Example Usage:** `mark/1`
- **Output**
  ```
  Expense 1 successfully marked!
  ```

---

#### Unmark settled expenses: `unmark`

User can unmark an expense that has been marked already.

- **Format** `unmark/<expense ID>`
- **Example Usage:** `unmark/1`
- **Output**
  ```
  Expense 1 successfully unmarked!
  ```

---

#### Find from previous expenses: `find`

User can find expenses that contain this keyword in the expense description

- **Format:** `find/<keyword>`
- **Usage:** `find/taxi`
- **Example:**
  ```
  Found 1 matching expense(s):
  Title: taxi
  Description: nil
  Date: 12-12-2024
  Amount: 50.00
  ```

---

#### Change currency: `change-currency`

User can change the currency of all expenses. Almost all currencies are supported to be exchanged to, even the currency you are currently using.

Currency to change to must follow the ISO 4217 standard (eg: SGD, USD, JPY), either in upper or lowercase.

Exchange rate must be positive and below 50000.

Rates in method 2 are only an estimate.

After exchange, the amount will be rounded off to 2dp.

- **Format for Method 1** `change-currency/1/<currency to change to>/<exchange rate>`
- **Format for Method 2** `change-currency/2/<currency to change to>`
- **Example Usage For Method 1:** `change-currency/1/EUR/0.75`
- **Example Usage For Method 2:** `change-currency/2/EUR`

- **Output**
  ```
  "Currency successfully changed to EUR"
  ```

---

#### View balance in wallet: `balance`

Shows total money the user needs to pay.

- **Format and Usage:** `balance`
-
- **Output:**
  ```
  Balance Overview
  ----------------
  Total number of unsettled expenses: <total number of unsettled expenses user has>
  Total unsettled amount: <total unsettled amount>
  ```

---

### Manage Group Members:

#### Create a new group: `create-group`

Create a new group to split expenses with.
If you would like to include yourself in the group, please add your name as well explicitly when the application asks to enter name.

- **Format** `create-group/<group-name>`
- **Usage** `create-group/test`

- **Output:**
  ```
  Who would you like to add to the group? (Type 'done' to finish)
  Enter name: name1
  Enter name: name2
  Enter name: done
  Group created successfully!
  ```

#### View friends and expenses in a certain group: `view-member`

View a specific group and see how much each member owes.

- **Format:** `view-group/<group-name>/<member name>`
- **Usage:** `view-group/testGroup/john`

- **Output:**

  ```
  Transactions for member 'john' in group 'testGroup':
  Transaction: Expense: expense1, Date: 10-10-2025, Group: testGroup, Member: john owes: 10.00
  Transaction: Expense: expense2, Date: 10-10-2025, Group: testGroup, Member: john owes: 30.00
  Transaction: Expense: expense3, Date: 10-10-2025, Group: testGroup, Member: john owes: 60.00
  Total Amount: 100.00

  ```

---

---

#### View friends and expenses in a certain group: `view-group`

View a specific group and its constituent members.

- **Format:** `view-group/<group-name>`
- **Usage:** `view-group/test`

- **Output:**

  ```
  Group: testGroup
  Members:
  john
  robert

  ```

---

#### Add Friends to a group: `add-member`

Adds a user to a group.

- **Format:** `add-member/<member-name>/<group-name>`
- **Usage:** `add-member/hij/test`

- **Output:**
  If the group exists, then adds to the existing group.

  ```
  hij has been added to test
  ```

  If the group does not exist, the user is prompted to create the group first.

  ```
  Group does not exist. Would you like to create this group first? (y/n): y
  Group test1 has been created and hij has been added.
  ```

---

#### Remove Friend from a group: `remove-member`

Removes a member from a group

- **Format:** `remove-member/<member name>/<group-name>`
- **Usage:** `remove-member/hij/test`

- **Output:**

  ```
  Are you sure you want to remove hij from test1? (y/n): y
  hij has been removed from test1
  ```

---

#### View all groups created by user: `my-groups`

Shows all the groups that the user has created.

- **Format and Usage:** `my-groups`

- **Output:**

  ```
  Group Name: TestGroup
  Members:
  - Alice
  - Bob
  - natasha

  Group Name: NewGroup
  Members:
  - Charlie

  Group Name: test group
  Members:
  - apple
  - mango
  - carrot
  ```

---

#### Removes an existing group: `remove-group`

Removes an entire group.

- **Format:** `remove-group/<group-name>`
- **Usage:** `remove-group/test1`

- **Output:**
  ```
  Are you sure you want to remove the group test1? (y/n): y
  Group test1 has been removed.
  ```

---

### Manage Payments:

#### Split an expense among group members: `split`

Splits a selected expense among members of a specific group, either **equally** or through **manual assignment**
(using absolute amounts or percentages).

- **Format:**
`split/<equal | assign>/<expense index>/<group name>`

- **Usage:**
`split/equal/1/testGroup`

- **Constraints:**

- Expense index must be a **positive integer** (starting from 1).
- The group must already exist and contain at least one member.
- The same expense cannot be split more than once by the same group.
- You can split either:
  - Equally among all members, or
  - Manually assign amounts using absolute values (`/a`) or percentages (`/p`).

- **Examples:**

- **Equal Split:**

  ```
  split/equal/1/friends
  ```

  **Output:**

  ```
  Selected expense amount: 100.00
  Splitting 100.00 equally among 2 members of group "friends":
  Transaction: Expense: Lunch, Date: 01-01-2025, Group: friends, Member: Alice owes: 50.00
  Transaction: Expense: Lunch, Date: 01-01-2025, Group: friends, Member: Bob owes: 50.00
  Updated list of transactions!
  ```

- **Manual Split (Absolute Amounts):**

  ```
  split/assign/1/friends
  ```

  **Prompted interaction:**

  ```
  Type '/a' for absolute amounts OR '/p' for percentages: /a
  Total expense amount to split: 100.00
  You can assign up to 100.00 in total.
  Enter amount for Alice: 30
  Remaining expense: 70.00
  Enter amount for Bob: 70
  ```

  **Output:**

  ```
  Transaction: Expense: Lunch, Date: 01-01-2025, Group: friends, Member: Alice owes: 30.00
  Transaction: Expense: Lunch, Date: 01-01-2025, Group: friends, Member: Bob owes: 70.00
  Updated list of transactions!
  ```

- **Manual Split (Percentage-Based):**

  ```
  split/assign/1/friends
  ```

  **Prompted interaction:**

  ```
  Type '/a' for absolute amounts OR '/p' for percentages: /p
  Total expense is 100.00. You can assign up to 100% in total.
  Enter percentage for Alice: 40
  Remaining percentage: 60.00%
  Enter percentage for Bob: 60
  ```

  **Output:**

  ```
  Transaction: Expense: Lunch, Date: 01-01-2025, Group: friends, Member: Alice owes: 40.00
  Transaction: Expense: Lunch, Date: 01-01-2025, Group: friends, Member: Bob owes: 60.00
  Updated list of transactions!
  ```

- **Notes:**

- Names are taken to be unique throughout the entire program. "John" in group1 is the same as "John" in group2.
- For the manual `assign` option, the absolute amount and percentage does not need to add up to 100% the full amount. While it cannot exceed the total value of the expense, the sum can be smaller than the full amount.
- The split function does not take into account you as the user. Add yourself as a member to the group if you'd like to keep track of your split expenses in that particular group.
- Each expense can only be split once per group.
- Splitting an expense bakes in the value for that member, when the expense is edited/deleted, the amount owed for the members (when it was split) does not change.
- Avoid using floating point numbers in the percentage ("/p" > "33.3") as it may be inprecise due to the way the number are stored.
- Calling the split function does not mark or settle the expense. THe user has to manually trigger those events through the prescribed commands.
- An expense can be split multiple times (by different groups).
- All transaction details are stored per member and per group for tracking and retrieval.

---

### Expense Analytics:

#### View summary of expenses: `summary`

Displays comprehensive analytics of your expenses through different visualization options. This command helps you track and analyze your spending patterns.

- **Format:** `summary/<BY-MONTH|BY-CATEGORY>/<Y|N>`

  - First parameter must be either `BY-MONTH` or `BY-CATEGORY`
  - Second parameter must be `Y` or `N` for visualization
  - Note: `BY-MONTH` only supports `N` option (no visualization)

- **Features:**

  1. **Monthly Summary (`summary/BY-MONTH/N`)**

  - Shows total expenses for each month
  - Lists all expenses within each month
  - Displays expense count per month
  - No visualization available for monthly view

  2. **Category-wise Summary (`summary/BY-CATEGORY/Y` or `summary/BY-CATEGORY/N`)**

  - Breaks down expenses into categories (Food, Travel, Entertainment, Shopping, Miscellaneous)
  - Shows total amount and count for each category
  - Optional pie chart visualization (Y/N)
  - Displays percentage distribution across categories

- **Example Usage:**

  ```
  summary/BY-CATEGORY/Y
  ```

- **Example Output:**

  ```
  Category-wise Expense Summary:
  ----------------------------
  Food: $480.00 (4 expenses)
  Travel: $300.00 (2 expenses)
  Entertainment: $180.00 (3 expenses)
  Shopping: $120.00 (1 expense)
  Miscellaneous: $120.00 (2 expenses)

  [Pie chart visualization will appear in a separate window]
  ```

  ![image](https://github.com/user-attachments/assets/5eb6f031-9924-43d2-ab6b-3540e15fcefb)

- **Notes about Pie Chart Visualization:**
  - Only available for category-wise summary
  - Shows percentages to one decimal place
  - For expenses with large value differences (e.g., $50,000 vs $10):
    - Very small expenses may not be clearly visible on the chart
    - Hover over segments to see exact values
    - Legend shows both amount and percentage for each category
  - **IMPORTANT**: You must close the pie chart window before exiting the program. Due to a limitation in the visualization API, if you do not close the window, the program will not terminate properly.
  - Chart window will automatically close when program exits
  - Close the chart window to return to the application

---

### Export Summary Reports:

#### Export expense summaries: `export`

Exports your expense summaries to text files for record-keeping, sharing, or external analysis.

- **Format:** `export/<monthly|category wise>`

  - Parameter must be either `monthly` or `category wise`

- **Features:**

  1. **Monthly Summary Export (`export/monthly`)**

     - Exports data to `monthly_summary.txt`
     - Contains total expenses for each month
     - Lists all expenses within each month with details
     - Maintains the same format as the monthly summary display

  2. **Category-wise Summary Export (`export/category wise`)**
     - Exports data to `category_summary.txt`
     - Contains breakdown of expenses by category
     - Shows total amount and count for each category
     - Displays percentage distribution across categories

- **Example Usage:**

  ```
  export/monthly
  ```

- **Output:**

  ```
  Monthly summary exported to monthly_summary.txt
  ```

- **Example File Content (monthly_summary.txt):**

  ```
  Monthly Expense Summary:
  ----------------------
  03-2025: $300.00 (1 expenses)
    Expenses:
    - March Expense (15-03-2025): $300.00

  04-2025: $400.00 (1 expenses)
    Expenses:
    - April Expense (15-04-2025): $400.00
  ```

- **Notes:**
  - Files are created in the same directory as the application
  - Existing files with the same name will be overwritten
  - The export uses the same formatting as the in-app summary display
  - Text files can be opened with any text editor

---

### Viewing Method for Expenses:

#### Select Viewing Method: `sort-list`

Allows the user to choose a method to view their expenses with the following options:

[1] Title (ascending alphabetically)
[2] Title (descending alphabetically)
[3] Amount (ascending)
[4] Amount (descending)

- **Format:** `sort-list/N`, where N = 1,2,3 or 4

- **Example Usage:**

```
sort-list/1
```

- **Output:**

```
Expenses sorted by title (ascending):

Title: breakfast
Description: McDonald's
Date: 01-01-2025
Amount: 10.00

Title: hihi
Description: pe
Date: 04-01-2024
Amount: 90.00

Title: ye
Description: yes
Date: 01-01-2025
Amount: 10.00
```

- **Example Usage:**

```
sort-list/2
```

- **Output:**

```
Expenses sorted by title (descending):

Title: ye
Description: yes
Date: 01-01-2025
Amount: 10.00

Title: hihi
Description: pe
Date: 04-01-2024
Amount: 90.00

Title: breakfast
Description: McDonald's
Date: 01-01-2025
Amount: 10.00
```

- **Example Usage:**

```
sort-list/3
```

- **Output:**

```
Expenses sorted by amount (ascending):

Title: breakfast
Description: McDonald's
Date: 01-01-2025
Amount: 10.00

Title: ye
Description: yes
Date: 01-01-2025
Amount: 10.00

Title: hihi
Description: pe
Date: 04-01-2024
Amount: 90.00
```

- **Example Usage:**

```
sort-list/4
```

- **Output:**

```
Expenses sorted by amount (descending):

Title: hihi
Description: pe
Date: 04-01-2024
Amount: 90.00

Title: breakfast
Description: McDonald's
Date: 01-01-2025
Amount: 10.00

Title: ye
Description: yes
Date: 01-01-2025
Amount: 10.00
```

## Command Summary

Here's a quick reference for all available commands:

| Command         | Format                                                  | Example Usage                 |
| --------------- |---------------------------------------------------------|-------------------------------|
| Help            | `help`                                                  | `help`                        |
| Exit            | `exit`                                                  | `exit`                        |
| Add Expense     | `add/<title>/<date>/<amount>`                           | `add/lunch/01-01-2024/15.50`  |
| Edit Expense    | `edit/<expense ID>/<new title>/<new date>/<new amount>` | `edit/1/dinner/x/20.00`       |
| Delete Expense  | `delete/<expense ID>`                                   | `delete/1`                    |
| List All        | `list`                                                  | `list`                        |
| List Unsettled  | `list-unsettled`                                        | `list-unsettled`              |
| List Settled    | `list-settled`                                          | `list-settled`                |
| Mark Settled    | `mark/<expense ID>`                                     | `mark/1`                      |
| Unmark Settled  | `unmark/<expense ID>`                                   | `unmark/1`                    |
| Find Expense    | `find/<keyword>`                                        | `find/taxi`                   |
| Change Currency | `change-currency/<method>/<currency>[/<rate>]`          | `change-currency/1/USD/0.75`  |
| View Balance    | `balance`                                               | `balance`                     |
| Create Group    | `create-group/<group-name>`                             | `create-group/test`           |
| View Group      | `view-group/<group-name>`                               | `view-group/test`             |
| Add Member      | `add-member/<member name>/<group-name>`                 | `add-member/John/Friends`     |
| Remove Member   | `remove-member/<member-name>/<group-name>`              | `remove-member/John/Friends`  |
| View All Groups | `my-groups`                                             | `my-groups`                   |
| Remove Group    | `remove-group/<group-name>`                             | `remove-group/test`           |
| Split Expense   | `split`                                                 | `split` (then follow prompts) |
| View Summary    | `summary/[BY-MONTH\|BY-CATEGORY]/[Y\|N]`                | `summary/BY-CATEGORY/Y`       |
| Export Summary  | `export/<monthly\|category wise>`                       | `export/monthly`              |
| Sort List       | `sort-list/<option>`                                    | `sort-list/1`                 |
