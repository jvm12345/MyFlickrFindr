package com.mydomain.yyjp.myflickrfindr.services;

/*
 * Description: flickr api service using Retrofit
 * Created by jmonani on 1/17/2018
 */


import com.mydomain.yyjp.myflickrfindr.services.apihelper.AsyncCallbackInf;
import com.mydomain.yyjp.myflickrfindr.services.apihelper.PhotoSearchResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlickrPhotoAPIService {

    // api url and key
    public static final String BASE_URL = "https://api.flickr.com/services/";
    public static final String API_KEY = "1508443e49213ff84d566777dc211f2a";
    public static final String PAGE_SIZE = "30";
    public static final String SEARCH_PHOTO_METHOD = "flickr.photos.search";

    /*
     * create rest adapter
     */
    public static Retrofit getRestAdapter() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return restAdapter;
    }

    public void searchPhotoAsync(int page, String keyWord, int perPage, final AsyncCallbackInf callbackInf) {

        FlickrPhotoApiInterface flickrPhotoApiInterface =
                getRestAdapter().create(FlickrPhotoApiInterface.class);

            Call<PhotoSearchResponse> call = flickrPhotoApiInterface.searchPhoto(SEARCH_PHOTO_METHOD, API_KEY, keyWord, "json", "1", 1, perPage, page);
            call.enqueue(new Callback<PhotoSearchResponse>() {
                @Override
                public void onResponse(Call<PhotoSearchResponse> call, Response<PhotoSearchResponse> response) {
                    if(response.isSuccessful()) {
                        PhotoSearchResponse photoSearchResponse = response.body();
                        callbackInf.onResponseCallback(0, photoSearchResponse);
                    } else {
                        PhotoSearchResponse failureResponse = new PhotoSearchResponse();
                        failureResponse.error = response.message();
                        callbackInf.onResponseCallback(1, failureResponse);
                    }
                }

                @Override
                public void onFailure(Call<PhotoSearchResponse> call, Throwable throwable) {
                    PhotoSearchResponse failureResponse = new PhotoSearchResponse();
                    failureResponse.error = throwable.getLocalizedMessage();
                    callbackInf.onResponseCallback(1, failureResponse);
                }
            });
    }
}
