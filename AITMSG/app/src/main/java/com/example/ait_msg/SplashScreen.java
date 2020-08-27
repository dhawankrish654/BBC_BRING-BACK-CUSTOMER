package com.example.ait_msg;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SplashScreen extends AppCompatActivity {
    int permissioncode=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        startActivity(new Intent(this,MainActivity.class));


    }
    public Boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return  (check== PackageManager.PERMISSION_GRANTED);

    }
}