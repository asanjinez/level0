package org.solution;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// For this solution we will not use a database, it is a good alternative but we will limit ourselves to solve all this as natively as possible, at least for level 0.
public class Main {
    public static void main(String[] args) {
//      Ruta Actual: E:\Ideas\programming-skills\level0\solution
        boolean logged = false;
        Map<String, String> users = loadUsers();

        do{
            System.out.println("Do you have an existing account?");
            System.out.println("1) Yes");
            System.out.println("2) No");

            String username;
            String password;

            switch (new Scanner(System.in).nextInt()){
                case 1:
                    System.out.println("Please, enter your username and password");
                    System.out.println("Enter Username:");
                    username = new Scanner(System.in).nextLine();

                    System.out.println("Enter Password:");
                    password = new Scanner(System.in).nextLine();

                    if (users.containsKey(username) && users.get(username).equals(password) ){
                        System.out.println("Logged in");
                        logged = true;
                        break;
                    }
                    System.out.println("Invalid username or password");
                    break;
                case 2:
                    System.out.println("Please register now");
                    System.out.println("Enter Username:");
                    username = new Scanner(System.in).nextLine();
                    if(users.containsKey(username)) {
                        System.out.println("This username alredy exists, try to log in");
                        continue;
                    }

                    System.out.println("Enter Password:");
                    password = new Scanner(System.in).nextLine();
                    users.put(username,password);

                    try {
                        JSONObject jsonObject = new JSONObject(users);
                        FileWriter fileWriter = new FileWriter("src\\main\\resources\\users\\users.json",false);
                        fileWriter.write(jsonObject.toString());
                        fileWriter.flush();
                        fileWriter.close();

                        System.out.println("User successfully  created");
                        logged = true;

                    } catch (IOException io){
                        users.remove(username);
                        System.out.println("Error creating user");
                        System.out.println(io.getMessage());
                    }


                    break;

                default:
                    System.out.println("Sorry, the option is wrong");
            }
        } while (!logged);

    }

    private static Map<String, String> loadUsers() {
        Map<String, String> map;
        try {
            String users = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\users\\users.json")));
            JSONObject jsonObject = new JSONObject(users);
            map = toMap(jsonObject);

            System.out.println(map.size() + " Users loaded\n");

        } catch (IOException io){
            System.out.println("Users File not found");
            System.out.println(io.getMessage());
            map = null;
        }

        return map;
    }

    // Convert from Json to Map
    private static Map<String, String> toMap(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.getString(key));
        }
        return map;
    }




}