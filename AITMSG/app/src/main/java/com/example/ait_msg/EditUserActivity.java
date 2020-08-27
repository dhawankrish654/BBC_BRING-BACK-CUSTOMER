package com.example.ait_msg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {
    EditText hrs,min,msg;
    ListView myListView;

    ArrayList<String> myArrayList= new ArrayList<String>();
    ArrayList<String> myphoneList= new ArrayList<String>();
    ArrayList<String> mydobList= new ArrayList<String>();
    ArrayList<String> mynameList= new ArrayList<String>();






    DatabaseReference mref;
    User userf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timebasedmsg);

        myListView=findViewById(R.id.listvoew);


        final ArrayAdapter<String> myArrayAdapter=new ArrayAdapter<String>(EditUserActivity.this,R.layout.row_layout,myArrayList);

        userf=new User();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mref= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY CUSTOMERS");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dts: dataSnapshot.getChildren()){

                    userf=dts.getValue(User.class);
                    String name=userf.getName();
                    String phone=userf.getPhone();
                    String dob=userf.getDOB();
                    System.out.println(name+" "+phone+" "+dob);
                    String v=name;
                    mydobList.add(dob);
                    myphoneList.add(phone);
                    myArrayList.add(v);
                    mynameList.add(v);
                    myListView.setAdapter(myArrayAdapter);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(EditUserActivity.this);
            builder1.setMessage("What do you want to edit for:-"+myphoneList.get(position));
            builder1.setCancelable(true);
            builder1.setNegativeButton("Name", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final EditText name=new EditText(EditUserActivity.this);
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(EditUserActivity.this);
                    builder2.setMessage("New name for:-"+myphoneList.get(position));
                    builder2.setCancelable(true);
                    builder2.setView(name);
                    builder2.setNegativeButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY CUSTOMERS").child(myphoneList.get(position));
db.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String naam=name.getText().toString();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", naam);
        db.updateChildren(userMap);
        startActivity(new Intent(getApplicationContext(), dealeractivity.class));
        finish();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
                        }
                    });
                    AlertDialog alert11 = builder2.create();
                    alert11.show();



                }
            });
            AlertDialog alert1 = builder1.create();
            alert1.show();

        }
    });







    }


}