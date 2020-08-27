package com.example.ait_msg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public void Customer(View view){




        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        final EditText inp=new EditText(MainActivity.this);
        inp.setHint("Enter Password HERE...");
        inp.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);

        builder1.setView(inp);
        builder1.setTitle("Enter Admin Password");

        builder1.setPositiveButton("AUTHENTICATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pass=inp.getText().toString().trim();
                if(pass.charAt(0)=='k' && pass.charAt(1)=='d' && pass.charAt(2)=='i' && pass.charAt(3)=='s' && pass.charAt(4)=='g' && pass.charAt(5)=='r' && pass.charAt(6)=='8' ){
                    startActivity(new Intent(getApplicationContext(), custloginactivity.class));
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "WELCOME ADMIN",
                            Toast.LENGTH_LONG);

                    toast.show();

                    finish();
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "INVALID PASSWORD",
                            Toast.LENGTH_LONG);

                    toast.show();
                    dialog.cancel();
                }
            }
        });
        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder1.setCancelable(true);







        AlertDialog alert11 = builder1.create();
        alert11.show();


        return;
    }





    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }









    public void dealer(View view){
        startActivity(new Intent(getApplicationContext(), dealerloginactivity.class));
        finish();
        return;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        System.out.println(date);



    }
}
