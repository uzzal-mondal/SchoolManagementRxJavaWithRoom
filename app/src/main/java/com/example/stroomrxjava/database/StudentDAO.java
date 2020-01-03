package com.example.stroomrxjava.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.stroomrxjava.model.StudentModel;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface StudentDAO {


    // first of all insert and it's success.
    @Insert
    Completable insertNewStudent(StudentModel studentModel);


    //second, all studentList Query.
    @Query("select * from tble_student")
    Flowable<List<StudentModel>> getAllStudents();


    // all student roll, unique query.
    @Query("select * from tble_student")
    Flowable<List<StudentModel>> getStudentByRoll();


    //search Query..
    @Query("select * from tble_student where std_roll like:roll")
    Flowable<List<StudentModel>> getStudentByRoll(long roll);


    // Edit
    @Query("select * from tble_student where studentID like:id")
    Flowable<StudentModel> getStudentById(long id); //this is


    // Delete
    @Delete
    Completable deleteStudent(StudentModel studentModel);


    // update data
    @Update
    Completable updateStudent(StudentModel studentModel);






     /*// first of all insert and it's success.


    @Insert
    long insertNewStudent(StudentModel student);


    //second, all studentList Query.
    @Query("select * from tble_student")
    List<StudentModel> getAllStudents();


    // all student roll, unique query.
    @Query("select * from tble_student")
    List<StudentModel> getStudentByRoll();


    //search Query..
    @Query("select * from tble_student where std_roll like:roll")
    StudentModel getStudentByRoll(long roll);


    // Edit
    @Query("select * from tble_student where studentID like:id")
    StudentModel getStudentById(long id);


    // Delete
    @Delete
    int deleteStudent(StudentModel student);


    // update data
    @Update
    int updateStudent(StudentModel student);*/


}
