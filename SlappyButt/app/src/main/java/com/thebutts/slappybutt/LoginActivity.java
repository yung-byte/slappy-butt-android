package com.thebutts.slappybutt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupUiEvents();
    }

    void setupUiEvents() {
        Button firstButton = (Button) findViewById(R.id.btn_login);
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.onClickStartLogin((Button) v);
            }
        });
    }

    private void onClickStartLogin(Button b) {
        String username = ((EditText)findViewById(R.id.editText4)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText2)).getText().toString();

        if(username.length() >= 1 && password.length() >= 1) {

            new PostDataTask().execute(new String[]{username, password});

            Intent intent = new Intent(this, FreeSlappingActivity.class);
            startActivity(intent);


        }
        else if (username.length() < 1 && password.length() < 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("All fields must be filled!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://slappybutt.azurewebsites.net/api/users/login");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("Username", params[0]));
                nameValuePairs.add(new BasicNameValuePair("Password", params[1]));
                nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));

                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                httpPost.setHeader("Accept", "application/x-www-form-urlencoded");

                HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost);
                String responseStr = EntityUtils.toString(response.getEntity());
                Log.d(getClass().getSimpleName(), responseStr);
                //ResponseHandler<String> handler = new BasicResponseHandler();
                //String result =httpClient.execute(httpPost,handler);

                final int statusCode = response.getStatusLine().getStatusCode();
                Log.d(getClass().getSimpleName(), "Status code " + statusCode + " for URL " + httpPost.getURI());


            }
            catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {

            }
            else {

            }
        }
    }
}