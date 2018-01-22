package com.mydomain.yyjp.myflickrfindr.services;
/*
 * Description: Flickr photo api retrofit interface
 * Created by jmonani on 1/17/18
 */

import com.mydomain.yyjp.myflickrfindr.services.apihelper.PhotoSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface FlickrPhotoApiInterface {
        @GET("rest")
        Call<PhotoSearchResponse> searchPhoto(@Query("method") String method,
                                              @Query("api_key") String apiKey,
                                              @Query("text") String keyword,
                                              @Query("format") String format,
                                              @Query("nojsoncallback") String value,
                                              @Query("safe_search") int safe,
                                              @Query("per_page") int perPage,
                                              @Query("page") int page);
}
