package com.example.stroomrxjava.student_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stroomrxjava.model.StudentClassCountModel;
import com.example.stroomrxjava.R;

import java.util.List;
public class ClassRecyclerAdapter extends RecyclerView
        .Adapter<ClassRecyclerAdapter.StudentViewHolder> {

    // create a context, with model list...##
    private Context context;
    private List<StudentClassCountModel> studentClassCountModelList;
   // private StudentItemClickListner studentItemClickListner;
    //this is use to adapter item listener.##
    StudentItemListener studentItemListener;



   // private ItemClickListner itemClickListner;
    //private StudentItemClickListener studentItemClickListener;


    private ItemClickListner itemClickListner;


    // create a constructor..##
    public ClassRecyclerAdapter(Context context, List<StudentClassCountModel>
            studentClassCountModelList) {
        this.context = context;
        this.studentClassCountModelList = studentClassCountModelList;
        //studentItemClickListner = (StudentItemClickListner) context;
        //this is use to adapter item listener..##
        studentItemListener = (StudentItemListener) context;
        itemClickListner = (ItemClickListner) context;
        //studentItemClickListener = (StudentItemClickListener) context;


    }

    public void clearAll() {
        studentClassCountModelList.clear();
        notifyDataSetChanged();
    }

    public void setListener(StudentListFragment listener) {
       this.itemClickListner = (ItemClickListner) listener;
      // this.studentItemClickListener = (StudentItemClickListener) listener;

    }

    public void addItems(List<StudentClassCountModel> modelList) {
        this.studentClassCountModelList = modelList;

        // what is the work this method.. !
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.student_list_recycler_row,
                parent, false);
        StudentViewHolder holder = new StudentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, final int position) {

        // adapter data set the list..##
        final StudentClassCountModel studentClassCountModel = studentClassCountModelList.get(position);

        //check to null and get data from class count model..##
        if (studentClassCountModel != null) {

            holder.countTv.setText(String.valueOf(studentClassCountModel.classCount));
            holder.nameTv.setText(studentClassCountModel.className);
        }

        // set listener and get model with position..##
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListner.clickListener(studentClassCountModel, position);
                //studentItemClickListener.clickListener(studentClassCountModel, position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return studentClassCountModelList.size();
    }

    // create a student View Holder
    public class StudentViewHolder extends RecyclerView.ViewHolder {

        // row view item..##
        AppCompatTextView nameTv, countTv;
        ConstraintLayout constraintLayout;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.text_name_row);
            countTv = itemView.findViewById(R.id.text_count_row);
            constraintLayout = itemView.findViewById(R.id.constraint_layout_id);
        }
    }

   /* // create a interface for - list adapter to main and StudentModel Record Add Fragment..##
    public interface StudentItemClickListner {
        // abstract method name...##
        void StudentItemClicked(StudentClassCountModel studentClassCountModel);

    }*/



    // danger zone..##
    // create a interface for - list adapter to main and StudentModel Details Fragment..##
    public interface ItemClickListner {
        //abstract method name..##
        void clickListener(StudentClassCountModel model, int position);
    }


}
