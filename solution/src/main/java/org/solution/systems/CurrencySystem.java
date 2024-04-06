package org.solution.systems;

import org.solution.enums.CCOptions;
import org.solution.manager.AccountManager;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CurrencySystem implements SystemC<AccountManager> {
    AccountManager accountManager;
    public CurrencySystem(AccountManager accountManager) {
        this.accountManager = accountManager;
    }
    @Override
    public void start(String username) {
        CCOptions option = null;

        while (option != CCOptions.BACK) {
            System.out.println("---Currency-Converter---");
            System.out.println("Total balance: " + accountManager.findByUsername(username).getBalance());
            System.out.println("Select the currency you want to convert to: ");
            CCOptions.showValues();

            try {
                option = CCOptions.fromIndex(getUserChoice());
                if (option == CCOptions.BACK)
                    continue;

                System.out.println("Select the amount to convert:");
                BigDecimal amount = new BigDecimal(new Scanner(System.in).nextLine());
                System.out.println("The amount converted would be: " + convertAmount(amount, accountManager.findByUsername(username).getMainCurrency(), option.toString()) + option.toString());

                System.out.println("If you wish to withdraw that amount, \nremember that there will be a 1% surcharge.");
                System.out.println("1) Yes");
                System.out.println("2) No");
                System.out.println("_________________");
                if (1 == getUserChoice())
                    accountManager.withdraw(username, amount.add(amount.divide(BigDecimal.valueOf(100))));

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

    private BigDecimal convertAmount(BigDecimal amount, String fromCurrency, String toCurrency) {
        CCOptions ccOptions = CCOptions.fromString(fromCurrency);
        BigDecimal exchangeRateFromUSD = ccOptions.getExchangeRateFromUSD();
        BigDecimal amountInUSD = amount.divide(exchangeRateFromUSD);
        return amountInUSD.multiply(CCOptions.fromString(toCurrency).getExchangeRateFromUSD());
    }
}
