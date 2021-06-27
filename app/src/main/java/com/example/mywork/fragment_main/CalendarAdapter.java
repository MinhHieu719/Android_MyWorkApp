package com.example.mywork.fragment_main;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.mywork.Database;
import com.example.mywork.MainActivity;
import com.example.mywork.Model.DateFormat;
import com.example.mywork.R;
import com.example.mywork.fragment_main.fragment_mywork.FragmentMwToDoList;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    ArrayList<DateFormat> daysOfMonth;
    Context context;

    FragmentMwToDoList fragmentMwToDoList;
    FragmentCalendar fragmentCalendar = new FragmentCalendar();
    public static String monthyeartv = "";

    public static boolean isOnClick = false;
    int _position;

    public CalendarAdapter(ArrayList<DateFormat> daysOfMonth, Context context) {
        this.daysOfMonth = daysOfMonth;
        this.context = context;
    }

    public CalendarAdapter(FragmentMwToDoList fragmentMwToDoList) {
        this.fragmentMwToDoList = fragmentMwToDoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.layout_calendar, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);

        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        String today;
        today = fragmentCalendar.dateMontnYearFromDate(LocalDate.now());
        if(today.equals(daysOfMonth.get(position).toString())) {
            holder.dayOfMonth.setText(daysOfMonth.get(position).getDate());
            holder.dayOfMonth.setBackgroundColor(R.color.purple_200);
//            holder.dayOfMonth.getResources().getColor(R.color.purple_200);
        } else {
            holder.dayOfMonth.setText(daysOfMonth.get(position).getDate());
        }
        if(isOnClick) {
            if(_position == position) {
            holder.dayOfMonth.setBackgroundColor(R.color.teal_200);
            } else {
                holder.dayOfMonth.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfMonth;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.tvCellDay);

            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    if(daysOfMonth.get(getPosition()).toString().equals("null/null")) {

                    } else {
                        isOnClick = true;
                        _position = getPosition();
                        notifyDataSetChanged();
                        ViewHolder holder = CalendarAdapter.ViewHolder.this;
                        onBindViewHolder(holder, _position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if(daysOfMonth.get(getPosition()).toString().equals("null/null")) {

                    } else {
                        isOnClick = true;
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_calendar_show);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView tvStatisticCalendar = dialog.findViewById(R.id.tvStatisticCalendar);
                        TextView tvChoose = dialog.findViewById(R.id.tvChoose);
                        Button btnDetailShow = dialog.findViewById(R.id.btnDetailShow);

                        tvChoose.setText(daysOfMonth.get(getPosition()).toString());

                        int total = 0, finish = 0, unfinish = 0;
                        Database database = new Database(context, "work.DB", null, 1);
                        Cursor dataWork = database.getdata("SELECT * FROM work");
                        while(dataWork.moveToNext()) {
                            if(dataWork.getString(4).equals(daysOfMonth.get(getPosition()).toString())) {
                                total++;
                                if(dataWork.getInt(1) == 1) {
                                    finish++;
                                } else {
                                    unfinish++;
                                }
                            }
                        }
                        if(total == 0) {
                            tvStatisticCalendar.setText("No work today!");
                        } else {
                            tvStatisticCalendar.setText("Finish: " + finish + " / Unfinish: " + unfinish);
                        }

                        btnDetailShow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                monthyeartv = daysOfMonth.get(getPosition()).toString();
                                if(context instanceof MainActivity) {
                                    dialog.dismiss();
                                    ((MainActivity) context).vpMain.setCurrentItem(0);
                                }
                            }
                        });

                        dialog.show();
                    }
                    return true;
                }
            });
        }
    }
}
