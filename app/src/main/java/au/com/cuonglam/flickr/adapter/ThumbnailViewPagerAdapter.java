package au.com.cuonglam.flickr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import au.com.cuonglam.flickr.R;
import au.com.cuonglam.flickr.fragment.ViewImagesFragment;
import au.com.cuonglam.flickr.network.VolleySingleton;

/**
 * Created by LAM on 07-11-15.
 */
public class ThumbnailViewPagerAdapter extends ViewPagerAdapter {
    private static final String TAG = ThumbnailViewPagerAdapter.class.getSimpleName();

    public ThumbnailViewPagerAdapter(Context mContext, float pageWidth, ImageView.ScaleType scaleType) {
        super(mContext, pageWidth, scaleType);
        this.mContext = mContext;
        this.mPageWidth = pageWidth;
        this.mScaleType = scaleType;
        mImageLoader = VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        //need to add 2 so the last image can be scrolled all the way to the left
        return mImageList.size() + 2;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.view_thumbnail, container, false);
        final ImageView imageView = (ImageView) v.findViewById(R.id.thumbnail_image);
        final View border = v.findViewById(R.id.thumbnail_border);
        //could not get the border to be visible on the left most item. Leaving invisible

        final String requestUrl = position < mImageList.size() ? mImageList.get(position) : "";

        //load the image using the image loader. When the image is ready, onResponse() will be called
        mImageLoader.get(requestUrl, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (!requestUrl.equals("")) {
                    if (mContext != null) {
                        Toast.makeText(mContext, "Could not load image.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                // on succesfull response set the image
                imageView.setImageBitmap(response.getBitmap());
            }
        });

        container.addView(v);
        return v;
    }

}
