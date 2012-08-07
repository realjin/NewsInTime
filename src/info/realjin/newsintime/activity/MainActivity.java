package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.view.VerticalTextView;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends Activity {
	/**
	 * main text view
	 */
	private TextView tvMain;

	private PopupWindow colSelector;
	private View colSelectorView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		NewsInTimeApp app = (NewsInTimeApp) getApplication();
		app.setMainActivity(this);

		// get screen size and save it
		Display display = getWindowManager().getDefaultDisplay();
		app.getData().setScrHeight(display.getHeight());
		app.getData().setScrWidth(display.getWidth());
		Log.e("===APPDATA===", app.getData().toString());

		// init main textview
		LinearLayout llMain = (LinearLayout) findViewById(R.id.llMain);

		tvMain = new VerticalTextView(this);
		tvMain.setText("nothing");
		tvMain.setTextSize(28.0f);
		tvMain.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);

		llMain.addView(tvMain);

		// init
		colSelector = null;

	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			// tvMain.startAnimation(new LongTextMovingAnimation(this, tvMain));

			showCover();

			Log.e("===onTouchEvent===", "start");
		}
		return true;
	}

	/**
	 * show top layer
	 */
	private void showCover() {
		showColSelector();
	}

	private void showColSelector() {
		if (colSelector == null) {
			Log.i("===MainActivity===", "colSelector creating");
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			colSelectorView = layoutInflater
					.inflate(R.layout.colselector, null);

			colSelector = new PopupWindow(colSelectorView, 200, 150);
			colSelector.setBackgroundDrawable(new BitmapDrawable());
			colSelector.setOutsideTouchable(true);
			colSelector.setFocusable(true);

			// colSelector.showAsDropDown(llMain, 200, 200);
		}
		colSelector.showAtLocation(colSelectorView, Gravity.TOP, 100, 30);
	}
}