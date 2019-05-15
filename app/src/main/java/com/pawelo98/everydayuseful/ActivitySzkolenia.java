package com.pawelo98.everydayuseful;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class ActivitySzkolenia extends AppCompatActivity implements LocationListener, SensorEventListener {

    // o szkoleniach
    String[] companies;
    String[] hours;
    String[] minutes;
    int[] indeksy;

    private LocationManager mLocMgr = null;
    double alt =0.0, lon = 0.0;

    private SensorManager mSrMgr = null;
    double temp = 0.0;
    boolean showTemp = true;

    // o klientach
    String[] names;
    String[] telephones;
    String[] emails;
    String[] cities;
    String[] streets;
    String[] numbers;

    String plikZapisuTelefon = "telephones.txt";
    String plikZapisuNazwiska = "names.txt";
    String plikZapisuMaile = "emails.txt";
    String plikZapisuAdresy = "addresses.txt";
    String plikZapisuSzkolen = "szkolenia.txt";
    FileOutputStream os;

    private class LVitem {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        Button button1;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        boolean[] zazn_pozycje;
        LVitem myLVitem;
        String[] lista;
        boolean[] czyJest;

        public MyAdapter(String[] lista) {
            super();
            zazn_pozycje = new boolean[lista.length];
            this.czyJest = new boolean[100];
            this.lista = lista;
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            int count = 0;
            for(int i=0; i<lista.length; i++) {
                if(lista[i]!=null) count++; czyJest[i] = true;
            }
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View listItemView, ViewGroup parent) {

            if(listItemView == null) {
                listItemView = layoutInflater.inflate(R.layout.list_row, null);
                myLVitem = new LVitem();
                myLVitem.tv1 = listItemView.findViewById(R.id.row_tv1);
                myLVitem.tv2 = listItemView.findViewById(R.id.row_tv2);
                myLVitem.tv3 = listItemView.findViewById(R.id.row_tv3);
                myLVitem.button1 = listItemView.findViewById(R.id.button1);
                listItemView.setTag(myLVitem);
            }
            else myLVitem = (LVitem) listItemView.getTag();

            myLVitem.tv1.setText(companies[position] + " (" + position + ")");
            myLVitem.tv2.setText(hours[position] + ":" + minutes[position]);
            myLVitem.tv3.setText(cities[indeksy[position]] + ", ul. " + streets[indeksy[position]] + " " + numbers[indeksy[position]]);
            // nawigacja
            myLVitem.button1.setText("Nawigacja");
            myLVitem.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                final Intent ii = new Intent(ActivitySzkolenia.this, ActivityGPS.class);
                Bundle dane = new Bundle();
                dane.putDouble("Alt", alt);
                dane.putDouble("Lon", lon);
                ii.putExtras(dane);
                startActivity(ii);
                }
            });

            final View listItemNew = listItemView;

            listItemView.setOnTouchListener(new OnSwipeTouchListener(ActivitySzkolenia.this) {

                public void onSwipeRight() {
                    Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                    mailIntent.setData(Uri.parse("mailto:"));
                    mailIntent.putExtra(Intent.EXTRA_EMAIL, emails[indeksy[position]]);
                    mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Przypomnienie o spotkaniu");
                    if (mailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mailIntent);
                    }
                    listItemNew.setOnTouchListener(new OnSwipeTouchListener(ActivitySzkolenia.this) {

                        public void onSwipeRight() {
                            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                            mailIntent.setData(Uri.parse("mailto:"));
                            mailIntent.putExtra(Intent.EXTRA_EMAIL, emails[indeksy[position]]);
                            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Przypomnienie o spotkaniu");
                            if (mailIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mailIntent);
                            }
                        }

                        public void onSwipeLeft() {
                            final Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + telephones[indeksy[position]]));
                            smsIntent.putExtra("sms_body", "Przypomnienie o spotkaniu");
                            startActivity(smsIntent);
                        }
                    });
                }

                public void onSwipeLeft() {
                    final Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + telephones[indeksy[position]]));
                    smsIntent.putExtra("sms_body", "Przypomnienie o spotkaniu");
                    startActivity(smsIntent);
                    listItemNew.setOnTouchListener(new OnSwipeTouchListener(ActivitySzkolenia.this) {
                        public void onSwipeRight() {
                            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                            mailIntent.setData(Uri.parse("mailto:"));
                            mailIntent.putExtra(Intent.EXTRA_EMAIL, emails[indeksy[position]]);
                            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Przypomnienie o spotkaniu");
                            if (mailIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mailIntent);
                            }
                        }

                        public void onSwipeLeft() {
                            final Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + telephones[indeksy[position]]));
                            smsIntent.putExtra("sms_body", "Przypomnienie o spotkaniu");
                            startActivity(smsIntent);
                        }
                    });
                }
            });

            return listItemView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szkolenia);

        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        mSrMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        // odczyt z pliku tekstowego
        if(savedInstanceState == null) {
            try {
                showTemp = true;
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        openFileInput(plikZapisuTelefon)));
                BufferedReader inputReader2 = new BufferedReader(new InputStreamReader(
                        openFileInput(plikZapisuNazwiska)));
                BufferedReader inputReader3 = new BufferedReader(new InputStreamReader(
                        openFileInput(plikZapisuMaile)));
                BufferedReader inputReader4 = new BufferedReader(new InputStreamReader(
                        openFileInput(plikZapisuAdresy)));
                BufferedReader inputReader5 = new BufferedReader(new InputStreamReader(
                        openFileInput(plikZapisuSzkolen)));
                String inputString;
                String inputString2;
                String inputString3;
                String inputString4;
                String inputString5;

                telephones = new String[100];
                int i = 0;
                while ((inputString = inputReader.readLine()) != null) {
                    telephones[i] = inputString;
                    i++;
                }

                names = new String[100];
                i = 0;
                while ((inputString2 = inputReader2.readLine()) != null) {
                    names[i] = inputString2;
                    i++;
                }

                emails = new String[100];
                i = 0;
                while ((inputString3 = inputReader3.readLine()) != null) {
                    emails[i] = inputString3;
                    i++;
                }

                cities = new String[100];
                streets = new String[100];
                numbers = new String[100];
                i = 0;
                int j = 0;
                while ((inputString4 = inputReader4.readLine()) != null) {
                    if (j % 3 == 0) cities[i] = inputString4;
                    if (j % 3 == 1) streets[i] = inputString4;
                    if (j % 3 == 2) numbers[i] = inputString4;
                    j++;
                    if (j % 3 == 0) i++;
                }

                companies = new String[100];
                hours = new String[100];
                minutes = new String[100];
                indeksy = new int[100];
                i = 0;
                j = 0;
                while ((inputString5 = inputReader5.readLine()) != null) {
                    if (j % 4 == 0) companies[i] = inputString5;
                    if (j % 4 == 1) hours[i] = inputString5;
                    if (j % 4 == 2) minutes[i] = inputString5;
                    if (j % 4 == 3) indeksy[i] = Integer.parseInt(inputString5);
                    j++;
                    if(j%4==0) i++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // tworzenie bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item1:
                        Intent a = new Intent(ActivitySzkolenia.this, ToDoListActivity.class);
                        startActivity(a);
                        break;
                    case R.id.item2:
                        break;
                }
                return false;
            }
        });

        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivitySzkolenia.this, AddSzkolenie.class);
                i.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);

                Bundle dane = new Bundle();
                dane.putStringArray("names", names);
                dane.putStringArray("telephones", telephones);
                i.putExtras(dane);

                startActivityForResult(i, 0);
            }
        });

        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivitySzkolenia.this, DeleteSzkolenie.class);
                i.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(i, 1);
            }
        });

        MyAdapter adapter = new MyAdapter(companies);
        ListView lista3 = findViewById(R.id.lista3);
        lista3.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor sensor = mSrMgr.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (sensor != null) {
            mSrMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "No Ambient Temperature Sensor !", Toast.LENGTH_LONG).show();
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        else ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) mLocMgr.removeUpdates(this);
        mSrMgr.unregisterListener(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) mLocMgr.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            temp = event.values[0];
            if(showTemp) showTemp(temp); showTemp = false;
    }

    public void showTemp(double temp) {
        Toast toast;
        if(temp<400.0) toast = Toast.makeText(this, String.format("Przy używaniu pojazdu uważaj na brak widoczności, jasność: %.2f", temp) + " lux", Toast.LENGTH_SHORT);
        else toast = Toast.makeText(this, String.format("Bardzo ciepło, uważaj na rażące słońce, jasność: %.2f", temp) + " lux", Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    @Override
    public void onProviderDisabled(String provider) {
        if(LocationManager.GPS_PROVIDER.contentEquals(provider)) finish();
    }

    @Override
    public void onLocationChanged(Location l) {
        alt = l.getLatitude();
        lon = l.getLongitude();
        MyAdapter adapter = new MyAdapter(companies);
        ListView lista3 = findViewById(R.id.lista3);
        lista3.setAdapter(adapter);
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle b) {

    }

    @Override
    protected void onActivityResult(int reqId, int resC, Intent ii) {
        if(resC == Activity.RESULT_OK && reqId == 0) {
            Bundle dane = ii.getExtras();
            String company = dane.getString("company");
            String hour = dane.getString("hour");
            String minute = dane.getString("minute");
            int indeks = dane.getInt("pos");

            int count = 0;
            while(companies[count]!=null) {
                count++;
            }
            companies[count] = company;
            hours[count] = hour;
            minutes[count] = minute;
            indeksy[count] = indeks;
            Toast.makeText(this, "Pomyślnie dodano szkolenie", Toast.LENGTH_SHORT).show();

            try {
                os = openFileOutput(plikZapisuTelefon, Context.MODE_PRIVATE);
                String data = "";
                count = 0;
                for(int i=0; i<telephones.length; i++) {
                    if(telephones[i]!=null && !telephones[i].isEmpty()) count++;
                    else {
                        for(int j=i; j<telephones.length; j++) {
                            if(telephones[j]!=null) {
                                for(int g=0; g<indeksy.length; g++) {
                                    if(indeksy[g]==j) indeksy[g]=i;
                                }
                                telephones[i] = telephones[j];
                                telephones[j] = null;
                                count++;
                                break;
                            }
                        }
                    }
                }
                for(int i=0; i<count; i++) {
                    data += telephones[i];
                    data += "\n";
                }
                os.write(data.getBytes());
                os.close();

                os = openFileOutput(plikZapisuNazwiska, Context.MODE_PRIVATE);
                data = "";
                count = 0;
                for(int i=0; i<names.length; i++) {
                    if(names[i]!=null && !names[i].isEmpty()) count++;
                    else {
                        for(int j=i; j<names.length; j++) {
                            if(names[j]!=null) {
                                names[i] = names[j];
                                names[j] = null;
                                count++;
                                break;
                            }
                        }
                    }
                }
                for(int i=0; i<count; i++) {
                    data += names[i];
                    data += "\n";
                }
                os.write(data.getBytes());
                os.close();

                os = openFileOutput(plikZapisuMaile, Context.MODE_PRIVATE);
                data = "";
                count = 0;
                for(int i=0; i<emails.length; i++) {
                    if(emails[i]!=null && !emails[i].isEmpty()) count++;
                    else {
                        for(int j=i; j<emails.length; j++) {
                            if(emails[j]!=null) {
                                emails[i] = emails[j];
                                emails[j] = null;
                                count++;
                                break;
                            }
                        }
                    }
                }
                for(int i=0; i<count; i++) {
                    data += emails[i];
                    data += "\n";
                }
                os.write(data.getBytes());
                os.close();

                os = openFileOutput(plikZapisuAdresy, Context.MODE_PRIVATE);
                data = "";
                count = 0;
                for(int i=0; i<cities.length; i++) {
                    if(cities[i]!=null) count++;
                    else {
                        for(int j=i; j<cities.length; j++) {
                            if(cities[j]!=null) {
                                cities[i] = cities[j];
                                cities[j] = null;
                                streets[i] = streets[j];
                                streets[j] = null;
                                numbers[i] = numbers[j];
                                numbers[j] = null;
                                count++;
                                break;
                            }
                        }
                    }
                }
                int j=0;
                int i=0;
                while(i<count) {
                    if(j%3==0) data += cities[i];
                    if(j%3==1) data += streets[i];
                    if(j%3==2) data += numbers[i];
                    data += "\n";
                    j++;
                    if(j%3==0) i++;
                }
                os.write(data.getBytes());
                os.close();
            } catch (Exception e) { e.printStackTrace(); }

            MyAdapter adapter = new MyAdapter(companies);
            ListView lista3 = findViewById(R.id.lista3);
            lista3.setAdapter(adapter);
        }
        else if(resC == Activity.RESULT_OK && reqId == 1) {
            Bundle dane = ii.getExtras();
            int pos = dane.getInt("pos");
            if(pos>=0 && pos<100) {
                companies[pos] = null;
                hours[pos] = null;
                minutes[pos] = null;
                indeksy[pos] = 0;
                for(int g=0; g<indeksy.length; g++) {
                    if(indeksy[g]==pos) indeksy[g]=0;
                }
                Toast.makeText(this, "Pomyślnie usunięto szkolenie o indeksie " + pos, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wprowadzono niepoprawny indeks", Toast.LENGTH_SHORT).show();
            }


            try {
                os = openFileOutput(plikZapisuTelefon, Context.MODE_PRIVATE);
                String data = "";
                int count = 0;
                for(int i=0; i<telephones.length; i++) {
                    if(telephones[i]!=null && !telephones[i].isEmpty()) count++;
                    else {
                        for(int j=i; j<telephones.length; j++) {
                            if(telephones[j]!=null) {
                                for(int g=0; g<indeksy.length; g++) {
                                    if(indeksy[g]==j) indeksy[g]=i;
                                }
                                telephones[i] = telephones[j];
                                telephones[j] = null;
                                count++;
                                break;
                            }
                        }
                    }
                }
                for(int i=0; i<count; i++) {
                    data += telephones[i];
                    data += "\n";
                }
                os.write(data.getBytes());
                os.close();

                os = openFileOutput(plikZapisuNazwiska, Context.MODE_PRIVATE);
                data = "";
                count = 0;
                for(int i=0; i<names.length; i++) {
                    if(names[i]!=null && !names[i].isEmpty()) count++;
                    else {
                        for(int j=i; j<names.length; j++) {
                            if(names[j]!=null) {
                                names[i] = names[j];
                                names[j] = null;
                                count++;
                                break;
                            }
                        }
                    }
                }
                for(int i=0; i<count; i++) {
                    data += names[i];
                    data += "\n";
                }
                os.write(data.getBytes());
                os.close();

                os = openFileOutput(plikZapisuMaile, Context.MODE_PRIVATE);
                data = "";
                count = 0;
                for(int i=0; i<emails.length; i++) {
                    if(emails[i]!=null && !emails[i].isEmpty()) count++;
                    else {
                        for(int j=i; j<emails.length; j++) {
                            if(emails[j]!=null) {
                                emails[i] = emails[j];
                                emails[j] = null;
                                count++;
                                break;
                            }
                        }
                    }
                }
                for(int i=0; i<count; i++) {
                    data += emails[i];
                    data += "\n";
                }
                os.write(data.getBytes());
                os.close();

                os = openFileOutput(plikZapisuAdresy, Context.MODE_PRIVATE);
                data = "";
                count = 0;
                for(int i=0; i<cities.length; i++) {
                    if(cities[i]!=null) count++;
                    else {
                        for(int j=i; j<cities.length; j++) {
                            if(cities[j]!=null) {
                                cities[i] = cities[j];
                                cities[j] = null;
                                streets[i] = streets[j];
                                streets[j] = null;
                                numbers[i] = numbers[j];
                                numbers[j] = null;
                                count++;
                                break;
                            }
                        }
                    }
                }
                int j=0;
                int i=0;
                while(i<count) {
                    if(j%3==0) data += cities[i];
                    if(j%3==1) data += streets[i];
                    if(j%3==2) data += numbers[i];
                    data += "\n";
                    j++;
                    if(j%3==0) i++;
                }
                os.write(data.getBytes());
                os.close();
            } catch (Exception e) { e.printStackTrace(); }

            MyAdapter adapter = new MyAdapter(companies);
            ListView lista3 = findViewById(R.id.lista3);
            lista3.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_right_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case R.id.item1:
                Intent a = new Intent(this, SettingsActivity.class);
                startActivity(a);
                break;
            case R.id.item2:
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(menu);
    }

    @Override
    public void onBackPressed()
    {
        final Intent intencja = new Intent(this, MainActivity.class);
        startActivity(intencja);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        try {
            os = openFileOutput(plikZapisuTelefon, Context.MODE_PRIVATE);
            String data = "";
            int count = 0;
            for(int i=0; i<telephones.length; i++) {
                if(telephones[i]!=null && !telephones[i].isEmpty()) count++;
                else {
                    for(int j=i; j<telephones.length; j++) {
                        if(telephones[j]!=null) {
                            for(int g=0; g<indeksy.length; g++) {
                                if(indeksy[g]==j) indeksy[g]=i;
                            }
                            telephones[i] = telephones[j];
                            telephones[j] = null;
                            count++;
                            break;
                        }
                    }
                }
            }
            for(int i=0; i<count; i++) {
                data += telephones[i];
                data += "\n";
            }
            os.write(data.getBytes());
            os.close();

            os = openFileOutput(plikZapisuNazwiska, Context.MODE_PRIVATE);
            data = "";
            count = 0;
            for(int i=0; i<names.length; i++) {
                if(names[i]!=null && !names[i].isEmpty()) count++;
                else {
                    for(int j=i; j<names.length; j++) {
                        if(names[j]!=null) {
                            names[i] = names[j];
                            names[j] = null;
                            count++;
                            break;
                        }
                    }
                }
            }
            for(int i=0; i<count; i++) {
                data += names[i];
                data += "\n";
            }
            os.write(data.getBytes());
            os.close();

            os = openFileOutput(plikZapisuMaile, Context.MODE_PRIVATE);
            data = "";
            count = 0;
            for(int i=0; i<emails.length; i++) {
                if(emails[i]!=null && !emails[i].isEmpty()) count++;
                else {
                    for(int j=i; j<emails.length; j++) {
                        if(emails[j]!=null) {
                            emails[i] = emails[j];
                            emails[j] = null;
                            count++;
                            break;
                        }
                    }
                }
            }
            for(int i=0; i<count; i++) {
                data += emails[i];
                data += "\n";
            }
            os.write(data.getBytes());
            os.close();

            os = openFileOutput(plikZapisuAdresy, Context.MODE_PRIVATE);
            data = "";
            count = 0;
            for(int i=0; i<cities.length; i++) {
                if(cities[i]!=null) count++;
                else {
                    for(int j=i; j<cities.length; j++) {
                        if(cities[j]!=null) {
                            cities[i] = cities[j];
                            cities[j] = null;
                            streets[i] = streets[j];
                            streets[j] = null;
                            numbers[i] = numbers[j];
                            numbers[j] = null;
                            count++;
                            break;
                        }
                    }
                }
            }
            int j=0;
            int i=0;
            while(i<count) {
                if(j%3==0) data += cities[i];
                if(j%3==1) data += streets[i];
                if(j%3==2) data += numbers[i];
                data += "\n";
                j++;
                if(j%3==0) i++;
            }
            os.write(data.getBytes());
            os.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
