package com.lttbdd.hrmsystem.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lttbdd.hrmsystem.data.local.DatabaseHelper;
import com.lttbdd.hrmsystem.data.model.Salary;

import java.util.ArrayList;
import java.util.List;

public class SalaryDao {

    private final DatabaseHelper db;

    public SalaryDao(DatabaseHelper db) {
        this.db = db;
    }

    public long insertOrReplace(Salary s) {
        return db.getWritableDatabase().insertWithOnConflict(
                DatabaseHelper.TABLE_SALARIES, null, toCV(s), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int update(Salary s) {
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_SALARIES, toCV(s),
                "id = ?", new String[]{String.valueOf(s.id)});
    }

    public int delete(int id) {
        return db.getWritableDatabase().delete(DatabaseHelper.TABLE_SALARIES,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public Salary getByMonth(int empId, String month) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT s.*, e.full_name AS emp_name FROM salaries s " +
                        "JOIN employees e ON s.employee_id = e.id " +
                        "WHERE s.employee_id = ? AND s.month = ? LIMIT 1",
                new String[]{String.valueOf(empId), month});
        Salary s = null;
        if (c.moveToFirst()) s = fromCursor(c);
        c.close();
        return s;
    }

    public List<Salary> getHistory(int empId) {
        List<Salary> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT s.*, e.full_name AS emp_name FROM salaries s\n" +
                        "JOIN employees e ON s.employee_id = e.id\n" +
                        "WHERE s.employee_id = ? ORDER BY s.month DESC",
                new String[]{String.valueOf(empId)});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public List<Salary> getAllByMonth(String month) {
        List<Salary> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT s.*, e.full_name AS emp_name FROM salaries s\n" +
                        "JOIN employees e ON s.employee_id = e.id\n" +
                        "WHERE s.month = ? ORDER BY s.full_name ASC",
                new String[]{month});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public List<Salary> getHistoryByEmployee(int empId) {
        List<Salary> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT s.*, e.full_name AS emp_name FROM salaries s\n" +
                        "JOIN employees e ON s.employee_id = e.id\n" +
                        "WHERE s.employee_id = ? ORDER BY s.month DESC",
                new String[]{String.valueOf(empId)});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public float getTotalByMonth(String month) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT SUM(total_salary) FROM salaries WHERE month = ?", new String[]{month});
        float n = 0f;
        if (c.moveToFirst()) {
            n = c.getFloat(0);
        }
        c.close();
        return n;
    }

    private List<Salary> query(String sql, String[] args) {
        List<Salary> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(sql, args);
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    private ContentValues toCV(Salary s) {
        ContentValues cv = new ContentValues();
        cv.put("employee_id", s.employeeId);
        cv.put("month", s.month);
        cv.put("working_days", s.workingDays);
        cv.put("standard_days", s.standardDays);
        cv.put("overtime_hours", s.overtimeHours);
        cv.put("base_salary", s.baseSalary);
        cv.put("bonus", s.bonus);
        cv.put("deduction", s.deduction);
        cv.put("total_salary", s.totalSalary);
        cv.put("note", s.note);
        cv.put("created_by", s.createdBy);
        cv.put("created_at", s.createdAt);
        return cv;
    }

    public static Salary fromCursor(Cursor c) {
        Salary s = new Salary();
        s.id = c.getInt(c.getColumnIndexOrThrow("id"));
        s.employeeId = c.getInt(c.getColumnIndexOrThrow("employee_id"));
        s.month = c.getString(c.getColumnIndexOrThrow("month"));
        s.workingDays = c.getInt(c.getColumnIndexOrThrow("working_days"));
        s.standardDays = c.getInt(c.getColumnIndexOrThrow("standard_days"));
        s.overtimeHours = c.getFloat(c.getColumnIndexOrThrow("overtime_hours"));
        s.baseSalary = c.getFloat(c.getColumnIndexOrThrow("base_salary"));
        s.bonus = c.getFloat(c.getColumnIndexOrThrow("bonus"));
        s.deduction = c.getFloat(c.getColumnIndexOrThrow("deduction"));
        s.totalSalary = c.getFloat(c.getColumnIndexOrThrow("total_salary"));
        s.note = c.getString(c.getColumnIndexOrThrow("note"));
        s.createdBy = c.getInt(c.getColumnIndexOrThrow("created_by"));
        s.createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
        int idx = c.getColumnIndex("emp_name");
        if (idx != -1) {
            s.employeeName = c.getString(idx);
        }
        return s;
    }
}
