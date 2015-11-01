package com.ownerkiosk.sample.ownerkiosk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;


public class MyKeys extends ActionBarActivity {
    ListView list_keys;
    ArrayList imgs, exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgs = new ArrayList();
        exp = new ArrayList();
        setContentView(R.layout.activity_my_keys);
        SharedPreferences sharedpreferences = getSharedPreferences("KEYS", Context.MODE_PRIVATE);
        Map<String, ?> keys = sharedpreferences.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d("map values", entry.getKey() + ": " +
                    entry.getValue().toString());
            imgs.add("" + entry.getKey());
            exp.add("" + entry.getValue().toString());

        }
        list_keys = (ListView) findViewById(R.id.keys_list);


        list_keys.setAdapter(new MyAdapter());
        list_keys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ii=new Intent(MyKeys.this,QRactivity.class);
                ii.putExtra("url",""+imgs.get(i));
                startActivity(ii);
            }
        });
    }


    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            view = inflater.inflate(R.layout.list_keys_cell, null);
            final ImageView keys_img = (ImageView) view.findViewById(R.id.key_img);
            TextView keys_text = (TextView) view.findViewById(R.id.keys_text);

            Typeface type = Typeface.createFromAsset(getAssets(), "fonts/semibold.otf");
            keys_text.setTypeface(type);

//          ImageLoader imgLoader = new ImageLoader(MyKeys.this);
//          //Log.e("Pres Path",""+Constants.Base_url + Constants.PrescriptionPath);
//          imgLoader.DisplayImage(""+imgs.get(i) , R.drawable.ic_launcher, keys_img);


            new mytask(i, keys_img).execute();
            Log.e("path", "" + imgs.get(i));
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(Long.parseLong(""+exp.get(i))*1000L);
            String date = DateFormat.format("dd-MM-yyyy", cal).toString();

            keys_text.setText("" + date);
            return view;
        }
    }

    class mytask extends AsyncTask<String, String, String> {
        int i;
        ImageView keys_img;
        Bitmap bmp;

        public mytask(int i, ImageView keys_img) {
            this.i = i;
            this.keys_img = keys_img;

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("" + imgs.get(i));
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            keys_img.setImageBitmap(bmp);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


}
