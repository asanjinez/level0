package org.solution.models;

import org.solution.exceptions.CustomExceptions.*;

import java.util.List;

public class City {
    String name;
    EnrollmentRecord seats;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnrollmentRecord getSeats() {
        return seats;
    }
    public void setSeats(EnrollmentRecord seats) {
        this.seats = seats;
    }

    public boolean cityAvailable(){
        return this.seats.available();
    }
    public void enroll(String username){
        if (!cityAvailable())
            throw new CityNotAvailableException("Sorry this city is full, try another");
        seats.getStudents().add(username);
    }

    public List<String> allStudents() {
        return seats.getStudents();
    }

//    public void enrollUser(String username){
//        if (!cityAvailable())
//            throw new CityNotAvailableException("There are no seats in this city");
//        this.students.add(username);
//
//    }


}
