package org.solution.models;

import org.solution.enums.CityOptions;
import org.solution.exceptions.CustomExceptions.*;

import java.util.List;

public class Program {
    private String title;
    private Integer maxStudents;
    private List<City> cities;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public boolean programAvalaible(){
        return this.totalStudents() < this.maxStudents;
    }
    public Integer totalStudents(){
        return cities.stream().mapToInt(city -> city.getSeats().getStudents().size()).sum();
    }
    public boolean containsUser(String username){
        return cities.stream().map(City::allStudents).flatMap(List::stream).toList().contains(username);
    }
    public City getCity(CityOptions cityOptions){
        return this.cities.stream().filter(city -> city.getName().equals(cityOptions.toString())).findFirst().get();
    }
    public List<String> allProgramStudents(){
        return this.getCities().stream()
                .flatMap(city -> city.allStudents().stream())
                .toList();
    }
    public void enroll(CityOptions city, String username){
        if(!programAvalaible())
            throw new ProgramNotAvailableException("Sorry but this program is full");
        getCity(city).enroll(username);
    }
//    public void enrollStudent(String username, City city){
//        if (!programAvalaible())
//            throw new ProgramNotAvailableException("Program is not available for enrollment.");
//        city.enrollStudent(String username);
//    }


}
