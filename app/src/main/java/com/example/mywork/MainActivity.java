package com.example.mywork;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywork.fragment_main.AlarmReceiver;
import com.example.mywork.fragment_main.NotiReceiver;
import com.example.mywork.fragment_main.ViewPagerMainAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ProgressBar progressBar;
    private android.os.Handler handler;
    private Button btnStart, btnStop;
    private volatile boolean stopThread = false;//
    private EditText edtTimeCountDown;

    FloatingActionButton btnCountdown;

    public ViewPager vpMain;
    public BottomNavigationView bnMain;

    ViewPagerMainAdapter viewPagerMainAdapter;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnCountdown = findViewById(R.id.btnCountDown);

        vpMain = findViewById(R.id.vpMain);
        bnMain = findViewById(R.id.bnMain);
        bnMain.setBackground(null);

        viewPagerMainAdapter = new ViewPagerMainAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpMain.setAdapter(viewPagerMainAdapter);

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bnMain.getMenu().findItem(R.id.menuMyWork).setChecked(true);
                        break;
                    case 1:
                        bnMain.getMenu().findItem(R.id.menuCalendar).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bnMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuMyWork:
                        vpMain.setCurrentItem(0);
                        break;
                    case R.id.menuCalendar:
                        vpMain.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_baseline_done_all_24);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("  My Work");

        btnCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_countdown);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
//                        Toast.makeText(MainActivity.this, "Stop thread!", Toast.LENGTH_SHORT).show();
                        stopThread = true;
                        cancelAlarm();
                    }
                });
                textView = dialog.findViewById(R.id.txt1);
                progressBar = dialog.findViewById(R.id.progressBar);
                btnStart = dialog.findViewById(R.id.btnStart);
                btnStop = dialog.findViewById(R.id.btnStop);
                edtTimeCountDown = dialog.findViewById(R.id.edtTimeCountDown);
                edtTimeCountDown.setInputType(InputType.TYPE_CLASS_NUMBER);

                btnStop.setEnabled(false);

                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edtTimeCountDown.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Enter time!", Toast.LENGTH_SHORT).show();
                        } else {
                            edtTimeCountDown.setEnabled(false);
                            stopThread = false;
                            ProgressThread runnable = new ProgressThread(Integer.parseInt(edtTimeCountDown.getText().toString()));
                            new Thread(runnable).start();
                            btnStart.setEnabled(false);
                            btnStop.setEnabled(true);
                            handler = new Handler() {
                                @Override
                                public void handleMessage(@NonNull Message msg) {
                                    super.handleMessage(msg);
                                    progressBar.setProgress(msg.arg1);
                                    textView.setText("Running...");
                                    if(msg.arg1 == Integer.parseInt(edtTimeCountDown.getText().toString())*60) {
                                        textView.setText("Time up!");
                                        btnStop.setEnabled(false);
                                        btnStart.setEnabled(true);
                                        btnStart.setText("Restart");
                                        cancelAlarm();
                                    }
                                }
                            };
                            startAlarm(Integer.parseInt(edtTimeCountDown.getText().toString())*60*1000);
//                            startAlarm(5000);
                        }
                    }
                });

                btnStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTimeCountDown.setEnabled(true);
                        stopThread = true;
                        btnStart.setEnabled(true);
                        btnStart.setText("Restart");
                        btnStop.setEnabled(false);
                        cancelAlarm();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_setting:
                Intent activityIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(activityIntent);
                return true;
            case R.id.menu_theme_dark:
                Toast.makeText(this, "The function is developing.", Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.menu_theme_light:
                Toast.makeText(this, "The function is developing.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_info:
                Intent infoActivity = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(infoActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ProgressThread implements Runnable {
        int minutes;
        ProgressThread(int minutes) {
            this.minutes = minutes;
        }
        public void run(){
            progressBar.setMax(minutes*60);
            for (int i = 1; i <= minutes*60; i++){
                if(stopThread) {
                    return;
                }
                try {
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.arg1 = i;
                handler.sendMessage(msg);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(long time) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotiReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1234, intent, 0);

        Calendar c = Calendar.getInstance();

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + time, pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotiReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1234, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

//    @Override
//    protected void onDestroy() {
//        stopThread = true;
//        cancelAlarm();
//        super.onDestroy();
//    }

    @Override
    protected void onStop() {
//        Toast.makeText(MainActivity.this, "Stop thread!", Toast.LENGTH_SHORT).show();
        stopThread = true;
        cancelAlarm();
        super.onStop();
    }
}