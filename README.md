The app uses Volley to connect to flicker to:
- get the list of images
- to download the images
The API class adds a request to the Volley request queue. When the response is received, an Intent is broadcasted
so that the ViewPagers will be updated.

- The app consists of 1 activity and 1 fragment. The fragment uses 2 viewpagers which are keep in sync. 
  The big ViewPager always displays the left most image of the thumbnail viewpager. The left most image of the 
  thumbnail viewpager is not highlighted because I could not get that to work.
  
- The activity has a refresh button at the top right. It will get a new list of images and refresh both viewpagers.

App has been tested on 4.3 and 5.1.1 devices