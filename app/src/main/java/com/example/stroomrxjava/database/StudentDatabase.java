package com.example.stroomrxjava.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.stroomrxjava.model.StudentModel;


@Database(entities = {StudentModel.class}, version = 1, exportSchema = false)
public abstract class StudentDatabase extends RoomDatabase {

    // database class are method must be declare to abstract..##
    private static StudentDatabase db;
    // this is for database method.##
    public abstract StudentDAO getStudentDao();


    //
    public static StudentDatabase getInstance(Context context){

        if (db!=null){

            return db;
        }

        db = Room.databaseBuilder(context,StudentDatabase.class,"student_db")
                .allowMainThreadQueries()
                .build();

        return db;

    }

   /*public static StudentDatabase getInstance(Context context){

       if (db==null){

           db = Room.databaseBuilder(context,StudentDatabase.class,"student_db")
                   .fallbackToDestructiveMigration()
                   .build();

       }

       return db;
   }*/


}
