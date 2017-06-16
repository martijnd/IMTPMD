package nl.martijndorsman.imtpmd.models;

import java.io.Serializable;

/**
 * Created by Martijn on 21/05/17.
 */

public class CourseModel implements Serializable {
    public String name;
    public String period;
    public String ects;
    public String grade;

    public CourseModel(String courseName, String ects, String grade, String period){
        this.name = courseName;
        this.ects = ects;
        this.grade = grade;
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getEcts() {
        return ects;
    }

    public void setEcts(String ects) {
        this.ects = ects;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
