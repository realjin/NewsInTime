package info.realjin.newsintime.view;

import info.realjin.newsintime.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ProgressBar;

public class VerticalProgressBarWithBug extends ProgressBar {
	private int x, y, z, w;

	public VerticalProgressBarWithBug(Context context) {
		super(context);
	}

	public VerticalProgressBarWithBug(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public VerticalProgressBarWithBug(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	// super.onSizeChanged(h, w, oldh, oldw);
	// this.x = w;
	// this.y = h;
	// this.z = oldw;
	// this.w = oldh;
	// }

	protected void onDraw(Canvas c) {
		// c.rotate(-90);
		// c.translate(-getHeight(), 0);
		Log.e("==[BUG]INITWIDTH===", "" + getWidth());

		Drawable progressDrawable = getProgressDrawable();

		LayerDrawable d = (LayerDrawable) progressDrawable;

		final int width = d.getBounds().right - d.getBounds().left;
		Log.e("===[BUG]NPB===", "width=" + width);

		super.onDraw(c);
	}

	private float getScale(int progress) {
		float scale = getMax() > 0 ? (float) progress / (float) getMax() : 0;

		return scale;
	}

}