package org.solution.models;

import org.solution.enums.CityOptions;
import org.solution.enums.ProgramOptions;
import org.solution.exceptions.CustomExceptions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class University {
    List<Program> programs;

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public Program getProgram(ProgramOptions programOption){
        return this.programs.stream()
                .filter(p -> p.getTitle().equals(programOption.name())).findFirst().orElseThrow(() -> new InexistentProgram("This is an Inexistent program"));
    }
    public Set<String> allStudents(){
        return this.programs
                .stream()
                .map(Program::allProgramStudents)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    public void enroll(ProgramOptions programOption, CityOptions cityOption, String username){
        this.getProgram(programOption).enroll(cityOption,username);
    }

    public boolean personEnrolled(String username){
        return this.allStudents().contains(username);
    }

    public List<Program> getProgramsEnrolled(String username){
        return this.programs.stream().filter(program -> program.containsUser(username)).toList();
    }
}
