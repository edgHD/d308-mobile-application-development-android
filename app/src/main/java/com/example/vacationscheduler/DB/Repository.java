package com.example.vacationscheduler.DB;

import android.app.Application;

import com.example.vacationscheduler.DAO.ExcursionDAO;
import com.example.vacationscheduler.DAO.VacationDAO;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private VacationDAO vacationDAO;
    private ExcursionDAO excursionDAO;
    private List<Vacation> vacations;
    private List<Excursion> excursions;
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        this.vacationDAO = db.vacationDAO();
        this.excursionDAO = db.excursionDAO();
    }
    public List<Vacation> getAllVacations() {
        databaseExecutor.execute(() -> {
            vacations = vacationDAO.getAllVacations();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return vacations;
    }
    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> {
            vacationDAO.insert(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> {
            vacationDAO.update(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> {
            vacationDAO.delete(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Excursion> getAllExcursions() {
        databaseExecutor.execute(() -> {
            excursions = excursionDAO.getAllExcursions();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return excursions;
    }
    public List<Excursion> getAssociatedExcursions(int vacationId) {
        databaseExecutor.execute(() -> {
            excursions = excursionDAO.getAssociatedExcursions(vacationId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return excursions;
    }
    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> {
            excursionDAO.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> {
            excursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> {
            excursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
