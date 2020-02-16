package com.sabekur2017.newsviewsv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sabekur2017.newsviewsv2.R;
import com.sabekur2017.newsviewsv2.activity.NewsDetailsctivity;
import com.sabekur2017.newsviewsv2.listener.ItemClickListener;
import com.sabekur2017.newsviewsv2.models.Article;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context context;
    private List<Article> articleList;


   public NewsAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, null);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        // Set image using Glide Library
        Glide.with(context).load(articleList.get(position).getUrlToImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground))
                .into(holder.newsImage);

        holder.newsTitle.setText(articleList.get(position).getTitle());
        holder.publishedTime.setText(articleList.get(position).getPublishedAt());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                Log.d("item","Item clicked");
                Intent intent=new Intent(context, NewsDetailsctivity.class);
                intent.putExtra("title",articleList.get(position).getTitle());
                intent.putExtra("imageUrl",articleList.get(position).getUrlToImage());
                intent.putExtra("description",articleList.get(position).getDescription());
                intent.putExtra("authoer",articleList.get(position).getAuthor());
                intent.putExtra("time",articleList.get(position).getPublishedAt());
                intent.putExtra("url",articleList.get(position).getUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView newsImage;
        TextView newsTitle, publishedTime;
        private ItemClickListener itemClickListener;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsImage);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            publishedTime = itemView.findViewById(R.id.publishedTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.itemClick(v,getLayoutPosition());

        }
        public void setItemClickListener(ItemClickListener clickListener){
            this.itemClickListener=clickListener;
        }
    }
}
