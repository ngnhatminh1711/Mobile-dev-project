package com.lttbdd.hrmsystem.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lttbdd.hrmsystem.MyApplication;
import com.lttbdd.hrmsystem.data.dao.SalaryDao;
import com.lttbdd.hrmsystem.data.model.Salary;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SalaryRepository {

    private final SalaryDao dao = new SalaryDao(MyApplication.getDbHelper());
    private static final ExecutorService exec = Executors.newSingleThreadExecutor();

    public void insertOrReplace(Salary s) {
        exec.execute(() -> dao.insertOrReplace(s));
    }

    public void update(Salary s) {
        exec.execute(() -> dao.update(s));
    }

    public void delete(int id) {
        exec.execute(() -> dao.delete(id));
    }

    public LiveData<Salary> getByMonth(int empId, String month) {
        MutableLiveData<Salary> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getByMonth(empId, month)));
        return ld;
    }

    public LiveData<List<Salary>> getHistory(int empId) {
        MutableLiveData<List<Salary>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getHistory(empId)));
        return ld;
    }

    public LiveData<List<Salary>> getAllByMonth(String month) {
        MutableLiveData<List<Salary>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getAllByMonth(month)));
        return ld;
    }

    public LiveData<List<Salary>> getHistoryByEmployee(int empId) {
        MutableLiveData<List<Salary>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getHistoryByEmployee(empId)));
        return ld;
    }

    public LiveData<Float> getTotalByMonth(String month) {
        MutableLiveData<Float> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getTotalByMonth(month)));
        return ld;
    }
}
