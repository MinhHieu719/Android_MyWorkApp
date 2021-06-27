package com.example.mywork.fragment_main.fragment_mywork;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mywork.Database;
import com.example.mywork.MainActivity;
import com.example.mywork.Model.Category;
import com.example.mywork.Model.Work;
import com.example.mywork.Model.WorkAdapter;
import com.example.mywork.R;
import com.example.mywork.fragment_main.AlarmReceiver;
import com.example.mywork.fragment_main.CalendarAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMwToDoList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMwToDoList extends Fragment {
    int id = 0;

    private ListView lvWork;
    private List<Work> listWork;
    private WorkAdapter workAdapter;
    public ArrayList<Category> listCategory;
    public ArrayList<String> listCategoryString;

    ImageView btnPrevious, btnNext;
    public TextView monthYearTV, tvFinishCount;
    public Spinner spCategory;

    private LocalDate selectedDate;

    public Database database;
    private ImageButton btnAddWork;

    public static CalendarAdapter calendarAdapter;

    private View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMwToDoList() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentToDoList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMwToDoList newInstance(String param1, String param2) {
        FragmentMwToDoList fragment = new FragmentMwToDoList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mw_to_do_list, container, false);

        btnAddWork = view.findViewById(R.id.btnAddWork);
        lvWork = view.findViewById(R.id.lvWork);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnNext = view.findViewById(R.id.btnNext);
        monthYearTV = view.findViewById(R.id.monthYearTV);
        tvFinishCount = view.findViewById(R.id.tvFinishCount);
        spCategory = view.findViewById(R.id.spCategory);

        spCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), listCategory.get(position).toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy");

        monthYearTV.setText(format.format(calendar.getTime()));

        listWork = new ArrayList<>();
        workAdapter = new WorkAdapter(getActivity(), R.layout.layout_work, listWork, FragmentMwToDoList.this);
        lvWork.setAdapter(workAdapter);

        database = new Database(getActivity(), "work.DB", null, 1);
//        database.querydata("DROP TABLE work");
//        database.querydata("DROP TABLE category");
//        database.querydata("CREATE TABLE IF NOT EXISTS category(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(200))");
        database.querydata("CREATE TABLE IF NOT EXISTS category(id INTEGER PRIMARY KEY, name VARCHAR(200))");
//        database.querydata("CREATE TABLE IF NOT EXISTS work(id INTEGER PRIMARY KEY AUTOINCREMENT, status INTEGER, name VARCHAR(200), description VARCHAR(200), date VARCHAR(200), time VARCHAR(200), category VARCHAR(200))");
        database.querydata("CREATE TABLE IF NOT EXISTS work(id INTEGER PRIMARY KEY, status INTEGER, name VARCHAR(200), description VARCHAR(200), date VARCHAR(200), time VARCHAR(200), category VARCHAR(200), categoryid INTEGER)");

        listCategory = new ArrayList<>();
        listCategoryString = new ArrayList<>();
        Cursor dataCategory = database.getdata("SELECT * FROM category");
        listCategory.clear();
        listCategoryString.clear();
        listCategoryString.add("All");
        listCategoryString.add("Personal");
        listCategoryString.add("Study");
        listCategoryString.add("Work");
        while(dataCategory.moveToNext()) {
            listCategoryString.add(dataCategory.getString(1));
            listCategory.add(new Category(dataCategory.getInt(0), dataCategory.getString(1)));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCategoryString);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(arrayAdapter);

        selectedDate = LocalDate.now();

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spCategory.getSelectedItem().toString().equals("All")) {
                    getAllWorkOther();
                } else {
                    getAllWork();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = selectedDate.minusDays(1);
                monthYearTV.setText(monthYearFromDate(selectedDate));

                if(spCategory.getSelectedItem().toString().equals("All")) {
                    getAllWorkOther();
                } else {
                    getAllWork();
                }
            }
        });

        monthYearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        String months = null;
//                        if(month+1 < 10) {
//                            months = "0" + (month+1);
//                        }
                        String date1s = dayOfMonth + "/" + (month+1) + "/" + year;
                        String date2s = monthYearFromDate(selectedDate);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date1 = null, date2 = null;
                        try {
                            date1 = sdf.parse(date1s);
                            date2 = sdf.parse(date2s);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff = date1.getTime() - date2.getTime();
                        long change = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                        selectedDate = selectedDate.plusDays(change);
                        monthYearTV.setText(monthYearFromDate(selectedDate));
                        if(spCategory.getSelectedItem().toString().equals("All")) {
                            getAllWorkOther();
                        } else {
                            getAllWork();
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = selectedDate.plusDays(1);
                monthYearTV.setText(monthYearFromDate(selectedDate));

                if(spCategory.getSelectedItem().toString().equals("All")) {
                    getAllWorkOther();
                } else {
                    getAllWork();
                }
            }
        });

        btnAddWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_work_add);

                EditText edtNameWorkAdd = dialog.findViewById(R.id.edtNameWorkAdd);
                EditText edtDesWorkAdd = dialog.findViewById(R.id.edtDesWorkAdd);
                TextView edtTimeWorkAdd = dialog.findViewById(R.id.edtTimeWorkAdd);
                Button btnAddWorkAdd = dialog.findViewById(R.id.btnAddWorkAdd);
                Spinner spCategoryAdd = dialog.findViewById(R.id.spCategoryAdd);

                Cursor dataCategory = database.getdata("SELECT * FROM category");
                ArrayList<String> listCategoryAdd = new ArrayList<>();
                listCategoryAdd.clear();
                listCategoryAdd.add("Personal");
                listCategoryAdd.add("Study");
                listCategoryAdd.add("Work");
                while(dataCategory.moveToNext()) {
                    listCategoryAdd.add(dataCategory.getString(1));
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCategoryAdd);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCategoryAdd.setAdapter(arrayAdapter);

                Calendar c = Calendar.getInstance();
                edtTimeWorkAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String [] time_spilt=monthYearTV.getText().toString().split("/");
                                int date=Integer.parseInt(time_spilt[0]);
                                int month=Integer.parseInt(time_spilt[1])-1;
                                int year=Integer.parseInt(time_spilt[2]);
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, date);
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.set(Calendar.SECOND, 0);

                                String timeText = "Alarm set for: ";
                                timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
                                edtTimeWorkAdd.setText(timeText);

                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                        timePickerDialog.show();
                    }
                });

                btnAddWorkAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edtNameWorkAdd.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "You need to enter the name!", Toast.LENGTH_SHORT).show();
                        } else {
                            id = (int) c.getTimeInMillis();
                            database.querydata("INSERT INTO work VALUES('" + id + "', '0', '"
                                    + edtNameWorkAdd.getText().toString() + "', '"
                                    + edtDesWorkAdd.getText().toString() + "', '"
                                    + monthYearTV.getText().toString() + "', '"
                                    + edtTimeWorkAdd.getText().toString() + "', '"
                                    + spCategoryAdd.getSelectedItem().toString() + "', '" + spCategoryAdd.getSelectedItemPosition() + "')");
                            if(spCategory.getSelectedItem().toString().equals("All")) {
                                getAllWorkOther();
                            } else {
                                getAllWork();
                            }
                            dialog.dismiss();

                            if(edtTimeWorkAdd.getText().toString().isEmpty()) {

                            } else {
                                startAlarm(c, edtNameWorkAdd.getText().toString(), id);
                            }
                            Toast.makeText(getContext(), "Added!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });


        if(spCategory.getSelectedItem().toString().equals("All")) {
            getAllWorkOther();
        } else {
            getAllWork();
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy");
        return date.format(formatter);
    }

    public ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlarm(Calendar c, String titleOfWork, int ID) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        intentArray = new ArrayList<PendingIntent>();
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("titleOfWork", titleOfWork);
//        intent.putExtra("idOfWork", ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), ID, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(),
                pendingIntent);
        intentArray.add(pendingIntent);
    }

    public void cancelAlarm(int ID) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), ID, intent, 0);

        alarmManager.cancel(pendingIntent);
        intentArray.remove(pendingIntent);
    }

    public void getAllWorkOther() {
        Cursor dataWork = database.getdata("SELECT * FROM work");
//        String category = spCategory.getSelectedItem().toString();
        listWork.clear();
        while(dataWork.moveToNext()) {
            if(dataWork.getString(4).equals(monthYearTV.getText())) {
                listWork.add(new Work(dataWork.getInt(0),
                        dataWork.getInt(1),
                        dataWork.getString(2),
                        dataWork.getString(3),
                        dataWork.getString(4),
                        dataWork.getString(5),
                        dataWork.getString(6),
                        dataWork.getInt(7)));
            }
        }

        int finishCount = 0;
        for(int i = 0; i < listWork.size(); i++) {
            if(listWork.get(i).getStatus() == 1) {
                finishCount++;
            }
        }
        if(listWork.size() == 0) {
            tvFinishCount.setText("List is empty!");
        } else if(finishCount == listWork.size()) {
            tvFinishCount.setText("Done all the work!");
        } else {
            tvFinishCount.setText("Finished: " + finishCount + "/" + listWork.size());
        }

        for(int i = 0; i < listWork.size(); i++) {
            for(int j = listWork.size()-1; j > i; j--) {
                if(listWork.get(i).getStatus() == 1 && listWork.get(j).getStatus() == 0) {
                    int id1 = listWork.get(i).getId();
                    int status1 = listWork.get(i).getStatus();
                    String name1 = listWork.get(i).getName();
                    String description1 = listWork.get(i).getDescription();
                    String time1 = listWork.get(i).getTime();
                    String category1 = listWork.get(i).getCategory();
                    int categoryid1 = listWork.get(i).getCategoryid();

                    int id2 = listWork.get(j).getId();
                    int status2 = listWork.get(j).getStatus();
                    String name2 = listWork.get(j).getName();
                    String description2 = listWork.get(j).getDescription();
                    String time2 = listWork.get(j).getTime();
                    String category2 = listWork.get(j).getCategory();
                    int categoryid2 = listWork.get(j).getCategoryid();

                    listWork.get(i).setId(id2);
                    listWork.get(i).setStatus(status2);
                    listWork.get(i).setName(name2);
                    listWork.get(i).setDescription(description2);
                    listWork.get(i).setTime(time2);
                    listWork.get(i).setCategory(category2);
                    listWork.get(i).setCategoryid(categoryid2);

                    listWork.get(j).setId(id1);
                    listWork.get(j).setStatus(status1);
                    listWork.get(j).setName(name1);
                    listWork.get(j).setDescription(description1);
                    listWork.get(j).setTime(time1);
                    listWork.get(j).setCategory(category1);
                    listWork.get(j).setCategoryid(categoryid1);

                    break;
                }
            }
        }

        workAdapter.notifyDataSetChanged();
    }

    public void getAllWork() {
        Cursor dataWork = database.getdata("SELECT * FROM work");
//        String category = spCategory.getSelectedItem().toString();
        listWork.clear();
        while(dataWork.moveToNext()) {
            if(dataWork.getString(4).equals(monthYearTV.getText())) {
                if(dataWork.getInt(7) == spCategory.getSelectedItemPosition()-1) {
                    listWork.add(new Work(dataWork.getInt(0),
                            dataWork.getInt(1),
                            dataWork.getString(2),
                            dataWork.getString(3),
                            dataWork.getString(4),
                            dataWork.getString(5),
                            dataWork.getString(6),
                            dataWork.getInt(7)));
                }
            }
        }

        int finishCount = 0;
        for(int i = 0; i < listWork.size(); i++) {
            if(listWork.get(i).getStatus() == 1) {
                finishCount++;
            }
        }
        if(listWork.size() == 0) {
            tvFinishCount.setText("List is empty!");
        } else if(finishCount == listWork.size()) {
            tvFinishCount.setText("Done all the work!");
        } else {
            tvFinishCount.setText("Finished: " + finishCount + "/" + listWork.size());
        }

        for(int i = 0; i < listWork.size(); i++) {
            for(int j = listWork.size()-1; j > i; j--) {
                if(listWork.get(i).getStatus() == 1 && listWork.get(j).getStatus() == 0) {
                        int id1 = listWork.get(i).getId();
                        int status1 = listWork.get(i).getStatus();
                        String name1 = listWork.get(i).getName();
                        String description1 = listWork.get(i).getDescription();
                        String time1 = listWork.get(i).getTime();
                        String category1 = listWork.get(i).getCategory();
                        int categoryid1 = listWork.get(i).getCategoryid();

                        int id2 = listWork.get(j).getId();
                        int status2 = listWork.get(j).getStatus();
                        String name2 = listWork.get(j).getName();
                        String description2 = listWork.get(j).getDescription();
                        String time2 = listWork.get(j).getTime();
                        String category2 = listWork.get(j).getCategory();
                        int categoryid2 = listWork.get(j).getCategoryid();

                        listWork.get(i).setId(id2);
                        listWork.get(i).setStatus(status2);
                        listWork.get(i).setName(name2);
                        listWork.get(i).setDescription(description2);
                        listWork.get(i).setTime(time2);
                        listWork.get(i).setCategory(category2);
                        listWork.get(i).setCategoryid(categoryid2);

                        listWork.get(j).setId(id1);
                        listWork.get(j).setStatus(status1);
                        listWork.get(j).setName(name1);
                        listWork.get(j).setDescription(description1);
                        listWork.get(j).setTime(time1);
                        listWork.get(j).setCategory(category1);
                        listWork.get(j).setCategoryid(categoryid1);

                        break;
                }
            }
        }

        workAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {

        listCategory = new ArrayList<>();
        Cursor dataCategory = database.getdata("SELECT * FROM category");
        listCategory.clear();
        listCategoryString.clear();
        listCategoryString.add("All");
        listCategoryString.add("Personal");
        listCategoryString.add("Study");
        listCategoryString.add("Work");
        while(dataCategory.moveToNext()) {
            listCategoryString.add(dataCategory.getString(1));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCategoryString);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(arrayAdapter);

        calendarAdapter = new CalendarAdapter(FragmentMwToDoList.this);
        if(calendarAdapter.isOnClick) {
            monthYearTV.setText(calendarAdapter.monthyeartv);
        }
        super.onResume();
    }
}