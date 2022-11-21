package com.rr.student.data.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rr.student.data.databinding.StudentListItemBinding;
import com.rr.student.data.info.StudentData;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<StudentData> studentData;
    private OnItemLongClickListener itemClickListener;

    public StudentAdapter(Context context, ArrayList<StudentData> studentData) {
        this.context = context;
        this.studentData = studentData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StudentListItemBinding binding = StudentListItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.itemBinding.studentItemName.setText("Name: "+studentData.get(position).getName());
        holder.itemBinding.studentItemRoll.setText("Roll: "+studentData.get(position).getRoll());
        holder.itemBinding.studentItemReg.setText("Reg.: "+studentData.get(position).getReg());
        holder.itemBinding.studentItemDep.setText("Dep: "+studentData.get(position).getDep());

    }

    @Override
    public int getItemCount() {
        return studentData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private StudentListItemBinding itemBinding;

        public MyViewHolder(@NonNull StudentListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            this.itemBinding.StudentItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
                    return false;
                }
            });
        }
    }

   public void setOnItemLongClickListener(OnItemLongClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemClick(View view, int position);
    }
}
