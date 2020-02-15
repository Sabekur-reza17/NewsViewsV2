package com.sabekur2017.newsviewsv2.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sabekur2017.newsviewsv2.R;
import com.sabekur2017.newsviewsv2.utility.AppConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NumberDateActivity extends AppCompatActivity {
    private static final String TAG = NumberDateActivity.class.getSimpleName();
    TextView numberDateInfo;
     String datevalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_date);
        numberDateInfo=findViewById(R.id.number_api_result);
        Intent intent=getIntent();
        datevalue=intent.getExtras().getString("value");
        numberDateInfo.setText(datevalue);
        Log.d("datekey"," "+datevalue);
        new GetNumberWithDate().execute();
    }
    class GetNumberWithDate extends AsyncTask<String, String, String> {
        private String numberWithDate = "";


        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(AppConstant.ANUMBER_API+datevalue+"/date");
                Log.d("dateurl",url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                numberWithDate = reader.readLine();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // super.onPostExecute(s);
            numberDateInfo.setText(numberWithDate);
            Log.d(TAG, "Date info: " + numberWithDate);
        }
    }
}
