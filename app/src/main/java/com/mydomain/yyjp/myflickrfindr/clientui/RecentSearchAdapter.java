package com.mydomain.yyjp.myflickrfindr.clientui;

/*
 * Created by jmonani on 1/17/2018.
 * recent search adapter
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mydomain.yyjp.myflickrfindr.R;

import java.util.ArrayList;

import static com.mydomain.yyjp.myflickrfindr.utility.MyFlickrFindrConst.PER_PAGE;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.MyViewHolder> {

    private static final String TAG = RecentSearchAdapter.class.getSimpleName();
    private ArrayList<String> mRecentSearchList;
    private MainActivity mActivity;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView recentSearchItem;

        MyViewHolder(View itemView) {
            super(itemView);
            this.recentSearchItem = itemView.findViewById(R.id.recent_search_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    if(index >=0 && index < mRecentSearchList.size()) {
                        mActivity.searchFlickrPhotos(mRecentSearchList.get(index), 1, PER_PAGE, false);
                    }
                }
            });

        }
    }

    public RecentSearchAdapter(MainActivity activity, ArrayList<String> data) {
        this.mRecentSearchList = data;
        mActivity = activity;
    }

    public void setData(ArrayList<String> data) {
        this.mRecentSearchList = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recentSearchItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_search_item_layout, parent, false);
        return new MyViewHolder(recentSearchItemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        TextView textViewName = holder.recentSearchItem;
        textViewName.setText(mRecentSearchList.get(listPosition));
    }

    @Override
    public int getItemCount() {
        return mRecentSearchList.size();
    }
}
