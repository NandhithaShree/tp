# Mohammed Hamdhan - Project Portfolio Page

## Overview

---

O$P$ is an expense-tracker application that allows users to keep track of their expenditure with ease! Users can also manage expenses in a group setting with simple splitting functions, as well as view analytics of their spending across multiple categories.

## Summary of Contributions

---

### Code Contributed

Code Contribution to the project: [Reposense Page](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21&tabOpen=true&tabType=authorship&tabAuthor=mohammedhamdhan&tabRepo=AY2425S2-CS2113-F11-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Features Implemented

#### 1. Core Expense Management Features

- **What It Does**: Implements fundamental expense management operations with robust error handling and data validation:

  1. Add Expenses
     - Create new expenses with detailed information
     - Validate all input fields before creation
     - Automatic date format verification
  2. List Expenses
     - View all expenses in chronological order
     - Display comprehensive expense details
     - Show currency and formatting
  3. Delete Expenses
     - Remove expenses with confirmation system
     - Prevent accidental deletions
     - Update all related data automatically

- **Justification**:

  - Forms the core functionality of the expense tracker
  - Ensures data integrity through validation
  - Provides intuitive user interaction
  - Prevents user errors through confirmation steps
  - Maintains data consistency across operations

- **Highlights**:
  - Add new expenses with:
    - Title and description fields
    - Date validation (DD-MM-YYYY format)
    - Amount validation (no negative values)
    - Automatic currency formatting
  - List functionality includes:
    - Chronological ordering of expenses
    - Clear formatting for readability
    - Currency display for amounts
    - Complete expense details in each entry
  - Delete operation features:
    - Index-based deletion system
    - Confirmation prompt for safety
    - Automatic data persistence
    - Updates to related data structures
  - Comprehensive error handling:
    - Input validation for all fields
    - Clear error messages
    - Recovery options for invalid inputs
    - Graceful error recovery
  - Data persistence:
    - Automatic saving after each operation
    - Consistent data state maintenance
    - Safe data handling procedures

#### 2. Expense Summary and Analytics Feature

- **What It Does**: Provides comprehensive expense summaries through an interactive menu system with multiple viewing options:
  1. Monthly Summary View
     - Groups expenses by month
     - Shows total spending per month
     - Lists all expenses within each month with details
  2. Category-wise Summary View
     - Classifies expenses into predefined categories
     - Shows spending distribution across categories
     - Provides percentage breakdown of expenses
- **Justification**:
  - Helps users track spending patterns over time through monthly summaries
  - Enables users to understand their spending habits by category
  - Supports better financial decision-making through organized data presentation
- **Highlights**:
  - Interactive menu system for choosing summary views
  - Monthly summary showing expenses grouped by month with totals
  - Category-wise summary with detailed breakdowns
  - Pie chart visualization for category distribution
  - Export functionality for both summary types
  - Flexible viewing options for different analysis needs

#### 3. Data Export Feature

- **What It Does**: Allows users to export their expense summaries to text files for record-keeping and analysis.
- **Justification**: Users can maintain permanent records of their expense summaries and share them with others if needed.
- **Highlights**:
  - Export monthly summaries to monthly_summary.txt
  - Export category summaries to category_summary.txt
  - Formatted output for easy reading

#### 4. Expense Visualization

- **What It Does**: Implements an interactive pie chart visualization for expense categories using the XChart library.
- **Justification**: Visual representation helps users quickly understand their spending distribution across categories.
- **Highlights**:
  - Interactive pie chart with percentage labels
  - Custom legend with formatted amounts
  - Clean UI with instructions for users

---

### Enhancements Implemented

#### Comprehensive Unit Tests

- **Input Validation Tests**:

  - Implemented tests for empty fields in add expense operations 
  - Developed comprehensive date validation tests for various invalid formats 
  - Created amount validation tests for negative, zero, and invalid format amounts 
  - Wrote tests for edit expense validation including handling empty expense IDs 

- **Summary Generation Tests**:

  - Implemented tests for monthly summary generation with multiple months 
  - Created tests for category summary generation showing correct totals (

- **Export Functionality Tests**:
  - Developed tests for exporting monthly summaries verifying file creation and content 
  - Implemented tests validating the export file's formatting and accuracy

### Contributions to Documentation

#### User Guide

- **Documented Sections**:
  - `Basic Expense Management Commands`
  - `Adding, Editing and Deleting Expenses`
  - `Expense Summary Features`
  - `Data Export Commands`
  - `Visualization Options`
  - `Category Analysis`

#### Developer Guide

- **Documented Sections**:
  - `ExpenseCommand Class`
  - `Core CRUD Operations Architecture`
  - `Input Validation Framework`
  - `Summary Generation Architecture`
  - `Data Export Implementation`
  - `Visualization Components`

### Contributions to Team-Based Tasks

- **Code Review**:
  - Reviewed pull requests for expense-related features
  - Provided feedback on code quality and test coverage
- **Bug Fixes**:
  - Fixed issues in expense addition, deletion, split and summary logic
  - Improved error handling in summary generation
  - Enhanced input validation for expense operations
  - Implemented robust error handling for CRUD operations
- **Feature Integration**:
  - Integrated visualization library
  - Coordinated with team for consistent UI/UX
  - Designed core expense management workflow

---

### Notable Pull Requests

- [PR #20](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/20): Implemented add, delete, list features along with data save functionality
- [PR #67](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/67): Added data vizualisation functionality
- [PR #60](https://github.com/AY2425S2-CS2113-F11-2/tp/pull/60): Added summary functionality. Merge conflicts not present at first, but resolved in PR #63 after merging another branch
