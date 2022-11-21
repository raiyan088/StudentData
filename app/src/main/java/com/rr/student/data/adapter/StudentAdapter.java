package com.rr.student.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rr.student.data.R;
import com.rr.student.data.info.StudentData;

import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter {

    Context context;
    ArrayList<StudentData> studentData;

    public StudentAdapter(Context context, ArrayList<StudentData> studentData) {
        this.context = context;
        this.studentData =studentData;
    }

    @Override
    public int getCount() {
        return studentData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.student_list_item, null);
        }

        final TextView name = view.findViewById(R.id.student_item_name);
        final TextView roll = view.findViewById(R.id.student_item_roll);
        final TextView reg = view.findViewById(R.id.student_item_reg);
        final TextView dep = view.findViewById(R.id.student_item_dep);

        name.setText("Name: "+studentData.get(i).getName());
        roll.setText("Roll: "+studentData.get(i).getRoll());
        reg.setText("Reg.: "+studentData.get(i).getReg());
        dep.setText("Dep: "+studentData.get(i).getDep());

        return view;
    }
}
