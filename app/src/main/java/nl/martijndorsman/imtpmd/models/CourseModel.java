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
}
