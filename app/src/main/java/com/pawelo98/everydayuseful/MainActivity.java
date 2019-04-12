package com.pawelo98.everydayuseful;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    int backButtonCount = 0;
    boolean credits = true;

    String plikZapisu = "telephones.txt";
    FileOutputStream os;
    String[] telephones = {"904-503-142", "504-001-301"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            credits = extras.getBoolean("credits");
        } else {
            credits = true;
        }

        // zapisuje numery telefonów do pliku tekstowego
        try {
            os = openFileOutput(plikZapisu, Context.MODE_PRIVATE);
            String data = "";
            for(int i=0; i<telephones.length; i++) {
                data += telephones[i];
                data += "\n";
            }
            os.write(data.getBytes());
            os.close();
        } catch (Exception e) { e.printStackTrace(); }

        final Intent intencja1 = new Intent(this, ToDoListActivity.class);
        Button przycisk1 = findViewById(R.id.button1);
        przycisk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intencja1);
            }
        });

        final Intent intencja3 = new Intent(this, SettingsActivity.class);
        Button przycisk3 = findViewById(R.id.button2);
        przycisk3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intencja3);
            }
        });

        final Intent intencja2 = new Intent(this, CreditsActivity.class);
        Button przycisk2 = findViewById(R.id.button3);
        intencja2.putExtra("credits", credits);
        przycisk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intencja2);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            credits = extras.getBoolean("credits");
        } else {
            credits = true;
        }

        final Intent intencja2 = new Intent(this, CreditsActivity.class);
        Button przycisk2 = findViewById(R.id.button3);
        intencja2.putExtra("credits", credits);
        przycisk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intencja2);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    public void exitApp(View view) {
        finish();
    }
}
