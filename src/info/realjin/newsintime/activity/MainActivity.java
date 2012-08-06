package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.anim.LongTextMovingAnimation;
import info.realjin.newsintime.view.VerticalTextView;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tvMain;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get screen size and save it
		Display display = getWindowManager().getDefaultDisplay();
		NewsInTimeApp app = (NewsInTimeApp) getApplication();
		app.getData().setScrHeight(display.getHeight());
		app.getData().setScrWidth(display.getWidth());
		Log.e("===APPDATA===", app.getData().toString());

		// init main textview
		LinearLayout llMain = (LinearLayout) findViewById(R.id.llMain);

		tvMain = new VerticalTextView(this);
		tvMain.setText("nothing");
		tvMain.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);

		llMain.addView(tvMain);

	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// RunAnimations();
			// Button btMain = (Button) findViewById(R.id.btMain);
			// btMain.startAnimation(new LongTextMovingAnimation(btMain));
			// TextView tv = (TextView) findViewById(R.id.tvMain);
			// tv.startAnimation(new LongTextMovingAnimation(this, tv));
			tvMain.startAnimation(new LongTextMovingAnimation(this, tvMain));

			Log.e("===onTouchEvent===", "start");
			// testSetLP();
		}
		return true;
	}
}