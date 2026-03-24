package com.lttbdd.hrmsystem.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lttbdd.hrmsystem.MyApplication;
import com.lttbdd.hrmsystem.data.dao.LeaveDao;
import com.lttbdd.hrmsystem.data.model.Leave;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LeaveRepository {

    private final LeaveDao dao = new LeaveDao(MyApplication.getDbHelper());
    private static final ExecutorService exec = Executors.newSingleThreadExecutor();

    public void insert(Leave l) {
        exec.execute(() -> dao.insert(l));
    }

    public void delete(int id) {
        exec.execute(() -> dao.delete(id));
    }

    public void approve(int leaveId, int approvedBy) {
        exec.execute(() -> dao.updateStatus(leaveId, "APPROVED",
                approvedBy, System.currentTimeMillis(), null));
    }

    public void reject(int leaveId, int approvedBy, String reason) {
        exec.execute(() -> dao.updateStatus(leaveId, "REJECTED",
                approvedBy, System.currentTimeMillis(), reason));
    }

    public LiveData<List<Leave>> getByEmployee(int empId) {
        MutableLiveData<List<Leave>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getByEmployee(empId)));
        return ld;
    }

    public LiveData<List<Leave>> getByEmployeeAndStatus(int empId, String status) {
        MutableLiveData<List<Leave>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getByEmployeeAndStatus(empId, status)));
        return ld;
    }

    public LiveData<List<Leave>> getPending() {
        MutableLiveData<List<Leave>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getPending()));
        return ld;
    }

    public LiveData<List<Leave>> getAll() {
        MutableLiveData<List<Leave>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getAll()));
        return ld;
    }

    public LiveData<Integer> countPending() {
        MutableLiveData<Integer> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.countPending()));
        return ld;
    }
}
