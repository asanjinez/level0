//Route: E:\Ideas\programming-skills\level0\solution
package org.solution;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

import static org.solution.Operations.*;

// For this solution we will not use a database, it is a good alternative, but we will limit ourselves to solve all this as natively as possible, at least for level 0.
public class Main {
    private static final int ATTEMPTS = 3;
    private static final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(2000.0);

    public static void main(String[] args) {
        Authenticator authenticator = new Authenticator();
        Account account = new Account();

        Optional<String> username = authenticateUser(authenticator, account);

        username.ifPresent(s -> processUserOperations(s, account));

    }
    private static Optional<String> authenticateUser(Authenticator authenticator, Account account) {
        Optional<String> username = Optional.empty();

        do {
            System.out.println("Do you have an existing account?");
            System.out.println("1) Yes");
            System.out.println("2) No");
            System.out.println("_________________");

            switch (new Scanner(System.in).nextInt()) {
                case 1:
                    username = authenticator.validateLoginAttempts(ATTEMPTS);
                    break;
                case 2:
                    username = registerNewUser(authenticator, account);
                    break;
                default:
                    System.out.println("Sorry, the option is wrong");
            }
        } while (username.isEmpty());

        return username;
    }

    private static Optional<String> registerNewUser(Authenticator authenticator, Account account) {
        System.out.println("Please register now");
        System.out.println("Enter Username:");
        String usernameCred = new Scanner(System.in).nextLine();

        if (!authenticator.validUsername(usernameCred))
            return Optional.empty();

        System.out.println("Enter Password:");
        String passwordCred = new Scanner(System.in).nextLine();

        Optional<String> registeredUser = authenticator.registerUser(usernameCred, passwordCred);
        if (registeredUser.isPresent() && account.createAccount(usernameCred, INITIAL_AMOUNT))
            return registeredUser;

        return Optional.empty();
    }

    private static void processUserOperations(String username, Account account) {
        Operations option = null;

        do {
            if (option != null) {
                System.out.println("Press a key to continue...");
                new Scanner(System.in).nextLine();
            }

            System.out.println("Welcome " + username);
            System.out.println("Choose the operation you wish to perform");
            Operations[] operations = values();
            for (int i = 0; i < operations.length; i++) {
                System.out.println(i + ") " + operations[i]);
            }
            System.out.println("_________________");

            try {
                option = Operations.fromIndex(new Scanner(System.in).nextInt());
                performOperation(username, account, option);
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Sorry, the option is wrong");
            }
        } while (option != Operations.EXIT);
    }

    private static void performOperation(String username, Account account, Operations option) {
        switch (option) {
            case VIEW:
                System.out.println("Balance: " + account.getBalance(username));
                break;
            case DEPOSIT:
                System.out.println("Enter the amount to deposit");
                account.deposit(username, BigDecimal.valueOf(new Scanner(System.in).nextFloat()));
                break;
            case WITHDRAW:
                System.out.println("Enter the amount to withdraw");
                account.withdraw(username, BigDecimal.valueOf(new Scanner(System.in).nextFloat()));
                break;
            case TRANSFER:
                System.out.println("Enter the amount to transfer");
                BigDecimal amount = BigDecimal.valueOf(new Scanner(System.in).nextFloat());

                System.out.println("Enter the user to transfer");
                String toUser = new Scanner(System.in).nextLine();

                account.transfer(username, toUser, amount);
                break;
            case EXIT:
                break;
            default:
                System.out.println("?");
                System.exit(1);
        }
    }
}