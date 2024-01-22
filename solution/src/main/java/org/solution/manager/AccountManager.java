package org.solution.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import org.solution.models.Account;
import org.solution.converter.JsonConverter;
import org.solution.exceptions.CustomExceptions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountManager implements DataManager<Account> {
    private final String route;
    private final JsonConverter<List<Account>> jsonConverter;
    private final List<Account> accountList;

    public AccountManager(String route){
        this.route = route;
        this.jsonConverter = new JsonConverter<>(new TypeReference<List<Account>>() {});
        this.accountList = this.load();
    }

    @Override
    public boolean create(Account account) {
        this.accountList.add(account);
        if (jsonConverter.save(this.route, this.accountList))
            return true;

        accountList.remove(account);
        return false;
    }
    @Override
    public boolean delete(Account account) {
        this.accountList.remove(account);
        if (jsonConverter.save(this.route, this.accountList))
            return true;

        accountList.add(account);
        return false;
    }
    public List<Account> load() {
        try {
            return jsonConverter.load(this.route).get();
        } catch (FileLoadException e) {
            handleException(e);
            return new ArrayList<>();
        }
    }

    public BigDecimal getBalance(String username) {
        try {
            return findByUsername(username).getBalance();
        } catch (UserNotFoundException e) {
            handleException(e);
            transactionFails();
            return BigDecimal.ZERO;
        }
    }

    public boolean deposit(String username, BigDecimal amount) {
        try {
            this.findByUsername(username).deposit(amount);
            this.jsonConverter.save(this.route,this.accountList);
            transactionWorks();
            return true;

        } catch (UserNotFoundException | IllegalArgumentException | FileSaveException e){
            handleException(e);
            transactionFails();
            return false;
        }
    }

    public boolean withdraw(String username ,BigDecimal amount){
        try {
            this.findByUsername(username).withdraw(amount);
            this.jsonConverter.save(this.route,this.accountList);
            transactionWorks();
            return true;
        } catch (IllegalArgumentException | InsufficientFundsException | FileSaveException e){
            handleException(e);
            transactionFails();
            return false;
        }
    }
    public boolean transfer(String fromUser, String toUser, BigDecimal amount) {
        try{
            Account sourceAccount = this.findByUsername(fromUser);
            Account destinationAccount = this.findByUsername(toUser);

            sourceAccount.transfer(destinationAccount,amount);
            transactionWorks();
            return true;

        } catch (IllegalArgumentException | UserNotFoundException | InsufficientFundsException userNotFoundException){
            handleException(userNotFoundException);
            transactionFails();
            return false;
        }
    }

    public Account findByUsername(String username){
            return accountList.stream()
                    .filter(account -> account.getUsername().equals(username))
                    .findFirst()
                    .orElseThrow(() -> new UserNotFoundException(username));
        }

    private void handleException(Exception e){
        System.out.println(e.getMessage());
    }
    private void transactionFails(){
        System.out.println("Transaction failed. Reverting changes.");
        System.out.println("_________________");
    }
    private void transactionWorks(){
        System.out.println("Transaction done!.");
        System.out.println("_________________");
    }
}