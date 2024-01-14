package org.solution;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Authenticator {
    private Map<String, String> users;
    private String usersRoute = "src\\main\\resources\\users\\users.json";


    private JsonParser<String, String> jsonParser;


    public Authenticator() {
        this.jsonParser = new JsonParser<>();
        this.users = this.loadUsers();
    }

    public Optional<String> registerUser(String usernameCred, String passwordCred) {

        users.put(usernameCred, passwordCred);
        if (jsonParser.writeJson(new JSONObject(users), usersRoute)){
            System.out.println("Registered!");
            return Optional.of(usernameCred);
        }
        users.remove(usernameCred);
        return Optional.empty();
    }

    public Optional<String> validUser(String username, String password) {
        if (this.users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Logged in");
            return Optional.of(username);
        }
        System.out.println("Invalid username or password");
        return Optional.empty();
    }

    public Optional<String> validateLoginAttemps(int attemps) {

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

    public boolean validUsername(String username) {
        if (!users.containsKey(username))
            return true;

        System.out.println("This username alredy exists, try to log in");
        System.out.println("_________________");
        return false;
    }

    private Map<String, String> loadUsers() {
        return jsonParser.loadJson(usersRoute);
    }
}