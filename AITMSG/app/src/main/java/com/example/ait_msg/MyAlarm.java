package com.example.ait_msg;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

public class MyAlarm extends BroadcastReceiver  {


    int hrs, Minutes;
    private static final String CHANNEL_ID = "1001";

    @Override
    public void onReceive(Context context, final Intent intent) {
        Calendar cal = Calendar.getInstance();
        hrs = cal.get(Calendar.HOUR_OF_DAY);
        Minutes = cal.get(Calendar.MINUTE);
        System.out.println("alarmtime " + hrs + " " + Minutes);
        NotificationCompat.Builder buildermotif = new NotificationCompat.Builder(context, "2002")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Messages Lodged")
                .setContentText("last sent AT:- " + hrs + ":" + Minutes)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(0, buildermotif.build());


        if (Integer.parseInt(intent.getStringExtra("hr")) == hrs && Integer.parseInt(intent.getStringExtra("mn")) == Minutes)
            sendmsg(intent.getStringExtra("msg"), intent.getStringExtra("gname"), intent.getStringExtra("day"));


    }

    public void sendmsg(final String send, String gname, final String day) {
//      send msg to user her
        System.out.println(gname + " " + day + " " + send);
        final String crntdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS").child(gname);
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
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
