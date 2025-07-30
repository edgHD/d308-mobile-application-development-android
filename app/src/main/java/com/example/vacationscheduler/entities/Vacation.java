package com.example.vacationscheduler.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "vacations")
@TypeConverters(Vacation.DateConverter.class)
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String lodging;
    private Date startDate;
    private Date endDate;

    public Vacation(int id, String title, String lodging, Date startDate, Date endDate) {
        this.id = id;
        this.title = title;
        this.lodging = lodging;
        this.startDate = startDate;
        this.endDate = endDate;
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
    public String getLodging() {
        return lodging;
    }
    public void setLodging(String lodging) {
        this.lodging = lodging;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
