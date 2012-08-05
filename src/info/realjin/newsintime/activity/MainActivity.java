package info.realjin.newsintime.activity;

import info.realjin.newsintime.R;
import info.realjin.newsintime.anim.LongTextMovingAnimation;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// RunAnimations();
			// Button btMain = (Button) findViewById(R.id.btMain);
			// btMain.startAnimation(new LongTextMovingAnimation(btMain));
			TextView tv = (TextView) findViewById(R.id.tvMain);
			tv.startAnimation(new LongTextMovingAnimation(this, tv));

			Log.e("===onTouchEvent===", "start");
			// testSetLP();
		}
		return true;
	}
}