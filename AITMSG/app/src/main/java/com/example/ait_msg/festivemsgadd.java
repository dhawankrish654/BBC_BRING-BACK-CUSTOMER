package com.example.ait_msg;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class festivemsgadd extends AppCompatActivity {
TimePicker timePicker;int hr,mn;
EditText fname,msg;int low,up;

    private void gettime() {
        progressDialog.show();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid());
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> ll=(Map<String,String>)dataSnapshot.getValue();
                String h=String.valueOf(ll.get("up"));
                if(h=="null")
                    up=9;
                else
                    up=Integer.valueOf(h.trim());
                String l=String.valueOf(ll.get("low"));
                if(l=="null")
                    low=21;
                else
                    low=Integer.valueOf(l.trim());
                progressDialog.hide();
                TextView limit=findViewById(R.id.limit);
                limit.setText("Time Limit "+low+" - "+up);
                limit.setTextColor(getResources().getColor(R.color.black));


                Toast toast = Toast.makeText(getApplicationContext(),
                        "Time Limit "+low+" - "+up,
                        Toast.LENGTH_LONG);

                toast.show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), bdayactivity.class));
        finish();
        super.onBackPressed();
    }
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festivemsgadd);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        gettime();
        dtae=findViewById(R.id.daate);
        fname=findViewById(R.id.fname);
        msg=findViewById(R.id.msg);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
    }
    EditText dtae;

    public void dateset(View view) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {



                        Toast toast = Toast.makeText(getApplicationContext(),
                                dayOfMonth + "-" + (monthOfYear + 1) + "-" + year,
                                Toast.LENGTH_SHORT);

                        toast.show();
                        dtae.setText( year + "-" + (monthOfYear + 1)+"-" +dayOfMonth );


                    }
                },mYear,mMonth,mDay);
        datePickerDialog.show();
    }

    public void saved(View view) {

        hr=timePicker.getHour();
        mn=timePicker.getMinute();
        if(hr<low || hr > up)
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Time Out of Bounds",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }
        final String festival=fname.getText().toString();
        if(festival.length()<1 || msg.getText().toString().length()<1){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Enter appropriate Message and Name",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("FESTIVE MSGS").child(dtae.getText().toString());
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("msg", msg.getText().toString().trim());
                userMap.put("date", dtae.getText().toString());
                userMap.put("hrs",String.valueOf(hr));
                userMap.put("min",String.valueOf(mn));
                userMap.put("fname",fname.getText().toString());
                Calendar calendar = Calendar.getInstance();



                mUserDB.updateChildren(userMap);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Msg Saved",
                        Toast.LENGTH_SHORT);

                toast.show();
                startActivity(new Intent(getApplicationContext(), bdayactivity.class));
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }
}
