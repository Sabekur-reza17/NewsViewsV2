package com.sabekur2017.newsviewsv2.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
    private List<String> API_LIST = new ArrayList<>();
    ApiService apiService;
    private DatePicker picker;
    private AutoCompleteTextView searchAuto;
    private Button datePickerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datePickerBtn=findViewById(R.id.search_go);

       searchAuto=findViewById(R.id.search_button_auto);

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

        // api list
        initApiList();
        loadNews();
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = searchAuto.getText().toString().trim();
                Log.d("dvalue",value);
                if (!TextUtils.isEmpty(value)) {
                    Intent intent = new Intent(MainActivity.this, NumberDateActivity.class);
                    intent.putExtra("value", value);
                    startActivity(intent);

                }
            }
        });

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
        MenuInflater searchInfalter=getMenuInflater();
        searchInfalter.inflate(R.menu.search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search_id);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setInputType(InputType.TYPE_CLASS_DATETIME);
        searchView.setQueryHint("Search number");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                number = query;
                Intent intent=new Intent(getApplicationContext(),NumberActivity.class);
                intent.putExtra("number",number);
                startActivity(intent);
                Log.d("search","searchview");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.date_number:
                searchDate();
                Log.d("date", "date selected");
                Toast.makeText(this, "date selected", Toast.LENGTH_SHORT).show();
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




    private void initApiList() {
        API_LIST.add(AppConstant.API_1);
        API_LIST.add(AppConstant.API_2);
        API_LIST.add(AppConstant.API_3);
        API_LIST.add(AppConstant.API_4);
        API_LIST.add(AppConstant.API_5);
        API_LIST.add(AppConstant.API_6);
        API_LIST.add(AppConstant.API_7);
        API_LIST.add(AppConstant.API_8);
        API_LIST.add(AppConstant.API_9);
        API_LIST.add(AppConstant.API_10);
    }
    private void loadNews() {


        apiService =new RestApiBuilder().getService();

        for (int i = 0; i < 10; i++) {
            load(API_LIST.get(i));
        }

    }
    private void load(String url) {
        apiService.getNewsResponces(url + AppConstant.API_KEY).enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {

                if (response.isSuccessful()) {

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
    }
    private void searchDate() {
        picker = new DatePicker(this);
        int curYear = picker.getYear();
        int curMonth = picker.getMonth()+1;
        int curDayOfMonth = picker.getDayOfMonth();
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                searchAuto.setText((month+1)+"/"+dayOfMonth); //dayOfMonth+"/"+(month+1)+"/"+year
            }
        }, curYear, curMonth, curDayOfMonth);
        pickerDialog.show();
    }

}
