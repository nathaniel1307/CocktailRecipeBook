package com.example.cocktailrecipebook;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class CocktailTimeActivity extends AppCompatActivity {
    //Define Components
    EditText chooseTime;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    int selectedHour;
    int selectedMinute;
    String amPm;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_cocktails:
                    cocktails();
                    return true;
                case R.id.navigation_favourites:
                    favourites();
                    return true;
                case R.id.navigation_cocktailtime:
                    cocktailTime();
                    return true;
                case R.id.navigation_userguide:
                    userGuide();
                    return true;
            }
            return false;
        }
    };

    protected void cocktails(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    protected void cocktailTime(){
        Intent intent = new Intent(this, CocktailTimeActivity.class);
        startActivity(intent);
    }
    protected void favourites(){
        Intent intent = new Intent(this, FavouritesActivity.class);
        startActivity(intent);
    }
    protected void userGuide(){
        Intent intent = new Intent(this, UserGuideActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create Form
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_time);

        //Define Components
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);
        chooseTime = findViewById(R.id.etChooseTime);
        chooseTime.setText("00:00");

        final ToggleButton toggle = findViewById(R.id.toggleButton);

        loadForm(toggle);

        //On Click chooseTime
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(CocktailTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        selectedHour = hourOfDay;
                        selectedMinute = minutes;
                        chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                },currentHour, currentMinute, false);
                timePickerDialog.show();
            }
        });



        //Toggle Cocktail Time On or Off
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //disable control
                    chooseTime.setEnabled(false);
                } else {
                    // The toggle is disabled
                    //disable control
                    chooseTime.setEnabled(true);
                }
                setNotification(toggle);
                saveForm(toggle);

            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();

        final ToggleButton toggle = findViewById(R.id.toggleButton);
        loadForm(toggle);
    }

    public void loadForm(ToggleButton toggle){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Integer hour = prefs.getInt("hour",0);
        Integer min = prefs.getInt("minute",0);
        String time = hour.toString()+":"+min.toString();
        chooseTime.setText(time);
        toggle.setChecked(prefs.getBoolean("on_off",false));
        if(prefs.getBoolean("on_off",false)){
            chooseTime.setEnabled(false);
        }
    }

    public void setNotification(ToggleButton toggle){

        NotificationHelper helper = new NotificationHelper(this);
        String title = "Cocktail Time";
        String content = "It's Cocktail Time!";

        Notification.Builder builder = helper.getCocktail_channel_notification(title, content);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        System.out.println(selectedHour);
        System.out.println(selectedMinute);
        calendar2.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar2.set(Calendar.MINUTE, selectedMinute);
        calendar2.set(Calendar.SECOND, 0);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(NotificationHelper.ID, 0);
        notificationIntent.putExtra(NotificationHelper.CHANNEL_NAME, builder.build());
        PendingIntent pendingIntent_ = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if(toggle.isChecked()){

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent_);
        }else{
            alarmManager.cancel(pendingIntent_);
        }


    }

    public void saveForm(ToggleButton toggle){
        //save form
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("hour", selectedHour);
        editor.putInt("minute", selectedMinute);
        editor.putBoolean("on_off", toggle.isChecked());
        editor.apply();
    }


}
