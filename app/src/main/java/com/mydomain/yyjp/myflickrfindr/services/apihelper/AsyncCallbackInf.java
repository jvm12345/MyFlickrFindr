
package com.mydomain.yyjp.myflickrfindr.services.apihelper;

/*
 * async callback interface for handling async callbacks from Retrofit/server
 *
 */
public interface AsyncCallbackInf {
    void onResponseCallback(int result, BaseApiResponse response);
}
