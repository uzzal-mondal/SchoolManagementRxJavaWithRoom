package com.example.stroomrxjava.student_details;

import com.example.stroomrxjava.model.StudentModel;

public interface AdapterUpDelListener {

    void onDelete(StudentModel studentModel);

    void onUpdate(StudentModel studentModel);
}
