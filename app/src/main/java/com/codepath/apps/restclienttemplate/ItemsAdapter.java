package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    public ItemsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tweets.get(position));
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    public void add_all(List<Tweet> twits){
        tweets.addAll(twits);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView avatar;
        TextView name;
        TextView handle;
        TextView tweet_text;
        TextView age;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            handle = itemView.findViewById(R.id.at_name);
            tweet_text = itemView.findViewById(R.id.tweet);
            age = itemView.findViewById(R.id.tweet_age);
        }

        public void bind(Tweet a_tweet) {
            Glide.with(context).load(a_tweet.user.profile_image_url).into(avatar);
            name.setText(a_tweet.user.name);
            handle.setText(a_tweet.user.handle);
            tweet_text.setText(a_tweet.body);
            age.setText(TimeFormatter.getTimeDifference(a_tweet.created_at));
        }
    }
}