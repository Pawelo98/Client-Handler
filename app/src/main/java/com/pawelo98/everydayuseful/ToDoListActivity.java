package com.pawelo98.everydayuseful;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.support.design.widget.BottomNavigationView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.attribute.FileOwnerAttributeView;

public class ToDoListActivity extends AppCompatActivity {

    String[] names = {"Janic Paweł", "Grzesiak Marian"};
    String[] telephones;
    String[] emails = {"pjanic98@gmail.com", "papajoz@onet.pl"};

    String plikZapisu = "telephones.txt";
    FileOutputStream os;

    public enum Action {
        LR, // Left to right
        RL, // Right to left
        TB, // Top to bottom
        BT, // Bottom to top
        None // Action not found
    }
    public class SwipeDetector implements View.OnTouchListener {
        private static final int HORIZONTAL_MIN_DISTANCE = 30;
        private static final int VERTICAL_MIN_DISTANCE = 80;
        private float downX, downY, upX, upY;
        private Action mSwipeDetected = Action.None;
        public boolean swipeDetected() {
            return mSwipeDetected != Action.None;
        }
        public Action getAction() {
            return mSwipeDetected;
        }
        /**
         * Swipe detection
         */@Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                {
                    downX = event.getX();
                    downY = event.getY();
                    mSwipeDetected = Action.None;
                    return false; // allow other events like Click to be processed
                }
                case MotionEvent.ACTION_MOVE:
                {
                    upX = event.getX();
                    upY = event.getY();
                    float deltaX = downX - upX;
                    float deltaY = downY - upY;
                    // horizontal swipe detection
                    if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
                        // left or right
                        if (deltaX < 0) {
                            mSwipeDetected = Action.LR;
                            return true;
                        }
                        if (deltaX > 0) {
                            mSwipeDetected = Action.RL;
                            return true;
                        }
                    } else
                        // vertical swipe detection
                        if (Math.abs(deltaY) > VERTICAL_MIN_DISTANCE) {
                            // top or down
                            if (deltaY < 0) {
                                mSwipeDetected = Action.TB;
                                return false;
                            }
                            if (deltaY > 0) {
                                mSwipeDetected = Action.BT;
                                return false;
                            }
                        }
                    return true;
                }
            }
            return false;
        }
    }

    private class LVitem {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        Button bt1;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        LVitem myLVitem;

        public MyAdapter(String[] lista) {
            super();
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
                myLVitem.bt1 = listItemView.findViewById(R.id.button1);
                listItemView.setTag(myLVitem);
            }
            else myLVitem = (LVitem) listItemView.getTag();

            myLVitem.tv1.setText(names[position]);
            myLVitem.tv2.setText(telephones[position]);
            myLVitem.tv3.setText(emails[position]);
            myLVitem.bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephones[position]));
                    startActivity(intent);
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
        Menu menu = bottomNavigationView.getMenu();

        MyAdapter adapter = new MyAdapter(names);
        ListView lista3 = findViewById(R.id.lista3);
        lista3.setAdapter(adapter);

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

        lista3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            SwipeDetector swipeDetector = new SwipeDetector();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Wybrany klient: " + names[position], Toast.LENGTH_SHORT).show();

                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == Action.LR) {
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setData(Uri.parse(emails[position]));
                    }
                    if (swipeDetector.getAction() == Action.RL) {
                        // perform any task
                    }
                    if (swipeDetector.getAction() == Action.TB) {
                        // perform any task
                    }
                    if (swipeDetector.getAction() == Action.BT) {
                        // perform any task
                    }
                }
            }
        });
    }

}
