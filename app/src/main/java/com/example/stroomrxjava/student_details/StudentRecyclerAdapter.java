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
    private StudentEditItemClickListener editItemClickListner;

    //this is rx java using compositeDisposable..##
    CompositeDisposable compositeDisposable;


    // private StudentInfoItemClcikListner listner;
    public StudentRecyclerAdapter(Context context, List<StudentModel> studentModelList) {
        this.context = context;
        this.studentModelList = studentModelList;
        editItemClickListner = (StudentEditItemClickListener) context;
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
            holder.stName.setText("Name: " + studentModel.getName());
            holder.stAddress.setText("Address: " + studentModel.getAddress());
            holder.stRoll.setText("Roll: " + String.valueOf(studentModel.getRoll()));
            holder.stClassName.setText(String.valueOf(studentModel.getClassName()));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // click to popup menu ...##
                PopupMenu popupMenu = new PopupMenu(context, view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            // Delete to get all student data...##
                            case R.id.row_itemDelete_id:
                                AlertDialog.Builder builder = new
                                        AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);

                                builder.setMessage("Confirm Delete" + " , "
                                        + studentModel.getName());


                                builder.setPositiveButton("Yes",
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
                                                                        " deleted",
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
                                builder.setNegativeButton("No", null);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                break;


                            //update info..##
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
                                                editItemClickListner.onStudentEditItemClicked(id);


                                               /* Toast.makeText(context, "Update Your Data",
                                                        Toast.LENGTH_SHORT).show();*/

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

        TextView stName, stRoll, stClassName, stAddress;
        ConstraintLayout constraintLayout;

        public StudentInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            stName = itemView.findViewById(R.id.text_name);
            stRoll = itemView.findViewById(R.id.text_roll);
            stClassName = itemView.findViewById(R.id.text_class_name);
            stAddress = itemView.findViewById(R.id.text_address);
            constraintLayout = itemView.findViewById(R.id.constraint_info_details_id);
        }
    }

   /* public interface StudentEditItemClickListner {

        void onStudentEditItemClicked(long id);

    }*/

}
