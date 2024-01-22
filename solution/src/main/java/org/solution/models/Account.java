package org.solution.models;

import org.solution.exceptions.CustomExceptions.*;

import java.math.BigDecimal;

public class Account {
    String username;
    BigDecimal balance;
    String mainCurrency;

    public Account(){
    }
    public Account(String username, BigDecimal balance, String mainCurrency) {
        this.username = username;
        this.balance = balance;
        this.mainCurrency = mainCurrency;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getMainCurrency() {
        return mainCurrency;
    }

    public void setMainCurrency(String mainCurrency) {
        this.mainCurrency = mainCurrency;
    }

    @Override
    public String toString() {
        return String.format("{username=%s, balance=%s, mainCurrency=%s}",
                username, balance, mainCurrency);
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException();
        }
        balance = balance.subtract(amount);
    }

    public void transfer(Account destinationAccount, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException();
        }

        withdraw(amount);
        destinationAccount.deposit(amount);
    }
}
