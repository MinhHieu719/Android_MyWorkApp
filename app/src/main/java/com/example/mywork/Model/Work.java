package com.example.mywork.Model;

import java.util.Date;

public class Work {
    private int id, status;
    private String name;
    private String description;
    private String date;
    private String time;
    private String category;
    private int categoryid;

    public Work(int id, int status, String name, String description, String date, String time, String category, int categoryid) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.category = category;
        this.categoryid = categoryid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }
}
