//@@author NandhithaShree
package seedu.duke.currency;
import seedu.duke.commands.Commands;
import seedu.duke.expense.BudgetManager;
import seedu.duke.messages.Messages;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents the Currency class which handles currency conversion and exchange rate management.
 */
public class Currency {
    private String currentCurrency;
    private final HashMap<String, Double> exchangeRates = new HashMap<>();
    private Scanner scanner;
    private BudgetManager budgetManager;

    /**
     * Constructor for the Currency class.
     * Initializes exchange rates and attempts to load the current currency from a file.
     *
     * @param scanner The scanner object for reading user input.
     * @param budgetManager The budget manager object to manage the expenses.
     */
    public Currency(Scanner scanner, BudgetManager budgetManager){
        initializeExchangeRates();
        this.scanner = scanner;
        this.budgetManager = budgetManager;

        try{
            File f = new File("./currentCurrency");
            Scanner s = new Scanner(f);
            String line;

            if(s.hasNextLine()){
                line = s.nextLine();
                checkCorruptedFile();
                currentCurrency = line;
            } else {
                currentCurrency = Commands.DEFAULT_CURRENCY;
            }
        } catch (FileNotFoundException e){
            File f = new File("./currentCurrency");
            System.out.println("file not found...");
            System.out.println("A new file to load your current currency will be created for you!");
            try{
                f.createNewFile();
            } catch (IOException g){
                System.out.println(" Error creating currency data file");
            }
            currentCurrency = Commands.DEFAULT_CURRENCY;
        }
    }

    /**
     * Initializes the exchange rates for various currencies.
     * Populates the exchangeRates HashMap with ISO 4217 currency codes and their respective exchange rates.
     */
    public void initializeExchangeRates() {
        exchangeRates.put("AED", 3.6725);
        exchangeRates.put("AFN", 88.9280);
        exchangeRates.put("ALL", 91.2780);
        exchangeRates.put("AMD", 1452.00);
        exchangeRates.put("ANG", 1.7850);
        exchangeRates.put("AOA", 3312.20);
        exchangeRates.put("ARS", 799.27);
        exchangeRates.put("AUD", 1.1876);
        exchangeRates.put("AWG", 1.7850);
        exchangeRates.put("AZN", 1.6669);
        exchangeRates.put("BAM", 1.3664);
        exchangeRates.put("BBD", 1.4878);
        exchangeRates.put("BDT", 80.60);
        exchangeRates.put("BGN", 1.3664);
        exchangeRates.put("BHD", 0.2764);
        exchangeRates.put("BIF", 2125.75);
        exchangeRates.put("BMD", 0.7439);
        exchangeRates.put("BND", 1.0000);
        exchangeRates.put("BOB", 5.1432);
        exchangeRates.put("BRL", 4.2961);
        exchangeRates.put("BSD", 0.7439);
        exchangeRates.put("BTN", 61.95);
        exchangeRates.put("BWP", 10.0894);
        exchangeRates.put("BYN", 2.4277);
        exchangeRates.put("BZD", 1.4976);
        exchangeRates.put("CAD", 1.0717);
        exchangeRates.put("CDF", 2085.39);
        exchangeRates.put("CHF", 0.6595);
        exchangeRates.put("CLP", 694.85);
        exchangeRates.put("CNY", 5.4269);
        exchangeRates.put("COP", 3105.21);
        exchangeRates.put("CRC", 379.80);
        exchangeRates.put("CUC", 0.7439);
        exchangeRates.put("CUP", 19.51);
        exchangeRates.put("CVE", 76.62);
        exchangeRates.put("CZK", 17.2121);
        exchangeRates.put("DJF", 132.18);
        exchangeRates.put("DKK", 5.1457);
        exchangeRates.put("DOP", 42.61);
        exchangeRates.put("DZD", 100.91);
        exchangeRates.put("EGP", 23.00);
        exchangeRates.put("ERN", 11.1589);
        exchangeRates.put("ETB", 42.48);
        exchangeRates.put("EUR", 0.6898);
        exchangeRates.put("FJD", 1.6748);
        exchangeRates.put("FKP", 0.5902);
        exchangeRates.put("GBP", 0.5770);
        exchangeRates.put("GEL", 1.9963);
        exchangeRates.put("GHS", 8.8688);
        exchangeRates.put("GIP", 0.5902);
        exchangeRates.put("GMD", 45.42);
        exchangeRates.put("GNF", 6378.18);
        exchangeRates.put("GTQ", 5.8081);
        exchangeRates.put("GYD", 155.14);
        exchangeRates.put("HKD", 5.8143);
        exchangeRates.put("HNL", 18.3377);
        exchangeRates.put("HRK", 5.2019);
        exchangeRates.put("HTG", 98.4285);
        exchangeRates.put("HUF", 274.84);
        exchangeRates.put("IDR", 12394.49);
        exchangeRates.put("ILS", 2.7682);
        exchangeRates.put("INR", 64.04);
        exchangeRates.put("IQD", 971.06);
        exchangeRates.put("IRR", 31468.52);
        exchangeRates.put("ISK", 99.6784);
        exchangeRates.put("JMD", 114.74);
        exchangeRates.put("JOD", 0.5269);
        exchangeRates.put("JPY", 111.92);
        exchangeRates.put("KES", 97.4641);
        exchangeRates.put("KGS", 65.5786);
        exchangeRates.put("KHR", 3037.19);
        exchangeRates.put("KMF", 340.23);
        exchangeRates.put("KPW", 669.53);
        exchangeRates.put("KRW", 1096.97);
        exchangeRates.put("KWD", 0.2305);
        exchangeRates.put("KYD", 0.6195);
        exchangeRates.put("KZT", 376.69);
        exchangeRates.put("LAK", 15637.20);
        exchangeRates.put("LBP", 6706.74);
        exchangeRates.put("LKR", 226.54);
        exchangeRates.put("LRD", 132.18);
        exchangeRates.put("LSL", 13.9184);
        exchangeRates.put("LYD", 3.6063);
        exchangeRates.put("MAD", 7.4208);
        exchangeRates.put("MDL", 13.1258);
        exchangeRates.put("MGA", 3317.07);
        exchangeRates.put("MKD", 42.4578);
        exchangeRates.put("MMK", 1554.68);
        exchangeRates.put("MNT", 2577.10);
        exchangeRates.put("MOP", 6.0029);
        exchangeRates.put("MRU", 26.3312);
        exchangeRates.put("MUR", 33.7423);
        exchangeRates.put("MVR", 11.4552);
        exchangeRates.put("MWK", 1286.04);
        exchangeRates.put("MXN", 12.6397);
        exchangeRates.put("MYR", 3.4976);
        exchangeRates.put("MZN", 47.7940);
        exchangeRates.put("NAD", 13.9184);
        exchangeRates.put("NGN", 679.80);
        exchangeRates.put("NIO", 27.2882);
        exchangeRates.put("NOK", 7.9179);
        exchangeRates.put("NPR", 118.97);
        exchangeRates.put("NZD", 1.2053);
        exchangeRates.put("OMR", 0.2868);
        exchangeRates.put("PAB", 0.7439);
        exchangeRates.put("PEN", 2.7682);
        exchangeRates.put("PGK", 2.6105);
        exchangeRates.put("PHP", 42.0232);
        exchangeRates.put("PKR", 207.70);
        exchangeRates.put("PLN", 3.0136);
        exchangeRates.put("PYG", 5437.91);
        exchangeRates.put("QAR", 2.7118);
        exchangeRates.put("RON", 3.4486);
        exchangeRates.put("RSD", 81.0456);
        exchangeRates.put("RUB", 62.83);
        exchangeRates.put("RWF", 830.65);
        exchangeRates.put("SAR", 2.7916);
        exchangeRates.put("SBD", 6.2216);
        exchangeRates.put("SCR", 10.1589);
        exchangeRates.put("SDG", 446.40);
        exchangeRates.put("SEK", 7.6444);
        exchangeRates.put("SHP", 0.5902);
        exchangeRates.put("SLL", 15155.10);
        exchangeRates.put("SOS", 427.64);
        exchangeRates.put("SRD", 27.9155);
        exchangeRates.put("STN", 16.2791);
        exchangeRates.put("SYP", 2222.10);
        exchangeRates.put("SZL", 13.9184);
        exchangeRates.put("THB", 26.28);
        exchangeRates.put("TJS", 8.4435);
        exchangeRates.put("TMT", 2.6573);
        exchangeRates.put("TND", 3.2754);
        exchangeRates.put("TOP", 1.6991);
        exchangeRates.put("TRY", 21.6195);
        exchangeRates.put("TTD", 5.0426);
        exchangeRates.put("TWD", 22.9719);
        exchangeRates.put("TZS", 1745.46);
        exchangeRates.put("UAH", 27.4268);
        exchangeRates.put("UGX", 3706.87);
        exchangeRates.put("UYU", 34.8603);
        exchangeRates.put("UZS", 8707.80);
        exchangeRates.put("VND", 17696.30);
        exchangeRates.put("VUV", 84.18);
        exchangeRates.put("WST", 1.9904);
        exchangeRates.put("XAF", 461.43);
        exchangeRates.put("XOF", 461.43);
        exchangeRates.put("XPF", 88.18);
        exchangeRates.put("YER", 186.98);
        exchangeRates.put("ZAR", 13.9184);
        exchangeRates.put("ZMK", 8570.10);
        exchangeRates.put("ZWD", 633.36);
        exchangeRates.put("SGD", 1.0000);
        exchangeRates.put("USD", 0.7439);
    }

    /**
     * Returns the current currency.
     *
     * @return The current currency ISO code.
     */
    public String getCurrentCurrency() {
        try{
            checkCorruptedFile();
        } catch (FileNotFoundException e){
            File f = new File("./currentCurrency");
            System.out.println("file not found...");
            System.out.println("A new file to load your current currency will be created for you!");
            try{
                f.createNewFile();
            } catch (IOException g){
                System.out.println(" Error creating currency data file");
            }
        }
        return currentCurrency;
    }

    /**
     * Checks the file "./currentCurrency" for the stored currency.
     * If the stored currency is invalid (i.e., not found in the exchangeRates map),
     * the file contents are cleared, and the currency is reverted to "SGD" (Singapore Dollar).
     *
     * @throws FileNotFoundException If the file "./currentCurrency" does not exist or cannot be opened for reading.
     */
    public void checkCorruptedFile() throws FileNotFoundException {
        File f = new File("./currentCurrency");
        Scanner s = new Scanner(f);
        String line;

        if(s.hasNextLine()) {
            line = s.nextLine();
            currentCurrency = line;
        }

        // Check if currentCurrency is not found in the exchangeRates
        if (exchangeRates.get(currentCurrency) == null) {
            // Clear the file contents
            try (FileWriter writer = new FileWriter(f)) {
                // The file is truncated automatically when opened in write mode
                System.out.println("Current currency reverted to SGD because file is tampered with");
                currentCurrency = "SGD";
                writer.write(currentCurrency);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the exchange rate for a specified currency code.
     *
     * @param currencyCode The ISO 4217 currency code.
     * @return The exchange rate for the specified currency, or null if the currency code is not found.
     */
    public Double getExchangeRate(String currencyCode) {
        if (exchangeRates.containsKey(currencyCode)) {
            return exchangeRates.get(currencyCode);

        } else {
            System.out.println("Currency code " + currencyCode + " not found.");
            return null;
        }
    }

    /**
     * Changes the currency based on the user's input command.
     * <p>
     * This method parses the command, validates the input, and delegates
     * to either {@code handleCustomExchangeRate} or {@code handleEstimatedExchangeRate}
     * based on the specified method.
     * </p>
     *
     * @param command the user input string representing the currency change command.
     * @throws NumberFormatException if the method part of the command is not a valid number.
     */
    public void changeCurrency(String command) {
        try {
            String [] splitInput = command.trim().split("\\s*/\\s*");
            String method = splitInput[1].trim();
            String newCurrency = splitInput[2].toUpperCase().trim();
            int intMethod = Integer.parseInt(method);

            if(intMethod == 1 && splitInput.length != 4){
                System.out.println("Invalid format. Usage: change-currency/1/<currency to change to>/<exchange rate>");
                return;
            } else if(intMethod == 2 && splitInput.length != 3){
                System.out.println("Invalid format. Usage: change-currency/2/<currency to change to>");
                return;
            }

            if (intMethod != 1 && intMethod != 2) {
                System.out.println("Please input either '1' or '2'");
            } else if (intMethod == 1) {
                String exchangeRate = splitInput[3];
                handleCustomExchangeRate(newCurrency, exchangeRate);
            } else {
                handleEstimatedExchangeRate(newCurrency);
            }

        } catch (NumberFormatException e) {
            System.out.println("Please input a valid number");
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Invalid format. Usage: change-currency/1/<currency to change to>/<exchange rate>\nOR "
                    + "change-currency/2/<currency to change to>");
        }
    }

    /**
     * Handles currency conversion using a custom exchange rate provided by the user.
     *
     * @param newCurrency  the target currency to convert expenses to
     * @param exchangeRate the custom exchange rate as a string
     */
    private void handleCustomExchangeRate(String newCurrency, String exchangeRate) {
        getCurrentCurrency();
        if (!exchangeRates.containsKey(newCurrency)) {
            System.out.println("Please provide a valid currency...");
            return;
        }
        try {
            double finalExchangeRate = Double.parseDouble(exchangeRate);

            if(finalExchangeRate <= 0 || finalExchangeRate >= 50000){
                System.out.println("You must give a positive exchange rate and an exchange rate less than 50000");
                return;
            }
            editExpenseCurrency(finalExchangeRate, newCurrency);
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid exchange rate");
        }
    }

    /**
     * Handles currency conversion using an estimated exchange rate.
     *
     * @param newCurrency the target currency to convert expenses to
     */
    private void handleEstimatedExchangeRate(String newCurrency) {
        getCurrentCurrency();
        Double exchangeRate = getExchangeRate(newCurrency);

        if (exchangeRate == null) {
            return;
        }

        double finalExchangeRate = exchangeRate / getExchangeRate(currentCurrency);
        editExpenseCurrency(finalExchangeRate, newCurrency);
    }

    /**
     * Updates the budget manager with the new currency exchange rate and changes the current currency.
     *
     * @param finalExchangeRate The exchange rate to be applied to the expenses.
     * @param newCurrency The new currency ISO code.
     */
    public void editExpenseCurrency(Double finalExchangeRate, String newCurrency){
        budgetManager.editExpenseCurrency(finalExchangeRate);
        currentCurrency = newCurrency;

        try{
            writeToFile(newCurrency);
        } catch (IOException e){
            System.out.println("error recording the change of currency");
            return;
        }

        System.out.println("Currency successfully changed to " + currentCurrency);
    }

    /**
     * Writes the new currency ISO code to a file to persist the change.
     *
     * @param newCurrency The new currency to be written to the file.
     * @throws IOException If an error occurs while writing to the file.
     */
    public void writeToFile(String newCurrency) throws IOException {
        FileWriter fw = new FileWriter("./currentCurrency", false);
        fw.write(newCurrency);
        fw.close();
    }
}
//@@author
