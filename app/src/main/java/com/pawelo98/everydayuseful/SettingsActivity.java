package com.pawelo98.everydayuseful;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {

    private static final String SCREEN_BRIGHTNESS_VALUE_PREFIX = "Current device screen brightness value is ";

    boolean obrocone;
    boolean credits;
    ToggleButton toggleButton;
    String[] listaCzasow = {"48 godzin", "24 godziny", "3 godziny", "1 godzinę"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sp = getSharedPreferences("spinner_czasu", MODE_PRIVATE);
        int index = sp.getInt("pozycja", 0);

        Spinner opcje = findViewById(R.id.spinner1);
        if (opcje != null) {
            opcje.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (obrocone) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Wysyłanie powiadomienia " + listaCzasow[position] + " przed wizytą", Toast.LENGTH_LONG);
                        LinearLayout layout = (LinearLayout) toast.getView();
                        TextView tv = (TextView) layout.getChildAt(0);
                        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

                        SharedPreferences sp = getSharedPreferences("spinner_czasu", MODE_PRIVATE);
                        SharedPreferences.Editor spe = sp.edit();
                        spe.putInt("pozycja", position);
                        spe.apply();

                        toast.show();
                    } else obrocone = true;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaCzasow);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            opcje.setAdapter(adapter);
            opcje.setSelection(index);
        }

        final TextView text4 = findViewById(R.id.text4);
        SeekBar seekBar = findViewById(R.id.seekbar1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                Context context = getApplicationContext();

                boolean canWriteSettings = Settings.System.canWrite(context);

                if(canWriteSettings) {

                    int screenBrightnessValue = i*255/100;
                    text4.setText(SCREEN_BRIGHTNESS_VALUE_PREFIX + screenBrightnessValue + "/255");

                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue*4);
                } else
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        int currBrightness = Settings.System.getInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0);
        text4.setText( SCREEN_BRIGHTNESS_VALUE_PREFIX + currBrightness);
        seekBar.setProgress(currBrightness);
    }

    @Override
    public void onBackPressed()
    {
        final Intent intencja = new Intent(this, MainActivity.class);
        startActivity(intencja);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);

        SharedPreferences sp = getSharedPreferences("spinner_czasu", MODE_PRIVATE);
        intent.putExtra("credits", credits);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
