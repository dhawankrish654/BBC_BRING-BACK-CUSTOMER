package com.example.ait_msg;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class dealeractivity extends AppCompatActivity {
    String arr[][];int c;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.acitvity_main_menu,menu);
        arr=new String[2][10];c=0;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();





        return super.onCreateOptionsMenu(menu);








    }



    DatabaseReference ref;
    TextView prnt;
    public Boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return  (check== PackageManager.PERMISSION_GRANTED);

    }
    String CHANNEL_ID="2002";
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    int permissioncode=0;ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();

        Calendar calender = Calendar.getInstance();
        int hrs=calender.get(Calendar.HOUR_OF_DAY);
        int Minutes=calender.get(Calendar.MINUTE)-1;

        calender.set(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH),
                hrs, Minutes, 0);
        long h=calender.getTimeInMillis();
        setContentView(R.layout.activity_dealeractivity);
        if(!checkPermission(Manifest.permission.FOREGROUND_SERVICE)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.FOREGROUND_SERVICE},permissioncode);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(),MyService.class));
        }
        else
            startService(new Intent(getApplicationContext(),MyService.class));


        prnt=findViewById(R.id.textView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String dealerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println(dealerId);



        ref= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(dealerId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String shopname=dataSnapshot.child("name").getValue().toString();
                prnt.setText("WECLOME TO "+shopname);
                progressDialog.hide();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void add(View view)
    {
        Timer time=new Timer();

        time.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), AddUsersActivity.class));
                finish();
            }
        },3000);
    }
    public void msg(View view)
    {
        Timer time=new Timer();

        time.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), AddMsgActivity.class));
                finish();
            }
        },3000);
    }
public void UserList(View x){
    Timer time=new Timer();

    time.schedule(new TimerTask() {
        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(), userlistactivity.class));
            finish();
        }
    },3000);

}

    public void yo(MenuItem item) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "LogOut to go back",
                Toast.LENGTH_SHORT);

        toast.show();
    }
    public void logout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        stopService(new Intent(this, MyService.class));
        Toast toast = Toast.makeText(getApplicationContext(),
                "SIGNED OUT",
                Toast.LENGTH_SHORT);

        toast.show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    public void web(View view){
        Timer time=new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.iloveimpact.com/"));
                startActivity(viewIntent);
            }
        },3000);

    }

    public void addnewmsg(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), MsgTemplateActivity.class));
        finish();
    }
    public void msgtemp(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), WatchMsgActivity.class));
        finish();
    }
    public void userdelete(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), DateActivity.class));
        finish();

    }
    public void Group(View r){
        startActivity(new Intent(getApplicationContext(), GroupNameActivity.class));
        finish();
    }
    public void  grouplist(View w){

        startActivity(new Intent(getApplicationContext(), GroupListActivity.class));
        finish();
    }
    public void  useredit(View w){

        startActivity(new Intent(getApplicationContext(), EditUserActivity.class));
        finish();
    }


    public void bday(View view) {

        startActivity(new Intent(getApplicationContext(), bdayactivity.class));
        finish();

    }
}
