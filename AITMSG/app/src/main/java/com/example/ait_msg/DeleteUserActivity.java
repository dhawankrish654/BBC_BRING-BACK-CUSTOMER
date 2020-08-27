package com.example.ait_msg;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import pl.droidsonroids.gif.GifImageView;

public class DeleteUserActivity extends AppCompatActivity {
    GifImageView gif;EditText num,msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
         num=findViewById(R.id.number);
         msg=findViewById(R.id.msg2);
         String k=getIntent().getStringExtra("Number")+"  "+getIntent().getStringExtra("name");
         num.setText(k);









    }
    public void DELETE(View w){
        EditText num = findViewById(R.id.number);
        String n = getIntent().getStringExtra("Number");
        String h=msg.getText().toString();
        if(h.length()<1){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Invalid Msg",
                    Toast.LENGTH_SHORT);

            toast.show();
            return;
        }



        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager=SmsManager.getSmsManagerForSubscriptionId(0);
            smsManager.sendTextMessage( n,null,h,null,null);
        }
        Toast toast = Toast.makeText(getApplicationContext(),
                "Msg Sent",
                Toast.LENGTH_SHORT);

        toast.show();
        startActivity(new Intent(getApplicationContext(), userlistactivity.class));
        finish();








    }

    public Boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return  (check== PackageManager.PERMISSION_GRANTED);

    }
}
