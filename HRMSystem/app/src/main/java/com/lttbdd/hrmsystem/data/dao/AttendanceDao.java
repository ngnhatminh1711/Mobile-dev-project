package com.lttbdd.hrmsystem.data.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.lttbdd.hrmsystem.data.local.DatabaseHelper;
import com.lttbdd.hrmsystem.data.model.Attendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceDao {

    private final DatabaseHelper db;

    public AttendanceDao(DatabaseHelper db) {
        this.db = db;
    }

    public long insert(Attendance a) {
        return db.getWritableDatabase().insert(DatabaseHelper.TABLE_ATTENDANCE, null, toCV(a));
    }

    public int update(Attendance a) {
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_ATTENDANCE, toCV(a),
                "id = ?", new String[]{String.valueOf(a.id)});
    }

    public int delete(int id) {
        return db.getWritableDatabase().delete(DatabaseHelper.TABLE_ATTENDANCE,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Attendance> getByWeek(int empId, String from, String to) {
        List<Attendance> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT a.*, e.full_name AS emp_name FROM attendance a\n" +
                        "JOIN employees e ON a.employee_id = e.id\n" +
                        "WHERE a.employee_id = ? AND a.date BETWEEN ? AND ? ORDER BY a.date ASC",
                new String[]{String.valueOf(empId), from, to});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public List<Attendance> getByMonth(int empId, String month) {
        List<Attendance> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT a.*, e.full_name AS emp_name FROM attendance a\n" +
                        "JOIN employees e ON a.employee_id = e.id\n" +
                        "WHERE a.employee_id = ? AND a.date LIKE ? ORDER BY a.date DESC",
                new String[]{String.valueOf(empId), month + "%"});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public List<Attendance> getByYear(int empId, String year) {
        List<Attendance> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT a.*, e.full_name AS emp_name FROM attendance a\n" +
                        "JOIN employees e ON a.employee_id = e.id\n" +
                        "WHERE a.employee_id = ? AND a.date LIKE ? ORDER BY a.date DESC",
                new String[]{String.valueOf(empId), year + "%"});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public List<Attendance> getAllByMonth(String month) {
        List<Attendance> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT a.*, e.full_name AS emp_name FROM attendance a\n" +
                        "JOIN employees e ON a.employee_id = e.id\n" +
                        "WHERE a.date LIKE ? ORDER BY a.date ASC",
                new String[]{month + "%"});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public int countWorkingDays(int empId, String month) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM attendance WHERE employee_id=? AND check_in IS NOT NULL AND date LIKE ?",
                new String[]{String.valueOf(empId), month + "%"});
        int n = 0;
        if (c.moveToFirst()) n = c.getInt(0);
        c.close();
        return n;
    }

    public float getTotalOvertimeHours(int empId, String month) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT SUM(total_hours) FROM attendance WHERE employee_id=? AND is_overtime=1 AND date LIKE ?",
                new String[]{String.valueOf(empId), month + "%"});
        float n = 0f;
        if (c.moveToFirst()) n = c.getFloat(0);
        c.close();
        return n;
    }

    private List<Attendance> query(String sql, String[] args) {
        List<Attendance> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(sql, args);
        while (c.moveToNext()) list.add(fromCursor(c));
        c.close();
        return list;
    }

    private ContentValues toCV(Attendance a) {
        ContentValues cv = new ContentValues();
        cv.put("employee_id", a.employeeId);
        cv.put("date", a.date);
        cv.put("check_in", a.checkIn);
        cv.put("check_out", a.checkOut);
        cv.put("total_hours", a.totalHours);
        cv.put("is_overtime", a.isOvertime ? 1 : 0);
        cv.put("note", a.note);
        cv.put("created_at", a.createdAt);
        return cv;
    }

    public static Attendance fromCursor(Cursor c) {
        Attendance a = new Attendance();
        a.id = c.getInt(c.getColumnIndexOrThrow("id"));
        a.employeeId = c.getInt(c.getColumnIndexOrThrow("employee_id"));
        a.date = c.getString(c.getColumnIndexOrThrow("date"));
        a.checkIn = c.getString(c.getColumnIndexOrThrow("check_in"));
        a.checkOut = c.getString(c.getColumnIndexOrThrow("check_out"));
        a.totalHours = c.getFloat(c.getColumnIndexOrThrow("total_hours"));
        a.isOvertime = c.getInt(c.getColumnIndexOrThrow("is_overtime")) == 1;
        a.note = c.getString(c.getColumnIndexOrThrow("note"));
        a.createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
        int idx = c.getColumnIndex("emp_name");
        if (idx != -1) {
            a.employeeName = c.getString(idx);
        }
        return a;
    }
}
