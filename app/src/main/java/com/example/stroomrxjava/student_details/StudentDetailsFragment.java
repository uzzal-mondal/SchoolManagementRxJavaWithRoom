package com.example.stroomrxjava.student_details;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stroomrxjava.database.StudentDatabase;
import com.example.stroomrxjava.main.MainActivity;
import com.example.stroomrxjava.model.StudentModel;
import com.example.stroomrxjava.model.StudentClassModel;
import com.example.stroomrxjava.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentDetailsFragment extends Fragment {


    private RecyclerView recyclerDetails;
   // private TextView nameDt, rollDt, addressDt, classSpDt;
    private String className;

   // private List<StudentModel> studentList = new ArrayList<>();

    // list of arrayList.
    List<StudentModel> studentModelNewList = new ArrayList<>();

    // adapter er object create.. :)
    private List<StudentClassModel> studentViewInfoNewModels = new ArrayList<>();
    private StudentRecyclerAdapter detailsAdapter;
    androidx.appcompat.widget.Toolbar toolbar;


    private Context context;


    public StudentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_details,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // list clear..
        if(!studentModelNewList.isEmpty()){
            studentModelNewList.clear();
        }

        recyclerDetails = view.findViewById(R.id.recycler_fragment_student_details);
        recyclerDetails.setLayoutManager(new LinearLayoutManager(context));


        // this is important show the class details info.
        Bundle bundle = getArguments();
        if (bundle != null) {
            className = bundle.getString("student");

        }



        //using  to Rx java Implementation...##
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(StudentDatabase.getInstance(getActivity())
                .getStudentDao()
                .getAllStudents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<StudentModel>>() {
            @Override
            public void accept(List<StudentModel> studentModelList) throws Exception {


                for (int i = 0; i< studentModelList.size(); i++){
                    //studentModel list get position..##
                    StudentModel studentModel = studentModelList.get(i);

                    //null point selection..##
                    if (studentModel.getClassName()!=null){
                        if (studentModel.getClassName()!=null){
                            if (studentModel.getClassName().equals(className)){
                                studentModelNewList.add(studentModel);
                            }
                        }
                    }
                    detailsAdapter = new StudentRecyclerAdapter(context, studentModelNewList);
                    recyclerDetails.setAdapter(detailsAdapter);

                }

            }
        }));

    }
    @Override
    public void onResume() {
        super.onResume();
        //title bar set name.
        ((MainActivity)getActivity()).toolbar.setTitle("Student's Details");
    }
}
