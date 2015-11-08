package au.com.cuonglam.flickr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import au.com.cuonglam.flickr.fragment.ViewImagesFragment;
import au.com.cuonglam.flickr.network.VolleySingleton;

/**
 * Created by LAM on 07-11-15.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = ViewPagerAdapter.class.getSimpleName();

    protected Context mContext;
    protected ImageLoader mImageLoader;

    /** the scale type to apply to the ImageView */
    protected ImageView.ScaleType mScaleType;
    /** how width each page should be. 1.0f means the page is the width as the ViewPager */
    protected float mPageWidth;

    /** list of images to display */
    protected List<String> mImageList = new ArrayList<String>();

    public ViewPagerAdapter(Context mContext, float pageWidth, ImageView.ScaleType scaleType) {
        this.mContext = mContext;
        this.mPageWidth = pageWidth;
        this.mScaleType = scaleType;
        mImageLoader = VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setImageList(List<String> imageList) {
        this.mImageList = imageList;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        final ImageView imageView = new ImageView(mContext);
        imageView.setAdjustViewBounds(false);
        imageView.setScaleType(mScaleType);

        String requestUrl = position < mImageList.size() ? mImageList.get(position) : "";

        mImageLoader.get(requestUrl, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // not interested in error
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                // on succesfull response set the image
                imageView.setImageBitmap(response.getBitmap());
            }
        });

        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public float getPageWidth(int position) {
        return mPageWidth;
    }
}
