package com.example.vacationscheduler.UI;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;

import com.example.vacationscheduler.DB.Repository;
import com.example.vacationscheduler.R;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

import java.util.Date;
import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.vRecyclerView);
        final VacationAdapter adapter = new VacationAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setVacations(repository.getAllVacations());
    }
    @Override
    public void onResume() {
        super.onResume();
        List<Vacation> vacations = repository.getAllVacations();
        RecyclerView recyclerView = findViewById(R.id.vRecyclerView);
        final VacationAdapter adapter = new VacationAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setVacations(repository.getAllVacations());
    }
    public void goToVacationDetails (View view) {
        Intent intent = new Intent(this, VacationDetails.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sampleData) {
            repository = new Repository(getApplication());
            Vacation vacation = new Vacation(1, "Iceland", "Hotel", new Date(), new Date());
            repository.insert(vacation);
            vacation = new Vacation(2, "Bahamas", "Vacation Rental", new Date(), new Date());
            repository.insert(vacation);
            Excursion excursion = new Excursion(1, "Sightseeing", new Date(), 1);
            repository.insert(excursion);
            excursion = new Excursion(2, "Swimming", new Date(), 2);
            repository.insert(excursion);
            Toast.makeText(this, "Sample data added", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }
}