package com.example.ait_msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class GroupNameActivity extends AppCompatActivity {

    ListView myListView;

    ArrayList<String> myArrayList = new ArrayList<String>();
    ArrayList<String> myphoneList = new ArrayList<String>();
    ArrayList<String> mydobList = new ArrayList<String>();
    EditText gn;User userf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        gn=findViewById(R.id.grpname);




    }
    public void savename(View I){
        String name=gn.getText().toString();
        if(name.length()<1){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Select Another Name",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }
        Intent intent=new Intent(GroupNameActivity.this,SelectUserFromGroupActivity.class);
        intent.putExtra("name",  name);
        startActivity(intent);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.acitvity_main_menu, menu);
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

    public void userdelete(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), DeleteUserActivity.class));
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