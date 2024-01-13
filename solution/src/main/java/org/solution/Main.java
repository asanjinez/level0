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
        Authenticator authenticator = new Authenticator();
        boolean logged = false;
        String username;
        String password;

        do{
            System.out.println("Do you have an existing account?");
            System.out.println("1) Yes");
            System.out.println("2) No");
            System.out.println("_________________");


            switch (new Scanner(System.in).nextInt()){
                case 1:
                    System.out.println("Please, enter your username and password");
                    System.out.println("Enter Username:");
                    username = new Scanner(System.in).nextLine();

                    System.out.println("Enter Password:");
                    password = new Scanner(System.in).nextLine();

                    if(authenticator.validUser(username,password))
                        logged = true;
                    break;

                case 2:
                    System.out.println("Please register now");
                    System.out.println("Enter Username:");
                    username = new Scanner(System.in).nextLine();
                    if (!authenticator.validUsername(username))
                        continue;

                    System.out.println("Enter Password:");
                    password = new Scanner(System.in).nextLine();

                    if (authenticator.registerUser(username,password))
                        logged = true;
                    break;

                default:
                    System.out.println("Sorry, the option is wrong");
            }
        } while (!logged);

    }
}