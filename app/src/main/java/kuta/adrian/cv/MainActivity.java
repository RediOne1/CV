package kuta.adrian.cv;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

import kuta.adrian.cv.displayingbitmaps.ImageCache;
import kuta.adrian.cv.displayingbitmaps.ImageResizer;

public class MainActivity extends AppCompatActivity {

	private static final String IMAGE_CACHE_DIR = "imageCache";
	private SlidingTabLayout slidingTabLayout;
	private ImageResizer imageResizer;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpImageLoader();

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new NavigationAdapter(getSupportFragmentManager()));

		slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
		slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.accent));
		slidingTabLayout.setDistributeEvenly(true);
		slidingTabLayout.setViewPager(viewPager);
		slidingTabLayout.setTranslationY(-slidingTabLayout.getHeight());

		ScrollUtils.addOnGlobalLayoutListener(slidingTabLayout, new Runnable() {
			@Override
			public void run() {
				slidingTabLayout.setTranslationY(-slidingTabLayout.getHeight());
			}
		});
		slidingTabLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				showTabBar();
			}
		}, 500);
	}

	private void setUpImageLoader() {
		// Fetch screen height and width, to use as our max size when loading images as this
		// activity runs full screen
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		final int height = displayMetrics.heightPixels;
		final int width = displayMetrics.widthPixels;

		// For this sample we'll use half of the longest width to resize our images. As the
		// image scaling ensures the image is larger than this, we should be left with a
		// resolution that is appropriate for both portrait and landscape. For best image quality
		// we shouldn't divide by 2, but this will use more memory and require a larger memory
		// cache.
		final int longest = (height > width ? height : width) / 4;

		ImageCache.ImageCacheParams cacheParams =
				new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

		imageResizer = new ImageResizer(this, longest);
		imageResizer.addImageCache(getSupportFragmentManager(), cacheParams);
		imageResizer.setImageFadeIn(true);
	}

	public ImageResizer getImageResizer() {
		return imageResizer;
	}

	private void showTabBar() {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(slidingTabLayout, "translationY", -slidingTabLayout.getHeight(), getStatusBarHeight());
		objectAnimator.setDuration(500);
		objectAnimator.setStartDelay(1000);
		objectAnimator.start();

		viewPager.setAlpha(0f);
		viewPager.setVisibility(View.VISIBLE);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

		layoutParams.topMargin = slidingTabLayout.getHeight() + getStatusBarHeight();

		viewPager.setLayoutParams(layoutParams);

		ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(viewPager, "alpha", 0f, 1f);
		alphaAnimation.setDuration(500);
		alphaAnimation.setStartDelay(1500);
		alphaAnimation.start();
	}

	public int getStatusBarHeight() {
		int result = 0;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
			return result;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
