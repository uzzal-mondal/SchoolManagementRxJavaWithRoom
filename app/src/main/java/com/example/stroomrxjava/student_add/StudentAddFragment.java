package com.example.stroomrxjava.student_add;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stroomrxjava.database.StudentDatabase;
import com.example.stroomrxjava.main.MainActivity;
import com.example.stroomrxjava.model.StudentModel;
import com.example.stroomrxjava.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
public class StudentAddFragment extends Fragment {
    private Context context;
    private AppCompatEditText name, roll, address;
    private TextView className;
    private Spinner spinner;
    private String spinnerData;
    private CompositeDisposable compositeDisposable, disposable;
    private StudentModel studentModel;
    //listner
    private StudentAddUpListener studentAddUpListener;
    private FloatingActionButton fabSave, fabUp;

    private long id = 0;
    private int count = 0;
    public Toolbar toolbar;

    //array list for a spinner data...##
    List<String> categories = new ArrayList<>();


    public StudentAddFragment() {
        // Required empty public constructor

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        studentAddUpListener = (StudentAddUpListener) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.edit_text_add_name);
        roll = view.findViewById(R.id.edit_text_add_roll);
        address = view.findViewById(R.id.edit_text_add_address);
        className = view.findViewById(R.id.text_add_class_name);

        // spinner show...##
        spinner = view.findViewById(R.id.spinner_id);
        // fab add and update button working...##
        fabSave = view.findViewById(R.id.fab_save);
        fabUp = view.findViewById(R.id.fab_up);
        name.requestFocus();

        // create a Array list for a spinner data ..##
        categories.add(0, "Select Class");
        categories.add("Class: 1");
        categories.add("Class: 2");
        categories.add("Class: 3");
        categories.add("Class: 4");
        categories.add("Class: 5");

        //spinner showing method initialize..##
        initSpinnerShow();

        //data coming and get to arguments.. ##
        final Bundle bundle = getArguments();
        if (bundle != null) {
            // save & up hide this is fab..##
            fabSave.hide();
            fabUp.show();

            String stId = bundle.getString("id");
            id = Long.parseLong(stId);
        }

        //using rx java..##
        compositeDataShowing();
        // fab update & save data show method..##
        fabSaveDataShow();
        fabUpdateDataShow();
    }

    private void compositeDataShowing() {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable
                .add(StudentDatabase.getInstance(context)
                        .getStudentDao()
                        .getStudentById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<StudentModel>() {
                            @Override
                            public void accept(StudentModel studentModel) throws Exception {

                                // fist check null value. with set to data update ..##
                                if (studentModel != null) {
                                    name.setText(studentModel.getName());
                                    roll.setText(String.valueOf(studentModel.getRoll()));
                                    address.setText(studentModel.getAddress());

                                    // spinner set an data show ...##
                                    String spinnerData = studentModel.getClassName();
                                    for (int i = 0; i < categories.size(); i++) {
                                        if (categories.get(i).equals(spinnerData)) {
                                            // categories.remove(i);
                                            spinner.setSelection(i);
                                        }
                                    }

                                }
                                //using error throwable..##
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(context, " exception - " + studentModel.getName(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }));

    }

    // fab save method create..##
    private void fabSaveDataShow() {
        // click to add button,,!!
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateCheck()) {
                    count++;
                    Log.d("debug", "validate is true");
                    // final String rollString = roll.getText().toString();
                    roll.getText().toString();
                    // Log.d("roll", rolls);

                    try {
                        // final int finalRoll = roll;
                        String name = StudentAddFragment.this.name.getText().toString();
                        int roll = Integer.parseInt(StudentAddFragment.this.roll.getText().toString().trim());
                        String address = StudentAddFragment.this.address.getText().toString();
                        spinner.getSelectedItem();


                        //this is studentModel name is database access name...
                        studentModel = new StudentModel(name, roll, address, spinnerData);

                        // rx java CompositeDisposable in insert operation....##
                        disposable = new CompositeDisposable();
                        disposable.add(StudentDatabase.getInstance(context).getStudentDao()
                                .insertNewStudent(studentModel).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        //add listener  call in android
                                        studentAddUpListener.onAddStudentComplete();
                                    }
                                }).doOnError(new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Toast.makeText(context, "" + throwable.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).subscribe());

                    } catch (Exception e) {
                        Toast.makeText(context, "Roll is Unique, please another roll include",
                                Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    // fab save update data show create..##
    private void fabUpdateDataShow() {

        // update button click listener..##
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Updated Data",
                        Toast.LENGTH_SHORT).show();

                if (validateCheck()) {
                    count++;
                    try {
                        String name = StudentAddFragment.this.name.getText().toString();
                        int roll = Integer.parseInt(StudentAddFragment.this.roll.getText().toString());
                        String address = StudentAddFragment.this.address.getText().toString();
                        StudentModel studentModel = new StudentModel(name, roll, address, spinnerData);

                        // this is done to update id
                        studentModel.setStudentID(id);

                        CompositeDisposable compositeDisposable = new CompositeDisposable();
                        compositeDisposable.add(StudentDatabase.getInstance(context)
                                .getStudentDao()
                                .updateStudent(studentModel)
                                .subscribeOn(Schedulers.io()).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                                    @Override
                                    public void run() throws Exception {

                                        // update listener click..##
                                        studentAddUpListener.onUpdateStudentComplete();

                                    }
                                }).subscribe());

                    } catch (Exception e) {
                        Toast.makeText(context, e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });

    }

    // crate a method for a spinner...##
    private void initSpinnerShow() {
        final ArrayAdapter<String> adapter = new
                ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,
                categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(adapter);

        spinner.setSelection(((ArrayAdapter) spinner.getAdapter())
                .getPosition(spinner.getGravity()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long l) {
                if (parent.getItemAtPosition(position).equals("Your Class")) {
                    spinner.setSelection(position);
                    // do nothing
                } else {
                    spinnerData = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        spinner.setAdapter(adapter);
    }


    // check validity to in your record info added.
    public boolean validateCheck() {

        String roolValidity = roll.getText().toString().trim();
        String nameValidity = name.getText().toString().trim();
        String addressValidity = address.getText().toString().trim();
        String classNameValidity = className.getText().toString().trim();


        if (nameValidity.isEmpty()) {
            name.setError(getString(R.string.name_validity));
            return false;
        } else if (roolValidity.isEmpty()) {
            roll.setError(getString(R.string.roll_validity));
            return false;
        } else if (addressValidity.isEmpty()) {
            address.setError(getString(R.string.address_validity));
            return false;
        } /*else if (spinner.getSelectedItem().toString().trim().equals("Select Class")) {
            className.setError("Class, can't be empty");
            Toast.makeText(context, "Select your Class", Toast.LENGTH_SHORT).show();
           return true;
        }*/


        int roll = -1;

        try {
            roll = Integer.parseInt(roolValidity.trim());
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Roll can't be empty Button ",
                    Toast.LENGTH_SHORT).show();
        }
        Log.d("roll", roolValidity);
        if (roll > -1) {
            return true;
        } else {
            Toast.makeText(context, "Hello Students, Write fields you're required.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    // this is toolbar name changeable..
    @Override
    public void onResume() {
        super.onResume();
        //title bar set name. with cust..
        ((MainActivity) getActivity()).toolbar.setTitle("Student's  Add");
    }

}
