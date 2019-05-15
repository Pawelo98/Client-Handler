package com.pawelo98.everydayuseful;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddClient extends AppCompatActivity {

    EditText editText1, editText2, editText3, editText4, editText5, editText6;
    Button button;
    String name, telephone, email, city, street, number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(0.8*width), (int)(0.8*height));

        editText1 = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText1.getText().toString();
                email = editText2.getText().toString();
                telephone = editText3.getText().toString();
                city = editText4.getText().toString();
                street = editText5.getText().toString();
                number = editText6.getText().toString();

                Intent ii = getIntent();
                Bundle dane = new Bundle();
                dane.putString("name", name);
                dane.putString("email", email);
                dane.putString("telephone", telephone);
                dane.putString("city", city);
                dane.putString("street", street);
                dane.putString("number", number);

                ii.putExtras(dane);
                setResult(Activity.RESULT_OK, ii);
                finish();
            }
        });
    }
}
