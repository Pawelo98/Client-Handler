package com.pawelo98.everydayuseful;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ActivitySzkolenia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szkolenia);

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
}
