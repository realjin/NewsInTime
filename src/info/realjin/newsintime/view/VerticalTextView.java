package info.realjin.newsintime.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class VerticalTextView extends TextView {
	final boolean topDown;

	public VerticalTextView(Context context) {
		// super(context, attrs);
		super(context);
		final int gravity = getGravity();
		if (Gravity.isVertical(gravity)
				&& (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
			setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK)
					| Gravity.TOP);
			topDown = false;
		} else
			topDown = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.e("onMeasure=====", "1");
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		Log.e("onMeasure=====", "2");
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
		Log.e("onMeasure=====", "3");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		TextPaint textPaint = getPaint();
		textPaint.setColor(getCurrentTextColor());
		textPaint.drawableState = getDrawableState();

		canvas.save();

		if (topDown) {
			canvas.translate(getWidth(), 0);
			canvas.rotate(90);
		} else {
			canvas.translate(0, getHeight());
			canvas.rotate(-90);
		}

		canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

		getLayout().draw(canvas);
		canvas.restore();
	}
}
