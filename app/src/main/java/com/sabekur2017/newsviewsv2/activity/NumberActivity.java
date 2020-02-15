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

public class NumberActivity extends AppCompatActivity {
    private static final String TAG = NumberActivity.class.getSimpleName();
    TextView numberTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        numberTxt=findViewById(R.id.numberTxt);
        Intent intent=getIntent();
        final String number=intent.getExtras().getString("number");
        numberTxt.setText(number);
        new GetNumberData().execute();
    }
    class GetNumberData extends AsyncTask<String, String, String> {

        private String numberData = "";

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(AppConstant.ANUMBER_API+MainActivity.number);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                numberData = reader.readLine();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            numberTxt.setText(numberData);
            Log.d(TAG, "NUMBER info: " + numberData);
        }
    }
}
