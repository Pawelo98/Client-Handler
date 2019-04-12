package com.pawelo98.everydayuseful;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CreditsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String[] creditsArr = {"Freepik", "Roundicons", "Freebies", "Smashicon"};
    boolean showCredits = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        final Intent intencja1 = new Intent(this, Credits3Activity.class);
        Button przycisk1 = findViewById(R.id.button1);
        przycisk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intencja1);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            showCredits = extras.getBoolean("credits");
        } else {
            showCredits = true;
        }

        if(showCredits) {
            ListView lista1 = findViewById(R.id.lista1);
            lista1.setOnItemClickListener(this);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, creditsArr);
            lista1.setAdapter(adapter1);
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast toast = Toast.makeText(getApplicationContext(), "Icon made by " + creditsArr[position] + " from www.flaticon.com", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout layout = (LinearLayout) toast.getView();
        TextView tv = (TextView) layout.getChildAt(0);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        toast.show();
    }
}
