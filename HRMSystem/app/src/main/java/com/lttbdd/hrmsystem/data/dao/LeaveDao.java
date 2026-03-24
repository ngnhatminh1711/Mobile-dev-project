package com.lttbdd.hrmsystem.data.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.lttbdd.hrmsystem.data.local.DatabaseHelper;
import com.lttbdd.hrmsystem.data.model.Leave;

import java.util.ArrayList;
import java.util.List;

public class LeaveDao {

    private final DatabaseHelper db;

    public LeaveDao(DatabaseHelper db) {
        this.db = db;
    }

    public long insert(Leave l) {
        return db.getWritableDatabase().insert(DatabaseHelper.TABLE_LEAVES, null, toCV(l));
    }

    public int updateStatus(int id, String status, int approvedBy,
                            long approvedAt, String rejectionReason) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        cv.put("approved_by", approvedBy);
        cv.put("approved_at", approvedAt);
        cv.put("rejection_reason", rejectionReason);
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_LEAVES, cv,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public int delete(int id) {
        return db.getWritableDatabase().delete(DatabaseHelper.TABLE_LEAVES,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Leave> getByEmployee(int empId) {
        List<Leave> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT l.*, e.full_name AS emp_name FROM leaves l\n" +
                        "JOIN employees e ON l.employee_id = e.id\n" +
                        "WHERE l.employee_id = ? ORDER BY l.created_at DESC",
                new String[]{String.valueOf(empId)});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public List<Leave> getByEmployeeAndStatus(int empId, String status) {
        List<Leave> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT l.*, e.full_name AS emp_name FROM leaves l\n" +
                        "JOIN employees e ON l.employee_id = e.id\n" +
                        "WHERE l.employee_id = ? AND l.status = ? ORDER BY l.created_at DESC",
                new String[]{String.valueOf(empId), status});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public List<Leave> getPending() {
        List<Leave> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT l.*, e.full_name AS emp_name FROM leaves l\n" +
                        "JOIN employees e ON l.employee_id = e.id\n" +
                        "WHERE l.status = 'PENDING' ORDER BY l.created_at ASC", null);
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public List<Leave> getAll() {
        List<Leave> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT l.*, e.full_name AS emp_name FROM leaves l\n" +
                        "JOIN employees e ON l.employee_id = e.id\n" +
                        "ORDER BY l.created_at DESC", null);
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }


    public int countPending() {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM leaves WHERE status = 'PENDING'", null);
        int n = 0;
        if (c.moveToFirst()) {
            n = c.getInt(0);
        }
        c.close();
        return n;
    }

    private List<Leave> query(String sql, String[] args) {
        List<Leave> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(sql, args);
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    private ContentValues toCV(Leave l) {
        ContentValues cv = new ContentValues();
        cv.put("employee_id", l.employeeId);
        cv.put("from_date", l.fromDate);
        cv.put("to_date", l.toDate);
        cv.put("total_days", l.totalDays);
        cv.put("reason", l.reason);
        cv.put("status", l.status);
        cv.put("rejection_reason", l.rejectionReason);
        cv.put("approved_by", l.approvedBy);
        cv.put("approved_at", l.approvedAt);
        cv.put("created_at", l.createdAt);
        return cv;
    }

    public static Leave fromCursor(Cursor c) {
        Leave l = new Leave();
        l.id = c.getInt(c.getColumnIndexOrThrow("id"));
        l.employeeId = c.getInt(c.getColumnIndexOrThrow("employee_id"));
        l.fromDate = c.getString(c.getColumnIndexOrThrow("from_date"));
        l.toDate = c.getString(c.getColumnIndexOrThrow("to_date"));
        l.totalDays = c.getInt(c.getColumnIndexOrThrow("total_days"));
        l.reason = c.getString(c.getColumnIndexOrThrow("reason"));
        l.status = c.getString(c.getColumnIndexOrThrow("status"));
        l.rejectionReason = c.getString(c.getColumnIndexOrThrow("rejection_reason"));
        l.approvedBy = c.getInt(c.getColumnIndexOrThrow("approved_by"));
        l.approvedAt = c.getLong(c.getColumnIndexOrThrow("approved_at"));
        l.createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
        int idx = c.getColumnIndex("emp_name");
        if (idx != -1) l.employeeName = c.getString(idx);
        return l;
    }
}
