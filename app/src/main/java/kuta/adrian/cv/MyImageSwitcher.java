package kuta.adrian.cv;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import kuta.adrian.cv.displayingbitmaps.ImageResizer;

/**
 * Author:  Adrian
 * Index:   204423
 * Date:    01.10.2015
 */
public class MyImageSwitcher extends ImageSwitcher {

	private ImageResizer imageResizer;

	public MyImageSwitcher(Context context) {
		super(context);
	}

	public MyImageSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setImageResource(@DrawableRes int resid)
	{
		ImageView image = (ImageView)this.getNextView();
		if (imageResizer == null)
			image.setImageResource(resid);
		else
			imageResizer.loadImage(resid, image);
		showNext();
	}

	public void setImageResizer(ImageResizer imageResizer) {
		this.imageResizer = imageResizer;
	}
}
