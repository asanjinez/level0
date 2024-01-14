package org.solution;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

// For this solution we will not use a database, it is a good alternative but we will limit ourselves to solve all this as natively as possible, at least for level 0.
public class Main {
    private static final int ATEMPS = 3;
    private static final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(2000);

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

        System.out.println("Welcome " + username);
        System.out.println("Choose the operation you wish to perform");
        for(int i = 0; i < Operations.values().length;i++){
            System.out.println(i);
        }



    }
}