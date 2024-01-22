package org.solution.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import org.solution.exceptions.CustomExceptions;
import org.solution.models.Account;
import org.solution.converter.JsonConverter;

import java.math.BigDecimal;
import java.util.*;

import static org.solution.Main.accountsRoute;


import static java.lang.System.in;

public class UserManager implements DataManager<Map.Entry<String, String>>{
    private static final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(2000.0);
    private AccountManager accountManager;

    private String route;
    private JsonConverter<Map<String,String>> jsonConverter;
    private Map<String,String> users;

    public UserManager(String route){
        this.route = route;
        this.accountManager = new AccountManager(accountsRoute);
        this.jsonConverter = new JsonConverter<>(new TypeReference<Map<String,String>>() {});
        this.users = this.load();
    }

    public UserManager(String route,AccountManager accountManager){
        this.route = route;
        this.accountManager = accountManager;
        this.jsonConverter =  new JsonConverter<>(new TypeReference<Map<String,String>>() {});
        this.users = this.load();
    }
    @Override
    public boolean create(Map.Entry<String, String> entry) {
        users.put(entry.getKey(), entry.getValue());
        if (jsonConverter.save(this.route,this.users)) {
            System.out.println("Registered!");
            return true;
        }
        users.remove(entry.getKey());
        return false;
    }

    @Override
    public boolean delete(Map.Entry<String, String> entry) {
        this.users.remove(entry.getKey());
        if (jsonConverter.save(this.route,this.users))
            return true;

        users.put(entry.getKey(), entry.getValue());
        return false;
    }
    public Map<String, String> load() {
        try {
            return jsonConverter.load(this.route).get();
        } catch (CustomExceptions.FileLoadException e) {
            handleException(e);
            return Collections.emptyMap();
        }

    }



    public Optional<String> validUser(String username, String password) {
        if (this.users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Logged in");
            return Optional.of(username);
        }
        System.out.println("Invalid username or password");
        return Optional.empty();
    }

    public Optional<String> validateLoginAttempts(int attemps) {
        for (int i = attemps - 1; i >= 0; i--) {
            System.out.println("Please, enter your username and password");
            System.out.println("Enter Username:");
            String username = new Scanner(System.in).nextLine();

            System.out.println("Enter Password:");
            String password = new Scanner(System.in).nextLine();

            Optional<String> user = this.validUser(username, password);
            if (user.isPresent())
                return user;

            System.out.println(i + " attemps remaining");
            System.out.println("_________________");

        }
        System.out.println("maximum number of attempts, blocked account");
        System.out.println("_________________");
        return Optional.empty();
    }

    public Optional<String> registerNewUser() {
        System.out.println("Please register now");
        System.out.println("Enter Username:");
        String username = new Scanner(in).nextLine();

        if (!this.validUsername(username))
            return Optional.empty();

        System.out.println("Enter Password:");
        String password = new Scanner(in).nextLine();

        boolean registeredUser = this.create(Map.entry(username,password));

        Account newAccount = new Account(username, INITIAL_AMOUNT, "ARS");
        if (registeredUser && accountManager.create(newAccount))
            return Optional.of(username);

        return Optional.empty();
    }

    public boolean validUsername(String username) {
        if (!users.containsKey(username))
            return true;

        System.out.println("This username alredy exists, try to log in");
        System.out.println("_________________");
        return false;
    }

    private void handleException(Exception e){
        System.out.println(e.getMessage());
    }
}
