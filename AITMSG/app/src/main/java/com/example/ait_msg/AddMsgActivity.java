package com.example.ait_msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class AddMsgActivity extends AppCompatActivity {
    EditText msg,title;

    public void savewelcomemsg(View r){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("WELCOME MESSAGE");
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("MSG", msg.getText().toString());
                mUserDB.updateChildren(userMap);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Welcome Msg Updated",
                        Toast.LENGTH_SHORT);

                toast.show();




                startActivity(new Intent(getApplicationContext(), dealeractivity.class));
                finish();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_msg);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Showing Current Welcome Msg",
                Toast.LENGTH_SHORT);

        toast.show();
        msg=findViewById(R.id.msg);

        FirebaseUser crnt=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference show=FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(crnt.getUid()).child("WELCOME MESSAGE");
        show.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("MSG").exists()) {
                    String print = dataSnapshot.child("MSG").getValue().toString();

                    msg.setText(print);
                }
                else{
                    msg.setText("Thanks");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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
