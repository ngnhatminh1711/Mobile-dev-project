package com.lttbdd.hrmsystem.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lttbdd.hrmsystem.data.local.DatabaseHelper;
import com.lttbdd.hrmsystem.data.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    private final DatabaseHelper db;

    public EmployeeDao(DatabaseHelper db) {
        this.db = db;
    }

    public long insert(Employee e) {
        return db.getWritableDatabase().insert(DatabaseHelper.TABLE_EMPLOYEES, null, toCV(e));
    }

    public void insertAll(List<Employee> list) {
        SQLiteDatabase wdb = db.getWritableDatabase();
        wdb.beginTransaction();
        try {
            for (Employee e : list) {
                wdb.insert(DatabaseHelper.TABLE_EMPLOYEES, null, toCV(e));
                wdb.setTransactionSuccessful();
            }
        } finally {
            wdb.endTransaction();
        }
    }

    public int update(Employee e) {
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_EMPLOYEES, toCV(e),
                "id = ?", new String[]{String.valueOf(e.id)});
    }

    public int softDelete(int id) {
        ContentValues cv = new ContentValues();
        cv.put("is_active", 0);
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_EMPLOYEES, cv,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT e.*, d.name AS dept_name FROM employees e\n" +
                        "LEFT JOIN departments d ON e.department_id = d.id\n" +
                        "WHERE e.is_active = 1 ORDER BY e.full_name ASC", null);
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public Employee getById(int id) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT e.*, d.name AS dept_name FROM employees e\n" +
                        "LEFT JOIN departments d ON e.department_id = d.id\n" +
                        "WHERE e.id = ? LIMIT 1", new String[]{String.valueOf(id)});
        Employee e = null;
        if (c.moveToFirst()) {
            e = fromCursor(c);
        }
        c.close();
        return e;
    }

    public Employee getByUserId(int userId) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT e.*, d.name AS dept_name FROM employees e\n" +
                        "LEFT JOIN departments d ON e.department_id = d.id\n" +
                        "WHERE e.user_id = ? LIMIT 1", new String[]{String.valueOf(userId)});
        Employee e = null;
        if (c.moveToFirst()) {
            e = fromCursor(c);
        }
        c.close();
        return e;
    }

    public List<Employee> getByDepartment(int deptId) {
        List<Employee> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT e.*, d.name AS dept_name FROM employees e\n" +
                        "LEFT JOIN departments d ON e.department_id = d.id\n" +
                        "WHERE e.department_id = ? AND e.is_active = 1 ORDER BY e.full_name ASC",
                new String[]{String.valueOf(deptId)});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public List<Employee> search(String q) {
        String like = "%" + q + "%";
        List<Employee> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT e.*, d.name AS dept_name FROM employees e\n" +
                        "LEFT JOIN departments d ON e.department_id = d.id\n" +
                        "WHERE e.is_active = 1\n" +
                        "AND (e.full_name LIKE ? OR e.email LIKE ? OR e.phone LIKE ?)\n" +
                        "ORDER BY e.full_name ASC",
                new String[]{like, like, like});
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public int countActive() {
        Cursor c = db.getReadableDatabase().rawQuery("SELECT COUNT (*) FROM employees WHERE is_active = 1", null);
        int n = 0;
        if (c.moveToNext()) {
            n = c.getInt(0);
        }
        c.close();
        return n;
    }

    private List<Employee> query(String sql, String[] args) {
        List<Employee> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(sql, args);
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    private ContentValues toCV(Employee e) {
        ContentValues cv = new ContentValues();
        cv.put("user_id", e.id);
        cv.put("full_name", e.fullName);
        cv.put("email", e.email);
        cv.put("phone", e.phone);
        cv.put("position", e.position);
        cv.put("department_id", e.departmentId > 0 ? e.departmentId : null);
        cv.put("gender", e.gender);
        cv.put("date_of_birth", e.dateOfBirth);
        cv.put("join_date", e.joinDate);
        cv.put("base_salary", e.baseSalary);
        cv.put("avatar_path", e.avatarPath);
        cv.put("id_card", e.idCard);
        cv.put("is_active", e.isActive ? 1 : 0);
        cv.put("created_at", e.createdAt);
        return cv;
    }

    public static Employee fromCursor(Cursor c) {
        Employee e = new Employee();
        e.id = c.getInt(c.getColumnIndexOrThrow("id"));
        e.userId = c.getInt(c.getColumnIndexOrThrow("user_id"));
        e.fullName = c.getString(c.getColumnIndexOrThrow("full_name"));
        e.email = c.getString(c.getColumnIndexOrThrow("email"));
        e.phone = c.getString(c.getColumnIndexOrThrow("phone"));
        e.position = c.getString(c.getColumnIndexOrThrow("position"));
        e.departmentId = c.getInt(c.getColumnIndexOrThrow("department_id"));
        e.gender = c.getString(c.getColumnIndexOrThrow("gender"));
        e.dateOfBirth = c.getLong(c.getColumnIndexOrThrow("date_of_birth"));
        e.joinDate = c.getLong(c.getColumnIndexOrThrow("join_date"));
        e.baseSalary = c.getFloat(c.getColumnIndexOrThrow("base_salary"));
        e.avatarPath = c.getString(c.getColumnIndexOrThrow("avatar_path"));
        e.idCard = c.getString(c.getColumnIndexOrThrow("id_card"));
        e.isActive = c.getInt(c.getColumnIndexOrThrow("is_active")) == 1;
        e.createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
        int idx = c.getColumnIndex("dept_name");
        if (idx != -1) {
            e.departmentName = c.getString(idx);
        }
        return e;
    }
}
