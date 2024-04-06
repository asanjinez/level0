package org.solution.systems;

import org.solution.enums.HBOptions;
import org.solution.manager.AccountManager;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BankingSystem implements SystemC<AccountManager>{
    AccountManager accountManager;
    public BankingSystem(AccountManager accountManager) {
        this.accountManager = accountManager;
    }
    @Override
    public void start(String username) {
        HBOptions option = null;

        while (option != HBOptions.BACK) {
            System.out.println("---HOME_BANKING---");
            System.out.println("Choose the operation you wish to perform");
            HBOptions.showValues();

            try {
                option = HBOptions.fromIndex(getUserChoice());
                performOperation(username, option);
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Sorry, the option is wrong");
            }

            System.out.println("Press any key to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    @Override
    public int getUserChoice() {
        return new Scanner(System.in).nextInt();
    }

    private void performOperation(String username, HBOptions option) {
        switch (option) {
            case VIEW:
                System.out.println("Balance: " + accountManager.getBalance(username));
                break;
            case DEPOSIT:
                System.out.println("Enter the amount to deposit");
                accountManager.deposit(username, BigDecimal.valueOf(new Scanner(System.in).nextFloat()));
                break;
            case WITHDRAW:
                System.out.println("Enter the amount to withdraw");
                accountManager.withdraw(username, BigDecimal.valueOf(new Scanner(System.in).nextFloat()));
                break;
            case TRANSFER:
                System.out.println("Enter the amount to transfer");
                BigDecimal amount = BigDecimal.valueOf(new Scanner(System.in).nextFloat());

                System.out.println("Enter the user to transfer");
                String toUser = new Scanner(System.in).nextLine();

                accountManager.transfer(username, toUser, amount);
                break;
            case BACK:
                break;
            default:
                System.out.println("?");
                System.exit(1);
        }
    }
}
