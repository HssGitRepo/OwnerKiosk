package com.ownerkiosk.sample.ownerkiosk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {
String PROJECT_NUMBER="586495568918",regid;
    GoogleCloudMessaging gcm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_home);
 TextView ck = (TextView) findViewById(R.id.ck);
        registerPushNotification();
        TextView mk = (TextView) findViewById(R.id.mk);
        TextView lh = (TextView) findViewById(R.id.lh);
        TextView uh = (TextView) findViewById(R.id.uh);
        TextView cs = (TextView) findViewById(R.id.cs);
        TextView sh = (TextView) findViewById(R.id.sh);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/semibold.otf");
        mk.setTypeface(type);
        ck.setTypeface(type);
        lh.setTypeface(type);
        uh.setTypeface(type);
        cs.setTypeface(type);
        sh.setTypeface(type);
         ((LinearLayout) findViewById(R.id.create_key)).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(MainActivity.this, NewKey.class);
                 startActivity(i);
             }
         });

        ((LinearLayout)findViewById(R.id.my_keys)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,MyKeys.class);
                startActivity(i);
            }
        });

        ((LinearLayout)findViewById(R.id.spec)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,CreateSpec.class);
                startActivity(i);
            }
        });

        ((LinearLayout)findViewById(R.id.show)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ShowActivity.class);
                startActivity(i);
            }
        });

        ((LinearLayout)findViewById(R.id.lock)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("action", "doorcontrol");
                params.put("flag", "0");
                client.post("http://mevintech.esy.es/ventura/index.php/api", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                        Toast.makeText(getApplicationContext(), "please wait", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"

                        String res = new String(response);
                        try {
                            JSONObject root = new JSONObject(res);
                            String status = root.optString("status");
                            if (status.equals("1")) {

                                Toast.makeText(getApplicationContext(), "Door Locked", Toast.LENGTH_LONG).show();

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
        });

        ((LinearLayout)findViewById(R.id.unlock)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("action", "doorcontrol");
                params.put("flag", "1");
                client.post("http://mevintech.esy.es/ventura/index.php/api", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                        Toast.makeText(getApplicationContext(), "please wait", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"

                        String res = new String(response);
                        try {
                            JSONObject root = new JSONObject(res);
                            String status = root.optString("status");
                            if (status.equals("1")) {

                                Toast.makeText(getApplicationContext(), "Door UnLocked", Toast.LENGTH_LONG).show();

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
        });
    }

    public void getRegId()
    {
        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String msg = "";
                try {
                    if (gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    SharedPreferences preferences = getSharedPreferences("Registration Id",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Reg Id", regid);
                    editor.apply();

                    Log.e("GCM id", regid);

                }
                catch (IOException ex)
                {
                    Log.e("GCM id", "error "+ex);
//                    Constants.dialog=MyAlert.show(LoginActivity.this,"Sorry!","This mobile does not support this Application!",MyAlert.ERROR_TYPE);
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String s) {
                Log.e("GCM id", ""+regid);

            }
        }.execute(null, null, null);
    }

    public void registerPushNotification()
    {
        SharedPreferences preferences = getSharedPreferences("Registration Id", MODE_PRIVATE);
        if (preferences.getString("Reg Id","").equals("")) {

            getRegId();
        }
        else {
            Log.e("gcm",""+preferences.getString("Reg Id",""));
        }

    }


}
