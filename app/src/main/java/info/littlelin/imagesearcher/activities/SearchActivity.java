package info.littlelin.imagesearcher.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import info.littlelin.imagesearcher.R;
import info.littlelin.imagesearcher.models.ImageResult;
import info.littlelin.imagesearcher.models.SearchResult;
import info.littlelin.imagesearcher.views.adapters.ImageResultAdapter;
import info.littlelin.imagesearcher.views.fragments.SettingDialog;

public class SearchActivity extends FragmentActivity {
    private StaggeredGridView sgvResults;
    private SearchResult searchResult;
    private ImageResultAdapter aImageResults;

    private SearchView searchView;
    private String currentQuery;
    public static final int PAGE_SIZE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.setupViews();

        // Create the data source
        this.searchResult = new SearchResult();

        // Attaches the data source to an adapter
        this.aImageResults = new ImageResultAdapter(this, this.searchResult.results);

        // Link the adapter to another AdapterView(GridView)
        this.sgvResults.setAdapter(aImageResults);

        this.sgvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page >= 8) {
                    return;
                }

                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                fetchSearchResult(currentQuery, page);
            }
        });
    }

    private void setupViews() {
        this.sgvResults = (StaggeredGridView) this.findViewById(R.id.sgvResults);
        this.sgvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch the image display activity
                // Creating the intent
                Intent intent = new Intent(SearchActivity.this, ImageDisplayActivity.class);

                // Get the image result to display
                ImageResult result = aImageResults.getItem(position);

                // Pass image result into the intent
                intent.putExtra("result", result);

                // Launch the new activity
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        this.searchView = (SearchView) searchItem.getActionView();
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!hasNetworkConnection()) {
                    Toast.makeText(SearchActivity.this, "You don't have Internet connection now, please try again when you reconnect to network.", Toast.LENGTH_LONG);
                }

                if (query.equals(currentQuery)) {
                    return true;
                }
                currentQuery = query;
                aImageResults.clear();
                fetchSearchResult(query, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            android.app.FragmentManager fm = getFragmentManager();

            SettingDialog settingDialog = SettingDialog.newInstance("Advanced Settings");
            settingDialog.show(fm, "fragment_settings");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchSearchResult(String query, int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = this.getSearchUrl(query, page);
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                try {
                    searchResult = new SearchResult(response.getJSONObject("responseData"));

                    // When you make to the adapter, it does modify the underlying data
                    aImageResults.addAll(searchResult.results);
                } catch (JSONException e) {
                    searchResult = new SearchResult();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SearchActivity.this, "Some errors occurred while fetching image search result. Please check your network status and try again later.", Toast.LENGTH_LONG);
            }
        });
    }

    private String getSearchUrl(String query, int page) {
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&tbWidth=150&q=" + query + "&rsz=" + PAGE_SIZE;

        // Get the preference values
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String prefImageSize = pref.getString("imageSize", "");
        String prefColorFilter = pref.getString("colorFilter", "");
        String prefImageType = pref.getString("imageType", "");
        String prefSite = pref.getString("site", "");

        // Set the search arguments based on preference values
        if (!"".equals(prefImageSize)) {
            searchUrl += "&imgsz=" + prefImageSize;
        }

        if (!"".equals(prefColorFilter)) {
            searchUrl += "&imgcolor=" + prefColorFilter;
        }

        if (!"".equals(prefImageType)) {
            searchUrl += "&imgtype=" + prefImageType;
        }

        if (!"".equals(prefSite)) {
            searchUrl += "&as_sitesearch=" + prefSite;
        }

        if (page > 0) {
            searchUrl += "&start=" + PAGE_SIZE * page;
        }

        Log.d("DEBUG", "searchUrl=" + searchUrl);
        return searchUrl;
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager cm =(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
