package org.solution.models;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentRecord {
    private int maxStudents;
    private List<String> students;

    public EnrollmentRecord() {
        this.students = new ArrayList<>(); // Asegúrate de inicializar la lista

    }

    public EnrollmentRecord(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public EnrollmentRecord(int maxStudents, List<String> students) {
        this.maxStudents = maxStudents;
        this.students = students;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public boolean available(){
        return this.students.size() < maxStudents;
    }
}
