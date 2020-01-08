package com.example.stroomrxjava.student_list;

import com.example.stroomrxjava.model.StudentClassCountModel;

public interface StudentItemListener {

    // this is for adpter item listener..##
    void StudentItemClicked(StudentClassCountModel studentClassCountModel);
    void clickListener(StudentClassCountModel model, int position);
    //add new listener
    void loadAddNewStudentPage();


}
