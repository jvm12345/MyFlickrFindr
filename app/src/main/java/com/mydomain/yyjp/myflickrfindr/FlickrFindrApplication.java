package com.mydomain.yyjp.myflickrfindr;

import android.app.Application;

import com.mydomain.yyjp.myflickrfindr.services.apihelper.SearchPhoto;

import java.util.List;

/*
 * Created by jmonani on 1/17/2018.
 */

public class FlickrFindrApplication extends Application {

    private static FlickrFindrApplication mInstance;
    private List<SearchPhoto> mSearchPhotos;

    public static FlickrFindrApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mInstance.initializeInstance();
    }

    /*
     * initialize application level data
     */
    private void initializeInstance() {
    }

    public void setPhotoList(List<SearchPhoto> list) {
        this.mSearchPhotos = list;
    }

    public List<SearchPhoto> getPhotoList() {
        return mSearchPhotos;
    }
}
