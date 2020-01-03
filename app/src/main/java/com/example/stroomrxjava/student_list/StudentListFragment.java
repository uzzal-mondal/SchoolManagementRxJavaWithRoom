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

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentListFragment extends Fragment implements
        ClassRecyclerAdapter.ItemClickListner {

    private RecyclerView recyclerView;
    private Context context;
    private ClassRecyclerAdapter classRecyclerAdapter;
    private FloatingActionButton floatingActionButton;
    private AddNewStudentListner addNewStudentListner;
    private List<StudentClassCountModel> studentClassCountModelList = new ArrayList<>();


    // counter declare.
    int class1Counter = 0, class2Counter = 0, class3Counter = 0,
            class4Counter = 0, class5Counter = 0;


    public StudentListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        addNewStudentListner = (AddNewStudentListner) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = view.findViewById(R.id.fab_id);
        recyclerView = view.findViewById(R.id.recycler_fragment_student_list);
        classRecyclerAdapter = new ClassRecyclerAdapter(getActivity(), new ArrayList<StudentClassCountModel>());
        classRecyclerAdapter.setListener(this);

        //empty check..##
        if (!studentClassCountModelList.isEmpty()) {
            studentClassCountModelList.clear();
        }

        //using rx java .. ##
        CompositeDisposable compositeDisposable =  new CompositeDisposable();
        compositeDisposable.add(StudentDatabase.getInstance(getActivity())
        .getStudentDao()
        .getAllStudents()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<StudentModel>>() {
            @Override
            public void accept(List<StudentModel> studentModels) throws Exception {

                StudentClassCountModel model1  = new StudentClassCountModel();
                StudentClassCountModel model2 = new StudentClassCountModel();
                StudentClassCountModel model3 = new StudentClassCountModel();
                StudentClassCountModel model4 = new StudentClassCountModel();
                StudentClassCountModel model5 = new StudentClassCountModel();

                // declare to for each loop countable student get to studentList by :- shapla apu.:)
                for (StudentModel studentModel : studentModels) {

                    if (studentModel.getSpinner() != null) {

                        if (studentModel.getSpinner().equals("Class: 1")) {
                            model1.className = "Class: 1";
                            class1Counter++;
                            model1.classCount++;

                        } else if (studentModel.getSpinner().equals("Class: 2")) {
                            model2.className = "Class: 2";
                            class2Counter++;
                            model2.classCount++;

                        } else if (studentModel.getSpinner().equals("Class: 3")) {

                            model3.className = "Class: 3";
                            class3Counter++;
                            model3.classCount++;

                        } else if (studentModel.getSpinner().equals("Class: 4")) {
                            model4.className = "Class: 4";
                            class4Counter++;
                            model4.classCount++;

                        } else if (studentModel.getSpinner().equals("Class: 5")) {

                            model5.className = "Class: 5";
                            class5Counter++;
                            model5.classCount++;
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

                LinearLayoutManager lm = new LinearLayoutManager(context);

                recyclerView.setLayoutManager(lm);
                recyclerView.setAdapter(classRecyclerAdapter);

            }
        }));


        // fab listner to initialize adding to new student data add fragment..##
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addNewStudentListner.loadAddNewStudentPage();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).toolbar.setTitle("StudentModel List");
    }

    @Override
    public void clickListener(StudentClassCountModel model, int position) {

        ((MainActivity) getActivity()).fragmenttransection(new StudentDetailsFragment(), model);
    }

    // create a interface, go to main activity..##
    public interface AddNewStudentListner{

        void loadAddNewStudentPage();
    }
}
