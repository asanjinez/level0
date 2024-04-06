//Route: E:\Ideas\programming-skills\level0\solution
package org.solution;

import com.fasterxml.jackson.core.type.TypeReference;
import org.solution.dataConverter.DataJsonConverter;
import org.solution.manager.UniversityManager;
import org.solution.systems.CurrencySystem;
import org.solution.enums.SysOptions;
import org.solution.systems.BankingSystem;
import org.solution.manager.AccountManager;
import org.solution.manager.UserManager;
import org.solution.systems.UniversitySystem;

import java.util.*;

// For this solution we will not use a database, it is a good alternative, but we will limit ourselves to solve all this as natively as possible, at least for level 0.
public class Main {
    private static final int ATTEMPTS = 3;
    public static final String accountsRoute = loadConfig("accounts");
    private static final String usersRoute = loadConfig("users");
    private static final String universityRoute = loadConfig("university");
    private static AccountManager accountManager;
    private static UserManager userManager;
    private static UniversityManager universityManager;

    public static void main(String[] args) {
        accountManager = new AccountManager(accountsRoute);
        userManager = new UserManager(usersRoute,accountManager);
        universityManager = new UniversityManager(universityRoute,accountManager);


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

            SysOptions.showValues();
            option = performSysOption(username);
        }
    }

    private static SysOptions performSysOption(String username){
        try {
            SysOptions option = SysOptions.fromIndex(getUserChoice());
            switch (option){
                case HOME_BANKING -> new BankingSystem(accountManager).start(username);
                case CURRENCY_CONVERTER -> new CurrencySystem(accountManager).start(username);
                case UNVERSITY -> new UniversitySystem(universityManager).start(username);
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
        return new DataJsonConverter<>(new TypeReference<Map<String,String>>() {}).load("src\\main\\resources\\config.json").get().get(key);
    }

    private static int getUserChoice() {
        return new Scanner(System.in).nextInt();

    }

}