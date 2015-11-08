package au.com.cuonglam.flickr.model;

import java.util.List;

/**
 * Created by LAM on 07-11-15.
 */
public class FlickrResponse {

    private List<String> mImageList;

    public FlickrResponse() {
    }

    public void setImages(List<String> imageList) {
        mImageList = imageList;
    }

    public List<String> getImageList() {
        return mImageList;
    }

}
