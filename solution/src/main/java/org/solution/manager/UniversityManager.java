package org.solution.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import org.solution.dataConverter.DataJsonConverter;
import org.solution.enums.CityOptions;
import org.solution.enums.ProgramOptions;
import org.solution.exceptions.CustomExceptions.*;
import org.solution.models.Program;
import org.solution.models.University;

import java.util.List;

public class UniversityManager implements DataManager<University>{
    private AccountManager accountManager;
    private DataJsonConverter<University> jsonConverter;
    private final String route;
    private University university;

    public UniversityManager(String route, AccountManager accountManager) {
        this.route = route;
        this.accountManager = accountManager;
        this.jsonConverter =  new DataJsonConverter<>(new TypeReference<University>(){});
        this.university = load();

    }
    @Override
    public boolean create(University university) {
        if (jsonConverter.save(this.route, university)){
            System.out.println("Enrolled!");
            return true;
        }
        return false;
    }
    @Override
    public boolean delete(University university) {
        if (jsonConverter.save(this.route, university))
            return true;

        return false;
    }
    public List<Program> getEnrolledPrograms(String username){
        return this.university.getProgramsEnrolled(username);
    }

    public boolean enrollUser(ProgramOptions programOption, CityOptions cityOption, String username){
        try {
            university.enroll(programOption,cityOption,username);
            this.jsonConverter.save(this.route,university);
            return true;
        } catch (ProgramNotAvailableException | CityNotAvailableException e){
            handleException(e);
            return false;
        }
    }
    public University load() {
        try {
            return jsonConverter.load(this.route).get();
        } catch (FileLoadException e) {
            handleException(e);
            return null;
        }
    }


    private void handleException(Exception e){
        System.out.println(e.getMessage());
    }

}