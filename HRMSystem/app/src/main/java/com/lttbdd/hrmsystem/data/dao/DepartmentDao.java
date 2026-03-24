package com.lttbdd.hrmsystem.data.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.lttbdd.hrmsystem.data.local.DatabaseHelper;
import com.lttbdd.hrmsystem.data.model.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {

    private final DatabaseHelper db;

    public DepartmentDao(DatabaseHelper db) {
        this.db = db;
    }

    public long insert(Department d) {
        return db.getWritableDatabase().insert(DatabaseHelper.TABLE_DEPARTMENTS, null, toCV(d));
    }

    public int update(Department d) {
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_DEPARTMENTS, toCV(d),
                "id = ?", new String[]{String.valueOf(d.id)});
    }

    public int delete(int id) {
        return db.getWritableDatabase().delete(DatabaseHelper.TABLE_DEPARTMENTS,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Department> getAll() {
        List<Department> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT * FROM departments ORDER BY name ASC", null);
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public Department getById(int id) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT * FROM departments WHERE id=? LIMIT 1", new String[]{String.valueOf(id)});
        Department d = null;
        if (c.moveToFirst()) {
            d = fromCursor(c);
        }
        c.close();
        return d;
    }

    public int count() {
        Cursor c = db.getReadableDatabase().rawQuery("SELECT COUNT (*) FROM departments", null);
        int n = 0;
        if (c.moveToFirst()) {
            n = c.getInt(0);
        }
        c.close();
        return n;
    }

    private ContentValues toCV(Department d) {
        ContentValues cv = new ContentValues();
        cv.put("name", d.name);
        cv.put("description", d.description);
        cv.put("manager_id", d.managerId);
        cv.put("created_at", d.createdAt);
        return cv;
    }

    public static Department fromCursor(Cursor c) {
        Department d = new Department();
        d.id = c.getInt(c.getColumnIndexOrThrow("id"));
        d.name = c.getString(c.getColumnIndexOrThrow("name"));
        d.description = c.getString(c.getColumnIndexOrThrow("description"));
        d.managerId = c.getInt(c.getColumnIndexOrThrow("manager_id"));
        d.createdAt = c.getInt(c.getColumnIndexOrThrow("created_at"));
        return d;
    }
}
