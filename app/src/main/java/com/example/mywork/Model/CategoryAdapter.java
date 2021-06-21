package com.example.mywork.Model;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywork.CategoryActivity;
import com.example.mywork.Database;
import com.example.mywork.R;
import com.example.mywork.fragment_main.AlarmReceiver;
import com.example.mywork.fragment_main.fragment_mywork.FragmentMwToDoList;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Category> listCategory;

    CategoryActivity categoryActivity;

    public CategoryAdapter(Context context, int layout, ArrayList<Category> listCategory, CategoryActivity categoryActivity) {
        this.context = context;
        this.layout = layout;
        this.listCategory = listCategory;
        this.categoryActivity = categoryActivity;
    }

    @Override
    public int getCount() {
        return listCategory.size();
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

        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        ImageView btnUpdateCategory = convertView.findViewById(R.id.btnUpdateCategory);
        ImageView btnDeleteCategory = convertView.findViewById(R.id.btnDeleteCategory);

        Category category = listCategory.get(position);

        tvCategory.setText(category.getName());

        btnUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_category_update);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button btnUpdateCategoryUpdate = dialog.findViewById(R.id.btnUpdateCategoryUpdate);
                EditText edtUpdateCategoryUpdate = dialog.findViewById(R.id.edtUpdateCategoryUpdate);

                edtUpdateCategoryUpdate.setText(category.getName());

                btnUpdateCategoryUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edtUpdateCategoryUpdate.getText().toString().isEmpty()) {
                            Toast.makeText(context, "Enter name of category!", Toast.LENGTH_SHORT).show();
                        } else {
                            categoryActivity.database.querydata("UPDATE category SET name = '"+ edtUpdateCategoryUpdate.getText().toString() +"' WHERE id = '" + category.getId() + "'");
                            categoryActivity.getAllCategory();
                            dialog.dismiss();
                            Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("This action will delete all works in category!");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Database database = new Database(context, "work.DB", null, 1);
                        categoryActivity.database.querydata("DELETE FROM category WHERE id = '" + category.getId() + "'");

                        // Set lại giá trị id trong bảng category
                        int categoryid = category.getId();
                        Cursor dataCategory = database.getdata("SELECT * FROM category WHERE id > '" + categoryid + "'");
                        while(dataCategory.moveToNext()) {
                            database.querydata("UPDATE category SET id = '"+ (dataCategory.getInt(0)-1) +"' WHERE id = '" + dataCategory.getInt(0) +"'");
                        }

                        // Xóa works thuộc category
                        Cursor dataWork = database.getdata("SELECT * FROM work WHERE categoryid = '" + category.getId() + "'");
                        while(dataWork.moveToNext()) {
                            cancelAlarm(dataWork.getInt(0));
                        }
                        database.querydata("DELETE FROM work WHERE categoryid = '" + category.getId() + "'");

                        // Set lại categoryid của work
                        Cursor dataWorkUpdate = database.getdata("SELECT * FROM work WHERE categoryid > '" + categoryid + "'");
                        while (dataWorkUpdate.moveToNext()) {
                            database.querydata("UPDATE work SET categoryid = '" + (dataWorkUpdate.getInt(7)-1) + "' WHERE categoryid = '" + dataWorkUpdate.getInt(7) + "'");
                        }

                        categoryActivity.getAllCategory();
                        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
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

        return convertView;
    }

    public void cancelAlarm(int ID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID, intent, 0);

        alarmManager.cancel(pendingIntent);
//        intentArray.remove(pendingIntent);
    }
}
