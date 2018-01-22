package com.mydomain.yyjp.myflickrfindr.clientui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.mydomain.yyjp.myflickrfindr.FlickrFindrApplication;
import com.mydomain.yyjp.myflickrfindr.R;
import com.mydomain.yyjp.myflickrfindr.services.FlickrPhotoAPIService;
import com.mydomain.yyjp.myflickrfindr.services.apihelper.AsyncCallbackInf;
import com.mydomain.yyjp.myflickrfindr.services.apihelper.BaseApiResponse;
import com.mydomain.yyjp.myflickrfindr.services.apihelper.PhotoSearchResponse;
import com.mydomain.yyjp.myflickrfindr.services.apihelper.SearchPhoto;
import com.mydomain.yyjp.myflickrfindr.utility.ScrollListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mydomain.yyjp.myflickrfindr.utility.MyFlickrFindrConst.PER_PAGE;
import static com.mydomain.yyjp.myflickrfindr.utility.MyFlickrFindrConst.PHOTO_HOST;
import static com.mydomain.yyjp.myflickrfindr.utility.MyFlickrFindrConst.THUMB_NAIL;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final int SCROLL_DOWN = 1;
    private RecyclerView mSearchPhotoGridView;
    private SearchPhotoAdapter mSearchPhotoAdapter;
    private TextView mEmptyListMessage;
    private List<SearchPhoto> mSearchPhotoList;
    private String mKeyword;
    private SearchView mSearchView;
    private RecyclerView mSearchRecyclerView;
    private RecentSearchAdapter mRecentSearchAdapter;
    private ArrayList<String> mRecentSearchList = new ArrayList<>();
    private Set<String> mRecentSet = new HashSet<>();
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recent search listview
        mSearchRecyclerView = findViewById(R.id.recent_search_recycler_view);
        mSearchPhotoGridView = findViewById(R.id.search_photo_recycler_view);
        mEmptyListMessage = findViewById(R.id.empty_search_message);
        setRecentSearch();
        mSearchPhotoList = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        mSearchPhotoGridView.setLayoutManager(layoutManager);
        mSearchPhotoAdapter = new SearchPhotoAdapter((ArrayList<SearchPhoto>) mSearchPhotoList, this);
        mSearchPhotoGridView.setAdapter(mSearchPhotoAdapter);
        ScrollListener scrollListener = new ScrollListener(layoutManager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView) {
                if (mSearchPhotoGridView.canScrollVertically(SCROLL_DOWN)) {
                    SearchPhoto photo = mSearchPhotoList.get(mSearchPhotoList.size()-1);
                    searchFlickrPhotos(mKeyword, photo.getPageNumber()+1, PER_PAGE, true);
                }
                return true;
            }
        };
        scrollListener.resetState();
        mSearchPhotoGridView.setOnScrollListener(scrollListener);
    }

    /*
     * set recent search items
     */
    private void setRecentSearch() {
        LinearLayoutManager searchLayoutMgr = new LinearLayoutManager(MainActivity.this);
        mSearchRecyclerView.setLayoutManager(searchLayoutMgr);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mSearchRecyclerView.getContext(),
                searchLayoutMgr.getOrientation());
        mSearchRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecentSearchAdapter = new RecentSearchAdapter(MainActivity.this, mRecentSearchList);
        mSearchRecyclerView.setAdapter(mRecentSearchAdapter);
        showRecentSearch();
    }

    private void showRecentSearch() {
        if(mRecentSearchList.size() > 0) {
            mRecentSearchAdapter.setData(mRecentSearchList);
            mRecentSearchAdapter.notifyDataSetChanged();
            mSearchRecyclerView.setVisibility(View.VISIBLE);
            mSearchPhotoGridView.setVisibility(View.INVISIBLE);
            mEmptyListMessage.setVisibility(View.INVISIBLE);
        } else {
            mSearchRecyclerView.setVisibility(View.INVISIBLE);
            mSearchPhotoGridView.setVisibility(View.INVISIBLE);
            mEmptyListMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dismissKeyboard();
    }


    /*
     * create search option
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryRefinementEnabled(true);
        mSearchView.setIconified(false);
        mSearchView.clearFocus();

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //dismiss keyboard and show previous search result
                dismissKeyboard();
                if(mSearchPhotoList.size()>0) {
                    mSearchPhotoGridView.setVisibility(View.VISIBLE);
                    mSearchRecyclerView.setVisibility(View.INVISIBLE);
                    mEmptyListMessage.setVisibility(View.INVISIBLE);
                } else {
                    mEmptyListMessage.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        mSearchView.setOnSearchClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show recent search if any or initial message
                showRecentSearch();
            }
        });
        return true;
    }

    /*
     * search safe photos with key word form flickr api
     */
    public void searchFlickrPhotos(final String keyWord, int page, int total, boolean loadMore) {

        if(!loadMore) {
            showMessage("Searching photos for... " + keyWord);
            mSearchPhotoList.clear();
        }
        new FlickrPhotoAPIService().searchPhotoAsync(page, keyWord, total, new AsyncCallbackInf() {
            @Override
            public void onResponseCallback(int result, BaseApiResponse response) {
                PhotoSearchResponse photoSearchResponse = (PhotoSearchResponse) response;
                if(null != response && null != photoSearchResponse.stat && "ok".equals(photoSearchResponse.stat)) {
                    if(photoSearchResponse.photos.total.equals("0")) {
                        showMessage("No Search Result for keyword: " + keyWord);
                    } else if(photoSearchResponse.photos.photo.size() <= 0) {
                        String message = "No more photos for the keyword: " + keyWord;
                        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                    } else {
                        setSearchPhotos(photoSearchResponse);
                        mSearchPhotoAdapter.setData((ArrayList<SearchPhoto>) mSearchPhotoList);
                        mSearchPhotoAdapter.notifyDataSetChanged();
                        mEmptyListMessage.setVisibility(View.INVISIBLE);
                        mSearchPhotoGridView.setVisibility(View.VISIBLE);
                        mSearchRecyclerView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    // error scenario
                    String message = "No Photos Found:\n";
                    if(null != photoSearchResponse && null != photoSearchResponse.error) {
                        if(photoSearchResponse.error.contains("Unable to resolve host"))
                            message = message.concat("Connection Error, Please try again!");
                        else
                            message = message.concat(photoSearchResponse.error);
                    }
                    Log.e(TAG, "Error in searching photos:" + message);
                    showMessage(message);
                    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
     * display message
     */
    private void showMessage(String message) {
        dismissKeyboard();
        mEmptyListMessage.setText(message);
        mSearchPhotoGridView.setVisibility(View.INVISIBLE);
        mSearchRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyListMessage.setVisibility(View.VISIBLE);
    }
    /*
     * parsing response and setting photos
     */
    private void setSearchPhotos(PhotoSearchResponse response) {
        int pageNum = response.photos.page;
        int total = Integer.valueOf(response.photos.total);
        List<SearchPhoto> searchPhotos = response.photos.photo;
        if(null != searchPhotos) {
            for (SearchPhoto photo: searchPhotos) {
                photo.setPageNumber(pageNum);
                photo.setTotal(total);
                setSearchPhotoUrl(photo);
                mSearchPhotoList.add(photo);
            }
            FlickrFindrApplication.getInstance().setPhotoList(mSearchPhotoList);
        }
    }

    /*
     * set thumb image
     */
    private void setSearchPhotoUrl(SearchPhoto photo) {
        StringBuilder stringBuilder = new StringBuilder("https://farm");
        stringBuilder = stringBuilder.append(photo.getFarm()).append(PHOTO_HOST).append(photo.getServer()).append("/").append(photo.getId())
                .append("_").append(photo.getSecret()).append(THUMB_NAIL);
        photo.setPhotoUrl(stringBuilder.toString());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchPhotoList.clear();
        if(null != mSearchView)
            mSearchView.clearFocus();
        FlickrFindrApplication.getInstance().setPhotoList(mSearchPhotoList);
        query = query.trim();
        if(!mRecentSet.contains(query)) {
            mRecentSet.add(query);
            mRecentSearchList.add(query);
        }
        mKeyword = query;
        dismissKeyboard();
        searchFlickrPhotos(query, 1, PER_PAGE, false);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void dismissKeyboard() {
        //hides the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(null != mSearchView)
            imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        else if(null != mSearchPhotoGridView)
            imm.hideSoftInputFromInputMethod(mSearchPhotoGridView.getWindowToken(), 0);
    }
}
