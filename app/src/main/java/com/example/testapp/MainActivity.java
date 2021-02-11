package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.view.View;

import com.captaindroid.tvg.Tvg;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sPref;
    CircularProgressBar targetDays;
    CircularProgressBar maximumDays;
    TextView days;
    TextClock clock;
    Calendar currentDate = Calendar.getInstance();
    float daysCount;
    float recdays;
    private int tDaysCount;
    final int DIALOG_DATE = 1;
    final String START_DATE = "Start date";
    final String MAX_DATE = "Maximum date";
    final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText recCount = findViewById(R.id.editTextNumber);
        clock = findViewById(R.id.textClock);
        recCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                maximumDays = findViewById(R.id.circularProgressBar2);

                try {
                    recdays = Float.parseFloat(recCount.getText().toString());
                    maximumDays.setProgressMax(recdays);
                    maximumDays.setProgress(daysCount);


                }catch (NumberFormatException e){
                    maximumDays.setProgressMax(30);
                    maximumDays.setProgress(daysCount);
                }
                sPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sPref.edit();
                editor.putFloat(MAX_DATE, recdays);
                editor.apply();
                editor.commit();



            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.navbar2);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main:
                        break;
                    case R.id.inform:
                        Intent intent = new Intent(MainActivity.this, InformationActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sPref = getPreferences(MODE_PRIVATE);
        long starttimeinmills = sPref.getLong(START_DATE, (long) 0.0);
        if(starttimeinmills == 0.0){
            showDialog(DIALOG_DATE);
        }

        updateDays();
        updateTarget();
        updateCitate();
        updateTargetPB();
        updateRecordPB();
    }

    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_DATE){
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Установка даты начала");
            adb.setMessage("Установите дату начала воздержания.");
            adb.setNeutralButton("Установить", positiveButton);
            adb.setPositiveButton("Сегодняшний день", myOnClickListener);
            return adb.create();
        }
        return super.onCreateDialog(id);

    }

    public void currentDate(){
        long starttimeinmills = Calendar.getInstance().getTimeInMillis();
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putLong(START_DATE, starttimeinmills);
        editor.apply();
        editor.commit();
        updateDays();
    }

    public void setStartDate(){
        Calendar dateAndTime = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            long dateAndTimeinLong = dateAndTime.getTimeInMillis();

            sPref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putLong(START_DATE, dateAndTimeinLong);
            editor.apply();
            editor.commit();
            updateDays();
        };


        new DatePickerDialog(MainActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();


    }
    DialogInterface.OnClickListener myOnClickListener = (dialog, which) -> currentDate();
    DialogInterface.OnClickListener positiveButton = (dialog, which) -> setStartDate();

    public void updateDays(){
        sPref = getPreferences(MODE_PRIVATE);
        long starttimeinmills = sPref.getLong(START_DATE, (long) 0.0);
        long diff = currentDate.getTimeInMillis() - starttimeinmills;
        daysCount = (float) diff / (24 * 60 * 60 * 1000);
        days = findViewById(R.id.dayscounter);
        days.setText("Дней: " + (int) daysCount);
        int curenthourin24mode = currentDate.get(Calendar.HOUR_OF_DAY);



        if(curenthourin24mode >= 23){
            Tvg.change(clock, new int[]{
                    Color.parseColor("#188AEE"),
                    Color.parseColor("#188AEE")
            });
        }else if(curenthourin24mode >= 18) {
            Tvg.change(clock, new int[]{
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#188AEE"),
                    Color.parseColor("#188AEE"),
                    Color.parseColor("#188AEE")
            });
        }else if(curenthourin24mode >= 12){
            Tvg.change(clock, new int[]{
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#188AEE")
            });
        }else if(curenthourin24mode > 3){
            Tvg.change(clock, new int[]{
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#188AEE")
            });
        }else if(curenthourin24mode == 0){
            Tvg.change(clock, new int[]{
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#EA1332"),
                    Color.parseColor("#188AEE")
            });}




    }

    public void updateTarget(){
        TextView target = findViewById(R.id.targetlab);
        tDaysCount = (int) daysCount;
        if(tDaysCount <= 7){
            target.setText("Цель: 7 дней");
        }else if(tDaysCount <= 14){
            target.setText("Цель: 14 дней");
        }else if(tDaysCount <= 30){
            target.setText("Цель: 30 дней");
        }else if(tDaysCount <= 90){
            target.setText("Цель: 90 дней");
        }else if(tDaysCount <= 180){
            target.setText("Цель: 180 дней");
        }else if(tDaysCount <= 365){
            target.setText("Цель: 365 дней");
        }else if(tDaysCount > 365){
            target.setText("Вы Победитель!");
        }

    }

    public void updateCitate(){
        TextView citate = findViewById(R.id.citateslab);
        int randcit = random.nextInt(11) +1;
        switch (randcit){
            case 1:citate.setText(R.string.cit1);break;
            case 2:citate.setText(R.string.cit2);break;
            case 3:citate.setText(R.string.cit3);break;
            case 4:citate.setText(R.string.cit4);break;
            case 5:citate.setText(R.string.cit5);break;
            case 6:citate.setText(R.string.cit6);break;
            case 7:citate.setText(R.string.cit7);break;
            case 8:citate.setText(R.string.cit8);break;
            case 9:citate.setText(R.string.cit9);break;
            case 10:citate.setText(R.string.cit10);break;
            default:citate.setText(R.string.cit3);
            }
    }
    public void updateCitate(View view){
        TextView citate = findViewById(R.id.citateslab);
        int randcit = random.nextInt(11) +1;
        switch (randcit){
            case 1:citate.setText(R.string.cit1);break;
            case 2:citate.setText(R.string.cit2);break;
            case 3:citate.setText(R.string.cit3);break;
            case 4:citate.setText(R.string.cit4);break;
            case 5:citate.setText(R.string.cit5);break;
            case 6:citate.setText(R.string.cit6);break;
            case 7:citate.setText(R.string.cit7);break;
            case 8:citate.setText(R.string.cit8);break;
            case 9:citate.setText(R.string.cit9);break;
            case 10:citate.setText(R.string.cit10);break;
            default:citate.setText(R.string.cit3);
            }
    }

    public void updateTargetPB(){
       targetDays = findViewById(R.id.circularProgressBar);

       if(tDaysCount <= 7){
           targetDays.setProgressMax(7f);
       }else if(tDaysCount <= 14){
           targetDays.setProgressMax(14f);
       }else if(tDaysCount <= 30){
           targetDays.setProgressMax(30f);
       }else if(tDaysCount <= 90){
           targetDays.setProgressMax(90f);
       }else if(tDaysCount <= 180){
           targetDays.setProgressMax(180f);
       }else if(tDaysCount <= 365){
           targetDays.setProgressMax(365f);
       }else if(tDaysCount > 365){
           targetDays.setProgressMax(1000f);
       }
       targetDays.setProgress((int) daysCount);



    }

    public void updateRecordPB(){
        maximumDays = findViewById(R.id.circularProgressBar2);
        sPref = getPreferences(MODE_PRIVATE);
        float savedRecdate = sPref.getFloat(MAX_DATE, 30f);
        EditText recCount = findViewById(R.id.editTextNumber);
        String sv = String.valueOf((int) savedRecdate);
        recCount.setText(sv);
        maximumDays.setProgressMax(savedRecdate);
        maximumDays.setProgress(daysCount);


    }







}