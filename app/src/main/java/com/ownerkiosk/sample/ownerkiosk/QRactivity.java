package com.ownerkiosk.sample.ownerkiosk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;


public class QRactivity extends ActionBarActivity {
    ImageView iv;
    String url = "";
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);
        url = getIntent().getExtras().getString("url");
        iv = (ImageView) findViewById(R.id.iv);
        ((Button)findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ""+url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        new mytask().execute();
    }

    class mytask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL urls = new URL("" + url);
                bmp = BitmapFactory.decodeStream(urls.openConnection().getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            iv.setImageBitmap(bmp);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
