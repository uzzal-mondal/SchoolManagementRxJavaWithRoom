package com.example.stroomrxjava.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tble_student",
        indices = {@Index(value = {"std_roll"},
                unique = false)})
public class StudentModel {

    @ColumnInfo(name = "std_name")
    private String name;

    @PrimaryKey(autoGenerate = true)
    private long studentID;


    @ColumnInfo(name = "std_roll")
    private int roll;

    @ColumnInfo(name = "std_address")
    private String address;


    // you have to change this is spinner..##
    @ColumnInfo(name = "std_className")
    private String className;



    @Ignore
    public StudentModel(String name, long studentID, int roll, String address,
                        String className) {
        this.name = name;
        this.studentID = studentID;
        this.roll = roll;
        this.address = address;
        this.className = className;
    }

    public StudentModel(String name, int roll,
                        String address, String className) {
        this.name = name;
        this.roll = roll;
        this.address = address;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStudentID() {
        return studentID;
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
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
}
