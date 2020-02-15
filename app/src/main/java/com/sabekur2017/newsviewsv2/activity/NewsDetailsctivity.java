package com.sabekur2017.newsviewsv2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sabekur2017.newsviewsv2.R;
import com.sabekur2017.newsviewsv2.models.Article;

public class NewsDetailsctivity extends AppCompatActivity {

    TextView titleTextView;

    TextView authorTextView;

    TextView timeTextView;

    ImageView newsPhoto;

    TextView detailsTextView;

    TextView viewMore;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detailsctivity);
        titleTextView=findViewById(R.id.titleTextView);
        authorTextView=findViewById(R.id.authorTextView);
        timeTextView=findViewById(R.id.timeTextView);
        newsPhoto=findViewById(R.id.newsPhoto);
        detailsTextView=findViewById(R.id.detailsTextView);
        viewMore=findViewById(R.id.viewMore);
        Intent intent=getIntent();
        final String titile=intent.getExtras().getString("title");
        final String author=intent.getExtras().getString("authoer");
        final String time=intent.getExtras().getString("time");
        final String imgeUrl=intent.getExtras().getString("imageUrl");
        final String description=intent.getExtras().getString("description");

        titleTextView.setText(titile);
        authorTextView.setText(author);
       timeTextView.setText(time);
        detailsTextView.setText(description);
        Glide.with(this).load(imgeUrl).apply(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_background)).into(newsPhoto);

        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "viewmore", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void viewMore(View view) {
        Toast.makeText(this, "viewmore"+article.getUrl(), Toast.LENGTH_SHORT).show();
    }
}
