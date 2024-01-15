package org.solution;

import java.awt.image.PackedColorModel;
import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

import static org.solution.Operations.*;

// For this solution we will not use a database, it is a good alternative but we will limit ourselves to solve all this as natively as possible, at least for level 0.
public class Main {
    private static final int ATEMPS = 3;
    private static final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(2000.0);

    public static void main(String[] args) {
        Authenticator authenticator = new Authenticator();
        Account account = new Account();

        //      Route: E:\Ideas\programming-skills\level0\solution
        boolean logged = false;
        Optional<String> username = Optional.empty();

        do {
            System.out.println("Do you have an existing account?");
            System.out.println("1) Yes");
            System.out.println("2) No");
            System.out.println("_________________");

            switch (new Scanner(System.in).nextInt()) {
                case 1:
                    username = authenticator.validateLoginAttemps(ATEMPS);
                    if (username.isPresent())
                        break;
                    break;

                case 2:
                    System.out.println("Please register now");
                    System.out.println("Enter Username:");
                    String usernameCred = new Scanner(System.in).nextLine();

                    if (!authenticator.validUsername(usernameCred))
                        continue;

                    System.out.println("Enter Password:");
                    String passwordCred = new Scanner(System.in).nextLine();

                    username = authenticator.registerUser(usernameCred,passwordCred);
                    if (username.isPresent() && account.createAccount(usernameCred,INITIAL_AMOUNT))
                        break;
                    break;
                default:
                    System.out.println("Sorry, the option is wrong");
            }
        }
        while (!username.isPresent());
        Operations option = null;
        do {
            if (option != null) {
                System.out.println("press a key to continue...");
                new Scanner(System.in).nextLine();
            }


            System.out.println("Welcome " + "username");
            System.out.println("Choose the operation you wish to perform");
            Operations[] operations = values();
            for (int i = 0; i < operations.length; i++) {
                System.out.println(i + ") " + operations[i]);
            }
            System.out.println("_________________");


            try {
                option = Operations.fromIndex(new Scanner(System.in).nextInt());

                switch (option) {
                    case VIEW:
                        System.out.println("Balance: " + account.getBalance(username.get()));
                        break;
                    case DEPOSIT:
                        System.out.println("Enter the amount to deposit");
                        account.deposit(username.get(), BigDecimal.valueOf(new Scanner(System.in).nextFloat()));
                        break;
                    case WITHDRAW:
                        System.out.println("Enter the amount to withdraw");
                        account.withdraw(username.get(), BigDecimal.valueOf(new Scanner(System.in).nextFloat()));
                        break;
                    case TRANSFER:
                        System.out.println("Enter the mount to transfer");
                        BigDecimal amount = BigDecimal.valueOf(new Scanner(System.in).nextFloat());

                        System.out.println("Enter the user to transfer");
                        String toUser = new Scanner(System.in).nextLine();

                        account.transfer(username.get(),toUser,amount);

                        break;
                    case EXIT:
                        break;
                    default:
                        System.out.println("?");
                        System.exit(1);
                }
            } catch (InputMismatchException| IllegalArgumentException e) {
                System.out.println("Sorry, the option is wrong");
                System.out.println("_________________");
            }
        } while(option != EXIT);
    }
}