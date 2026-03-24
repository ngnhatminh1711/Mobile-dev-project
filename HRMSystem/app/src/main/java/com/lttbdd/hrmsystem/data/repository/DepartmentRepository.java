package com.lttbdd.hrmsystem.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lttbdd.hrmsystem.MyApplication;
import com.lttbdd.hrmsystem.data.dao.DepartmentDao;
import com.lttbdd.hrmsystem.data.model.Department;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DepartmentRepository {

    private final DepartmentDao dao = new DepartmentDao(MyApplication.getDbHelper());
    private static final ExecutorService exec = Executors.newSingleThreadExecutor();

    public void insert(Department d) {
        exec.execute(() -> dao.insert(d));
    }

    public void update(Department d) {
        exec.execute(() -> dao.update(d));
    }

    public void delete(int id) {
        exec.execute(() -> dao.delete(id));
    }

    public List<Department> getAllSync() {
        return dao.getAll();
    }

    public LiveData<List<Department>> getAll() {
        MutableLiveData<List<Department>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getAll()));
        return ld;
    }

    public LiveData<Department> getById(int id) {
        MutableLiveData<Department> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getById(id)));
        return ld;
    }

    public LiveData<Integer> count() {
        MutableLiveData<Integer> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.count()));
        return ld;
    }
}
