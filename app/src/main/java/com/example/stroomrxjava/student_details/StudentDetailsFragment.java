package com.example.stroomrxjava.student_details;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.stroomrxjava.database.StudentDatabase;
import com.example.stroomrxjava.main.MainActivity;
import com.example.stroomrxjava.model.StudentModel;
import com.example.stroomrxjava.model.StudentClassModel;
import com.example.stroomrxjava.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentDetailsFragment extends Fragment implements AdapterUpDelListener {


    private RecyclerView recyclerDetails;
    // private TextView nameDt, rollDt, addressDt, classSpDt;
    private String className;
    // list of arrayList.
    List<StudentModel> studentModelNewList = new ArrayList<>();
    private CompositeDisposable compositeDisposable;
    private StudentModel studentModel;
    // adapter er object create.. :)
    private List<StudentClassModel> studentViewInfoNewModels = new ArrayList<>();
    private StudentRecyclerAdapter detailsAdapter;
    private Toolbar toolbar;
    private Context context;

    //AdapterUpDelListener adapterUpDelListener;


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

        // check studentModelNewList with list clear..
        if (!studentModelNewList.isEmpty()) {
            studentModelNewList.clear();
        }
        recyclerDetails = view.findViewById(R.id.recycler_fragment_student_details);
        recyclerDetails.setLayoutManager(new LinearLayoutManager(context));


        // this is important show the class details info.
        Bundle bundle = getArguments();
        if (bundle != null) {
            className = bundle.getString("student");
        }

        // using to rx java CompositeDisposable..##
        comPosingDataShow();

    }


    public void comPosingDataShow() {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(StudentDatabase.getInstance(getActivity())
                .getStudentDao()
                .getAllStudents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<StudentModel>>() {
                    @Override
                    public void accept(List<StudentModel> studentModelList) throws Exception {

                        for (int i = 0; i < studentModelList.size(); i++) {
                            //studentModel list get position..##
                            studentModel = studentModelList.get(i);

                            //null point selection..##
                            if (studentModel.getClassName() != null) {
                                if (studentModel.getClassName() != null) {
                                    if (studentModel.getClassName().equals(className)) {
                                        studentModelNewList.add(studentModel);
                                    }
                                }
                            }


                        }

                        detailsAdapter = new StudentRecyclerAdapter(context, studentModelNewList,
                                StudentDetailsFragment.this);
                        detailsAdapter.setListener((StudentEditItemListener) getActivity());
                        recyclerDetails.setAdapter(detailsAdapter);
                    }
                }));

    }


    @Override
    public void onResume() {
        super.onResume();
        //title bar set name.
        ((MainActivity) getActivity()).toolbar.setTitle(getString(R.string.student_details));
    }

    @Override
    public void onDelete(final StudentModel studentModel) {

        AlertDialog.Builder builder = new
                AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);

        builder.setMessage(context.getString(R.string.confirm_delete) + " , "
                + studentModel.getName());


        // create a interface
        builder.setPositiveButton(R.string.builder_yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // composite disposable delete.. ##
                        compositeDisposable = new CompositeDisposable();
                        compositeDisposable.add(StudentDatabase
                                .getInstance(context)
                                .getStudentDao()
                                .deleteStudent(studentModel)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnComplete(new Action() {
                                    @Override
                                    public void run() throws Exception {

                                        detailsAdapter.removeItem(studentModel);

                                        Toast.makeText(context,
                                                "deleted",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .subscribe());


                    }
                });
        builder.setNegativeButton(R.string.builder_no, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    @Override
    public void onUpdate(final StudentModel studentModel) {



        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(StudentDatabase
                .getInstance(context)
                .getStudentDao()
                .updateStudent(studentModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                        //long id = studentModel.getStudentID();
                        //studentModelList.add(studentModel);
                        detailsAdapter.updateItem(studentModel);

                        Toast.makeText(context, "Update Your Data",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .subscribe());





    }
}
