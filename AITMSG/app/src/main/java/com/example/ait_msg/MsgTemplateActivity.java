package com.example.ait_msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;

public class MsgTemplateActivity extends AppCompatActivity {
    EditText days,message;
    ListView myListView;

    ArrayList<String> myArrayList= new ArrayList<String>();
    ArrayList<String> myphoneList= new ArrayList<String>();
    ArrayList<String> mydobList= new ArrayList<String>();




    DatabaseReference mref;
    User userf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_template);

        myListView=findViewById(R.id.listview);
        final ArrayAdapter<String> myArrayAdapter=new ArrayAdapter<String>(MsgTemplateActivity.this,R.layout.row_layout,myArrayList);

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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MsgTemplateActivity.this,ShowMsgActivity.class);
                intent.putExtra("GroupName",myArrayList.get(position));
                startActivity(intent);
            }
        });














    }
    public void userdelete(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), DeleteUserActivity.class));
        finish();

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
}
