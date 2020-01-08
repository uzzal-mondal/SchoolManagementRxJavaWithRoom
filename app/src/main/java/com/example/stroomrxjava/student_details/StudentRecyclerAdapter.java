package com.example.stroomrxjava.student_details;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stroomrxjava.database.StudentDatabase;
import com.example.stroomrxjava.model.StudentModel;
import com.example.stroomrxjava.R;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class StudentRecyclerAdapter extends
        RecyclerView.Adapter<StudentRecyclerAdapter.StudentInfoViewHolder> {

    private Context context;
    private List<StudentModel> studentModelList;
    private StudentEditItemListener mEditClickListner;
    private CompositeDisposable compositeDisposable;



    public StudentRecyclerAdapter(Context context, List<StudentModel> studentModelList) {
        this.context = context;
        this.studentModelList = studentModelList;
    }

    public void setListener(StudentEditItemListener mEditClickListner) {
        this.mEditClickListner = mEditClickListner;
    }

    public void upListener(){

    }

    public void delListener(){

    }

    @NonNull
    @Override
    public StudentInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.student_details_recycler_row,
                        parent, false);
        StudentInfoViewHolder holder = new StudentInfoViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentInfoViewHolder holder, final int position) {

        //position get ..#
        final StudentModel studentModel = studentModelList.get(position);
        // check to null
        if (studentModel != null) {
            holder.textViewName.setText(context.getString(R.string.set_name) + studentModel.getName());
            holder.textViewAddress.setText(context.getString(R.string.st_address) + studentModel.getAddress());
            //holder.textViewRoll.setText(context.getString(R.string.st_roll)+ studentModel.getRoll());
            holder.textViewRoll.setText("Roll: " + studentModel.getRoll());
            holder.textViewClassName.setText(String.valueOf(studentModel.getClassName()));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.row_itemDelete_id:
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
                                                                Toast.makeText(context,
                                                                        "deleted",
                                                                        Toast.LENGTH_SHORT).show();

                                                                // from collection data delete..##
                                                                //   studentModelViewInfoList.remove(studentModelViewInfoNewModel);
                                                                studentModelList.remove(studentModel);
                                                                notifyDataSetChanged();
                                                            }
                                                        })
                                                        .subscribe());




                                            }
                                        });
                                builder.setNegativeButton(R.string.builder_no, null);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();


                                break;


                            case R.id.row_itemEdit_id:
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

                                                long id = studentModel.getStudentID();

                                                studentModelList.add(studentModel);
                                                mEditClickListner.onStudentEditItemClicked(id);

                                                Toast.makeText(context, "Update Your Data",
                                                        Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .subscribe());
                                break;
                        }
                        return false;
                    }
                });

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentModelList.size();
    }

    // my view holder view create..##
    public class StudentInfoViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewRoll, textViewClassName, textViewAddress;
        ConstraintLayout constraintLayout;

        public StudentInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewRoll = itemView.findViewById(R.id.text_view_roll);
            textViewClassName = itemView.findViewById(R.id.text_class_name);
            textViewAddress = itemView.findViewById(R.id.text_view_address);
            constraintLayout = itemView.findViewById(R.id.constraint_details);
        }
    }
}
