package com.lttbdd.hrmsystem.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lttbdd.hrmsystem.MyApplication;
import com.lttbdd.hrmsystem.data.dao.EmployeeDao;
import com.lttbdd.hrmsystem.data.model.Employee;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmployeeRepository {

    private final EmployeeDao dao = new EmployeeDao(MyApplication.getDbHelper());
    private static final ExecutorService exec = Executors.newSingleThreadExecutor();

    public void insert(Employee e, Runnable onDone) {
        exec.execute(() -> {
            dao.insert(e);
            if (onDone != null) {
                onDone.run();
            }
        });
    }

    public void insertAll(List<Employee> list) {
        exec.execute(() -> dao.insertAll(list));
    }

    public void update(Employee e) {
        exec.execute(() -> dao.update(e));
    }

    public void softDelete(int id) {
        exec.execute(() -> dao.softDelete(id));
    }

    public Employee getByUserId(int userId) {
        return dao.getByUserId(userId);
    }

    public Employee getByIdSync(int id) {
        return dao.getById(id);
    }

    public LiveData<List<Employee>> getAll() {
        MutableLiveData<List<Employee>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getAll()));
        return ld;
    }

    public LiveData<Employee> getById(int id) {
        MutableLiveData<Employee> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getById(id)));
        return ld;
    }

    public LiveData<List<Employee>> getByDepartment(int deptId) {
        MutableLiveData<List<Employee>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getByDepartment(deptId)));
        return ld;
    }

    public LiveData<List<Employee>> search(String q) {
        MutableLiveData<List<Employee>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.search(q)));
        return ld;
    }

    public LiveData<Integer> countActive() {
        MutableLiveData<Integer> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.countActive()));
        return ld;
    }
}
