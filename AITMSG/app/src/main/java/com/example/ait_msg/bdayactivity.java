package com.example.ait_msg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import java.util.HashMap;
import java.util.Map;

public class bdayactivity extends AppCompatActivity {
    ListView myListView;

    ArrayList<String> myArrayList= new ArrayList<String>();
    ArrayList<String> myfnameList= new ArrayList<String>();
    ArrayList<String> myfdateList= new ArrayList<String>();
    ArrayList<String> fhrlist= new ArrayList<String>();
    ArrayList<String> fmnlist= new ArrayList<String>();
    ArrayList<String> fmsglist= new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdayactivity);
        //list view to show festive msgs
        myListView=findViewById(R.id.listview);
        final ArrayAdapter<String> myArrayAdapter=new ArrayAdapter<String>(bdayactivity.this,R.layout.row_layout,myArrayList);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference DB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("FESTIVE MSGS");
        DB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                    return;
                for(DataSnapshot dts: dataSnapshot.getChildren()){

                    String fdate=dts.getKey();
                    Map<String,String> userMap =(Map<String, String>) dts.getValue();
                    String fname=userMap.get("fname");
                    System.out.println(dts.getKey());
                    myfnameList.add(fname);
                    myfdateList.add(userMap.get("date"));
                    fhrlist.add(userMap.get("hrs"));
                    fmnlist.add(userMap.get("min"));
                    fmsglist.add(userMap.get("msg"));
                    myArrayList.add(fdate+" "+fname);
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
        AlertDialog.Builder ab=new AlertDialog.Builder(bdayactivity.this);
        LayoutInflater li = LayoutInflater.from(bdayactivity.this);
        final View promptsView = li.inflate(R.layout.dialogue, null);
        ab.setView(promptsView);
        ab.setTitle(myfnameList.get(position)+" MSG");
        TextView daaaate=promptsView.findViewById(R.id.date);
        TextView namef=promptsView.findViewById(R.id.name);
        TextView fmsssg=promptsView.findViewById(R.id.msg);
        daaaate.setText("DATE:-"+myfdateList.get(position)+" & Time:-"+fhrlist.get(position)+":"+fmnlist.get(position));

        fmsssg.setText("Msg:-"+fmsglist.get(position));
        ab.setCancelable(false);
        ab.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference DBxx = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("FESTIVE MSGS").child(myfdateList.get(position));
                DBxx.removeValue();
                dialog.cancel();

                    startActivity(new Intent(getApplicationContext(), bdayactivity.class));
                    finish();



            }
        });
        ab.setNegativeButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        ab.create();
        ab.show();
    }
});









    }
    public void bday(View view){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(bdayactivity.this);
        builder1.setMessage("Select Your Action");
        final  EditText msg=new EditText(bdayactivity.this);
        builder1.setView(msg);
        FirebaseUser crnt= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference show= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(crnt.getUid()).child("BDAY MESSAGE");
        show.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("MSG").exists()) {
                    String print = dataSnapshot.child("MSG").getValue().toString();

                    msg.setText(print);
                }
                else{
                    msg.setText("HAPPY BIRTHDAY");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        builder1.setCancelable(true);
        builder1.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String save=msg.getText().toString();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                final DatabaseReference mU = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("BDAY MESSAGE");
                mU.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("MSG", msg.getText().toString());
                        mU.updateChildren(userMap);
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Birthday Msg Updated",
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    public void festive(View view) {
        startActivity(new Intent(getApplicationContext(), festivemsgadd.class));
        finish();

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), dealeractivity.class));
        finish();
        super.onBackPressed();
    }
}