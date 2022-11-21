package com.rr.student.data.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.splashscreen.SplashScreenViewProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.rr.student.data.App;
import com.rr.student.data.R;
import com.rr.student.data.adapter.StudentAdapter;
import com.rr.student.data.databinding.ActivityMainBinding;
import com.rr.student.data.databinding.DialogViewBinding;
import com.rr.student.data.firebase.FirebaseService;
import com.rr.student.data.firebase.FirebaseManager;
import com.rr.student.data.info.StudentData;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DialogViewBinding dialogView;
    private AlertDialog dialog;
    private ArrayList<StudentData> studentData;
    private StudentAdapter adapter;

    private FirebaseManager firebaseManager;
//    private SplashScreen splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_StudentData_NoActionBar);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentData = new ArrayList<>();
        firebaseManager = new FirebaseService();

        adapter = new StudentAdapter(this, studentData);

        binding.recyclerView.setAdapter(adapter);

        ProgressDialog loading = new ProgressDialog(this);
        loading.setTitle("Load Student Data");
        loading.setMessage("please wait few second");
        loading.setCancelable(false);
        loading.show();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(MainActivity.this).create();
                dialogView = DialogViewBinding.inflate(getLayoutInflater());

                dialog.setView(dialogView.getRoot());

                dialogView.studentSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendData(dialogView.studdentName.getText().toString(),
                                dialogView.studdentRoll.getText().toString(),
                                dialogView.studdentReg.getText().toString(),
                                dialogView.studdentDep.getText().toString(),
                                false, 0);
                    }
                });
                dialog.show();
            }
        });


        adapter.setOnItemLongClickListener(new StudentAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AlertDialog.Builder udDialog = new AlertDialog.Builder(MainActivity.this);
                udDialog.setTitle("Update or Delete Data");
                udDialog.setMessage("You can update or delete this data");

                udDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //firebase data update dialog
                        dialog = new AlertDialog.Builder(MainActivity.this).create();
                        dialogView = DialogViewBinding.inflate(getLayoutInflater());

                        dialogView.studdentRoll.setEnabled(false);

                        dialogView.studdentName.setText(studentData.get(position).getName());
                        dialogView.studdentRoll.setText(studentData.get(position).getRoll());
                        dialogView.studdentReg.setText(studentData.get(position).getReg());
                        dialogView.studdentDep.setText(studentData.get(position).getDep());

                        dialog.setView(dialogView.getRoot());

                        dialogView.studentSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendData(dialogView.studdentName.getText().toString(),
                                        dialogView.studdentRoll.getText().toString(),
                                        dialogView.studdentReg.getText().toString(),
                                        dialogView.studdentDep.getText().toString(),
                                        true, position);
                            }
                        });
                        dialog.show();
                    }
                });

                udDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //firebase data delete dialog
                        AlertDialog.Builder dDialog = new AlertDialog.Builder(MainActivity.this);
                        dDialog.setTitle("Confirm Delete Data");
                        dDialog.setMessage("You want delete this data");

                        dDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ProgressDialog loading = new ProgressDialog(MainActivity.this);
                                loading.setTitle("Delete Student Data");
                                loading.setMessage("please wait few second");
                                loading.setCancelable(false);
                                loading.show();

                                firebaseManager.deleteData(studentData.get(position).getRoll(), new FirebaseManager.CallBack() {
                                    @Override
                                    public void onResut(int code) {
                                        if(code == 200) {
                                            if(loading != null && loading.isShowing()) loading.dismiss();

                                            Toast.makeText(getApplicationContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                                            studentData.remove(position);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            if(loading != null && loading.isShowing()) loading.dismiss();
                                            Toast.makeText(getApplicationContext(), "Delete Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });

                        dDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        dDialog.create().show();

                        //end
                    }
                });

                udDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                udDialog.create().show();
            }
        });

        firebaseManager.getData(new FirebaseManager.DataSpanshot() {
            @Override
            public void onResut(ArrayList<StudentData> data) {
                if(loading != null && loading.isShowing()) loading.dismiss();

                studentData.addAll(data);
                adapter.notifyDataSetChanged();
            }
        });

//        splashScreen.setOnExitAnimationListener(new SplashScreen.OnExitAnimationListener() {
//            @Override
//            public void onSplashScreenExit(@NonNull SplashScreenViewProvider splashScreenViewProvider) {
//
//            }
//        });
    }

    public void sendData(String name, String roll, String reg, String dep, boolean update, int position) {
        //hide keyboard
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }

        if(name.equals("") || roll.equals("") || reg.equals("") || dep.equals("")) {
            Toast.makeText(this, "Input Value Empty", Toast.LENGTH_SHORT).show();
        } else if(roll.length() < 5 || 6 < roll.length()){
            Toast.makeText(this, "Invalid Roll", Toast.LENGTH_SHORT).show();
        } else if(reg.length() < 6 || 10 < reg.length()){
            Toast.makeText(this, "Invalid Reg.", Toast.LENGTH_SHORT).show();
        } else if(App.checkInternet()){
            //check has student data and upload data
            if(update || !hasStudentExists(roll)) {

                if(dialog != null && dialog.isShowing()) dialog.dismiss();

                ProgressDialog loading = new ProgressDialog(this);
                loading.setTitle("Student Data Insert");
                loading.setMessage("please wait few second");
                loading.setCancelable(false);
                loading.show();

                firebaseManager.setData(name, roll, reg, dep, new FirebaseManager.CallBack() {
                    @Override
                    public void onResut(int code) {
                        if(code == 200) {
                            if(loading != null && loading.isShowing()) loading.dismiss();

                            if(update) {
                                Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();
                                StudentData data = studentData.get(position);
                                data.setName(name);
                                data.setReg(reg);
                                data.setDep(dep);
                            } else {
                                Toast.makeText(getApplicationContext(), "Data Insert Success", Toast.LENGTH_SHORT).show();
                                StudentData data = new StudentData(name, roll, reg, dep);
                                studentData.add(data);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            if(loading != null && loading.isShowing()) loading.dismiss();
                            Toast.makeText(getApplicationContext(), "Insert Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "This Student Data Already Insert", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasStudentExists(String roll) {
        for(int i=0; i<studentData.size(); i++) {
            if(studentData.get(i).getRoll().equals(roll)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}