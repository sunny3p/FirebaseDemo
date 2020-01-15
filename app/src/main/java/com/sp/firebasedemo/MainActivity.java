package com.sp.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Firebase Realtime Database Implementation
 *Add the dependency for Realtime Database to your app-level build.gradle file:
 *   implementation 'com.google.firebase:firebase-database:19.2.0'
 *
 */

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnChangeListner {
    private Button logout,add, update;
    private EditText name, lName, marks;
    private Student student;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter ;
    ArrayList<Student> data;
    // Instantiate your FirbaseDB
    private DatabaseReference reference;
    //static boolean calledAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.getDatabase();
//        if (!calledAlready)
//        {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//            calledAlready = true;
//        }
        logout = findViewById(R.id.lgtBtn);
        add = findViewById(R.id.buttonAdd);
        update = findViewById(R.id.updateBtn);
        name = findViewById(R.id.editTextName);
        lName = findViewById(R.id.editTextLName);
        marks = findViewById(R.id.editTextMarks);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        recyclerView.setLayoutManager(layoutManager);   // Also StaggeredGridLayoutManager and GridLayoutManager or a custom Layout manager
        data = new ArrayList<>();
        adapter = new RecyclerAdapter(data,this);
        recyclerView.setAdapter(adapter);
        // get a reference of database and create a table of Student Data
        reference = FirebaseDatabase.getInstance().getReference().child("Student Data");
        reference.keepSynced(true);
        clickLogout();
        addData();
        //viewData();
        // calling childEventListener
        reference.addChildEventListener(new StudentChildEventListener());
    }
        public void clickLogout(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to logout user
                FirebaseAuth.getInstance().signOut();
                String msg = "Logout Successful";
                toastMsg(msg);
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });
    }

    // Adding Data to your firebase
    public void addData(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_fname = name.getText().toString();
                String text_lname = lName.getText().toString();
                String text_marks = marks.getText().toString();

                student = new Student(text_fname, text_lname,text_marks);
                if(text_fname.isEmpty()|| text_lname.isEmpty()||text_marks.isEmpty()){
                    String msg = "No data entered";
                    toastMsg(msg);
                }else {
                    // This will push the data to the database
                    FirebaseDatabase.getInstance().getReference().child("Student Data").push().setValue(student);
                }
            }
        });
    }

    // To view data but this will not enable offline capabilities. To enable offline capabilities we need to add a ChildEventListener
    public void viewData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student Data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Student student = snapshot.getValue(Student.class);
                    //String value = String.valueOf(student.getFirst_name());

                    System.out.println(snapshot.getKey());
                   // System.out.println(student.getFirst_name()+ " "+student.getLast_name()+" "+student.getMarks());
                    Student student1 = new Student(student.getFirst_name(),student.getLast_name(),student.getMarks());
                    student1.setKey(snapshot.getKey());
                    data.add(student1);

                }
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void toastMsg(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    // To update data on database from the form when click on update data
    public void updateData(final Student key){
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_fname = name.getText().toString();
                String text_lname = lName.getText().toString();
                String text_marks = marks.getText().toString();
                final Student student = new Student(text_fname,text_lname,text_marks);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Student Data").child(key.getKey());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef()
                                .setValue(student)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        // ...
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        // ...
                                    }
                                });

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("User", databaseError.getMessage());
                    }
                });
            }
        });
    }


    // To place the data from the view to the form when we click on card view where we add the first_name, last_name and marks
    @Override
    public void onUpdateListner(int position) {
        Log.d("Clickk", "onUpdateListner: BUTTON clicked");
        name.setText(data.get(position).getFirst_name());
        lName.setText(data.get(position).getLast_name());
        marks.setText(data.get(position).getMarks());
        updateData(data.get(position));
    }

    //To delete from database
    @Override
    public void onDeleteListner(int position) {
        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student Data");
        Log.i("IndexValue", String.valueOf(data.get(position).getKey()));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query appQuery = ref.child("Student Data").child(data.get(position).getKey());
        appQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@Nullable DataSnapshot dataSnapshot) {
                for (DataSnapshot appSnapshot: dataSnapshot.getChildren()) {
                    appSnapshot.getRef().removeValue();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("onCancelled", "onCancelled", databaseError.toException());
            }
        });
        //reference.removeValue(); // To remove all data
        //data.remove(position); // To remove from the list
        //adapter.notifyDataSetChanged();
    }


    // To enable offline capabilities we have added a child listener and we can now add, update and delete the data to cloud when there is no internet
    class StudentChildEventListener implements ChildEventListener {
        // If we have added a new item
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Student student = dataSnapshot.getValue(Student.class);
            student.setKey(dataSnapshot.getKey());
            data.add(student);
            adapter.notifyDataSetChanged();

        }
        // If we want to update an existing item
        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String key = dataSnapshot.getKey();
            Student updateStudent = dataSnapshot.getValue(Student.class);
            for (Student student: data){
                if(student.getKey().equals(key)){
                    student.setValues(updateStudent);
                    adapter.notifyDataSetChanged();
                    return;
                }
            }


        }
        // If we want to remove an existing item
        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (Student student: data){
                if(student.getKey().equals(key)){
                    data.remove(student);
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("onCancelled", "onCancelled: " + databaseError );

        }
    }

}
