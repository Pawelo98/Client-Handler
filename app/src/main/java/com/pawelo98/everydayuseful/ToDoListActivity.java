package com.pawelo98.everydayuseful;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.support.design.widget.BottomNavigationView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class ToDoListActivity extends AppCompatActivity{

    // o szkoleniach
    String[] companies;
    String[] hours;
    String[] minutes;
    int[] indeksy;

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

            myLVitem.tv1.setText(names[position] + " (" + position + ")");
            myLVitem.tv2.setText(telephones[position]);
            myLVitem.tv3.setText(cities[position] + ", ul. " + streets[position] + " " + numbers[position]);
            myLVitem.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephones[position]));
                    startActivity(intent);
                }
            });

            final View listItemNew = listItemView;

            listItemView.setOnTouchListener(new OnSwipeTouchListener(ToDoListActivity.this) {

                public void onSwipeRight() {
                    Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                    mailIntent.setData(Uri.parse("mailto:"));
                    mailIntent.putExtra(Intent.EXTRA_EMAIL, emails[position]);
                    mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Przypomnienie o spotkaniu");
                    if (mailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mailIntent);
                    }
                    listItemNew.setOnTouchListener(new OnSwipeTouchListener(ToDoListActivity.this) {

                        public void onSwipeRight() {
                            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                            mailIntent.setData(Uri.parse("mailto:"));
                            mailIntent.putExtra(Intent.EXTRA_EMAIL, emails[position]);
                            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Przypomnienie o spotkaniu");
                            if (mailIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mailIntent);
                            }
                        }

                        public void onSwipeLeft() {
                            final Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + telephones[position]));
                            smsIntent.putExtra("sms_body", "Przypomnienie o spotkaniu");
                            startActivity(smsIntent);
                        }
                    });
                }

                public void onSwipeLeft() {
                    final Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + telephones[position]));
                    smsIntent.putExtra("sms_body", "Przypomnienie o spotkaniu");
                    startActivity(smsIntent);
                    listItemNew.setOnTouchListener(new OnSwipeTouchListener(ToDoListActivity.this) {
                        public void onSwipeRight() {
                            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                            mailIntent.setData(Uri.parse("mailto:"));
                            mailIntent.putExtra(Intent.EXTRA_EMAIL, emails[position]);
                            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Przypomnienie o spotkaniu");
                            if (mailIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mailIntent);
                            }
                        }

                        public void onSwipeLeft() {
                            final Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + telephones[position]));
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
        setContentView(R.layout.activity_to_do_list);

        // odczyt z pliku tekstowego
        if(savedInstanceState == null) {
            try {
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
                    break;
                case R.id.item2:
                    Intent a = new Intent(ToDoListActivity.this, ActivitySzkolenia.class);
                    startActivity(a);
                    break;
            }
            return false;
            }
        });

        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ToDoListActivity.this, AddClient.class);
                i.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(i, 0);
            }
        });

        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ToDoListActivity.this, DeleteClient.class);
                i.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(i, 1);
            }
        });

        MyAdapter adapter = new MyAdapter(names);
        ListView lista3 = findViewById(R.id.lista3);
        lista3.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int reqId, int resC, Intent ii) {
        if(resC == Activity.RESULT_OK && reqId == 0) {
            Bundle dane = ii.getExtras();
            String name = dane.getString("name");
            String email = dane.getString("email");
            String telephone = dane.getString("telephone");
            String city = dane.getString("city");
            String street = dane.getString("street");
            String number = dane.getString("number");

            int count = 0;
            while(names[count]!=null) {
                count++;
            }
            names[count] = name;
            emails[count] = email;
            telephones[count] = telephone;
            cities[count] = city;
            streets[count] = street;
            numbers[count] = number;
            Toast.makeText(this, "Pomyślnie dodano klienta", Toast.LENGTH_SHORT).show();

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

                os = openFileOutput(plikZapisuSzkolen, Context.MODE_PRIVATE);
                data = "";
                count = 0;
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

            MyAdapter adapter = new MyAdapter(names);
            ListView lista3 = findViewById(R.id.lista3);
            lista3.setAdapter(adapter);
        }
        else if(resC == Activity.RESULT_OK && reqId == 1) {
            Bundle dane = ii.getExtras();
            int pos = dane.getInt("pos");
            if(pos>=0 && pos<100) {
                names[pos] = null;
                emails[pos] = null;
                telephones[pos] = null;
                cities[pos] = null;
                streets[pos] = null;
                numbers[pos] = null;
                for(int g=0; g<indeksy.length; g++) {
                    if(indeksy[g]==pos) indeksy[g]=0;
                }
                Toast.makeText(this, "Pomyślnie usunięto klienta o indeksie " + pos, Toast.LENGTH_SHORT).show();
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

                os = openFileOutput(plikZapisuSzkolen, Context.MODE_PRIVATE);
                data = "";
                count = 0;
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

            MyAdapter adapter = new MyAdapter(names);
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

            os = openFileOutput(plikZapisuSzkolen, Context.MODE_PRIVATE);
            data = "";
            count = 0;
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
    }

}
