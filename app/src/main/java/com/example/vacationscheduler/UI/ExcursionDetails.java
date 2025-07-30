package com.example.vacationscheduler.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
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

import com.example.vacationscheduler.DB.Repository;
import com.example.vacationscheduler.R;
import com.example.vacationscheduler.entities.Excursion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    private int id;
    private String title;
    private Date date;
    private int vacationId;
    private EditText editTitle;
    private TextView displayDate;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTitle = findViewById(R.id.excTitleTXT);
        displayDate = findViewById(R.id.excDateTXT);
        repository = new Repository(getApplication());
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        title = intent.getStringExtra("title");
        vacationId = intent.getIntExtra("vacationId", -1);
        date = (Date) intent.getSerializableExtra("date");
        if (title != null) editTitle.setText(title);
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            displayDate.setText(sdf.format(date));
        }
    }
    public void selectDate(View view) {
        showDatePickerDialog((selectedDate) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(selectedDate);
            displayDate.setText(formattedDate);
            this.date = selectedDate;
            Toast.makeText(this, "Excursion Date: " + formattedDate, Toast.LENGTH_SHORT).show();
        });
    }
    private void showDatePickerDialog(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.excursionSave) {
            Excursion excursion;
            String title = editTitle.getText().toString();
            String dateString = displayDate.getText().toString();

            if (title.isEmpty()) {
                Toast.makeText(this, "Title cannot be empty.", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (dateString.isEmpty() || this.date == null) {
                Toast.makeText(this, "Please select a date.", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == -1) {
                int nextId = 1;
                if (!repository.getAllExcursions().isEmpty()) {
                    nextId = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getId() + 1;
                }
                excursion = new Excursion(nextId, title, date, vacationId);
                repository.insert(excursion);
                Toast.makeText(this, "Excursion saved", Toast.LENGTH_SHORT).show();
            } else {
                excursion = new Excursion(id, title, date, vacationId);
                repository.update(excursion);
                Toast.makeText(this, "Excursion updated", Toast.LENGTH_SHORT).show();
            }
            this.finish();
        }
        if (item.getItemId() == R.id.excursionDelete) {
            if (id != -1) {
                Excursion excursionToDelete = null;
                for (Excursion ex : repository.getAllExcursions()) {
                    if (ex.getId() == id) excursionToDelete = ex;
                }
                if (excursionToDelete != null) {
                    repository.delete(excursionToDelete);
                    Toast.makeText(this, "Excursion deleted", Toast.LENGTH_SHORT).show();
                    this.finish();
                } else {
                    Toast.makeText(this, "Excursion not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No excursion selected to delete", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.excursionAlert) {
            String dateFromScreen = displayDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
                if (myDate == null) throw new Exception();
            } catch (Exception e) {
                Toast.makeText(this, "Please select a valid date.", Toast.LENGTH_SHORT).show();
                return true;
            }
            long triggerAtMillis = myDate.getTime();
            Intent alertIntent = new Intent(this, MyReceiver.class);
            alertIntent.putExtra("key", "Excursion: " + editTitle.getText().toString() + " is today!");
            PendingIntent sender = PendingIntent.getBroadcast(
                    this, ++MainActivity.numAlert, alertIntent, PendingIntent.FLAG_IMMUTABLE
            );
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender);
            Toast.makeText(this, "Notification set for this excursion!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }
}