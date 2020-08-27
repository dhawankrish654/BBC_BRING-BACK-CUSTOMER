package com.example.ait_msg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

public class ShowMsgActivity extends AppCompatActivity {
    ListView myListView;

    ArrayList<String> myArrayList= new ArrayList<String>();
    ArrayList<String> myphoneList= new ArrayList<String>();
    ArrayList<String> mydobList= new ArrayList<String>();Msg msg;
    ArrayList<String> daynumber= new ArrayList<String>();
    ArrayList<String> msgonday= new ArrayList<String>();






    DatabaseReference mref;
    User userf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_msg);
        TextView gn=findViewById(R.id.gn);
        gn.setText("Messages For Group-"+getIntent().getStringExtra("GroupName"));


//        MSGLIST FOR A GROUP
        myListView=findViewById(R.id.listview);
        final ArrayAdapter<String> myArrayAdapter=new ArrayAdapter<String>(ShowMsgActivity.this,R.layout.row_layout,myArrayList);

        userf=new User();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mref= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY MESSAGES").child(getIntent().getStringExtra("GroupName"));
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Edit Msg for a group
       msg=new Msg();
      ms=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY MESSAGES").child(getIntent().getStringExtra("GroupName")).child(myArrayList.get(position));
        ms.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);

                Map<String, String> userMap = (Map)dataSnapshot.getValue();

                daynum=userMap.get("day");
                messa=userMap.get("msg");
                String hr=userMap.get("hrs");
                String min=userMap.get("min");
                daynumber.add(daynum);
                msgonday.add(messa);

                System.out.println(daynum+"   "+messa);
                AlertDialog.Builder builder1=new AlertDialog.Builder(ShowMsgActivity.this);
                builder1.setTitle("Message will be sent on Day number:-"+daynum);
                final EditText input=new EditText(ShowMsgActivity.this);
                builder1.setView(input);
                input.setText(messa+" "+"(Time : "+hr+":"+min+")");
                builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ms.removeValue();

                        Intent intent=new Intent(ShowMsgActivity.this,ShowMsgActivity.class);
                        intent.putExtra("GroupName",getIntent().getStringExtra("GroupName"));
                        startActivity(intent);
                        dialog.cancel();

                    }
                });
                builder1.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Delete this Msg and add a new one.",
                                Toast.LENGTH_SHORT);

                        toast.show();
                        dialog.cancel();
                    }
                });
                AlertDialog alert11 = builder1.create();
                alert11.show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });











    }
});










    }
    String daynum,messa;  DatabaseReference ms;
    public  void  newmsg(View t
    ){

     Intent intent=new Intent(ShowMsgActivity.this,WatchMsgActivity.class);
     intent.putExtra("GroupName",getIntent().getStringExtra("GroupName"));
     startActivity(intent);
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
        startActivity(new Intent(getApplicationContext(), MsgTemplateActivity.class));
        finish();
    }

    public void userdelete(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), DeleteUserActivity.class));
        finish();

    }


}
