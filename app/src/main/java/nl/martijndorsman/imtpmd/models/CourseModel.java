package nl.martijndorsman.imtpmd.models;

import java.io.Serializable;

/**
 * Created by Martijn on 21/05/17.
 */

// Klasse waarin het model van een course staat vastgesteld, samen met de getters en setters
public class CourseModel implements Serializable {
    String name;
    String ects;
    String period;
    String grade;
    String status;

    //constructor
    public CourseModel(String courseName, String ects, String period, String grade, String status){
        this.name = courseName;
        this.ects = ects;
        this.period = period;
        this.grade = grade;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEcts() {
        return ects;
    }

    public void setEcts(String ects) {
        this.ects = ects;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
