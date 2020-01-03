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
    private String adress;


    // you have to change this is spinner..##
    @ColumnInfo(name = "std_spinner")
    private String spinner;



    @Ignore
    public StudentModel(String name, long studentID, int roll, String adress,
                        String spinner) {
        this.name = name;
        this.studentID = studentID;
        this.roll = roll;
        this.adress = adress;
        this.spinner = spinner;
    }

    public StudentModel(String name, int roll, String adress, String spinner) {
        this.name = name;
        this.roll = roll;
        this.adress = adress;
        this.spinner = spinner;
    }



    public long getStudentID() {
        return studentID;
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getSpinner() {
        return spinner;
    }

    public void setSpinner(String spinner) {
        this.spinner = spinner;
    }

}
