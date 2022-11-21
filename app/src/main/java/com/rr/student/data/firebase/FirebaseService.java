package com.rr.student.data.firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rr.student.data.info.StudentData;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FirebaseService implements FirebaseManager {

    private Context context;
    private DatabaseReference mDataBase;

    public FirebaseService() {
        mDataBase = FirebaseDatabase.getInstance().getReference("raiyan").child("student");

    }

    @Override
    public void getData(DataSpanshot callback) {
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<StudentData> list = new ArrayList<>();

                try {
                    for(DataSnapshot child: snapshot.getChildren()) {
                        String roll = child.getKey();
                        String name = child.child("Name").getValue().toString();
                        String reg = child.child("Reg").getValue().toString();
                        String dep = child.child("Dep").getValue().toString();
                        StudentData data = new StudentData(name, roll, reg, dep);
                        list.add(data);
                    }
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }

                callback.onResut(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResut(null);
            }
        });
    }

    @Override
    public void setData(String name, String roll, String reg, String dep, CallBack callback) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Reg", reg);
        map.put("Dep", dep);
        mDataBase.child(roll).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onResut(200);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onResut(400);
            }
        });
    }

    @Override
    public void deleteData(String roll, CallBack callback) {
        mDataBase.child(roll).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onResut(200);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onResut(400);
            }
        });
    }
}
