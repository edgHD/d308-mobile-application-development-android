package com.example.vacationscheduler.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.DB.Repository;
import com.example.vacationscheduler.R;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    int id;
    String title;
    String lodging;
    Date startDate;
    Date endDate;
    EditText editTitle;
    EditText editLodging;
    TextView displayStartDate;
    TextView displayEndDate;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTitle = findViewById(R.id.titleTXT);
        editLodging = findViewById(R.id.LodgingTXT);
        displayStartDate = findViewById(R.id.startDateTXT);
        displayEndDate = findViewById(R.id.endDateTXT);

        title = getIntent().getStringExtra("title");
        editTitle.setText(title);
        lodging = getIntent().getStringExtra("lodging");
        editLodging.setText(lodging);
        id = getIntent().getIntExtra("id", -1);

        startDate = (Date) getIntent().getSerializableExtra("startDate");
        endDate = (Date) getIntent().getSerializableExtra("endDate");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        if (startDate != null) {
            String formattedStartDate = sdf.format(startDate);
            displayStartDate.setText(formattedStartDate);
        }
        if (endDate != null) {
            String formattedEndDate = sdf.format(endDate);
            displayEndDate.setText(formattedEndDate);
        }

        List<Excursion> filteredExcursions = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.vDetailsRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter adapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (Excursion excursion : repository.getAllExcursions()) {
            if (excursion.getVacationId() == id) filteredExcursions.add(excursion);
        }
        adapter.setExcursions(filteredExcursions);

    }
    @Override
    public void onResume() {
        super.onResume();
        List<Excursion> filteredExcursions = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.vDetailsRecyclerView);
        final ExcursionAdapter adapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (Excursion excursion : repository.getAllExcursions()) {
            if (excursion.getVacationId() == id) filteredExcursions.add(excursion);
        }
        adapter.setExcursions(filteredExcursions);
    }
    public void selectStartDate(View view) {
        TextView startDateTXT = findViewById(R.id.startDateTXT);
        showDatePickerDialog((date) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(date);
            startDateTXT.setText(formattedDate);
            this.startDate = date;
            Toast.makeText(this, "Start Date: " + formattedDate, Toast.LENGTH_SHORT).show();
        });
    }
    public void selectEndDate(View view) {
        TextView endDateTXT = findViewById(R.id.endDateTXT);
        showDatePickerDialog((date) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(date);
            endDateTXT.setText(formattedDate);
            this.endDate = date;
            Toast.makeText(this, "End Date: " + formattedDate, Toast.LENGTH_SHORT).show();
        });
    }
    public void goToExcursionDetails (View view) {
        Intent intent = new Intent(this, ExcursionDetails.class);
        intent.putExtra("vacationId", id);
        startActivity(intent);
    }
    private void showDatePickerDialog(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                    Date selectedDate = selectedCalendar.getTime();
                    listener.onDateSelected(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    interface OnDateSelectedListener {
        void onDateSelected(Date date);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vacationSave) {
            Vacation vacation;
            String titleText = editTitle.getText().toString();
            String lodgingText = editLodging.getText().toString();

            if (titleText.isEmpty() || lodgingText.isEmpty() || startDate == null || endDate == null) {
                Toast.makeText(this, "Please fill in all fields and select dates.", Toast.LENGTH_LONG).show();
                return true;
            }
            if (id == -1) {
                int newId;
                if (repository.getAllVacations().isEmpty()) newId = 1;
                else newId = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getId() + 1;
                vacation = new Vacation(newId, titleText, lodgingText, startDate, endDate);
                repository.insert(vacation);
                Toast.makeText(this, "Vacation saved.", Toast.LENGTH_SHORT).show();
            } else {
                vacation = new Vacation(id, titleText, lodgingText, startDate, endDate);
                repository.update(vacation);
                Toast.makeText(this, "Vacation updated.", Toast.LENGTH_SHORT).show();
            }
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.vacationDelete) {
            if (id != -1) {
                Vacation vacationToDelete = null;
                for(Vacation vac : repository.getAllVacations()){
                    if(vac.getId() == id) vacationToDelete = vac;
                }
                if (vacationToDelete != null) {
                    repository.delete(vacationToDelete);
                    Toast.makeText(this, "Vacation deleted.", Toast.LENGTH_SHORT).show();
                    this.finish();
                } else Toast.makeText(this, "Vacation not found.", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "No vacation selected to delete.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.vacationAlert) {
            if (title == null || startDate == null || endDate == null) {
                Toast.makeText(this, "Please save the vacation before setting notifications.", Toast.LENGTH_LONG).show();
                return true;
            }
            Intent startIntent = new Intent(VacationDetails.this, MyReceiver.class);
            startIntent.putExtra("key", title + " is starting today!");
            PendingIntent startSender = PendingIntent.getBroadcast(VacationDetails.this, (int) System.currentTimeMillis(), startIntent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManagerStart = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (startDate != null) {
                long triggerStart = startDate.getTime();
                alarmManagerStart.set(AlarmManager.RTC_WAKEUP, triggerStart, startSender);
            }
            Intent endIntent = new Intent(VacationDetails.this, MyReceiver.class);
            endIntent.putExtra("key", title + " is ending today!");
            PendingIntent endSender = PendingIntent.getBroadcast(VacationDetails.this, (int) (System.currentTimeMillis() + 1), endIntent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManagerEnd = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (endDate != null) {
                long triggerEnd = endDate.getTime();
                alarmManagerEnd.set(AlarmManager.RTC_WAKEUP, triggerEnd, endSender);
            }
            Toast.makeText(this, "Notifications set for start and end dates.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.vacationShare) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vacation Details");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Title: " + title + "\nLodging: " + lodging + "\nStart Date: " + startDate + "\nEnd Date: " + endDate);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}