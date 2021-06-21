package com.example.mywork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
//    CheckBox cbHasPass;
//    EditText edtSetPass;
//    Button btnSetPass;

//    int hasPass;

    Button btnPassSetting, btnCategorySetting;

//    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Setting");

        btnPassSetting = findViewById(R.id.btnPassSetting);
        btnCategorySetting = findViewById(R.id.btnCategorySetting);

//        cbHasPass = findViewById(R.id.cbHasPass);
//        edtSetPass = findViewById(R.id.edtSetPass);
//        btnSetPass = findViewById(R.id.btnSetPass);

//        SharedPreferences settings = getSharedPreferences("PREFS", 0);
//        password = settings.getString("password", "");

        Database database = new Database(this, "pass.DB", null, 1);

        btnPassSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(SettingActivity.this);
                dialog.setContentView(R.layout.dialog_setting_password);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                CheckBox cbHasPass = dialog.findViewById(R.id.cbHasPass);
                EditText edtSetPass = dialog.findViewById(R.id.edtSetPass);
                Button btnSetPassSet = dialog.findViewById(R.id.btnSetPassSet);
//                edtSetPass.setInputType(InputType.TYPE_CLASS_NUMBER);
//                edtSetPass.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);

                Cursor dataPass = database.getdata("SELECT * FROM pass");

                while(dataPass.moveToNext()) {
                    if(dataPass.getInt(1) == 1) {
                        cbHasPass.setChecked(true);
                        edtSetPass.setEnabled(true);
                    } else {
                        cbHasPass.setChecked(false);
                        edtSetPass.setEnabled(false);
                    }

                    edtSetPass.setText(dataPass.getString(2));
                }

                cbHasPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked == true) {
                            edtSetPass.setEnabled(true);
                        } else {
                            edtSetPass.setEnabled(false);
                            edtSetPass.setText("");
                        }
                    }
                });
//                if(!cbHasPass.isChecked()) {
//                    edtSetPass.setEnabled(false);
//                }
//                cbHasPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if(isChecked) {
//                            edtSetPass.setEnabled(true);
//
//                        } else {
//                            edtSetPass.setEnabled(false);
//                            edtSetPass.setText("");
//
//                        }
//                    }
//                });
//
//                if(!password.equals("")) {
//                    cbHasPass.setChecked(true);
//                    edtSetPass.setText(password);
//
//                } else {
//                    cbHasPass.setChecked(false);
//                    edtSetPass.setText("");
//
//                }

                btnSetPassSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if(!cbHasPass.isChecked()) {
//                            SharedPreferences settings = getSharedPreferences("PREFS", 0);
//                            SharedPreferences.Editor editor = settings.edit();
//                            editor.putString("password", "");
//                            editor.apply();
//        //                    Toast.makeText(SettingActivity.this, "Canceled password!", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(SettingActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                        else {
//                            if(edtSetPass.getText().toString().equals("")) {
//                                Toast.makeText(SettingActivity.this, "No password entered!", Toast.LENGTH_SHORT).show();
//                            } else {
//                                SharedPreferences settings = getSharedPreferences("PREFS", 0);
//                                SharedPreferences.Editor editor = settings.edit();
//                                editor.putString("password", edtSetPass.getText().toString());
//                                editor.apply();
//                                dialog.dismiss();
//                                Toast.makeText(SettingActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
                        int hasPass = 0;
                        if(cbHasPass.isChecked()) {
                            hasPass = 1;
                            if(edtSetPass.getText().toString().equals("")) {
                                Toast.makeText(SettingActivity.this, "Enter pass!", Toast.LENGTH_SHORT).show();
                            } else {
                                database.querydata("INSERT INTO pass VALUES(null, '" + hasPass + "', '" + edtSetPass.getText().toString() + "')");
                                database.querydata("UPDATE pass SET haspass = '"+ hasPass +"', namepass = '" + edtSetPass.getText().toString() + "' WHERE id = '1'");
                                dialog.dismiss();
                                Toast.makeText(SettingActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            database.querydata("INSERT INTO pass VALUES(null, '" + hasPass + "', '" + edtSetPass.getText().toString() + "')");
                            database.querydata("UPDATE pass SET haspass = '"+ hasPass +"', namepass = '" + edtSetPass.getText().toString() + "' WHERE id = '1'");
                            dialog.dismiss();
                            Toast.makeText(SettingActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        btnCategorySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

//        if(!password.equals("")) {
//            cbHasPass.setChecked(true);
//            edtSetPass.setText(password);
//        }

//        btnSetPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!cbHasPass.isChecked()) {
//                    SharedPreferences settings = getSharedPreferences("PREFS", 0);
//                    SharedPreferences.Editor editor = settings.edit();
//                    editor.putString("password", "");
//                    editor.apply();
////                    Toast.makeText(SettingActivity.this, "Canceled password!", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    if(edtSetPass.getText().toString().equals("")) {
////                        Toast.makeText(SettingActivity.this, "No password entered!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putString("password", edtSetPass.getText().toString());
//                        editor.apply();
//                    }
//                }
//                Toast.makeText(SettingActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}