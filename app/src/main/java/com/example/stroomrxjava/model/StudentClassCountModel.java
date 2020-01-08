package com.example.stroomrxjava.model;

import java.io.Serializable;

public class StudentClassCountModel implements Serializable {

    private String className;
    private int classCount;

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
