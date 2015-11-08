package au.com.cuonglam.flickr.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import au.com.cuonglam.flickr.FlickrApplication;
import au.com.cuonglam.flickr.model.FlickrResponse;

/**
 * Created by LAM on 07-11-15.
 */
public class JsonParser {

    private static final String TAG = JsonParser.class.getSimpleName();

    /**
     * Parse the specified json object and build a java object modelling the data.
     *
     * @param jsonObject json object
     * @return a java object modelling the data
     */
    public static FlickrResponse parseResponse(JSONObject jsonObject){
        FlickrResponse flickrResponse = new FlickrResponse();
        List<String> imageList = new ArrayList<String>();
        flickrResponse.setImages(imageList);

        if(jsonObject != null && jsonObject.length() > 0){
            try{
                Log.d(TAG, "response is " + jsonObject.toString());
                JSONArray data = jsonObject.getJSONArray("items");
                for(int i = 0; i < data.length(); i ++) {
                    String m = data.getJSONObject(i).getJSONObject("media").getString("m");
                    imageList.add(m);
                    Log.d(TAG, "value is " + m);
                }
            } catch (JSONException e) {
                Log.d(TAG, e.toString(), e);
            }
        }

        return flickrResponse;
    }
}
