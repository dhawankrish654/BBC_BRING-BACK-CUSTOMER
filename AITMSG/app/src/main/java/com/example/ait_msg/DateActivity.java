package com.example.ait_msg;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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
import java.util.Map;

public class DateActivity extends AppCompatActivity {
    TimePicker timePicker;int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();x=0;

        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY MESSAGES");
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dts: dataSnapshot.getChildren()) {
                    String grpname=dts.getKey();
                    for(DataSnapshot dt:dts.getChildren()){
                        Map<String,String> msgdet=(Map<String,String>)dt.getValue();
                        String day=msgdet.get("day");
                        String hr=msgdet.get("hrs");
                        String mn=msgdet.get("min");
                        String msg=msgdet.get("msg");
                        int hrs=Integer.valueOf(hr);
                        int mins=Integer.valueOf(mn);
                        System.out.println(grpname+" "+hrs+" "+mins);


                        Calendar calendar = Calendar.getInstance();
                        if (android.os.Build.VERSION.SDK_INT >= 23) {
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                    hrs, mins, 0);
                        } else {
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                    hrs, mins, 0);
                        }
                        setAlarm(calendar.getTimeInMillis(),x,grpname,day,msg,hr,mn);
                        x=x+1;
                        EditText data=findViewById(R.id.data);
                        data.setText(x+"  messages have been scheduled");




                    }
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    String dbs;

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

        Toast.makeText(this, "Messages are being scheduled", Toast.LENGTH_SHORT).show();











    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.acitvity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public void yo(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), MsgTemplateActivity.class));
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