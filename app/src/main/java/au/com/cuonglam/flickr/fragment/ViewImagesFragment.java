package au.com.cuonglam.flickr.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import au.com.cuonglam.flickr.FlickrApplication;
import au.com.cuonglam.flickr.R;
import au.com.cuonglam.flickr.adapter.ThumbnailViewPagerAdapter;
import au.com.cuonglam.flickr.adapter.ViewPagerAdapter;
import au.com.cuonglam.flickr.network.API;

public class ViewImagesFragment extends Fragment {
    private static final String TAG = ViewImagesFragment.class.getSimpleName();

    //full image view pager constants
    public static final float FULL_IMAGE_VIEW_PAGE_PAGE_WIDTH = 1.0f;
    public static final ImageView.ScaleType FULL_IMAGE_SCALE_TYPE = ImageView.ScaleType.FIT_CENTER;

    //thubmbnail view pager constants
    public static final float THUMBNAIL_VIEW_PAGER_PAGE_WIDTH = 0.3f;
    public static final ImageView.ScaleType THUMBNAIL_SCALE_TYPE = ImageView.ScaleType.CENTER_CROP;

    private BroadcastReceiver mBroadcastReceiver;
    private FlickrApplication flickrApplication;

    private ViewPager mFullImageViewPager;
    private ViewPagerAdapter mFullImageAdapter;

    private ViewPager mThumbnailViewPager;
    private ViewPagerAdapter mThumbnailAdapter;

    private ProgressBar mProgressBar;

    //the current selected position in the viewpager
    private int mCurrentPosition;

    public ViewImagesFragment() {
    }

    public ViewImagesFragment newInstance() {
        ViewImagesFragment fragment = new ViewImagesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        flickrApplication = (FlickrApplication) getActivity().getApplication();
        setupAndRegisterBroadcastReceiver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_view_images, container, false);
        mThumbnailViewPager = (ViewPager) layout.findViewById(R.id.thumbnail_view_pager);
        mFullImageViewPager = (ViewPager) layout.findViewById(R.id.full_image_view_pager);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progress_bar);

        List<String> imageList = flickrApplication.getImageList();
        if (imageList != null && imageList.isEmpty()) {
            mProgressBar.setVisibility(View.VISIBLE);
            if (!FlickrApplication.getInstance().getFlickrImageData()) {
                Toast.makeText(getActivity(), "No network connection available", Toast.LENGTH_LONG).show();
            }
        } else {
            mProgressBar.setVisibility(View.GONE);
            updateViewPagers(imageList);
        }

        return layout;
    }

    /**
     * Update the ViewPagers with the new image list
     *
     * @param imageList the image list
     */
    private void updateViewPagers(List<String> imageList) {
        mProgressBar.setVisibility(View.GONE);
        updateFullImageViewPager(imageList);
        updateThumbnailViewPager(imageList);
    }

    private void updateFullImageViewPager(final List<String> imageList) {
        mFullImageAdapter = new ViewPagerAdapter(getActivity(), FULL_IMAGE_VIEW_PAGE_PAGE_WIDTH, FULL_IMAGE_SCALE_TYPE);
        mFullImageAdapter.setImageList(imageList);
        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                Log.d(TAG, "full image page selected " + position);
                mThumbnailViewPager.setCurrentItem(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mFullImageViewPager.setOnPageChangeListener(listener);
        mFullImageViewPager.setAdapter(mFullImageAdapter);
    }

    private void updateThumbnailViewPager(final List<String> imageList) {
        mThumbnailAdapter = new ThumbnailViewPagerAdapter(getActivity(), THUMBNAIL_VIEW_PAGER_PAGE_WIDTH, THUMBNAIL_SCALE_TYPE);
        mThumbnailAdapter.setImageList(imageList);
        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                Log.d(TAG, "thumbnail viewpager page selected " + position);
                mFullImageViewPager.setCurrentItem(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mThumbnailViewPager.setOnPageChangeListener(listener);
        mThumbnailViewPager.setAdapter(mThumbnailAdapter);
    }

    public void setupAndRegisterBroadcastReceiver() {
        //setup the broadcast receiver that will be notified when there is a new image list
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getStringExtra(API.ERROR_MESSAGE) != null) {
                    Toast.makeText(context, intent.getStringExtra(API.ERROR_MESSAGE), Toast.LENGTH_LONG).show();
                } else {
                    updateViewPagers(flickrApplication.getImageList());
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(API.BROADCAST_INTENT_ACTION);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mBroadcastReceiver, intentFilter);
    }
}
