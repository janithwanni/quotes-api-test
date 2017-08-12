package com.janithwannidev.quotesapi_test;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public TextView textView;
    public String quote_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.txt);
        textView.setText("Getting Quote");
        new Thread(){
          @Override
            public void run(){
              HTTPHANDLER t = new HTTPHANDLER();
              t.execute();
          }
        }.start();

    }
    public void updateUI(){
        textView.setText(quote_string);
    }
    private class HTTPHANDLER extends AsyncTask<Void,Void,Void>{

        public final String TAG = HTTPHANDLER.class.getCanonicalName();
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url  = new URL("http://quotes.rest/qod.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.i(TAG,"connection opened");
                connection.setRequestMethod("GET");
                InputStream in = new BufferedInputStream((connection.getInputStream()));
                Log.i(TAG,"input stream connected");
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder builder = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line).append("\n");
                }
                in.close();
                String response = builder.toString();
                Log.i(TAG,"recieved response");
                //Log.i(TAG,response);
                JSONObject root = new JSONObject(response);
                JSONObject contents = root.getJSONObject("contents");
                JSONArray quotes = contents.getJSONArray("quotes");
                for(int i =0;i<quotes.length();i++){
                    JSONObject quote = quotes.getJSONObject(i);
                    String quote_text = quote.getString("quote");
                    Log.i(TAG,"recieved quote");
                    Log.i(TAG,quote_text);
                    quote_string = quote_text;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            updateUI();
        }
    }
}


