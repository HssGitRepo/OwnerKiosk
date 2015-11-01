package com.ownerkiosk.sample.ownerkiosk;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class NewKey extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_key);

        TextView generate = (TextView) findViewById(R.id.generate);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/semibold.otf");
        generate.setTypeface(type);
                ((Button) findViewById(R.id.generate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("action", "generatekey");
                params.put("validity", "" + ((EditText) findViewById(R.id.editText)).getText().toString());
                client.post("http://mevintech.esy.es/ventura/index.php/api", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                        Toast.makeText(getApplicationContext(), "Creating key please wait", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        Toast.makeText(getApplicationContext(), "Key created, Please check in My keys.", Toast.LENGTH_LONG).show();
                        String res = new String(response);
                        try {
                            JSONObject root = new JSONObject(res);
                            String status = root.optString("status");
                            if (status.equals("1")) {
                                String url = root.optString("keyurl");
                                String expiry = root.optString("expirytime");
                                SharedPreferences sharedpreferences = getSharedPreferences("KEYS", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString(url, expiry);
                                editor.commit();
                                NewKey.this.finish();
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


}
