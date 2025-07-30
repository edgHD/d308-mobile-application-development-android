package com.example.vacationscheduler.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "excursions")
@TypeConverters(Excursion.DateConverter.class)
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Date date;
    private int vacationId;

    public Excursion(int id, String title, Date date, int vacationId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.vacationId = vacationId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getVacationId() {
        return vacationId;
    }
    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public static class DateConverter {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }
        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }
}
