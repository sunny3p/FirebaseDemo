package com.sp.firebasedemo;

import com.google.firebase.database.Exclude;

public class Student {
    private String first_name;
    private String last_name;
    private String marks;
    private String key;

    public Student(){

    }
    public Student(String first_name, String last_name, String marks) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.marks = marks;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public void setValues(Student updateStudent) {
        this.first_name = updateStudent.first_name;
        this.last_name = updateStudent.last_name;
        this.marks = updateStudent.marks;
    }
}
