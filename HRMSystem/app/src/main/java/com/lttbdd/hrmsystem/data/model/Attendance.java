package com.lttbdd.hrmsystem.data.model;

public class Attendance {
    public int id;
    public int employeeId;
    public String employeeName;
    public String date;
    public String checkIn;
    public String checkOut;
    public float totalHours;
    public boolean isOvertime;
    public String note;
    public long createdAt;
}
