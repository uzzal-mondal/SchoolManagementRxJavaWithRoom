package com.example.stroomrxjava.student_add;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
    private AppCompatEditText nameEt, rollEt, addressEt;
    private Spinner spinner;

    private String showSpinner;
    private StudentModel studentModel;


    //listner
    private StudentAddRecordCompleteListenr addListenr;
    private StudentUpdateCompleteListner updateListner;
    private FloatingActionButton fabAddButton, fabUpButton;
    //count
    private long id = 0;
    private int count = 0;
    androidx.appcompat.widget.Toolbar toolbar;
    private CompositeDisposable compositeDisposable;

    //array list for a spinner data...##
    List<String> categories = new ArrayList<>();


    public StudentAddFragment() {
        // Required empty public constructor

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        addListenr = (StudentAddRecordCompleteListenr) context;
        updateListner = (StudentUpdateCompleteListner) context;
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

        nameEt = view.findViewById(R.id.edit_name_id);
        rollEt = view.findViewById(R.id.edit_roll_id);
        addressEt = view.findViewById(R.id.edit_address_id);
        // spinner show...##
        spinner = view.findViewById(R.id.spinner_id);


        // fab add and update button working...##
        fabAddButton = view.findViewById(R.id.fabAddButton_id);
        fabUpButton = view.findViewById(R.id.fabUpButton_id);
        nameEt.requestFocus();


        // create a Array list for a spinner data ..##
        categories.add(0,"Select Class");
        categories.add("Class: 1");
        categories.add("Class: 2");
        categories.add("Class: 3");
        categories.add("Class: 4");
        categories.add("Class: 5");

        // method initialize..##
        initSpinner();




        //data coming and get to arguments.. ##
        final Bundle bundle = getArguments();
        if (bundle != null) {

            /*fabAddButton.setVisibility(View.GONE);
            fabUpButton.setVisibility(View.VISIBLE);*/

            fabAddButton.hide();
            fabUpButton.show();



            /* studentModel = StudentDatabase.getInstance(context)
                    .getStudentDao()
                    .getStudentById(id);
             */


            //this is id include a data from onEditStudent item Clicked..##
         id = bundle.getLong("id");



           //using rx java with call update data..##
            CompositeDisposable compositeDisposable = new CompositeDisposable();
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
                                nameEt.setText(studentModel.getName());
                                rollEt.setText(String.valueOf(studentModel.getRoll()));
                                addressEt.setText(studentModel.getAdress());

                                // spinner set an data show ...##
                                String spinnerData = studentModel.getSpinner();
                                for(int i = 0; i<categories.size(); i++){
                                    if(categories.get(i).equals(spinnerData)){
                                       // categories.remove(i);
                                        spinner.setSelection(i);
                                    }
                                }

                                //categories.add(0, vv);

                            }
                            //using error throwable..##
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            nameEt.setText(studentModel.getName());
                        }
                    }));

        }


        // click to add button,,!!
        fabAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {

                    count++;
                    Log.d("debug", "validate is true");
                   // final String rollString = rollEt.getText().toString();
                    String rolls = rollEt.getText().toString();
                    Log.d("roll", rolls);


                    try {

                       // final int finalRoll = roll;
                         String name = nameEt.getText().toString();
                        final int roll = Integer.parseInt(rollEt.getText().toString().trim());
                        final String address = addressEt.getText().toString();



                        //this is studentModel name is database access name...
                        StudentModel studentModel = new StudentModel(name, roll, address, showSpinner);


                        // rx java CompositeDisposable in insert operation....##
                        CompositeDisposable disposable = new CompositeDisposable();
                        disposable.add(StudentDatabase.getInstance(context).getStudentDao()
                                .insertNewStudent(studentModel).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                                    @Override
                                    public void run() throws Exception {

                                        addListenr.onAddStudentComplete();
                                    }
                                }).doOnError(new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        // if any return to false..##
                                       // addStudentStatus = false;
                                    }
                                }).subscribe());

                    } catch (Exception e) {
                        Toast.makeText(context, "Roll is Unique, please another roll include",
                                Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


        // update button click listener..##
        fabUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Updated Data",
                        Toast.LENGTH_SHORT).show();

                if (validate()) {
                    count++;
                    try {


                        String name = nameEt.getText().toString();
                        int roll = Integer.parseInt(rollEt.getText().toString());
                        String address = addressEt.getText().toString();

                        StudentModel studentModel = new StudentModel(name, roll, address, showSpinner);

                        // this is done to update id
                        studentModel.setStudentID(id);


                        //when a click working to update by rx..##
                        CompositeDisposable compositeDisposable = new CompositeDisposable();
                        compositeDisposable.add(StudentDatabase.getInstance(context)
                        .getStudentDao()
                        .updateStudent(studentModel)
                        .subscribeOn(Schedulers.io()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
                                    @Override
                                    public void run() throws Exception {

                                        // update listener click..##
                                        updateListner.onUpdateStudentComplete();


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

    // check validity to in your record info added.
    public boolean validate() {

        String rolls = rollEt.getText().toString();
         int roll = -1;

        try {
            roll = Integer.parseInt(rolls.trim());

        } catch (NumberFormatException e) {
            Toast.makeText(context, "Roll can't be empty Button ",
                    Toast.LENGTH_SHORT).show();
        }
        Log.d("roll", rolls);

        if (roll > -1) {

            return true;

        } else {

            Toast.makeText(context, "Hello Students, Write fields you're required.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }


       // crate a method for a spinner...##
    private void initSpinner(){
        final ArrayAdapter<String> adapter = new
                ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,
                categories);
        // Dropdown layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // ataching data adapter to spinner
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
                    showSpinner = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        spinner.setAdapter(adapter);

    }

    // this is toolbar name changeable..
    @Override
    public void onResume() {
        super.onResume();
        //title bar set name. with cust..
        ((MainActivity) getActivity()).toolbar.setTitle("StudentModel  Add");
    }

    //create a interface going to Main Activity for a studentModel details activity..##
    public interface StudentAddRecordCompleteListenr {

        void onAddStudentComplete();

    }
    // create a interface by call back go to Main Activity with go to
    public interface StudentUpdateCompleteListner {

        void onUpdateStudentComplete();

    }

}
