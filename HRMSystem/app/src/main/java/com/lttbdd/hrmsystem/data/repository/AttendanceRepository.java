package com.lttbdd.hrmsystem.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lttbdd.hrmsystem.MyApplication;
import com.lttbdd.hrmsystem.data.dao.AttendanceDao;
import com.lttbdd.hrmsystem.data.model.Attendance;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AttendanceRepository {
    
    private final AttendanceDao dao = new AttendanceDao(MyApplication.getDbHelper());
    private static final ExecutorService exec = Executors.newSingleThreadExecutor();

    public void insert(Attendance a) {
        exec.execute(() -> dao.insert(a));
    }

    public void update(Attendance a) {
        exec.execute(() -> dao.update(a));
    }

    public void delete(int id) {
        exec.execute(() -> dao.delete(id));
    }

    public int countWorkingDays(int empId, String month) {
        return dao.countWorkingDays(empId, month);
    }

    public float getTotalOvertimeHours(int empId, String month) {
        return dao.getTotalOvertimeHours(empId, month);
    }

    public LiveData<List<Attendance>> getByWeek(int empId, String from, String to) {
        MutableLiveData<List<Attendance>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getByWeek(empId, from, to)));
        return ld;
    }

    public LiveData<List<Attendance>> getByMonth(int empId, String month) {
        MutableLiveData<List<Attendance>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getByMonth(empId, month)));
        return ld;
    }

    public LiveData<List<Attendance>> getByYear(int empId, String year) {
        MutableLiveData<List<Attendance>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getByYear(empId, year)));
        return ld;
    }

    public LiveData<List<Attendance>> getAllByMonth(String month) {
        MutableLiveData<List<Attendance>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getAllByMonth(month)));
        return ld;
    }
}
