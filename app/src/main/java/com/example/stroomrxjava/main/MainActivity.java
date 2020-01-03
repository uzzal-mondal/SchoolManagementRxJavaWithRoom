package com.example.stroomrxjava.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.stroomrxjava.R;
import com.example.stroomrxjava.model.StudentClassCountModel;
import com.example.stroomrxjava.student_details.StudentRecyclerAdapter;
import com.example.stroomrxjava.student_list.ClassRecyclerAdapter;
import com.example.stroomrxjava.student_add.StudentAddFragment;
import com.example.stroomrxjava.student_details.StudentDetailsFragment;
import com.example.stroomrxjava.student_list.StudentListFragment;

public class MainActivity extends AppCompatActivity implements
        ClassRecyclerAdapter.StudentItemClickListner,
        StudentListFragment.AddNewStudentListner,
        StudentAddFragment.StudentAddRecordCompleteListenr,
        ClassRecyclerAdapter.ItemClickListner,
        StudentRecyclerAdapter.StudentEditItemClickListner,
        StudentAddFragment.StudentUpdateCompleteListner {

    FragmentManager fragmentManager;
    public androidx.appcompat.widget.Toolbar toolbar;
    Fragment prevFragment, currentFragment;
    //using to toolbar return to boolean value..
    boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar id find.
        toolbar = findViewById(R.id.toolbar_id);
        //  toolbar.setTitle("StudentModel Database");
        toolbar.setTitleTextColor(getResources().getColor(R.color.smsp_white_color));
        // toolbar set actionbar.
        setSupportActionBar(toolbar);

        //back button create to enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // first fragment hold.. and go to StudentList Fragment..##
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        StudentListFragment studentListFragment = new StudentListFragment();
        ft.add(R.id.fragment_container_id, studentListFragment);
        ft.commit();

    }

    // that is fragmenttransection...##
    public void fragmenttransection(Fragment fragment, StudentClassCountModel model) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // That is very important..##
        prevFragment = new StudentListFragment();
        currentFragment = new StudentDetailsFragment();
        flag = false;
        StudentDetailsFragment studentDetailsFragment = new StudentDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("student", model.className);
        studentDetailsFragment.setArguments(bundle);
        ft.replace(R.id.fragment_container_id, studentDetailsFragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


    // second adapter hold ... and go to StudentModel Details Fragment..##
    @Override
    public void StudentItemClicked(StudentClassCountModel studentClassCountModel) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        StudentDetailsFragment studentDetailsFragment = new StudentDetailsFragment();
        //StudentClassCountModel object niye jete hobe..##
        Bundle bundle = new Bundle();
        bundle.putSerializable("studentClassCountModel", studentClassCountModel);
        studentDetailsFragment.setArguments(bundle);
        ft.replace(R.id.fragment_container_id, studentDetailsFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    // 3rd .. implements listener .. ## go to record add page..##..Add
    @Override
    public void loadAddNewStudentPage() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        StudentAddFragment studentAddFragment = new StudentAddFragment();
        ft.replace(R.id.fragment_container_id, studentAddFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    //4th .. implements istener .. ## go to student details activity..##
    @Override
    public void onAddStudentComplete() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        StudentListFragment studentListFragment = new StudentListFragment();
        ft.replace(R.id.fragment_container_id, studentListFragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void clickListener(StudentClassCountModel model, int position) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        StudentDetailsFragment studentDetailsFragment = new StudentDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("student", position);
        studentDetailsFragment.setArguments(bundle);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    // when a click edit item , and came to get some data ...##  impt..
    @Override
    public void onStudentEditItemClicked(long id) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        StudentAddFragment studentRecordAddFragment = new StudentAddFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);
        studentRecordAddFragment.setArguments(bundle);
        ft.replace(R.id.fragment_container_id, studentRecordAddFragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onUpdateStudentComplete() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        StudentListFragment studentRecordListFragment = new StudentListFragment();
        ft.replace(R.id.fragment_container_id, studentRecordListFragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    // onOptions item selected menu item, this is back button default..
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // back button call to home default..
            case android.R.id.home:
                backPressed(prevFragment, currentFragment);
                break;

            /*case R.id.action_search:
                searchPressed();
                break;*/

        }
        return super.onOptionsItemSelected(item);
    }

    //  This is for important backPressed method Create,
    public void backPressed(Fragment prevFragment, Fragment currentFragment) {

        // back button clickable currentFragment or prevFragment container loaded.
        if (!flag) {
            // then fragment initialize..
            // set to prevFragment and currentFragment.
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
           // prevFragment = new StudentDetailsFragment();
            currentFragment = new StudentListFragment();
            flag = true;
            ft.replace(R.id.fragment_container_id, currentFragment);
            ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
        } else {
            finish();
        }
    }

}
