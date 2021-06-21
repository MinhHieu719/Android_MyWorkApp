package com.example.mywork.Model;

public class DateFormat {
    String date, monthyear;

    public DateFormat() {
    }

    public DateFormat(String date, String monthyear) {
        this.date = date;
        this.monthyear = monthyear;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonthyear() {
        return monthyear;
    }

    public void setMonthyear(String monthyear) {
        this.monthyear = monthyear;
    }

    @Override
    public String toString() {
        return date + '/' + monthyear;
    }
}
