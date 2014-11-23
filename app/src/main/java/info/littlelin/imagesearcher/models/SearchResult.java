package info.littlelin.imagesearcher.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchResult {

    public List<ImageResult> results;

    public SearchResult() {
        this.results = new ArrayList<ImageResult>();
    }

    public SearchResult(JSONObject jsonObjectResponseData) {
        Gson gson = new Gson();
        try {
            Type imageResultsType = new TypeToken<List<ImageResult>>() {}.getType();
            String rawJsonString = jsonObjectResponseData.getJSONArray("results").toString();
            Log.d("Debug", rawJsonString);
            this.results = gson.fromJson(rawJsonString, imageResultsType);
        } catch (JSONException e) {
            this.results = new ArrayList<ImageResult>();
            e.printStackTrace();
        }
    }

    public void clear() {
        this.results.clear();
    }
}
