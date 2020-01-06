package com.example.stroomrxjava.model;

import java.io.Serializable;

public class StudentClassModel implements Serializable {

    // model class create..
    private String name;
    private String address;
    private String className;  // spinner data show..!
    private int roll;

    public StudentClassModel(String name, String address,
                             String className, int roll) {
        this.name = name;
        this.address = address;
        this.className = className;
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }
}
