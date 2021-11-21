package com.example.mywork.Model;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.mywork.Database;
import com.example.mywork.R;
import com.example.mywork.fragment_main.AlarmReceiver;
import com.example.mywork.fragment_main.fragment_mywork.FragmentMwToDoList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WorkAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Work> listWork;

    FragmentMwToDoList fragmentMwToDoList;

    public Context getContext() {
        return context;
    }

    public WorkAdapter(Context context, int layout, List<Work> listWork, FragmentMwToDoList fragmentMwToDoList) {
        this.context = context;
        this.layout = layout;
        this.listWork = listWork;
        this.fragmentMwToDoList = fragmentMwToDoList;
    }

    @Override
    public int getCount() {
        return listWork.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout, null);

        CheckBox cbWork = convertView.findViewById(R.id.cbWork);
        ImageView btnDeleteWork = convertView.findViewById(R.id.btnDeleteWork);
        ImageView btnUpdateWork = convertView.findViewById(R.id.btnUpdateWork);
        ImageView btnCopyWork = convertView.findViewById(R.id.btnCopyWork);

        Work work = listWork.get(position);

        cbWork.setText(work.getName());
        cbWork.setChecked(work.getStatus() != 0);

        cbWork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fragmentMwToDoList.database.querydata("UPDATE work SET status = '1' WHERE id = '" + work.getId() + "'");
                    if (fragmentMwToDoList.spCategory.getSelectedItem().toString().equals("All")) {
                        fragmentMwToDoList.getAllWorkOther();
                    } else {
                        fragmentMwToDoList.getAllWork();
                    }
                } else {
                    fragmentMwToDoList.database.querydata("UPDATE work SET status = '0' WHERE id = '" + work.getId() + "'");
                    if (fragmentMwToDoList.spCategory.getSelectedItem().toString().equals("All")) {
                        fragmentMwToDoList.getAllWorkOther();
                    } else {
                        fragmentMwToDoList.getAllWork();
                    }
                }
            }
        });

        btnUpdateWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_work_update);
                dialog.setCanceledOnTouchOutside(true);

                EditText edtNameWorkUpdate = dialog.findViewById(R.id.edtNameWorkUpdate);
                EditText edtDesWorkUpdate = dialog.findViewById(R.id.edtDesWorkUpdate);
                CheckBox cbStatusWorkUpdate = dialog.findViewById(R.id.cbStatusWorkUpdate);
                TextView edtTimeWorkUpdate = dialog.findViewById(R.id.edtTimeWorkUpdate);
                Button btnUpdateUpdate = dialog.findViewById(R.id.btnUpdateUpdate);
                TextView tvCancelAlarm = dialog.findViewById(R.id.tvCancelAlarm);
                Spinner spCategoryUpdate = dialog.findViewById(R.id.spCategoryUpdate);

                Database database = new Database(context, "work.DB", null, 1);
                Cursor dataCategory = database.getdata("SELECT * FROM category");
                ArrayList<String> listCategoryUpdate = new ArrayList<>();
                listCategoryUpdate.clear();
                listCategoryUpdate.add("Personal");
                listCategoryUpdate.add("Study");
                listCategoryUpdate.add("Work");
                while (dataCategory.moveToNext()) {
                    listCategoryUpdate.add(dataCategory.getString(1));
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listCategoryUpdate);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCategoryUpdate.setAdapter(arrayAdapter);

                edtNameWorkUpdate.setText(listWork.get(position).getName());
                edtDesWorkUpdate.setText(listWork.get(position).getDescription());
                edtTimeWorkUpdate.setText(listWork.get(position).getTime());

//                if(listWork.get(position).getCategory().equals("Work")) {
//                    spCategoryUpdate.setSelection(0);
//                } else if(listWork.get(position).getCategory().equals("Study")) {
//                    spCategoryUpdate.setSelection(1);
//                } else if(listWork.get(position).getCategory().equals("Personal")) {
//                    spCategoryUpdate.setSelection(2);
//                } else {
                spCategoryUpdate.setSelection(listWork.get(position).getCategoryid());
//                }

                cbStatusWorkUpdate.setChecked(listWork.get(position).getStatus() != 0);

                Calendar c = Calendar.getInstance();
                edtTimeWorkUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.set(Calendar.SECOND, 0);

                                String timeText = "Alarm set for: ";
                                timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
                                edtTimeWorkUpdate.setText(timeText);

                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                        timePickerDialog.show();
                    }
                });

                btnUpdateUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentMwToDoList.cancelAlarm(work.getId());
                        if (edtNameWorkUpdate.getText().toString().isEmpty()) {
                            Toast.makeText(context, "You need to enter the name!", Toast.LENGTH_SHORT).show();
                        } else {
                            int status;
                            if (cbStatusWorkUpdate.isChecked()) {
                                status = 1;
                            } else {
                                status = 0;
                            }
                            fragmentMwToDoList.database.querydata("UPDATE work SET name = '" + edtNameWorkUpdate.getText()
                                    + "', status = '" + status + "', description = '"
                                    + edtDesWorkUpdate.getText() + "', date = '"
                                    + fragmentMwToDoList.monthYearTV.getText() + "', time = '"
                                    + edtTimeWorkUpdate.getText() + "', category = '"
                                    + spCategoryUpdate.getSelectedItem() + "', categoryid = '" + spCategoryUpdate.getSelectedItemPosition() + "' WHERE id = '"
                                    + work.getId() + "'");
                            if (fragmentMwToDoList.spCategory.getSelectedItem().toString().equals("All")) {
                                fragmentMwToDoList.getAllWorkOther();
                            } else {
                                fragmentMwToDoList.getAllWork();
                            }
                            dialog.dismiss();

                            if (edtTimeWorkUpdate.getText().toString().isEmpty()) {

                            } else {
//                                Intent intent = new Intent(getContext(), AlarmReceiver.class);
//                                intent.putExtra("titleOfWork", edtNameWorkUpdate.getText().toString());
                                cancelAlarm(work.getId());
                                startAlarm(c, edtNameWorkUpdate.getText().toString(), work.getId());
//                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                                ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
//                                Intent intent = new Intent(getContext(), AlarmReceiver.class);
//                                intent.putExtra("titleOfWork", edtNameWorkUpdate.getText().toString());
//                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, work.getId(), intent, 0);
//                                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
//                                        c.getTimeInMillis(),
//                                        pendingIntent);
//                                fragmentMwToDoList.intentArray.add(pendingIntent);
                            }
                        }
                        Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();

                        if (edtTimeWorkUpdate.getText().toString().isEmpty()) {
                            fragmentMwToDoList.cancelAlarm(work.getId());
                        }
                    }
                });

                tvCancelAlarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentMwToDoList.cancelAlarm(work.getId());
                        edtTimeWorkUpdate.setText("");
                        Toast.makeText(getContext(), "Canceled alarm!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });

        btnDeleteWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Confirm");
                dialog.setMessage("Are you sure to delete this work?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragmentMwToDoList.database.querydata("DELETE FROM work WHERE id = '" + work.getId() + "'");
                        if (fragmentMwToDoList.spCategory.getSelectedItem().toString().equals("All")) {
                            fragmentMwToDoList.getAllWorkOther();
                        } else {
                            fragmentMwToDoList.getAllWork();
                        }
                        Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                        fragmentMwToDoList.cancelAlarm(work.getId());
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                Dialog dialogCofirmDelete = dialog.create();
                dialogCofirmDelete.show();
            }
        });

        btnCopyWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateCopy;
                        if (dayOfMonth < 10 && month + 1 >= 10) {
                            dateCopy = "0" + dayOfMonth + "/" + (month + 1) + "/" + year;
                        } else if (dayOfMonth >= 10 && month + 1 < 10) {
                            dateCopy = dayOfMonth + "/" + "0" + (month + 1) + "/" + year;
                        } else if (dayOfMonth < 10 && month + 1 < 10) {
                            dateCopy = "0" + dayOfMonth + "/" + "0" + (month + 1) + "/" + year;
                        } else {
                            dateCopy = dayOfMonth + "/" + (month + 1) + "/" + year;
                        }
                        int id = (int) c.getTimeInMillis();
                        fragmentMwToDoList.database.querydata("INSERT INTO work VALUES('" + id + "', '0', '"
                                + work.getName() + "', '"
                                + work.getDescription() + "', '"
                                + dateCopy + "', null, '"
                                + work.getCategory() + "', '" + work.getCategoryid() + "')");
                        Toast.makeText(context, "Copied to " + dateCopy, Toast.LENGTH_SHORT).show();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        return convertView;
    }

    public ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlarm(Calendar c, String titleOfWork, int ID) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        intentArray = new ArrayList<PendingIntent>();
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("titleOfWork", titleOfWork);
//        intent.putExtra("idOfWork", ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), ID, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(),
                pendingIntent);
        intentArray.add(pendingIntent);
    }

    public void cancelAlarm(int ID) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), ID, intent, 0);

        alarmManager.cancel(pendingIntent);
        intentArray.remove(pendingIntent);
    }
}
