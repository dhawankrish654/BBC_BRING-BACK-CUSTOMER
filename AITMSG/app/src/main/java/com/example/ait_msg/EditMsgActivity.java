package com.example.ait_msg;

import android.content.Intent;
import android.os.Bundle;
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

public class EditMsgActivity extends AppCompatActivity {
    EditText days,text;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_msg);

    }

    public void savemsg(View r){

        final String msg=text.getText().toString();
        final String d=days.getText().toString();
        if(msg.length()<1 || d.length()<1 || Integer.valueOf(d)==0)
        {
            Toast toasty = Toast.makeText(getApplicationContext(),
                    "Please Enter the Msg and Days",
                    Toast.LENGTH_SHORT);

            toasty.show();
            return;
        }

        Toast toast = Toast.makeText(getApplicationContext(),
                "Message Saved",
                Toast.LENGTH_SHORT);

        toast.show();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("SHOPKEEPER").child(user.getUid()).child("MY MEESAGES").child(d);
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("msg", msg);
                userMap.put("days", d);
                mUserDB.updateChildren(userMap);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Message Template saved",
                        Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        startActivity(new Intent(getApplicationContext(), WatchMsgActivity.class));
        finish();


    }

}
