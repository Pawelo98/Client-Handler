package com.pawelo98.everydayuseful;

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

public class ToDoListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    String[] names = {"Janic Paweł", "Grzesiak Marian"};
    String[] telephones;
    String[] emails = {"pjanic98@gmail.com", "papajoz@onet.pl"};

    String[] companies = {"Electronic Arts", "Steam"};
    String[] cities = {"Warszawa", "Wrocław"};
    String[] streets = {"Główna", "Boczna"};
    String[] numbers = {"8", "2/5"};

    String plikZapisu = "telephones.txt";
    FileOutputStream os;

    public enum Action {
        LR, // Left to right
        RL, // Right to left
        TB, // Top to bottom
        BT, // Bottom to top
        None // Action not found
    }

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

        public MyAdapter(String[] lista) {
            super();
            zazn_pozycje = new boolean[lista.length];
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return names.length;
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

            myLVitem.tv1.setText(names[position]);
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
                public void onSwipeTop() {
                }

                public void onSwipeRight() {
                    Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                    mailIntent.setData(Uri.parse("mailto:"));
                    mailIntent.putExtra(Intent.EXTRA_EMAIL, emails[position]);
                    mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Przypomnienie o spotkaniu");
                    if (mailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mailIntent);
                    }
                    listItemNew.setOnTouchListener(new OnSwipeTouchListener(ToDoListActivity.this) {
                        public void onSwipeTop() {
                        }

                        public void onSwipeRight() {
                            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                            mailIntent.setData(Uri.parse("mailto:"));
                            mailIntent.putExtra(Intent.EXTRA_EMAIL, emails[position]);
                            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Przypomnienie o spotkaniu");
                            if (mailIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mailIntent);
                            }
                            listItemNew.setOnTouchListener(new OnSwipeTouchListener(ToDoListActivity.this) {
                                public void onSwipeTop() {
                                }

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

                                public void onSwipeBottom() {
                                }
                            });
                        }

                        public void onSwipeLeft() {
                            final Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + telephones[position]));
                            smsIntent.putExtra("sms_body", "Przypomnienie o spotkaniu");
                            startActivity(smsIntent);
                        }

                        public void onSwipeBottom() {
                        }
                    });
                }

                public void onSwipeLeft() {
                    final Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + telephones[position]));
                    smsIntent.putExtra("sms_body", "Przypomnienie o spotkaniu");
                    startActivity(smsIntent);
                }

                public void onSwipeBottom() {
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
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput(plikZapisu)));
            String inputString;
            telephones = new String[100];
            int i = 0;
            while ((inputString = inputReader.readLine()) != null) {
                telephones[i] = inputString;
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
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

        final Intent intentExtra = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + telephones[0]));
        intentExtra.putExtra("sms_body", "We have scheduled appointment");
        Button buttonExtra = findViewById(R.id.buttonExtra);
        buttonExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentExtra);
            }
        });

        final Intent intentMIME = new Intent();
        intentMIME.setType("image/pictures/*");
        intentMIME.setAction(Intent.ACTION_GET_CONTENT);
        Button buttonMIME = findViewById(R.id.buttonMIME);
        buttonMIME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentMIME);
            }
        });

        MyAdapter adapter = new MyAdapter(telephones);
        ListView lista3 = findViewById(R.id.lista3);
        lista3.setAdapter(adapter);

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

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
