package com.example.mywork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hanks.passcodeview.PasscodeView;

public class PassCodeActivity extends AppCompatActivity {
    EditText edtEnterPassword;
    Button btnOkPassword;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);

        getSupportActionBar().hide();


        edtEnterPassword = findViewById(R.id.edtEnterPassword);
        btnOkPassword = findViewById(R.id.btnOkPassword);

//        SharedPreferences settings = getSharedPreferences("PREFS", 0);
//        password = settings.getString("password", "");

        Database database = new Database(this, "pass.DB", null, 1);
        Cursor dataPass = database.getdata("SELECT * FROM pass");
        while(dataPass.moveToNext()) {
            password = dataPass.getString(2);
        }

        btnOkPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEnterPassword.getText().toString().equals(password)) {
//                    SharedPreferences settings = getSharedPreferences("PREFS", 0);
//                    SharedPreferences.Editor editor = settings.edit();
//                    editor.putString("password", edtEnterPassword.getText().toString());
//                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PassCodeActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}