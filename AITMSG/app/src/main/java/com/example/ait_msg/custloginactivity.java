package com.example.ait_msg;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class custloginactivity extends AppCompatActivity {
    ArrayList<String> myArrayList= new ArrayList<String>();
    ArrayList<String> myphoneList= new ArrayList<String>();
    ArrayList<String> myidList= new ArrayList<String>();
    ArrayList<String> mylowList= new ArrayList<String>();
    ArrayList<String> myhighList= new ArrayList<String>();

    ListView myListView;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custloginactivity);
        myListView=findViewById(R.id.listview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final ArrayAdapter<String> myArrayAdapter=new ArrayAdapter<String>(custloginactivity.this,R.layout.row_layout,myArrayList);


        DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dts: dataSnapshot.getChildren()) {
                    myidList.add(dts.getKey()) ;
                    Map<String,String> ll=(Map<String,String>)dts.getValue();

                    System.out.println(ll.get("name")+" "+ll.get("phone"));
                    myArrayList.add(ll.get("name"));
                    String h=String.valueOf(ll.get("up"));
                    if(h=="null")
                        myhighList.add("9");
                    else
                        myhighList.add(String.valueOf(ll.get("up")));

                    String l=String.valueOf(ll.get("low"));
                    if(l=="null")
                        mylowList.add("9");
                    else

                    mylowList.add(String.valueOf(ll.get("low")));




                    myphoneList.add(ll.get("phone"));
                    myListView.setAdapter(myArrayAdapter);



                }
                progressDialog.hide();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(custloginactivity.this);
        final TextView inp=new TextView(custloginactivity.this);
        inp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        builder1.setView(inp);
        builder1.setTitle("SELECT YOUR ACTION For "+myArrayList.get(position));

        builder1.setCancelable(true);

        DatabaseReference set=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(myidList.get(position)).child("MY CUSTOMERS");
        set.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cnt=dataSnapshot.getChildrenCount();
                inp.setText("Customer count:-"+cnt+"\n Number:-"+myphoneList.get(position)+"\n Time:-\n from \n"+mylowList.get(position)+"\nTo\n"+myhighList.get(position));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
builder1.setPositiveButton("DELETE USER", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        DatabaseReference set=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(myidList.get(position));
        set.removeValue();
        startActivity(new Intent(getApplicationContext(), custloginactivity.class));
        finish();


    }
});
builder1.setNegativeButton("Set Time Limit", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
//        startActivity(new Intent(getApplicationContext(), timelimit.class));
//        finish();
        timedialog(myidList.get(position));
        dialog.cancel();

    }
});






        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
});
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();


        super.onBackPressed();
    }

    int lowinp;
    private void timedialog(final String id) {

        AlertDialog.Builder ab=new AlertDialog.Builder(custloginactivity.this);
        LayoutInflater li = LayoutInflater.from(custloginactivity.this);
        final View promptsView = li.inflate(R.layout.time_dialogue, null);
        ab.setView(promptsView);
        Button addlow=promptsView.findViewById(R.id.addlow);
        Button sublow=promptsView.findViewById(R.id.sublow);
        final  EditText low=promptsView.findViewById(R.id.low);
        final  EditText high=promptsView.findViewById(R.id.high);
         lowinp=0;
        addlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowinp++;
                if(lowinp>23)
                {
                    lowinp=0;
                }
                low.setText(String.valueOf(lowinp));


            }
        });


        sublow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowinp--;
                if(lowinp<0)
                {
                    lowinp=23;
                }
                low.setText(String.valueOf(lowinp));
            }
        });
        ab.setTitle("Set Time Limit...");
        Button addhigh=promptsView.findViewById(R.id.addhigh);
        Button subhigh=promptsView.findViewById(R.id.subhigh);
          highinp=0;

        addhigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highinp++;
                if(highinp>23)
                {
                    highinp=0;
                }
                high.setText(String.valueOf(highinp));


            }
        });
        subhigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highinp--;
                if(highinp<0)
                {
                  highinp=23;
                }
                high.setText(String.valueOf(highinp));
            }
        });
        ab.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final DatabaseReference setxx=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(id);
                setxx.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> userMap = new HashMap<>();
                        if(lowinp>highinp)
                        {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    " Invalid",
                                    Toast.LENGTH_SHORT);

                            toast.show();
                            return;
                        }
                        userMap.put("low", lowinp);
                        userMap.put("up", highinp);

                        setxx.updateChildren(userMap);
                        startActivity(new Intent(getApplicationContext(), custloginactivity.class));
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "SAVED",
                                Toast.LENGTH_SHORT);

                        toast.show();//fes\tch

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.cancel();



            }
        });







        ab.create();
        ab.show();

    }
int highinp;
    long cnt;
}
