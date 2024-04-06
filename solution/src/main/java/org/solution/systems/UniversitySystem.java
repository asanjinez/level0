package org.solution.systems;

import org.solution.enums.*;
import org.solution.manager.AccountManager;
import org.solution.manager.UniversityManager;
import org.solution.models.Program;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UniversitySystem implements SystemC<UniversityManager> {
    private UniversityManager universityManager;

    public UniversitySystem(UniversityManager universityManager) {
        this.universityManager = universityManager;
    }

    @Override
    public void start(String username) {
        ProgramOptions programOption = ProgramOptions.BACK;
        do {
            System.out.println("---UNIVERSITY---");

            List<Program> programsUserEnrolled = universityManager.getEnrolledPrograms(username);
            if (!programsUserEnrolled.isEmpty()) {
                System.out.println("You are already enrolled at:");
                programsUserEnrolled.forEach(program -> System.out.println(program.getTitle()));
            }

            try {
                System.out.println("Select the program you want");
                ProgramOptions.showValues();
                programOption = ProgramOptions.fromIndex(getUserChoice());
                if (!ProgramOptions.BACK.equals(programOption)) {
                    System.out.println("Choose a city");
                    CityOptions.showValues();
                    CityOptions cityOption = CityOptions.fromIndex(getUserChoice());
                    if (!CityOptions.BACK.equals(cityOption)) {
                        this.universityManager.enrollUser(programOption, cityOption, username);
                    }
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Sorry, the option is wrong");
            }
        } while (!ProgramOptions.BACK.equals(programOption));
    }

    @Override
    public int getUserChoice() {
        return new Scanner(System.in).nextInt();
    }
}