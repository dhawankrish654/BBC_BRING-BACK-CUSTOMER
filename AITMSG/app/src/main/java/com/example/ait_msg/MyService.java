package com.example.ait_msg;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



    public void sendbday(){
        final String crntdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY CUSTOMERS");
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    return;
                }
                for (DataSnapshot dts : dataSnapshot.getChildren()){
                    Map<String, String> msgdet = (Map<String, String>) dts.getValue();
                    String dayxx = msgdet.get("DOB");
                    final String num=msgdet.get("phone");
                    final String name=msgdet.get("name");
                    String datbday[]=dayxx.split("-");
                    String day=datbday[0];
                    String mm=datbday[1];
                    Calendar cal = Calendar.getInstance();
                    int thisMonth = cal.get(Calendar.MONTH)+1;
                    int thiday= cal.get(Calendar.DATE);

                    System.out.println(day+" "+mm+"           "+thiday+" "+thisMonth);

                        if (Integer.valueOf(day)==thiday && Integer.valueOf(mm)==thisMonth){
                            System.out.println("yaaaaaaaaaaaaaaaaa");

                            if (9 == cal.get(Calendar.HOUR_OF_DAY) && 0 == cal.get(Calendar.MINUTE) && cal.get(Calendar.SECOND) == 0 && cal.get(Calendar.MILLISECOND) < 10) {
// send bday msg

                                FirebaseUser crnt= FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference show= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(crnt.getUid()).child("BDAY MESSAGE");
                                show.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("MSG").exists()) {
                                            String print = dataSnapshot.child("MSG").getValue().toString();


                                            SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(0);
                                            smsManager.sendTextMessage(num, null, "Hi "+name+" "+print , null, null);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }



                        }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);
        sendbday();sendfestive();
        Intent g=new Intent(MyService.this,MyAlarm.class);
if(isNetworkConnected()){
        NotificationCompat.Builder buildermotif = new NotificationCompat.Builder(getApplicationContext(),"2002")
                .setSmallIcon(R.drawable.msgicon)
                .setContentTitle("RUNNING")
                .setContentText("I am running")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_SOUND)




                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


        notificationManager.notify(0, buildermotif.build());}
else
{

    NotificationCompat.Builder buildermotif = new NotificationCompat.Builder(getApplicationContext(),"2002")
            .setSmallIcon(R.drawable.msgicon)
            .setContentTitle("ERROR")
            .setContentText("I am Not able to send Msgs . Please turn on the data Service.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.swiftly))


            .setAutoCancel(true);
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


    notificationManager.notify(0, buildermotif.build());





}





        if (FirebaseAuth.getInstance().getCurrentUser()!= null) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY MESSAGES");
            mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dts : dataSnapshot.getChildren()) {
                        String grpname = dts.getKey();
                        for (DataSnapshot dt : dts.getChildren()) {
                            Map<String, String> msgdet = (Map<String, String>) dt.getValue();
                            String day = msgdet.get("day");
                            String hr = msgdet.get("hrs");
                            String mn = msgdet.get("min");
                            String msg = msgdet.get("msg");
                            int hrs = Integer.valueOf(hr);
                            int mins = Integer.valueOf(mn);
                            System.out.println(grpname + " " + hrs + " " + mins);
                            Calendar cal = Calendar.getInstance();
                            if (hrs == cal.get(Calendar.HOUR_OF_DAY) && mins == cal.get(Calendar.MINUTE) && cal.get(Calendar.SECOND) == 0 && cal.get(Calendar.MILLISECOND) < 10) {
                                sendmsg(msg, grpname, day);

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        else
            System.out.println("No user");









        return super.onStartCommand(intent, flags, startId);


    }

    private void sendfestive() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("FESTIVE MSGS");
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                    return;
                for(DataSnapshot dts: dataSnapshot.getChildren()){

                    String fdate=dts.getKey();
                    Map<String,String> userMap =(Map<String, String>) dts.getValue();
                    String date=userMap.get("date");
                    String msg=userMap.get("msg");
                    String hrs=userMap.get("hrs");
                    String min=userMap.get("min");
                    try {
                        if (daydiff(crntdatexxx, date) == 0){
                            System.out.println("jaaaaaaaaaaaao");
                            sendfestivemsg(msg,hrs,min);

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });















            }

    private void sendfestivemsg(final String msg, String hrs, String min) {





        Calendar cal = Calendar.getInstance();
        if (Integer.valueOf(hrs) == cal.get(Calendar.HOUR_OF_DAY) && Integer.valueOf(min) == cal.get(Calendar.MINUTE) && cal.get(Calendar.SECOND) == 0 && cal.get(Calendar.MILLISECOND) < 10) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS").child("MASTER");
            mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dts : dataSnapshot.getChildren()) {
                        Map<String, Object> userMap = (Map<String, Object>) dts.getValue();
                        String num = userMap.get("number").toString();
                        String un = userMap.get("username").toString();
                        SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(0);
                        smsManager.sendTextMessage(num, null, "Hi " + un + " " + msg, null, null);



                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }










    }


    public void setalarm(String grpname,String msg,String day,String hr,String mn)
    {

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(this, MyAlarm.class);
        i.putExtra("gname",grpname);
        i.putExtra("msg",msg);
        i.putExtra("day",day);
        i.putExtra("hr",hr);
        i.putExtra("mn",mn);
        int max=6584;
        int min=127;
        int    req=(int)(Math.random() * (max - min + 1) + min);
        req=req*(int)(Math.random() * (max - min + 1) + min)+req*3+98;
        PendingIntent pi = PendingIntent.getBroadcast(this, req*(int)(Math.random() * (max - min + 1) + min), i, 0);
        Calendar calendar=Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                calendar.HOUR_OF_DAY, calendar.MINUTE, 0);
        long time=calendar.getTimeInMillis();
        am.setExact(AlarmManager.RTC_WAKEUP, time, pi);



    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),MyService.class);
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }
    @Override
    public void onDestroy(){
        Intent restartServiceIntent = new Intent(getApplicationContext(),MyService.class);
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onDestroy();

    }
    final String crntdatexxx = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    public void sendmsg(final String send, String gname, final String day) {
//      send msg to user her
        System.out.println(gname + " " + day + " " + send);
        final String crntdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS").child(gname);
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dts : dataSnapshot.getChildren()) {
                    Map<String, Object> userMap = (Map<String, Object>) dts.getValue();
                    String num = userMap.get("number").toString();
                    String un = userMap.get("username").toString();
                    String doj = userMap.get("DOJ").toString();

                    System.out.println(doj + " " + crntdate);

                    try {
                        if (daydiff(crntdate, doj) == Long.valueOf(day)) {

                            System.out.println("bhejo msg to " + un + "msg" + send);
                            SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(0);
                            smsManager.sendTextMessage(num, null, "Hi " + un + " " + send, null, null);

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public Long daydiff(String a, String b) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date firstDate = sdf.parse(b);
        Date secondDate = sdf.parse(a);

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }




}
