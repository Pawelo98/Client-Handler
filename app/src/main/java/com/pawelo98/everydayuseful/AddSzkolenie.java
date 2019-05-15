package com.pawelo98.everydayuseful;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddSzkolenie extends AppCompatActivity {

    int pos;
    EditText editText1, editText2, editText3;
    String company, hour, minute;
    Button button;
    String[] lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_szkolenie);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(0.8*width), (int)(0.8*height));

        Intent i = getIntent();
        Bundle dane = i.getExtras();
        String[] names = dane.getStringArray("names");
        String[] telephones = dane.getStringArray("telephones");
        int count = 0;
        for(int j=0; j<names.length; j++) {
            if(names[j]!=null) count++;
        }
        lista = new String[count];
        for(int j=0; j<count; j++) {
            lista[j] = names[j] + " " + telephones[j];

        }

        Spinner opcje = findViewById(R.id.spinner);
        if(opcje!=null) {
            opcje.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pos = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    pos=99;
                }
            });
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lista);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            opcje.setAdapter(adapter);
        }

        editText1 = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                company = editText1.getText().toString();
                hour = editText2.getText().toString();
                minute = editText3.getText().toString();

                Intent ii = getIntent();
                Bundle dane = new Bundle();
                dane.putString("company", company);
                dane.putString("hour", hour);
                dane.putString("minute", minute);
                dane.putInt("pos", pos);

                ii.putExtras(dane);
                setResult(Activity.RESULT_OK, ii);
                finish();
            }
        });
    }
}
