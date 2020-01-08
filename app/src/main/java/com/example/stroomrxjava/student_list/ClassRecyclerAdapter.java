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
    //this is use to adapter item listener.##
    StudentItemListener studentItemListener;


    // create a constructor..##
    public ClassRecyclerAdapter(Context context, List<StudentClassCountModel>
            studentClassCountModelList) {
        this.context = context;
        this.studentClassCountModelList = studentClassCountModelList;
        //studentItemListener = (StudentItemListener) context;
    }

    public void clearAll() {
        studentClassCountModelList.clear();
        notifyDataSetChanged();
    }

    public void setListener(StudentListFragment listener) {
       this.studentItemListener = (StudentItemListener) listener;

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

        if (studentClassCountModel != null) {
            holder.textCount.setText(String.valueOf(studentClassCountModel.getClassCount()));
            holder.textName.setText(studentClassCountModel.getClassName());
        }
        // set listener and get model with position..##
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentItemListener.clickListener(studentClassCountModel, position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return studentClassCountModelList.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textName, textCount;
        ConstraintLayout constraintLayout;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name_row);
            textCount = itemView.findViewById(R.id.text_count_row);
            constraintLayout = itemView.findViewById(R.id.constraint_layout_id);
        }
    }

}
