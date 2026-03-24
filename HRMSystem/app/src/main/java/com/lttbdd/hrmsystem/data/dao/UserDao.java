package com.lttbdd.hrmsystem.data.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.lttbdd.hrmsystem.data.local.DatabaseHelper;
import com.lttbdd.hrmsystem.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final DatabaseHelper db;

    public UserDao(DatabaseHelper db) {
        this.db = db;
    }

    public long insert(User u) {
        return db.getWritableDatabase().insert(DatabaseHelper.TABLE_USERS, null, toCV(u));
    }

    public int update(User u) {
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_USERS, toCV(u),
                "id = ?", new String[]{String.valueOf(u.id)});
    }

    public int updateRole(int userId, String role) {
        ContentValues cv = new ContentValues();
        cv.put("role", role);
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_USERS, cv,
                "id = ?", new String[]{String.valueOf(userId)});
    }

    public int setActive(int userId, boolean active) {
        ContentValues cv = new ContentValues();
        cv.put("is_active", active ? 1 : 0);
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_USERS, cv,
                "id = ?", new String[]{String.valueOf(userId)});
    }

    public int updateProfile(int id, String fullName, String phone, String avatarPath) {
        ContentValues cv = new ContentValues();
        cv.put("full_name", fullName);
        cv.put("phone", phone);
        cv.put("avatar_path", avatarPath);
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_USERS, cv,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public int updatePassword(int id, String password) {
        ContentValues cv = new ContentValues();
        cv.put("password", password);
        return db.getWritableDatabase().update(DatabaseHelper.TABLE_USERS, cv,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public User login(String email, String password) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT * FROM users WHERE email and password=? AND is_active=1 LIMIT 1",
                new String[]{email, password});
        User u = null;
        if (c.moveToFirst()) {
            u = fromCursor(c);
        }
        c.close();
        return u;
    }

    public User findByEmail(String email) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT * FROM users WHERE email=? LIMIT 1", new String[]{email});
        User u = null;
        if (c.moveToFirst()) {
            u = fromCursor(c);
        }
        c.close();
        return u;
    }

    public User getById(int id) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT * FROM users WHERE id=? LIMIT 1", new String[]{String.valueOf(id)});
        User u = null;
        if (c.moveToFirst()) {
            u = fromCursor(c);
        }
        c.close();
        return u;
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT * FROM users ORDER BY full_name ASC", null);
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    private ContentValues toCV(User u) {
        ContentValues cv = new ContentValues();
        cv.put("email", u.email);
        cv.put("password", u.password);
        cv.put("full_name", u.fullName);
        cv.put("phone", u.phone);
        cv.put("avatar_path", u.avatarPath);
        cv.put("id_card", u.idCard);
        cv.put("role", u.role);
        cv.put("is_active", u.isActive ? 1 : 0);
        cv.put("created_at", u.createdAt);
        return cv;
    }

    public static User fromCursor(Cursor c) {
        User u = new User();
        u.id = c.getInt(c.getColumnIndexOrThrow("id"));
        u.email = c.getString(c.getColumnIndexOrThrow("email"));
        u.password = c.getString(c.getColumnIndexOrThrow("password"));
        u.fullName = c.getString(c.getColumnIndexOrThrow("full_name"));
        u.phone = c.getString(c.getColumnIndexOrThrow("phone"));
        u.avatarPath = c.getString(c.getColumnIndexOrThrow("avatar_path"));
        u.idCard = c.getString(c.getColumnIndexOrThrow("id_card"));
        u.role = c.getString(c.getColumnIndexOrThrow("role"));
        u.isActive = c.getInt(c.getColumnIndexOrThrow("is_active")) == 1;
        u.createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
        return u;
    }
}
