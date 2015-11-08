package au.com.cuonglam.flickr;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import au.com.cuonglam.flickr.network.API;
import au.com.cuonglam.flickr.network.VolleySingleton;
import au.com.cuonglam.flickr.util.NetworkHelper;

/**
 * Created by LAM on 10-10-15.
 */
public class FlickrApplication extends Application {

    private static FlickrApplication mInstance;

    private List<String> mImageList = new ArrayList<>();

    public static FlickrApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public void setImageList(List<String> imageList) {
        mImageList = imageList;
    }

    public List<String> getImageList() {
        if (mImageList == null) {
            mImageList = new ArrayList<>();
        }
        return mImageList;
    }

    /**
     * Get the flicker data. Return true if there is a network connection
     *
     * @return Return true if there is a network connection; otherwise false
     */
    public boolean getFlickrImageData() {
        if (NetworkHelper.isNetworkAvailable(this)) {
            API.getImageData(VolleySingleton.getInstance().getRequestQueue());
            return true;
        }
        return false;
    }
}
