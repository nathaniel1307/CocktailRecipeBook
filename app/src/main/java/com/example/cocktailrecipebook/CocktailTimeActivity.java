package com.example.cocktailrecipebook;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

        final Switch swi_mon = findViewById(R.id.swi_mon);
        final Switch swi_tue = findViewById(R.id.swi_tue);
        final Switch swi_wed = findViewById(R.id.swi_wed);
        final Switch swi_thu = findViewById(R.id.swi_thu);
        final Switch swi_fri = findViewById(R.id.swi_fri);
        final Switch swi_sat = findViewById(R.id.swi_sat);
        final Switch swi_sun = findViewById(R.id.swi_sun);
        final ToggleButton toggle = findViewById(R.id.toggleButton);

        loadForm(swi_mon,swi_tue,swi_wed,swi_thu,swi_fri,swi_sat,swi_sun,toggle);

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



        //Retrieve time
        String pickedTime = chooseTime.getText().toString();
        Log.d("run",pickedTime);
        final Integer pickedHour = Integer.parseInt(pickedTime.substring(0,pickedTime.indexOf(":")));
        final Integer pickedMinute = Integer.parseInt(pickedTime.substring(pickedTime.indexOf(":")+1));
        Log.d("run",pickedTime.substring(0,pickedTime.indexOf(":")));
        Log.d("run",pickedTime.substring(pickedTime.indexOf(":")+1));
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                saveForm(pickedHour, pickedMinute, mon,tue,wed,thu,fri,sat,sun,toggle);
//            }
//        }, 0, 250);//put here time 1000 milliseconds=1 second



        //Toggle Cocktail Time On or Off
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(),NotificationHelper.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (isChecked) {
                    // The toggle is enabled
                    //disable all controls
                    chooseTime.setEnabled(false);
                    swi_mon.setClickable(false);
                    swi_tue.setClickable(false);
                    swi_wed.setClickable(false);
                    swi_thu.setClickable(false);
                    swi_fri.setClickable(false);
                    swi_sat.setClickable(false);
                    swi_sun.setClickable(false);

                    setNotification(manager, pendingIntent);
                   saveForm(swi_mon,swi_tue,swi_wed,swi_thu,swi_fri,swi_sat,swi_sun,toggle);

                } else {
                    // The toggle is disabled
                    //disable all controls
                    chooseTime.setEnabled(true);
                    swi_mon.setClickable(true);
                    swi_tue.setClickable(true);
                    swi_wed.setClickable(true);
                    swi_thu.setClickable(true);
                    swi_fri.setClickable(true);
                    swi_sat.setClickable(true);
                    swi_sun.setClickable(true);
                    stopNotification(manager, pendingIntent);
                }
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        final Switch swi_mon = findViewById(R.id.swi_mon);
        final Switch swi_tue = findViewById(R.id.swi_tue);
        final Switch swi_wed = findViewById(R.id.swi_wed);
        final Switch swi_thu = findViewById(R.id.swi_thu);
        final Switch swi_fri = findViewById(R.id.swi_fri);
        final Switch swi_sat = findViewById(R.id.swi_sat);
        final Switch swi_sun = findViewById(R.id.swi_sun);
        final ToggleButton toggle = findViewById(R.id.toggleButton);
        loadForm(swi_mon,swi_tue,swi_wed,swi_thu,swi_fri,swi_sat,swi_sun,toggle);
    }

    public void loadForm(Switch swi_mon, Switch swi_tue, Switch swi_wed, Switch swi_thu, Switch swi_fri, Switch swi_sat, Switch swi_sun, ToggleButton toggle){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Integer hour = prefs.getInt("hour",0);
        Integer min = prefs.getInt("minute",0);
        String time = hour.toString()+":"+min.toString();
        chooseTime.setText(time);
        swi_mon.setChecked(prefs.getBoolean("mon",false));
        swi_tue.setChecked(prefs.getBoolean("tue",false));
        swi_wed.setChecked(prefs.getBoolean("wed",false));
        swi_thu.setChecked(prefs.getBoolean("thu",false));
        swi_fri.setChecked(prefs.getBoolean("fri",false));
        swi_sat.setChecked(prefs.getBoolean("sat",false));
        swi_sun.setChecked(prefs.getBoolean("sun",false));
        toggle.setChecked(prefs.getBoolean("on_off",false));
        if(prefs.getBoolean("on_off",false)){
            swi_mon.setClickable(false);
            swi_tue.setClickable(false);
            swi_wed.setClickable(false);
            swi_thu.setClickable(false);
            swi_fri.setClickable(false);
            swi_sat.setClickable(false);
            swi_sun.setClickable(false);
            chooseTime.setEnabled(false);
        }
        Log.d("Run","Loaded");
    }

    public void setNotification(AlarmManager manager, PendingIntent pendingIntent){

        NotificationHelper helper = new NotificationHelper(this);
        String title = "Cocktail Time";
        String content = "It's Cocktail Time!";

        Notification.Builder builder=helper.getCocktail_channel_notification(title, content);

        helper.getManager().notify(new Random().nextInt(),builder.build());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        System.out.println(selectedHour);
        System.out.println(selectedMinute);
        calendar2.set(Calendar.HOUR_OF_DAY, 9);
        calendar2.set(Calendar.MINUTE, 36);
        calendar2.set(Calendar.SECOND, 0);

//        Intent intent = new Intent(getApplicationContext(),NotificationHelper.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        System.out.println(calendar2.getTimeInMillis());

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void stopNotification(AlarmManager manager, PendingIntent pendingIntent){
        manager.cancel(pendingIntent);
    }

    public void saveForm(Switch mon, Switch tue, Switch wed, Switch thu, Switch fri, Switch sat, Switch sun, ToggleButton toggle){
        //save form
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("hour", selectedHour);
        editor.putInt("minute", selectedMinute);
        editor.putBoolean("mon", mon.isChecked());
        editor.putBoolean("tue", tue.isChecked());
        editor.putBoolean("wed", wed.isChecked());
        editor.putBoolean("thu", thu.isChecked());
        editor.putBoolean("fri", fri.isChecked());
        editor.putBoolean("sat", sat.isChecked());
        editor.putBoolean("sun", sun.isChecked());
        editor.putBoolean("on_off", toggle.isChecked());
        editor.apply();
        Log.d("Run","Saved");
    }


}
