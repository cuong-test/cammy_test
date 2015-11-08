package au.com.cuonglam.flickr.network;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import au.com.cuonglam.flickr.FlickrApplication;
import au.com.cuonglam.flickr.fragment.ViewImagesFragment;
import au.com.cuonglam.flickr.model.FlickrResponse;
import au.com.cuonglam.flickr.util.JsonParser;

/**
 * Created by LAM on 07-11-15.
 */
public class API {
    public static final String BROADCAST_INTENT_ACTION = API.class.getPackage().getName() + ".FlickRequestFinished";
    public static final String REQUEST_URL = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";
    public static final String ERROR_MESSAGE = "error_message";

    private static FlickrApplication mApplication = FlickrApplication.getInstance();

    public static List<String> getImageData(RequestQueue requestQueue) {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    FlickrResponse flickResponse = JsonParser.parseResponse(response);
                    List<String> imageList = flickResponse.getImageList();
                    mApplication.setImageList(imageList);
                }

                //notify fragment with the new data
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(BROADCAST_INTENT_ACTION);
                LocalBroadcastManager.getInstance(mApplication).sendBroadcast(broadcastIntent);
            }
        };
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, REQUEST_URL, (String) null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(BROADCAST_INTENT_ACTION);
                broadcastIntent.putExtra(ERROR_MESSAGE, "A network error has occurred");
                LocalBroadcastManager.getInstance(mApplication).sendBroadcast(broadcastIntent);
            }
        });
        Log.d("API", "request url:" + req.getUrl());
        requestQueue.add(req);

        return new ArrayList<String>();
    }
}
