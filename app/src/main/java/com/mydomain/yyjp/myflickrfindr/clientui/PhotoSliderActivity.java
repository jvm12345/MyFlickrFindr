package com.mydomain.yyjp.myflickrfindr.clientui;
/*
 * Description: Photo viewer activity with sliding left and right
 * Created by jmonani on 1/17/18
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mydomain.yyjp.myflickrfindr.FlickrFindrApplication;
import com.mydomain.yyjp.myflickrfindr.R;
import com.mydomain.yyjp.myflickrfindr.services.apihelper.SearchPhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.mydomain.yyjp.myflickrfindr.utility.MyFlickrFindrConst.EXTRA_PHOTO_INDEX;

public class PhotoSliderActivity extends AppCompatActivity {
    private static final String TAG = PhotoSliderActivity.class.getSimpleName();
    private int mSize;
    private List<SearchPhoto> searchPhotoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSize = 0;
        int mCurrentPage = 0;
        searchPhotoList = FlickrFindrApplication.getInstance().getPhotoList();
        mSize = searchPhotoList.size();
        if(null != getIntent()) {
            mCurrentPage = getIntent().getIntExtra(EXTRA_PHOTO_INDEX, 0);
        }
        setContentView(R.layout.photo_slider_activity);
        ViewPager mViewPager = findViewById(R.id.photo_pager);
        PagerAdapter mPageAdapter = new MyAdapter(PhotoSliderActivity.this);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setCurrentItem(mCurrentPage);
        mPageAdapter.notifyDataSetChanged();
    }

    public class MyAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private Context context;

        MyAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View rootView = inflater.inflate(R.layout.photo_viewer_layout, view, false);
            ImageView imageView = rootView.findViewById(R.id.photo_image);
            if(position < searchPhotoList.size()) {
                SearchPhoto searchPhoto = searchPhotoList.get(position);
                if (null != searchPhoto) {
                    String url = searchPhoto.getPhotoUrl().replace("_q", "_z");
                    Picasso.with(context).load(url)
                            .resizeDimen(R.dimen.photo_slider_image_width, R.dimen.photo_slider_image_height)
                            .placeholder(R.drawable.image_placeholder)
                            .into(imageView);
                }
            }

            view.addView(rootView, 0);
            return rootView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }
}
