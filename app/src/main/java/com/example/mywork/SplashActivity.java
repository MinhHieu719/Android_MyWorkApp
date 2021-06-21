package com.example.mywork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

import com.example.mywork.R;

public class SplashActivity extends AppCompatActivity {
    int haspass;
//    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        SharedPreferences settings = getSharedPreferences("PREFS", 0);
//        password = settings.getString("password", "");

        getSupportActionBar().hide();

        Database database = new Database(this, "pass.DB", null, 1);
//        database.querydata("DROP TABLE pass");
        database.querydata("CREATE TABLE IF NOT EXISTS pass(id INTEGER PRIMARY KEY, haspass INTEGER, namepass VARCHAR(200))");
        Cursor dataPass = database.getdata("SELECT * FROM pass");
        while(dataPass.moveToNext()) {
            haspass = dataPass.getInt(1);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(haspass == 0) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), PassCodeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }
}