package com.lttbdd.hrmsystem.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "qlns_database.db";
    public static final int DB_VERSION = 2;

    public static final String TABLE_USERS = "users";
    public static final String TABLE_DEPARTMENTS = "departments";
    public static final String TABLE_EMPLOYEES = "employees";
    public static final String TABLE_ATTENDANCE = "attendance";
    public static final String TABLE_LEAVES = "leaves";
    public static final String TABLE_SALARIES = "salaries";

    private static final String CREATE_USERS =
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "email TEXT NOT NULL UNIQUE,\n" +
                    "password TEXT NOT NULL,\n" +
                    "full_name TEXT NOT NULL,\n" +
                    "phone TEXT,\n" +
                    "avatar_path TEXT,\n" +
                    "id_card TEXT,\n" +
                    "role TEXT NOT NULL DEFAULT 'USER',\n" +
                    "is_active INTEGER NOT NULL DEFAULT 1,\n" +
                    "created_at INTEGER NOT NULL\n" +
                    ")";

    private static final String CREATE_DEPARTMENTS =
            "CREATE TABLE departments (\n" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "name TEXT NOT NULL,\n" +
                    "description TEXT,\n" +
                    "manager_id INTEGER,\n" +
                    "created_at INTEGER NOT NULL\n" +
                    ")";

    private static final String CREATE_EMPLOYEES =
            "CREATE TABLE employees (\n" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "user_id INTEGER,\n" +
                    "full_name TEXT NOT NULL,\n" +
                    "email TEXT,\n" +
                    "phone TEXT,\n" +
                    "position TEXT,\n" +
                    "department_id INTEGER REFERENCES departments(id) ON DELETE SET NULL,\n" +
                    "gender TEXT,\n" +
                    "date_of_birth INTEGER,\n" +
                    "join_date INTEGER,\n" +
                    "base_salary REAL NOT NULL DEFAULT 0,\n" +
                    "avatar_path TEXT,\n" +
                    "id_card TEXT,\n" +
                    "is_active INTEGER NOT NULL DEFAULT 1,\n" +
                    "created_at INTEGER NOT NULL\n" +
                    ")";

    private static final String CREATE_ATTENDANCE =
            "CREATE TABLE attendance (\n" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "employee_id INTEGER NOT NULL REFERENCES employees(id) ON DELETE CASCADE,\n" +
                    "date TEXT NOT NULL,\n" +
                    "check_in TEXT,\n" +
                    "check_out TEXT,\n" +
                    "total_hours REAL NOT NULL DEFAULT 0,\n" +
                    "is_overtime INTEGER NOT NULL DEFAULT 0,\n" +
                    "note TEXT,\n" +
                    "created_at INTEGER NOT NULL\n" +
                    ")";

    private static final String CREATE_LEAVES =
            "CREATE TABLE leaves (\n" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "employee_id INTEGER NOT NULL REFERENCES employees(id) ON DELETE CASCADE,\n" +
                    "from_date TEXT NOT NULL,\n" +
                    "to_date TEXT NOT NULL,\n" +
                    "total_days INTEGER NOT NULL DEFAULT 1,\n" +
                    "reason TEXT,\n" +
                    "status TEXT NOT NULL DEFAULT 'PENDING',\n" +
                    "rejection_reason TEXT,\n" +
                    "approved_by INTEGER,\n" +
                    "approved_at INTEGER,\n" +
                    "created_at INTEGER NOT NULL\n" +
                    ")";

    private static final String CREATE_SALARIES =
            "CREATE TABLE salaries (\n" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "employee_id INTEGER NOT NULL REFERENCES employees(id) ON DELETE CASCADE,\n" +
                    "month TEXT NOT NULL,\n" +
                    "working_days INTEGER NOT NULL DEFAULT 0,\n" +
                    "standard_days INTEGER NOT NULL DEFAULT 26,\n" +
                    "overtime_hours REAL NOT NULL DEFAULT 0,\n" +
                    "base_salary REAL NOT NULL DEFAULT 0,\n" +
                    "bonus REAL NOT NULL DEFAULT 0,\n" +
                    "deduction REAL NOT NULL DEFAULT 0,\n" +
                    "total_salary REAL NOT NULL DEFAULT 0,\n" +
                    "note TEXT,\n" +
                    "created_by INTEGER,\n" +
                    "created_at INTEGER NOT NULL,\n" +
                    "UNIQUE(employee_id, month)\n" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_DEPARTMENTS);
        db.execSQL(CREATE_EMPLOYEES);
        db.execSQL(CREATE_ATTENDANCE);
        db.execSQL(CREATE_LEAVES);
        db.execSQL(CREATE_SALARIES);
        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS salaries");
        db.execSQL("DROP TABLE IF EXISTS leaves");
        db.execSQL("DROP TABLE IF EXISTS attendance");
        db.execSQL("DROP TABLE IF EXISTS employees");
        db.execSQL("DROP TABLE IF EXISTS departments");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }


    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void seedData(SQLiteDatabase db) {
        long now = System.currentTimeMillis();

        ContentValues admin = new ContentValues();
        admin.put("email", "admin@qlns.com");
        admin.put("password", "admin123");
        admin.put("full_name", "Nguyễn Nhật Minh");
        admin.put("role", "ADMIN");
        admin.put("is_active", 1);
        admin.put("created_at", now);
        db.insert(TABLE_USERS, null, admin);

        ContentValues dept = new ContentValues();
        dept.put("name", "Phòng Kỹ Thuật");
        dept.put("description", "Phòng phát triển phần mềm");
        dept.put("created_at", now);
        long deptId = db.insert(TABLE_DEPARTMENTS, null, dept);

        ContentValues userNV = new ContentValues();
        userNV.put("email", "nv001@qlns.com");
        userNV.put("password", "123456");
        userNV.put("full_name", "Nguyễn Văn A");
        userNV.put("role", "USER");
        userNV.put("is_active", 1);
        userNV.put("created_at", now);
        long userId = db.insert(TABLE_USERS, null, userNV);

        ContentValues emp = new ContentValues();
        emp.put("user_id", userId);
        emp.put("full_name", "Nguyễn Nhật Minh");
        emp.put("email", "nv001@qlns.com");
        emp.put("phone", "0901234567");
        emp.put("position", "Lập trình viên");
        emp.put("department_id", deptId);
        emp.put("base_salary", 15_000_000.0);
        emp.put("is_active", 1);
        emp.put("join_date", now);
        emp.put("created_at", now);
        db.insert(TABLE_EMPLOYEES, null, emp);
    }
}
