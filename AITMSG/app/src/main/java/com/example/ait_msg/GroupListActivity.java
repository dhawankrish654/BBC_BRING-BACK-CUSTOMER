package com.example.ait_msg;

import android.Manifest;
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
import android.widget.EditText;
import android.widget.ListView;
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

public class GroupListActivity extends AppCompatActivity {
    ListView myListView;

    ArrayList<String> myArrayList= new ArrayList<String>();
    ArrayList<String> myphoneList= new ArrayList<String>();
    ArrayList<String> mydobList= new ArrayList<String>();




    DatabaseReference mref;
    User userf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);



        myListView=findViewById(R.id.listview);
        final ArrayAdapter<String> myArrayAdapter=new ArrayAdapter<String>(GroupListActivity.this,R.layout.row_layout,R.id.list_item,myArrayList);

        userf=new User();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mref= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dts: dataSnapshot.getChildren()){

                    String gname=dts.getKey();
                    System.out.println(dts.getKey());
                    myArrayList.add(gname);
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
            AlertDialog.Builder builder1 = new AlertDialog.Builder(GroupListActivity.this);
            builder1.setMessage("Select Your Action");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Edit Group",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            Intent intentggg=new Intent(GroupListActivity.this,EditGroup.class);
                            intentggg.putExtra("gname",myArrayList.get(position));

                            startActivity(intentggg);





                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "Send Msg To Group",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           AlertDialog.Builder builder1=new AlertDialog.Builder(GroupListActivity.this);
                           builder1.setTitle("Enter Your Msg");
                           builder1.setMessage("press send to send the msg");
                            final EditText input=new EditText(GroupListActivity.this);
                            builder1.setView(input);
                            builder1.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String msg=input.getText().toString();
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    DatabaseReference mrefsend= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS").child(myArrayAdapter.getItem(position));

                                    mrefsend.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dts: dataSnapshot.getChildren()){

                                                String num=dts.getKey();
                                                if(checkPermission(Manifest.permission.SEND_SMS)){
                                                    SmsManager smsManager=SmsManager.getSmsManagerForSubscriptionId(0);
                                                    smsManager.sendTextMessage(num,null,msg,null,null);

                                                }



                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });




                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "MSg sned",
                                            Toast.LENGTH_SHORT);

                                    toast.show();
                                    dialog.cancel();
                                }
                            });
                            builder1.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog ad=builder1.create();
                            ad.show();


                            dialog.cancel(); }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();




        }
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
    public void userdelete(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), DeleteUserActivity.class));
        finish();

    }
    public void yo(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), dealeractivity.class));
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
    public void addnewmsg(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), MsgTemplateActivity.class));
        finish();
    }
}
