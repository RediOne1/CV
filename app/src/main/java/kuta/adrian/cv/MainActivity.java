package kuta.adrian.cv;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import kuta.adrian.cv.displayingbitmaps.ImageCache;
import kuta.adrian.cv.displayingbitmaps.ImageResizer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private static final String IMAGE_CACHE_DIR = "imageCache";
	private TextView start;
	private View overlay;
	private SlidingTabLayout slidingTabLayout;
	private ImageResizer imageResizer;
	private ViewPager viewPager;
	private MyImageSwitcher backgroundImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpImageLoader();

		backgroundImage = (MyImageSwitcher) findViewById(R.id.background_image);
		backgroundImage.setImageResizer(imageResizer);
		Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
		Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
		backgroundImage.setInAnimation(in);
		backgroundImage.setOutAnimation(out);
		backgroundImage.setFactory(new ViewSwitcher.ViewFactory() {
			@Override
			public View makeView() {
				ImageView imageView = new ImageView(getApplicationContext());
				imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				return imageView;
			}
		});
		backgroundImage.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				v.removeOnLayoutChangeListener(this);
				backgroundImage.setImageResource(R.drawable.background);
			}
		});

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new NavigationAdapter(getSupportFragmentManager()));

		slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
		slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.accent));
		slidingTabLayout.setDistributeEvenly(true);
		slidingTabLayout.setViewPager(viewPager);
		slidingTabLayout.setTranslationY(-slidingTabLayout.getHeight());

		slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i2) {
			}

			@Override
			public void onPageSelected(int i) {

			}

			@Override
			public void onPageScrollStateChanged(int i) {
			}
		});

		start = (TextView) findViewById(R.id.start);
		start.setOnClickListener(this);

		overlay = findViewById(R.id.blue_overlay);
		overlay.setScaleY(0f);
		ScrollUtils.addOnGlobalLayoutListener(slidingTabLayout, new Runnable() {
			@Override
			public void run() {
				slidingTabLayout.setTranslationY(-slidingTabLayout.getHeight());
			}
		});
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
		final int longest = (height > width ? height : width) / 2;

		ImageCache.ImageCacheParams cacheParams =
				new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

		imageResizer = new ImageResizer(this, longest);
		imageResizer.addImageCache(getSupportFragmentManager(), cacheParams);
		imageResizer.setImageFadeIn(true);
	}

	private void hideStart(View v) {
		// previously visible view

		// get the center for the clipping circle
		int cx = v.getWidth();
		int cy = v.getHeight() / 2;

		// get the initial radius for the clipping circle
		int initialRadius = v.getWidth();

		// create the animation (the final radius is zero)
		SupportAnimator animator =
				ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, initialRadius);
		animator.setDuration(2000);
		animator = animator.reverse();
		animator.addListener(new SupportAnimator.AnimatorListener() {
			@Override
			public void onAnimationStart() {

			}

			@Override
			public void onAnimationEnd() {
				start.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel() {

			}

			@Override
			public void onAnimationRepeat() {

			}
		});
		animator.start();
	}

	private void showOverlay() {
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.overlay_scale_anim);
		overlay.setScaleY(1f);
		overlay.startAnimation(animation);
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

	@Override
	public void onClick(View v) {
		if (v == start) {
			hideStart(v);
			showOverlay();
			showTabBar();
			backgroundImage.setImageResource(R.drawable.education);
		}
	}
}
