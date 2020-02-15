package com.sabekur2017.newsviewsv2.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.sabekur2017.newsviewsv2.R;
import com.sabekur2017.newsviewsv2.adapter.NewsAdapter;
import com.sabekur2017.newsviewsv2.models.Article;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static String number = "";
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    private List<Article> articleList = new ArrayList<>();

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
        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.refreshLayout);
        NavigationView nav_view = findViewById(R.id.navigation_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_about:
                        Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
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
        ApiService apiService = new RestApiBuilder().getService();
        String urlString1 = String.format("top-headlines?sources=google-news-uk&apiKey=%s", getString(R.string.news_api));
        Call<NewsModel> call = apiService.getNewsResponces(urlString1);
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                if (response.isSuccessful()) {
                    NewsModel newsModel = response.body();

                    if (response.body().getArticles() != null) {
                        articleList.addAll(response.body().getArticles());
                        Log.d("article", "" + articleList.get(0).getUrlToImage());
                        NewsAdapter adapter = new NewsAdapter(getApplicationContext(), articleList);
                        // LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);


                    }
                    //  int totalpage=newsModel.getTotalResults();
                    // Log.d(TAG,totalpage+" get");
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to response", Toast.LENGTH_SHORT).show();
                Log.e("news_response", t.getMessage());
                refreshLayout.setRefreshing(false);

            }
        });
        new GetNumberData().execute();
        new GetNumberWithDate().execute();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_number:
                Log.d("search", "Search selected");
                Toast.makeText(this, "search selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.view_GridView:
                Log.d("gridView", "view Grid");
                Toast.makeText(this, "view Grid", Toast.LENGTH_SHORT).show();
                break;
            case R.id.view_ListView:
                Log.d("ListView", "view ListView");
                Toast.makeText(this, "view ListView", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    class GetNumberData extends AsyncTask<String, String, String> {

        private String numberData = "";

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(AppConstant.ANUMBER_API);
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

            Log.d(TAG, "NUMBER info: " + numberData);
        }
    }

    class GetNumberWithDate extends AsyncTask<String, String, String> {
        private String numberWithDate = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(AppConstant.ANUMBER_API_DATE);
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
            Log.d(TAG, "Date info: " + numberWithDate);
        }
    }

}
