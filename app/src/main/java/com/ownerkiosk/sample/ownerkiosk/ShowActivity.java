package com.ownerkiosk.sample.ownerkiosk;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;


public class ShowActivity extends ActionBarActivity {
    String url="";
    Bitmap bmp=null;
    ImageView iv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        iv=(ImageView)findViewById(R.id.iv);


        Button refresh = ((Button)findViewById(R.id.refresh));
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/semibold.otf");
        refresh.setTypeface(type);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new mytask().execute();
            }
        });
        showImage();
    }

   public void showImage()
   {
       AsyncHttpClient client = new AsyncHttpClient();
       RequestParams params = new RequestParams();
       params.put("action", "getframe");

       client.post("http://mevintech.esy.es/ventura/index.php/api", params, new AsyncHttpResponseHandler() {

           @Override
           public void onStart() {
               // called before request is started
               Toast.makeText(getApplicationContext(), "getting live image... Please wait", Toast.LENGTH_LONG).show();
           }

           @Override
           public void onSuccess(int statusCode, Header[] headers, byte[] response) {
               // called when response HTTP status is "200 OK"
               String res=new String(response);
               try {
                   JSONObject root=new JSONObject(res);
                   String status=root.optString("status");
                   if (status.equals("1"))
                   {
                       url=root.optString("link");
                       new mytask().execute();

                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }

           @Override
           public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
               // called when response HTTP status is "4XX" (eg. 401, 403, 404)
           }

           @Override
           public void onRetry(int retryNo) {
               // called when request is retried
           }
       });
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
