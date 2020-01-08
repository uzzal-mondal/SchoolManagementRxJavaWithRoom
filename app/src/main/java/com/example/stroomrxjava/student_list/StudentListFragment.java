package com.example.stroomrxjava.student_list;


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

import com.example.stroomrxjava.model.StudentClassCountModel;
import com.example.stroomrxjava.database.StudentDatabase;
import com.example.stroomrxjava.main.MainActivity;
import com.example.stroomrxjava.model.StudentModel;
import com.example.stroomrxjava.R;
import com.example.stroomrxjava.student_details.StudentDetailsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class StudentListFragment extends Fragment implements
        StudentItemListener {

    private RecyclerView recyclerView;
    private Context context;
    private ClassRecyclerAdapter classRecyclerAdapter;
    private FloatingActionButton floatingActionButton;
    private List<StudentClassCountModel> studentClassCountModelList = new ArrayList<>();
    private CompositeDisposable compositeDisposable;
    private StudentClassCountModel model1, model2, model3, model4, model5;
    private StudentItemListener studentItemListener;

    // counter declare.
    int class1Counter = 0, class2Counter = 0, class3Counter = 0,
            class4Counter = 0, class5Counter = 0, count = 0;

    public StudentListFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        studentItemListener = (StudentItemListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = view.findViewById(R.id.fab_add);
        recyclerView = view.findViewById(R.id.recycler_fragment_student_list);
        classRecyclerAdapter = new ClassRecyclerAdapter(context,
                new ArrayList<StudentClassCountModel>());
        classRecyclerAdapter.setListener(this);

        if (!studentClassCountModelList.isEmpty()) {
            studentClassCountModelList.clear();
        }
        //using rx java..##
        newComposeShowingData();
        // working fab adding student's data
        fabAddShow();
    }

    // onView created is ending part..##
    public void fabAddShow() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentItemListener.loadAddNewStudentPage();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).toolbar.setTitle("Student's List");
    }

    @Override
    public void StudentItemClicked(StudentClassCountModel studentClassCountModel) {

    }

    @Override
    public void clickListener(StudentClassCountModel model, int position) {
        ((MainActivity) getActivity())
                .fragmentTransaction(new StudentDetailsFragment(), model);
    }

    @Override
    public void loadAddNewStudentPage() {

    }

    private void newComposeShowingData() {

        //using rx java .. ##
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(StudentDatabase.getInstance(getActivity())
                .getStudentDao()
                .getAllStudents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<StudentModel>>() {
                    @Override
                    public void accept(List<StudentModel> studentModels) throws Exception {

                        model1 = new StudentClassCountModel();
                        model2 = new StudentClassCountModel();
                        model3 = new StudentClassCountModel();
                        model4 = new StudentClassCountModel();
                        model5 = new StudentClassCountModel();

                        // declare to for each loop countable student get to studentList by :- shapla apu.:)
                        for (StudentModel studentModel : studentModels) {

                            if (studentModel.getClassName() != null) {

                                if (studentModel.getClassName().equals("Class: 1")) {
                                    class1Counter++;
                                    count = model1.getClassCount();
                                    model1.setClassCount(++count);
                                    model1.setClassName("Class: 1");

                                } else if (studentModel.getClassName().equals("Class: 2")) {
                                    class2Counter++;
                                    count = model2.getClassCount();
                                    model2.setClassCount(++count);
                                    model2.setClassName("Class: 2");
                                } else if (studentModel.getClassName().equals("Class: 3")) {
                                    class3Counter++;
                                    count = model3.getClassCount();
                                    model3.setClassCount(++count);
                                    model3.setClassName("Class: 3");
                                } else if (studentModel.getClassName().equals("Class: 4")) {
                                    class4Counter++;
                                    count = model4.getClassCount();
                                    model4.setClassCount(++count);
                                    model4.setClassName("Class: 4");
                                } else if (studentModel.getClassName().equals("Class: 5")) {
                                    class5Counter++;
                                    count = model5.getClassCount();
                                    model5.setClassCount(++count);
                                    model5.setClassName("Class: 5");
                                }
                            }
                        }
                        // this is classCountList adding to model.
                        studentClassCountModelList.add(model1);
                        studentClassCountModelList.add(model2);
                        studentClassCountModelList.add(model3);
                        studentClassCountModelList.add(model4);
                        studentClassCountModelList.add(model5);

                        classRecyclerAdapter.clearAll();
                        classRecyclerAdapter.addItems(studentClassCountModelList);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(classRecyclerAdapter);
                    }
                }));
    }

}
