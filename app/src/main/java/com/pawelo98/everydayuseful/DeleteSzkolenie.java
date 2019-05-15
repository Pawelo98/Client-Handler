package com.pawelo98.everydayuseful;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DeleteSzkolenie extends AppCompatActivity {

    EditText editText;
    Button button;
    String indeks;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_szkolenie);
        getWindow().setLayout(1000, 500);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indeks = editText.getText().toString();
                try
                {
                    pos = Integer.parseInt(indeks);
                }
                catch (NumberFormatException nfe)
                {
                    pos = 99;
                    System.out.println("NumberFormatException: " + nfe.getMessage());
                }

                Intent ii = getIntent();
                Bundle dane = new Bundle();
                dane.putInt("pos", pos);

                ii.putExtras(dane);
                setResult(Activity.RESULT_OK, ii);
                finish();
            }
        });
    }
}
