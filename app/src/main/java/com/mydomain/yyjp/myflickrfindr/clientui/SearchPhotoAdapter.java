package com.mydomain.yyjp.myflickrfindr.clientui;

/*
 * Created by jmonani on 1/17/2018.
 * photo adapter for laying out pictures
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydomain.yyjp.myflickrfindr.R;
import com.mydomain.yyjp.myflickrfindr.services.apihelper.SearchPhoto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.mydomain.yyjp.myflickrfindr.utility.MyFlickrFindrConst.EXTRA_PHOTO_INDEX;

public class SearchPhotoAdapter extends RecyclerView.Adapter<SearchPhotoAdapter.MyViewHolder> {

    private static final String TAG = SearchPhotoAdapter.class.getSimpleName();
    private ArrayList<SearchPhoto> mSearchPhotoList;
    private MainActivity mActivity;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView photoTitle;
        private ImageView photoThumbImage;

        MyViewHolder(View itemView) {
            super(itemView);
            this.photoTitle = itemView.findViewById(R.id.photo_title);
            this.photoThumbImage = itemView.findViewById(R.id.search_photo_thumb_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    SearchPhoto searchPhoto = mSearchPhotoList.get(index);
                    Intent intent = new Intent(mActivity, PhotoSliderActivity.class);
                    intent.putExtra(EXTRA_PHOTO_INDEX, index);
                    mActivity.startActivity(intent);
                }
            });

        }
    }

    public SearchPhotoAdapter(ArrayList<SearchPhoto> data, MainActivity activity) {
        this.mSearchPhotoList = data;
        mActivity = activity;
    }

    public void setData(ArrayList<SearchPhoto> data) {
        this.mSearchPhotoList = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View photoCardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_photo_layout, parent, false);

        return new MyViewHolder(photoCardView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {

        TextView textViewName = holder.photoTitle;
        ImageView thumbImage = holder.photoThumbImage;
        textViewName.setText(mSearchPhotoList.get(listPosition).getTitle());
        Picasso.with(mActivity).load(mSearchPhotoList.get(listPosition).getPhotoUrl()).into(thumbImage);
    }

    @Override
    public int getItemCount() {
        return mSearchPhotoList.size();
    }
}
