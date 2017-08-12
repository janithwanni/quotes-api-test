package com.janithwannidev.quotesapi_test;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JanithWanni on 12/08/2017.
 */

public class HTTPHandler extends AsyncTask<Void,Void,Void> {

    public static final int JSON = 0;
    public static final int STRING = 1;
    public static final int OTHER = 2;

    public static final int GET = 3;
    public static final int POST = 4;
    private HashMap<String,String> postParams;
    private ProgressDialog progressDialog;
    private String appURL;
    private String endPoint;
    private int sendType;
    private int returnType;
    private int requestMethod;
    private String response;

    public HTTPHandler(String cappURL, String cendPoint, int crequestMethod, int csendType, int creturnType, ProgressDialog cprogressDialog,
                       HashMap<String,String> cpostParams){
        appURL = cappURL;
        endPoint = cendPoint;
        sendType = csendType;
        returnType = creturnType;
        progressDialog = cprogressDialog;
        postParams = cpostParams;
        requestMethod = crequestMethod;
    }
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //Log.i(this.getClass().getCanonicalName(),"txinput is "+txtinput);
            URL url = new URL("https://cloud-sql-176109.appspot.com/data");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            switch (requestMethod){
                case(GET):
                    connection.setRequestMethod("GET");
                    // no fucking get requests in this app yo
                    break;
                case(POST):
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setReadTimeout(15000);
                    connection.setConnectTimeout(15000);
                    OutputStream outputStream = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    writer.write(getPostDataString(postParams));
                    writer.flush();writer.close();
                    outputStream.close();
                    int responseCode = connection.getResponseCode();
                    Log.i(this.getClass().getCanonicalName(),responseCode+"this is the response");
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        String line;
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                        while((line=br.readLine()) != null){
                            response += line;
                        }
                    }else{
                        response = "ERROR";
                    }
                    break;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);

    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
