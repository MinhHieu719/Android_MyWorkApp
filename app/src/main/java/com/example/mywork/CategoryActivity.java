package com.example.mywork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mywork.Model.Category;
import com.example.mywork.Model.CategoryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    int id;
    ArrayList<Category> listCategory;
    ListView lvCategory;
    FloatingActionButton btnAddCategory;
    CategoryAdapter categoryAdapter;

    public Database database = new Database(CategoryActivity.this, "work.DB", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_category);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Category setting");

        lvCategory = findViewById(R.id.lvCategory);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        listCategory = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, R.layout.layout_category_setting, listCategory, this);
        lvCategory.setAdapter(categoryAdapter);

//        listCategory.add("category 1");
//        listCategory.add("category 2");
//        listCategory.add("category 3");
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(CategoryActivity.this);
                dialog.setContentView(R.layout.dialog_category_add);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText edtAddCategoryAdd = dialog.findViewById(R.id.edtAddCategoryAdd);
                Button btnAddCategoryAdd = dialog.findViewById(R.id.btnAddCategoryAdd);

                btnAddCategoryAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edtAddCategoryAdd.getText().toString().equals("")) {
                            Toast.makeText(CategoryActivity.this, "Enter name of category!", Toast.LENGTH_SHORT).show();
                        } else {
                            id = 2;
                            getAllCategory();
                            Cursor dataCategory = database.getdata("SELECT * FROM category");
                            while (dataCategory.moveToNext()) {
                                id++;
                            }
                            database.querydata("INSERT INTO category VALUES('" + (id+1) + "', '" + edtAddCategoryAdd.getText().toString() + "')");
                            getAllCategory();
                            categoryAdapter.notifyDataSetChanged();
                            Toast.makeText(CategoryActivity.this, "Added!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

        getAllCategory();
        categoryAdapter.notifyDataSetChanged();
    }

    public void getAllCategory() {

        Cursor dataWork = database.getdata("SELECT * FROM category");
        listCategory.clear();
        while(dataWork.moveToNext()) {
            listCategory.add(new Category(dataWork.getInt(0),
                    dataWork.getString(1)));
        }
        categoryAdapter.notifyDataSetChanged();
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