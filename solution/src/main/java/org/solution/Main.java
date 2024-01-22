//Route: E:\Ideas\programming-skills\level0\solution
package org.solution;

import org.solution.manager.AccountManager;
import org.solution.manager.UserManager;

import java.math.BigDecimal;
import java.util.*;

import static java.lang.System.in;
import static org.solution.Operations.*;

// For this solution we will not use a database, it is a good alternative, but we will limit ourselves to solve all this as natively as possible, at least for level 0.
public class Main {
    private static final int ATTEMPTS = 3;
    public static final String accountsRoute = "src\\main\\resources\\accounts\\accounts.json";
    private static final String usersRoute = "src\\main\\resources\\users\\users.json";
    private static AccountManager accountManager;
    private static UserManager userManager;

    public static void main(String[] args) {
        accountManager = new AccountManager(accountsRoute);
        userManager = new UserManager(usersRoute,accountManager);

        Optional<String> username = authenticateUser();

        username.ifPresent(userLogged -> processUserOperations(userLogged));
    }
    private static Optional<String> authenticateUser() {
        Optional<String> username = Optional.empty();

        do {
            System.out.println("Do you have an existing account?");
            System.out.println("1) Yes");
            System.out.println("2) No");
            System.out.println("_________________");

            switch (new Scanner(in).nextInt()) {
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

    private static void processUserOperations(String username) {
        Operations option = null;

        while (option != Operations.EXIT) {
            if (option != null) {
                System.out.println("Press any key to continue...");
                new Scanner(in).nextLine();
            }

            System.out.println("Welcome " + username);
            System.out.println("_________________");
            System.out.println("Choose the operation you wish to perform");
            Operations[] operations = values();
            for (int i = 0; i < operations.length; i++) {
                System.out.println(i + ") " + operations[i]);
            }
            System.out.println("_________________");

            try {
                option = Operations.fromIndex(new Scanner(in).nextInt());
                performOperation(username, option);
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Sorry, the option is wrong");
            }
        }
    }

    private static void performOperation(String username, Operations option) {
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
            case EXIT:
                break;
            default:
                System.out.println("?");
                System.exit(1);
        }
    }
}