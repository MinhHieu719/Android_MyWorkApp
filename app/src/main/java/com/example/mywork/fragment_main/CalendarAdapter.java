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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywork.Database;
import com.example.mywork.Model.DateFormat;
import com.example.mywork.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    ArrayList<DateFormat> daysOfMonth;
    Context context;
    FragmentCalendar fragmentCalendar = new FragmentCalendar();

    boolean isOnClick = false;
    int _position;

    public CalendarAdapter(ArrayList<DateFormat> daysOfMonth, Context context) {
        this.daysOfMonth = daysOfMonth;
        this.context = context;
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
                        Toast.makeText(context, daysOfMonth.get(getPosition()).toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if(daysOfMonth.get(getPosition()).toString().equals("null/null")) {

                    } else {
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_calendar_show);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView tvStatisticCalendar = dialog.findViewById(R.id.tvStatisticCalendar);
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
                        dialog.show();
                    }
                    return true;
                }
            });
        }
    }

//    private final ArrayList<String> daysOfMonth;
//    private final OnItemListener onItemListener;
//
//    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
//        this.daysOfMonth = daysOfMonth;
//        this.onItemListener = onItemListener;
//    }
//
//    @NonNull
//    @Override
//    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
//
//        return new CalendarViewHolder(view, onItemListener);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
//        holder.dayOfMonth.setText(daysOfMonth.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return daysOfMonth.size();
//    }
//
//    public interface OnItemListener {
//        void onItemClick(int position, String dayText);
//    }
}
