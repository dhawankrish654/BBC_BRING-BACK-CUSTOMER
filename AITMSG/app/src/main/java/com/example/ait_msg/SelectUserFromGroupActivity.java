package com.example.ait_msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SelectUserFromGroupActivity extends AppCompatActivity {
    ListView myListView;

    ArrayList<String> myArrayList= new ArrayList<String>();
    ArrayList<String> myphoneList= new ArrayList<String>();
    ArrayList<String> mydobList= new ArrayList<String>();
    ArrayList<String> mydojList= new ArrayList<String>();


    User userf;int ind;

String numarr[];
String namearr[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_from_group);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Click To add Members to group",
                Toast.LENGTH_SHORT);

        toast.show();

  numarr=new String[1000];
 namearr=new String[1000];
 ind=0;
        TextView name=findViewById(R.id.name);
        name.setText("Select Users For  "+getIntent().getStringExtra("name"));


        myListView=findViewById(R.id.listview);
        final ArrayAdapter<String> myArrayAdapter=new ArrayAdapter<String>(SelectUserFromGroupActivity.this,android.R.layout.simple_list_item_multiple_choice,myArrayList);

        userf=new User();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY CUSTOMERS");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dts: dataSnapshot.getChildren()){

                    userf=dts.getValue(User.class);
                    String name=userf.getName();
                    String phone=userf.getPhone();
                    String dob=userf.getDOB();
                    String doj=userf.getDateOFJoin();
                    String v=name;
                    mydojList.add(doj);
                    mydobList.add(dob);
                    myphoneList.add(phone);
                    myArrayList.add(v);
                    myListView.setAdapter(myArrayAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        SearchView sc=findViewById(R.id.sv);
        sc.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myArrayAdapter.getFilter().filter(newText);
                return false;
            }
        });







          myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                  view.setSelected(true);
final String daterst = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                  final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY GROUPS").child(getIntent().getStringExtra("name")).child(myphoneList.get(position));

                  mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          Map<String, Object> userMap = new HashMap<>();

                          userMap.put("username", myArrayList.get(position));
                          userMap.put("number", myphoneList.get(position));
                          userMap.put("DOJ",daterst);

                          mUserDB.updateChildren(userMap);

                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });



              }
          });





        }
    int a;
 public void create(View t) {



     Toast toast = Toast.makeText(getApplicationContext(),
             "Group Created",
             Toast.LENGTH_SHORT);

     toast.show();
     startActivity(new Intent(getApplicationContext(), dealeractivity.class));
     finish();



 }
}