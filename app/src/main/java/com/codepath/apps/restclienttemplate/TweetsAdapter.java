package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProfileImage;
        TextView textViewName;
        TextView textViewScreenName;
        TextView textViewBody;
        TextView textViewCreatedAt;
        ImageView imageViewMedia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfileImage = itemView.findViewById(R.id.itemViewProfileImage);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewScreenName = itemView.findViewById(R.id.textViewScreenName);
            textViewBody = itemView.findViewById(R.id.textViewTweetBody);
            textViewCreatedAt = itemView.findViewById(R.id.textViewCreatedAt);
            imageViewMedia = itemView.findViewById(R.id.imageViewMedia);
        }

        public void bind(Tweet tweet) {
            textViewBody.setText(tweet.body);
            textViewName.setText(tweet.user.name);
            textViewScreenName.setText(String.format("@%s", tweet.user.screenName));
            textViewCreatedAt.setText(getRelativeTimeAgo(tweet.createdAt));
            Glide.with(context).load(tweet.user.profileImageUrl)
                               .into(imageViewProfileImage);
            if (!tweet.mediaUrl.isEmpty()) {
                Glide.with(context).load(tweet.mediaUrl)
                                   .override(Target.SIZE_ORIGINAL)
                                   .into(imageViewMedia);
            }
            else {
                imageViewMedia.setVisibility(View.INVISIBLE);
            }
        }

        public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            String relativeDate = "";
            try {
                long dateMillis = sf.parse(rawJsonDate).getTime();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return relativeDate;
        }
    }
}
