# Nandhitha Shree - Project Portfolio Page

## Overview

---

O\$P\$ is an expense-tracker application that allows the user to keep track of their expenditure with ease! Users can also manage expenses in a group setting with simple
splitting functions, as well as view analytics of their spending across multiple categories.

## Summary of Contributions

---

### Code Contributed

Code Contribution to the project: [Reposense Page](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=nandhithashree&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

### Features Implemented
Here is the list of feature implemented:

#### 1. Mark and Unmark Feature

- **Functionality**: Users can now mark expenses as settled or unsettle them as needed. This allows for easy tracking of which expenses have been resolved and which are still pending.
- **Explanation**: Users benefit from this as they are able to differentiate between what expenses they have yet to settle, and those they have already settled.

#### 2. Display settled and unsettled Feature

- **Functionality**: Separately displays settled expenses and unsettled expenses when prompted, followed by the number of expenses for each list.
- **Justification**: Allows user to visualise and collate the number of each of those expenses, keeping track of paid expenses, and identifying outstanding payments more effectively. This separation provides clarity, allowing users to quickly assess their financial obligations and manage their budgets accordingly.

#### Currency Management

- **Functionality**: Manages currency conversion and exchange rates, allowing users to track their budget in different currencies. The system initializes exchange rates, loads the user's preferred currency from a file, and ensures accurate currency handling in expense tracking.
- **Justification**:  This feature provides flexibility for users dealing with multiple currencies by enabling seamless conversions, thereby enhancing financial management and expense tracking across different monetary systems.

---

### Enhancements Implemented

#### 1. Unit Tests Across the Currency class and mark, unmark, settled-list and unsettled-list
- **Robust Input Validation**: Ensures that all inputs are properly validated, with appropriate error messages returned for invalid entries.
- **Comprehensive Boundary and Edge Case Testing**: Verifies system behavior under extreme or unexpected conditions, such as out-of-range values.
- **Reliable Error Handling**: Confirms that meaningful error messages are displayed when issues arise, enhancing user experience.
- **Accurate Data Persistence**: Ensures that expenses and current currency are correctly saved, retrieved, and maintained within the system.

#### 2. Enhanced balance command
- **Improvement**: The balance calculation now excludes settled amounts, ensuring that only outstanding owed amounts are considered.
- **Impact**: Users gain a clearer and more accurate view of their outstanding balances, making it easier to track unpaid expenses.

---

### Contributions to User Guide

- **Documented Sections:**
- **Documentations**
    - Mark settled expenses: `mark`
    - Unmark settled expenses: `unmark`
    - View settled expenses: `list-settled`
    - View unsettled expenses: `list-unsettled`
    - Change currency: `change-currency`

---

### Contributions to Developer Guide

#### Documented Sections
- **Documentations**
  - Displaying Settled Expenses
  - Displaying Unsettled Expenses
  - Marking Expenses
  - Unmarking Expenses
  - Currency Class
    - Currency Constructor
    - Initialising Exchange Rates
    - Change Currency Method
    - Edit Expense Currency Method
    - Write to File Method

**Relevant Pull Request:** 
- [PR #27](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/27/files)
- [PR #33](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/33/files)
- [PR #59](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/59/files)
---

### Contributions to Team-Based Tasks
- **Code Review**
  - Object Diagram for currency
  - Code reviews for teammates
  - Pasting screenshots for version 1.0 during tutorials
  - Sequence Diagram for change currency
  - Object diagram for change currency

### Review/mentoring contributions:
- **Bug Finding and Suggestions:** [PR review](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/34/files)

---
