package com.lttbdd.hrmsystem.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lttbdd.hrmsystem.MyApplication;
import com.lttbdd.hrmsystem.data.dao.UserDao;
import com.lttbdd.hrmsystem.data.model.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private final UserDao dao = new UserDao(MyApplication.getDbHelper());
    private static final ExecutorService exec = Executors.newSingleThreadExecutor();

    public User login(String email, String password) {
        return dao.login(email, password);
    }

    public User findByEmail(String email) {
        return dao.findByEmail(email);
    }

    public long insert(User u) {
        return dao.insert(u);
    }

    public void update(User u) {
        exec.execute(() -> dao.update(u));

    }

    public void updateRole(int id, String role) {
        exec.execute(() -> dao.updateRole(id, role));
    }

    public void updateProfile(int id, String name, String phone, String avatar) {
        exec.execute(() -> dao.updateProfile(id, name, phone, avatar));
    }

    public void updatePassword(int id, String pwd) {
        exec.execute(() -> dao.updatePassword(id, pwd));
    }

    public LiveData<User> getById(int id) {
        MutableLiveData<User> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getById(id)));
        return ld;
    }

    public LiveData<List<User>> getAll() {
        MutableLiveData<List<User>> ld = new MutableLiveData<>();
        exec.execute(() -> ld.postValue(dao.getAll()));
        return ld;
    }
}
