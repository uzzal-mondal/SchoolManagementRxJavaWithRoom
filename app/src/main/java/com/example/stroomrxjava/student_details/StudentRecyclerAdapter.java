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
    private List<StudentModel> studentModelViewInfoList;
    private StudentEditItemClickListner editItemClickListner;

    CompositeDisposable compositeDisposable;


    // private StudentInfoItemClcikListner listner;
    public StudentRecyclerAdapter(Context context, List<StudentModel> studentModelViewInfoList) {
        this.context = context;
        this.studentModelViewInfoList = studentModelViewInfoList;
        editItemClickListner = (StudentEditItemClickListner) context;
    }

    @NonNull
    @Override
    public StudentInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.student_info_details_recycler_row,
                        parent, false);
        StudentInfoViewHolder holder = new StudentInfoViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentInfoViewHolder holder, final int position) {

        //position get ..#
        final StudentModel studentModelViewInfoNewModel = studentModelViewInfoList.get(position);
        // check to null
        if (studentModelViewInfoNewModel != null) {
            holder.stName.setText("Name: " + studentModelViewInfoNewModel.getName());
            holder.stAddress.setText("Address: " + studentModelViewInfoNewModel.getAdress());
            holder.stRoll.setText("Roll: " + String.valueOf(studentModelViewInfoNewModel.getRoll()));
            holder.stSpinnerClassShow.setText(String.valueOf(studentModelViewInfoNewModel.getSpinner()));
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
                                        + studentModelViewInfoNewModel.getName());


                                builder.setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                // composite disposable delete.. ##
                                                compositeDisposable = new CompositeDisposable();
                                                compositeDisposable.add(StudentDatabase
                                                        .getInstance(context)
                                                        .getStudentDao()
                                                        .deleteStudent(studentModelViewInfoNewModel)
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .doOnComplete(new Action() {
                                                            @Override
                                                            public void run() throws Exception {
                                                                Toast.makeText(context,
                                                                        " deleted",
                                                                        Toast.LENGTH_SHORT).show();

                                                                // from collection data delete..##
                                                                studentModelViewInfoList
                                                                        .remove(studentModelViewInfoNewModel);
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
                                CompositeDisposable compositeDisposable = new CompositeDisposable();
                                compositeDisposable.add(StudentDatabase
                                        .getInstance(context)
                                        .getStudentDao()
                                        .updateStudent(studentModelViewInfoNewModel)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnComplete(new Action() {
                                            @Override
                                            public void run() throws Exception {


                                                long id = studentModelViewInfoNewModel.getStudentID();

                                                studentModelViewInfoList.add(studentModelViewInfoNewModel);
                                                editItemClickListner.onStudentEditItemClicked(id);

                                                Toast.makeText(context, "StudentModel Update Info",
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
        return studentModelViewInfoList.size();
    }

    // my view holder view create..##
    public class StudentInfoViewHolder extends RecyclerView.ViewHolder {

        TextView stName, stRoll, stSpinnerClassShow, stAddress;
        ConstraintLayout constraintLayout;

        public StudentInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            stName = itemView.findViewById(R.id.textName_id);
            stRoll = itemView.findViewById(R.id.textRoll_id);
            stSpinnerClassShow = itemView.findViewById(R.id.textSpinnerClass_id);
            stAddress = itemView.findViewById(R.id.textAdress_id);
            constraintLayout = itemView.findViewById(R.id.constraint_info_details_id);
        }
    }

    public interface StudentEditItemClickListner {

        void onStudentEditItemClicked(long id);

    }


}
