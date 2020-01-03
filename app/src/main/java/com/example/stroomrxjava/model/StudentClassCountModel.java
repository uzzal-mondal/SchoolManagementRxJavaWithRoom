package com.example.stroomrxjava.model;

import java.io.Serializable;

public class StudentClassCountModel implements Serializable {

    public String className;
    public int classCount;



    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }
}
