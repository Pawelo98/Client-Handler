package com.pawelo98.everydayuseful;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class Credits3Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public class MyAdapter extends BaseAdapter {

        private Context ctx;

        public Integer[] id_obrazkow = {
                R.drawable.how_to_icon, R.drawable.egg, R.drawable.phone_call,
                R.drawable.egg, R.drawable.phone_call, R.drawable.how_to_icon,
                R.drawable.phone_call, R.drawable.how_to_icon, R.drawable.egg,
                R.drawable.how_to_icon, R.drawable.phone_call, R.drawable.egg,
        };

        public MyAdapter(Context c) {
            ctx = c;
        }

        public int getCount() {
            return id_obrazkow.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int pos, View cV, ViewGroup parent) {
            ImageView mV;

            if(cV == null) {
                mV = new ImageView(ctx);
                mV.setLayoutParams(new GridView.LayoutParams(200,200));
                mV.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mV.setPadding(8,8,8,8);
            }
            else mV = (ImageView) cV;

            mV.setImageResource(id_obrazkow[pos]);
            return mV;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits3);

        GridView gridView = findViewById(R.id.grid1);
        gridView.setAdapter(new MyAdapter(this));
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Wybrany item z pozycji: " + position, Toast.LENGTH_SHORT).show();
    }

    public void exitApp(View view) {
        onBackPressed();
    }
}
