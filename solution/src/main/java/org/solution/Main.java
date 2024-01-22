//Route: E:\Ideas\programming-skills\level0\solution
package org.solution;

import com.fasterxml.jackson.core.type.TypeReference;
import org.solution.converter.JsonConverter;
import org.solution.manager.AccountManager;
import org.solution.manager.UserManager;
import org.solution.models.Account;

import java.math.BigDecimal;
import java.util.*;

import static java.lang.System.in;

// For this solution we will not use a database, it is a good alternative, but we will limit ourselves to solve all this as natively as possible, at least for level 0.
public class Main {
    private static final int ATTEMPTS = 3;
    public static final String accountsRoute = loadConfig("accounts");
    private static final String usersRoute = loadConfig("users");
    private static AccountManager accountManager;
    private static UserManager userManager;

    public static void main(String[] args) {
        accountManager = new AccountManager(accountsRoute);
        userManager = new UserManager(usersRoute,accountManager);

        Optional<String> username = authenticateUser();

        username.ifPresent(userLogged -> processSystemOption(userLogged));
    }
    private static Optional<String> authenticateUser() {
        Optional<String> username = Optional.empty();

        do {
            System.out.println("Do you have an existing account?");
            System.out.println("1) Yes");
            System.out.println("2) No");
            System.out.println("_________________");

            switch (getUserChoice()) {
                case 1:
                    username = userManager.validateLoginAttempts(ATTEMPTS);
                    break;
                case 2:
                    username = userManager.registerNewUser();
                    break;
                default:
                    System.out.println("Sorry, the option is wrong");
            }
        } while (username.isEmpty());

        return username;
    }

    private static void processSystemOption(String username) {
        SysOptions option = null;

        while (option != SysOptions.EXIT) {
            System.out.println("Welcome " + username);
            System.out.println("_________________");
            System.out.println("Please choose the system you will use");

            showOptions(SysOptions.class);
            option = performSysOption(username);
        }
    }
    private static void startHomeBanking(String username) {
        HBOptions option = null;
        System.out.println("---HOME_BANKING---");
        do {
            System.out.println("Choose the operation you wish to perform");
            showOptions(HBOptions.class);

            try {
                option = HBOptions.fromIndex(getUserChoice());
                performOperation(username, option);
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Sorry, the option is wrong");
            }

            System.out.println("Press any key to continue...");
            new Scanner(in).nextLine();

        } while (option != HBOptions.BACK);
    }

    private static void startConverter(String username) {
        Account account = accountManager.findByUsername(username);
        System.out.println("---Currency-Converter---");

        CCOptions option;
        do {
            System.out.println("Total balance: " + account.getBalance() + account.getMainCurrency());
            System.out.println("Select the currency you want to convert to: ");
            showOptions(CCOptions.class);

            try {
                option = CCOptions.fromIndex(getUserChoice());
                if (option == CCOptions.BACK) {
                    break;
                }

                System.out.println("Select the amount to convert:");
                BigDecimal amount = new BigDecimal(new Scanner(in).nextLine());
                BigDecimal convertedAmount = convertAmount(amount, account.getMainCurrency(), option.toString());

                System.out.println("The amount converted would be: " + convertedAmount);

                System.out.println("If you wish to withdraw that amount, \nremember that there will be a 1% surcharge.");
                System.out.println("1) Yes");
                System.out.println("2) No");
                System.out.println("_________________");

                if (getUserChoice() == 1) {
                    BigDecimal surcharge = amount.divide(BigDecimal.valueOf(100));
                    accountManager.withdraw(account.getUsername(), amount.add(surcharge));
                }

            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Sorry, the option is wrong");
            }

            System.out.println("Press any key to continue...");
            new Scanner(in).nextLine();

        } while (true);
    }
    private static void performOperation(String username, HBOptions option) {
        switch (option) {
            case VIEW:
                System.out.println("Balance: " + accountManager.getBalance(username));
                break;
            case DEPOSIT:
                System.out.println("Enter the amount to deposit");
                accountManager.deposit(username, BigDecimal.valueOf(new Scanner(in).nextFloat()));
                break;
            case WITHDRAW:
                System.out.println("Enter the amount to withdraw");
                accountManager.withdraw(username, BigDecimal.valueOf(new Scanner(in).nextFloat()));
                break;
            case TRANSFER:
                System.out.println("Enter the amount to transfer");
                BigDecimal amount = BigDecimal.valueOf(new Scanner(in).nextFloat());

                System.out.println("Enter the user to transfer");
                String toUser = new Scanner(in).nextLine();

                accountManager.transfer(username, toUser, amount);
                break;
            case BACK:
                break;
            default:
                System.out.println("?");
                System.exit(1);
        }
    }

    private static SysOptions performSysOption(String username){
        try {
            SysOptions option = SysOptions.fromIndex(getUserChoice());
            switch (option){
                case HOME_BANKING -> startHomeBanking(username);
                case CURRENCY_CONVERTER -> startConverter(username);
                case EXIT -> System.out.println("Thanks for using our systems, Goodbye!");
                default -> System.out.println("Option not developed");
            }
            return option;

        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.println("Sorry, the option is wrong");
            return null;
        }

    }
    private static String loadConfig(String key) {
        return new JsonConverter<>(new TypeReference<Map<String,String>>() {}).load("src\\main\\resources\\config.json").get().get(key);
    }

    private static int getUserChoice() {
        return new Scanner(System.in).nextInt();

    }
    public static <T extends Enum<T>> void showOptions(Class<T> enumClass) {
        T[] options = enumClass.getEnumConstants();
        if (options != null) {
            for (int i = 0; i < options.length; i++) {
                System.out.println(i + ") " + options[i]);
            }
            System.out.println("_________________");
        } else {
            System.out.println("Not an enum class");
        }
    }

    public static BigDecimal convertAmount(BigDecimal amount, String fromCurrency, String toCurrency){
        CCOptions ccOptions = CCOptions.fromString(fromCurrency);
        BigDecimal exchangeRateFromUSD = ccOptions.getExchangeRateFromUSD();
        BigDecimal amountInUSD = amount.divide(exchangeRateFromUSD);
        return amountInUSD.multiply(CCOptions.fromString(toCurrency).getExchangeRateFromUSD());
    }


}