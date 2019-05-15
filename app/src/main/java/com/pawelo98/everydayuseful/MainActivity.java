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
    String plikZapisuNazwiska = "names.txt";
    String plikZapisuMaile = "emails.txt";
    String plikZapisuAdresy = "addresses.txt";
    String plikZapisuSzkolen = "szkolenia.txt";
    FileOutputStream os;

    String[] companies = {"Electronic Arts", "Steam"};
    String[] hours = {"5", "14"};
    String[] minutes = {"19", "49"};
    int[] indeksy = {0, 1};
    String[] telephones = {"904-503-142", "504-001-301"};
    String[] names = {"Janic Paweł", "Grzesiak Marian"};
    String[] emails = {"pjanic98@gmail.com", "papajoz@onet.pl"};
    String[] cities = {"Warszawa", "Wrocław"};
    String[] streets = {"Główna", "Boczna"};
    String[] numbers = {"8", "2/5"};

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

            os = openFileOutput(plikZapisuNazwiska, Context.MODE_PRIVATE);
            data = "";
            for(int i=0; i<names.length; i++) {
                data += names[i];
                data += "\n";
            }
            os.write(data.getBytes());
            os.close();

            os = openFileOutput(plikZapisuMaile, Context.MODE_PRIVATE);
            data = "";
            for(int i=0; i<emails.length; i++) {
                data += emails[i];
                data += "\n";
            }
            os.write(data.getBytes());
            os.close();

            os = openFileOutput(plikZapisuAdresy, Context.MODE_PRIVATE);
            data = "";
            int j=0;
            int i=0;
            while(i<cities.length) {
                if(j%3==0) data += cities[i];
                if(j%3==1) data += streets[i];
                if(j%3==2) data += numbers[i];
                data += "\n";
                j++;
                if(j%3==0) i++;
            }
            os.write(data.getBytes());
            os.close();

            os = openFileOutput(plikZapisuSzkolen, Context.MODE_PRIVATE);
            data = "";
            int count = 0;
            for(i=0; i<companies.length; i++) {
                if(companies[i]!=null) count++;
                else {
                    for(j=i; j<companies.length; j++) {
                        if(companies[j]!=null) {
                            companies[i] = companies[j];
                            companies[j] = null;
                            hours[i] = hours[j];
                            hours[j] = null;
                            minutes[i] = minutes[j];
                            minutes[j] = null;
                            indeksy[i] = indeksy[j];
                            indeksy[j] = 0;
                            count++;
                            break;
                        }
                    }
                }
            }
            j=0;
            i=0;
            while(i<count) {
                if(j%4==0) data += companies[i];
                if(j%4==1) data += hours[i];
                if(j%4==2) data += minutes[i];
                if(j%4==3) data += indeksy[i];
                data += "\n";
                j++;
                if(j%4==0) i++;
            }
            os.write(data.getBytes());
            os.close();
        } catch (Exception e) { e.printStackTrace(); }

        final Intent intencja1 = new Intent(this, ActivitySzkolenia.class);
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
            System.exit(0);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    public void exitApp(View view) {
        System.exit(0);
    }
}
