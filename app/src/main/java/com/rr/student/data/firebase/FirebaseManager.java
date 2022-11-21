package com.rr.student.data.firebase;

import com.rr.student.data.info.StudentData;

import java.util.ArrayList;

public interface FirebaseManager {

    interface CallBack {
        void onResut(int code);
    }

    interface DataSpanshot {
        void onResut(ArrayList<StudentData> studentData);
    }

    void getData(DataSpanshot callback);

    void setData(String name, String roll, String reg, String dep, CallBack callback);

    void deleteData(String roll, CallBack callback);

}
