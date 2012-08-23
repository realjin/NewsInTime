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

public class VerticalNewsProgressBar extends ProgressBar {
	private int x, y, z, w;

	@Override
	protected void drawableStateChanged() {
		// TODO Auto-generated method stub
		super.drawableStateChanged();
	}

	public VerticalNewsProgressBar(Context context) {
		super(context);
	}

	public VerticalNewsProgressBar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public VerticalNewsProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);
		this.x = w;
		this.y = h;
		this.z = oldw;
		this.w = oldh;
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	protected void onDraw(Canvas c) {
		updateProgressBar();
		c.rotate(-90);
		c.translate(-getHeight(), 0);
		super.onDraw(c);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			return false;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			setSelected(true);
			setPressed(true);
			break;
		case MotionEvent.ACTION_MOVE:
			setProgress(getMax()
					- (int) (getMax() * event.getY() / getHeight()));
			onSizeChanged(getWidth(), getHeight(), 0, 0);

			break;
		case MotionEvent.ACTION_UP:
			setSelected(false);
			setPressed(false);
			break;

		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return true;
	}

//	@Override
//	public synchronized void setProgress(int progress) {
//
//		if (progress >= 0)
//			super.setProgress(progress);
//
//		else
//			super.setProgress(0);
//		onSizeChanged(x, y, z, w);
//
//	}
	
	
	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);

		// the setProgress super will not change the details of the progress bar
		// anymore so we need to force an update to redraw the progress bar
		invalidate();

		 onSizeChanged(x, y, z, w);
	}

	private float getScale(int progress) {
		float scale = getMax() > 0 ? (float) progress / (float) getMax() : 0;

		return scale;
	}

	/**
	 * Instead of using clipping regions to uncover the progress bar as the
	 * progress increases we increase the drawable regions for the progress bar
	 * and pattern overlay. Doing this gives us greater control and allows us to
	 * show the rounded cap on the progress bar.
	 */
	private void updateProgressBar() {
		Drawable progressDrawable = getProgressDrawable();

		if (progressDrawable != null
				&& progressDrawable instanceof LayerDrawable) {
			LayerDrawable d = (LayerDrawable) progressDrawable;

			final float scale = getScale(getProgress());

			// get the progress bar and update it's size
			Drawable progressBar = d.findDrawableByLayerId(R.id.progress);

			final int width = d.getBounds().right - d.getBounds().left;
			Log.e("===NPB===", "width=" + width);

			if (progressBar != null) {
				Rect progressBarBounds = progressBar.getBounds();
				progressBarBounds.right = progressBarBounds.left
						+ (int) (width * scale + 0.5f);
				progressBar.setBounds(progressBarBounds);
			}

			// get the pattern overlay
			Drawable patternOverlay = d.findDrawableByLayerId(R.id.pattern);

			if (patternOverlay != null) {
				if (progressBar != null) {
					// we want our pattern overlay to sit inside the bounds of
					// our progress bar
					Rect patternOverlayBounds = progressBar.copyBounds();
					final int left = patternOverlayBounds.left;
					final int right = patternOverlayBounds.right;

					patternOverlayBounds.left = (left + 1 > right) ? left
							: left + 1;
					patternOverlayBounds.right = (right > 0) ? right - 1
							: right;
					patternOverlay.setBounds(patternOverlayBounds);
				} else {
					// we don't have a progress bar so just treat this like the
					// progress bar
					Rect patternOverlayBounds = patternOverlay.getBounds();
					patternOverlayBounds.right = patternOverlayBounds.left
							+ (int) (width * scale + 0.5f);
					patternOverlay.setBounds(patternOverlayBounds);
				}
			}
		}
	}
}