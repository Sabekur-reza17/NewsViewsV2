package com.sabekur2017.newsviewsv2.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.sabekur2017.newsviewsv2.R;
import com.sabekur2017.newsviewsv2.models.NewsModel;
import com.sabekur2017.newsviewsv2.service.ApiService;
import com.sabekur2017.newsviewsv2.service.RestApiBuilder;
import com.sabekur2017.newsviewsv2.utility.AppConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = MainActivity.class.getSimpleName();
    DrawerLayout drawerLayout;
    public static String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView nav_view = findViewById(R.id.navigation_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_about:
                        Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_exit:
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

     // api call news api
        ApiService apiService=new RestApiBuilder().getService();
        //String urlString="top-headlines?sources=google-news-uk&apiKey=29a31e34dd694e0ebd7e9cd4b3cdb99a";
        String urlString1=String.format("top-headlines?sources=google-news-uk&apiKey=%s",getString(R.string.news_api));
       // String urlString2 = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s", MainActivity.latitude,MainActivity.longitude,MainActivity.units,getString(R.string.weather_api));
        Call<NewsModel> call=apiService.getNewsResponces(urlString1);
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if(response.isSuccessful()){
                    NewsModel newsModel=response.body();
                    int totalpage=newsModel.getTotalResults();
                    Log.d(TAG,totalpage+" get");
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

            }
        });
        new GetNumberData().execute();
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
    class GetNumberData extends AsyncTask<String, String, String> {

        private String numberData = "";

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(AppConstant.ANUMBER_API_DATE);
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
           // numberInfoTextView.setText(result);
            Log.d(TAG,"NUMBERDate info: "+numberData);
        }
    }




}
