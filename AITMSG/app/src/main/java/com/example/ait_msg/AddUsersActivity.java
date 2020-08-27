package com.example.ait_msg;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AddUsersActivity extends AppCompatActivity {
    final int permissioncode=1;
    String date;
    private EditText dobt;private EditText num,name;


    public void msgtemp(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), WatchMsgActivity.class));
        finish();
    }

    public void save(View view) {
        Date dateInstance = new Date();

        int hours = dateInstance.getHours();
        int min = dateInstance.getMinutes();
        final String savetime=String.valueOf(hours)+":"+String.valueOf(min);

        if(num.getText().toString().length()!=10){
            Toast tos = Toast.makeText(getApplicationContext(),
                    "Invalid Mobile Number",
                    Toast.LENGTH_SHORT);
        tos.show();
           return;

        }
        for (char c : num.getText().toString().toCharArray())
        {
            if (!Character.isDigit(c))
            {
                Toast tos = Toast.makeText(getApplicationContext(),
                        "Invalid Mobile Number",
                        Toast.LENGTH_SHORT);
                tos.show();
                return;}
        }
        



            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY CUSTOMERS").child("+91"+num.getText().toString());

            mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("phone", "+91"+num.getText().toString());
                    userMap.put("name", name.getText().toString());
                    userMap.put("DOB", dobt.getText().toString());
                    userMap.put("DateOFJoin", date);
                    userMap.put("Time",savetime);



                    mUserDB.updateChildren(userMap);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Customer Saved",
                            Toast.LENGTH_SHORT);

                    toast.show();


                    final DatabaseReference mUserDB2= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS").child("MASTER").child("+91"+num.getText().toString());

                    mUserDB2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> userMap = new HashMap<>();

                            userMap.put("username", name.getText().toString());
                            userMap.put("number", "+91"+num.getText().toString());
                            userMap.put("DOJ", date);

                            mUserDB2.updateChildren(userMap);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



















                    if(checkPermission(Manifest.permission.SEND_SMS)){

                        FirebaseUser crnt=FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference show=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(crnt.getUid()).child("WELCOME MESSAGE");
                        show.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String print=dataSnapshot.child("MSG").getValue().toString();
                                print="Hello"+" "+name.getText().toString()+" "+print;

                                SmsManager smsManager=SmsManager.getSmsManagerForSubscriptionId(0);
                                smsManager.sendTextMessage("+91"+num.getText().toString(),null,print,null,null);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }



                    Timer time = new Timer();
                    time.schedule(new TimerTask() {
                        @Override
                        public void run() {


                            startActivity(new Intent(getApplicationContext(), AddUsersActivity.class));
                            finish();
                        }
                    }, 3000);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        num = findViewById(R.id.phonenum);
        name = findViewById(R.id.username);
        dobt = findViewById(R.id.dob);
        if(!checkPermission(Manifest.permission.SEND_SMS)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},permissioncode);
        }

    }
    public Boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return  (check== PackageManager.PERMISSION_GRANTED);

    }
    public void datepick(View view){
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog=new DatePickerDialog(AddUsersActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dobt.setText(day+"-"+(month+1)+"-"+year);
            }
        },year,month,day);
        datePickerDialog.show();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.acitvity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public void yo(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), dealeractivity.class));
        finish();
    }

    public void logout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Toast toast = Toast.makeText(getApplicationContext(),
                "SIGNED OUT",
                Toast.LENGTH_SHORT);

        toast.show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void userdelete(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), DeleteUserActivity.class));
        finish();

    }





    public void addnewmsg(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), MsgTemplateActivity.class));
        finish();
    }
}
