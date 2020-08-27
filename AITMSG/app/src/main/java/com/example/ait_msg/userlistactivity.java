package com.example.ait_msg;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class userlistactivity extends AppCompatActivity {


    String welcomemsg;
    ListView myListView;

    ArrayList<String> myArrayList= new ArrayList<String>();
    ArrayList<String> myphoneList= new ArrayList<String>();
    ArrayList<String> mydobList= new ArrayList<String>();
    ArrayList<String> mynameList= new ArrayList<String>();






    DatabaseReference mref;
User userf;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlistactivity);
        myListView=findViewById(R.id.listview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        final ArrayAdapter<String> myArrayAdapter=new ArrayAdapter<String>(userlistactivity.this,R.layout.row_layout,myArrayList);

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



                final ImageView pic = findViewById(R.id.imageView);
                pic.setAlpha((float)0.00);
                final TextView custname = findViewById(R.id.custname);
                final  TextView custphone = findViewById(R.id.custnum);
                final TextView custdob = findViewById(R.id.custdob);
                custdob.setAlpha((float) 0.00);
                custname.setAlpha((float) 0.00);
                custphone.setAlpha((float) 0.00);
                Toast toastpp = Toast.makeText(getApplicationContext(),
                        "Fetching Data From Server",
                        Toast.LENGTH_SHORT);
                toastpp.show();

                final GifImageView gif = findViewById(R.id.gif);
                gif.setAlpha((float) 1.00);



                Timer time = new Timer();
                time.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        gif.setAlpha((float) 0.0);

                        pic.setAlpha((float)1.00);

                        custdob.setAlpha((float) 1.00);
                        custname.setAlpha((float) 1.00);
                        custphone.setAlpha((float) 1.00);
                        custname.setText(myArrayList.get(position));
                        custphone.setText(myphoneList.get(position));
                        custdob.setText(mydobList.get(position));

                    }
                }, 1000);




            }

            });

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(userlistactivity.this);
                builder1.setMessage("Select Your Action");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                 final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY CUSTOMERS").child(myphoneList.get(position));
                                mUserDB.removeValue();
                                startActivity(new Intent(getApplicationContext(), userlistactivity.class));
                                finish();
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "USER DELETED",
                                        Toast.LENGTH_SHORT);

                                toast.show();
                                if(checkPermission(Manifest.permission.SEND_SMS)){
                                    DatabaseReference grpref=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS");
                                    grpref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dts: dataSnapshot.getChildren()){

                                                String gname=dts.getKey();
                                            DatabaseReference dele=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS").child(gname).child(myphoneList.get(position));
                                            dele.removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    SmsManager smsManager=SmsManager.getSmsManagerForSubscriptionId(0);
                                    smsManager.sendTextMessage( myphoneList.get(position),null,"No More Msgs will be sent to you",null,null);
                                }



                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Send Msg",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {





                            Intent intent=new Intent(userlistactivity.this,DeleteUserActivity.class);

                            intent.putExtra("Number",myphoneList.get(position));
                            intent.putExtra("name",myArrayList.get(position));
                            startActivity(intent);






                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true; }

        });


        }









    public Boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return  (check== PackageManager.PERMISSION_GRANTED);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.acitvity_main_menu,menu);
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
    public void addnewmsg(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), MsgTemplateActivity.class));
        finish();
    }
    public void msgtemp(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), WatchMsgActivity.class));
        finish();
    }
    public void userdelete(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), DeleteUserActivity.class));
        finish();

    }
}
