package com.example.stroomrxjava.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.stroomrxjava.R;
import com.example.stroomrxjava.model.StudentClassCountModel;
import com.example.stroomrxjava.student_add.StudentAddUpListener;
import com.example.stroomrxjava.student_details.StudentEditItemListener;
import com.example.stroomrxjava.student_list.StudentItemListener;
import com.example.stroomrxjava.student_add.StudentAddFragment;
import com.example.stroomrxjava.student_details.StudentDetailsFragment;
import com.example.stroomrxjava.student_list.StudentListFragment;

public class MainActivity extends AppCompatActivity implements StudentItemListener,
        StudentAddUpListener, StudentEditItemListener {

    private FragmentManager fragmentManager;
    public Toolbar toolbar;
    private Fragment prevFragment, currentFragment;
    private boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inItView();
    }

    public void inItView() {
        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.smsp_white_color));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        commitFragment(new StudentListFragment(), "", "");
    }

    public void commitFragment(Fragment fragment, String key, String value) {
        if (fragment != null) {
            if (!key.isEmpty() && !value.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString(key, value);
                fragment.setArguments(bundle);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        }

    }

    public void fragmentTransaction(Fragment fragment, StudentClassCountModel model) {
        prevFragment = new StudentListFragment();
        currentFragment = new StudentDetailsFragment();
        flag = false;
        commitFragment(currentFragment, getString(R.string.student_model), model.getClassName());
    }


    @Override
    public void StudentItemClicked(StudentClassCountModel studentClassCountModel) {
        StudentDetailsFragment studentDetailsFragment = new StudentDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.student_class_count_model), studentClassCountModel);
        studentDetailsFragment.setArguments(bundle);
        commitFragment(studentDetailsFragment, "", "");
    }

    @Override
    public void loadAddNewStudentPage() {
        commitFragment(new StudentAddFragment(), "", "");
    }

    @Override
    public void onAddStudentComplete() {
        commitFragment(new StudentListFragment(), "", "");
    }

    @Override
    public void clickListener(StudentClassCountModel model, int position) {
        commitFragment(new StudentDetailsFragment(), getString(R.string.student),
                String.valueOf(position));
    }

    @Override
    public void onStudentEditItemClicked(long id) {
        commitFragment(new StudentAddFragment(),
                getString(R.string.id), String.valueOf(id));
    }

    @Override
    public void onUpdateStudentComplete() {
        commitFragment(new StudentListFragment(), "", "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!flag) {
                prevFragment = new StudentDetailsFragment();
                currentFragment = new StudentListFragment();
                flag = true;
                commitFragment(currentFragment, "", "");
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
