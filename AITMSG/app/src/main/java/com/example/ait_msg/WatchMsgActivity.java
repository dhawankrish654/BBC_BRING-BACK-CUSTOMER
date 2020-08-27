package com.example.ait_msg;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class WatchMsgActivity extends AppCompatActivity {
    TextView d;
    int low,up;
    public void add(View r){
        String e=d.getText().toString();
        int num=Integer.valueOf(e);
        num=num+1;
        d.setText(String.valueOf(num));

    }

    public void sub(View r){
        String e=d.getText().toString();
        int num=Integer.valueOf(e);
        if(num==0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Days cant be less than 0",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }
        num=num-1;

        d.setText(String.valueOf(num));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.acitvity_main_menu,menu);
        return super.onCreateOptionsMenu(menu);


    }
    public void addnewmsg(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), MsgTemplateActivity.class));
        finish();
    }
    public void msgtemp(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), WatchMsgActivity.class));
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

    public void yo(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), dealeractivity.class));
        finish();
    }
    public void userdelete(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), DeleteUserActivity.class));
        finish();

    }

EditText msg;
    TimePicker timePicker;

ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_msg);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        gettime();

        d=findViewById(R.id.days);
        msg=findViewById(R.id.msg);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        getnum();

        msg.setHint("Enter Msg For Group name-"+getIntent().getStringExtra("GroupName"));




    }

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

    int hr,mn;long idxx;
    private void  getnum(){
progressDialog.show();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY MESSAGES").child(getIntent().getStringExtra("GroupName"));



        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.hide();
                if(dataSnapshot.exists()) {
                    idxx = dataSnapshot.getChildrenCount();
                System.out.println("number save hoga yeaaaaaaaaa   "+idxx);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }





    public void savemsg(View b){
        getnum();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
         hr=timePicker.getHour();
         mn=timePicker.getMinute();}
        if(hr<low || hr > up){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Time Out of Bounds",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }
        else
        {
            int hr=timePicker.getCurrentHour();
            int min=timePicker.getCurrentMinute();
        }
        final String message=msg.getText().toString();
        if(message.length()<1){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Enter appropriate Message",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }
        progressDialog.show();

        final String din=d.getText().toString();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY MESSAGES").child(getIntent().getStringExtra("GroupName")).child(String.valueOf(idxx+1));


mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("msg", message);
        userMap.put("day", din);
        userMap.put("hrs",String.valueOf(hr));
        userMap.put("min",String.valueOf(mn));
        Calendar calendar = Calendar.getInstance();



        mUserDB.updateChildren(userMap);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Msg Saved",
                Toast.LENGTH_SHORT);

        toast.show();
        progressDialog.hide();
        Intent intent=new Intent(WatchMsgActivity.this,ShowMsgActivity.class);

        intent.putExtra("GroupName",getIntent().getStringExtra("GroupName"));
        startActivity(intent);


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});







    }
    private void setAlarm(long time, int req, String name, String day, String msg, String hr, String mn) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);
        i.putExtra("gname",name);
        i.putExtra("msg",msg);
        i.putExtra("day",day);
        i.putExtra("hr",hr);
        i.putExtra("mn",mn);
        int max=6584;
        int min=127;
        if(req==0)
            req=(int)(Math.random() * (max - min + 1) + min);
        req=req*(int)(Math.random() * (max - min + 1) + min)+req*3+98;
        PendingIntent pi = PendingIntent.getBroadcast(this, req*(int)(Math.random() * (max - min + 1) + min), i, 0);
        System.out.println("Alarm for "+req);
        long interval=1*60*1000;
        am.setRepeating(AlarmManager.RTC_WAKEUP, time, 1000*60*1, pi);

        Toast.makeText(this, "Message  scheduled", Toast.LENGTH_SHORT).show();











    }



}


