package com.ownerkiosk.sample.ownerkiosk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class CreateSpec extends ActionBarActivity {
String str="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spec);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/semibold.otf");
        Button generate =  ((Button)findViewById(R.id.generate));
        generate.setTypeface(type);
       generate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               str = ((EditText) findViewById(R.id.specs)).getText().toString();

               AsyncHttpClient client = new AsyncHttpClient();
               RequestParams params = new RequestParams();
               params.put("action", "generateqrspec");
               params.put("message", "" + ((EditText) findViewById(R.id.specs)).getText().toString());
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
                               ((EditText) findViewById(R.id.specs)).setText("");
                               String url = root.optString("qrurl");
                               Intent sendIntent = new Intent();
                               sendIntent.setAction(Intent.ACTION_SEND);
                               sendIntent.putExtra(Intent.EXTRA_TEXT, "" + url);
                               sendIntent.setType("text/plain");
                               startActivity(sendIntent);


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
