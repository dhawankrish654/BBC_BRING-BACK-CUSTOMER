package com.example.ait_msg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class EditGroup extends AppCompatActivity {
    ListView myListView;

    ArrayList<String> myArrayList= new ArrayList<String>();
    ArrayList<String> myphoneList= new ArrayList<String>();
    ArrayList<String> mydobList= new ArrayList<String>();
    ArrayList<String> mynameList= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_users);
        TextView n=findViewById(R.id.dd);
        n.setText(getIntent().getStringExtra("gname"));
        myListView=findViewById(R.id.listview);

        final ArrayAdapter<String> myArrayAdapter=new ArrayAdapter<String>(EditGroup.this,android.R.layout.simple_list_item_1,myArrayList);




        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS").child(getIntent().getStringExtra("gname"));
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dts: dataSnapshot.getChildren()){
                    Map<String, Object> userMap = (Map<String, Object>) dts.getValue();
                    String num= userMap.get("number").toString();
                    String un=userMap.get("username").toString();
                    myArrayList.add(un);
                    myphoneList.add(num);
                    myListView.setAdapter(myArrayAdapter);

                }
                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditGroup.this);
                        builder1.setMessage("Delete User");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        DatabaseReference del=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS").child(getIntent().getStringExtra("gname")).child(myphoneList.get(position));
                                        del.removeValue();


                                        startActivity(new Intent(getApplicationContext(), GroupListActivity.class));
                                        finish();

                                        dialog.cancel();
                                    }
                                });
                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                startActivity(new Intent(getApplicationContext(), GroupListActivity.class));
                                finish();
                            }
                        });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public  void adduser(View a){



//?? add user wala chod kro abd
Intent intent=new Intent(EditGroup.this,SelectUserFromGroupActivity.class);
intent.putExtra("name",getIntent().getStringExtra("gname"));
startActivity(intent);



    }
    public void deletegroup(View r){

                AlertDialog.Builder builder1 = new AlertDialog.Builder(EditGroup.this);
                builder1.setMessage("Delete Group");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                DatabaseReference del=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS").child(getIntent().getStringExtra("gname"));
                                del.removeValue();
                                Intent k=new Intent(EditGroup.this,GroupListActivity.class);
                                startActivity(k);
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

                AlertDialog alert11 = builder1.create();
                alert11.show();

    }
}
