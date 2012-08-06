package info.realjin.newsintime.anim;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.domain.AppConfig;
import info.realjin.newsintime.domain.News;
import info.realjin.newsintime.domain.NewsList;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * anim cyclicly
 * 
 * @author REALJIN
 * 
 */
public class LongTextMovingAnimation extends Animation {
	private Activity a;
	private TextView tv;
	private int batchSize;

	// VIP
	public enum Orientation {
		Horizontal, Vertical
	}

	// VIP
	Orientation or;
	int scrWidth;
	int scrHeight;

	// VIP
	// private String curNewsId; // id of the last news
	private News curNews; // last news
	private String curText; // text displaying
	private float curTextWidth; // text displaying
	private int startLeft;
	boolean firstTime; // if it is the first time of anim

	private int repCount;

	// The steps to skip between colors
	// private int stepSize;// = 6;
	// private long animationDuration;// = 10;

	public LongTextMovingAnimation(Activity a, TextView tv) {
		this(a, tv, Orientation.Vertical);
	}

	public LongTextMovingAnimation(Activity a, TextView tv, Orientation or) {
		this.a = a;
		this.tv = tv;
		this.or = or;

		NewsInTimeApp app = ((NewsInTimeApp) a.getApplication());

		// load screen width and height
		scrWidth = app.getData().getScrWidth();
		scrHeight = app.getData().getScrHeight();

		// init batch size from config
		batchSize = Integer.parseInt(app.getConfig().get(
				AppConfig.CFGNAME_BATCHSIZE));

		Log.e("===Animation===", "batchSize=" + batchSize);

		// init cur news
		curNews = null;
		// curNewsId = 0;

		// init flag
		firstTime = true;

		// first time
		prepareText();

		// animationDuration = 10;
		setDuration(10);
		// setRepeatCount(3000 / stepSize);
		setRepeatCount(repCount);
		setFillAfter(true);
		setInterpolator(new AccelerateInterpolator());
		setAnimationListener(new LongTextMovingAnimationListener());

	}

	public void prepareText() {
		NewsInTimeApp app = ((NewsInTimeApp) a.getApplication());

		Log.e("===ANIM===", "prepareText: curNews=" + curNews + ", batchSize"
				+ batchSize);

		List<News> nl = app.getSubList(curNews, batchSize);

		curText = NewsList.newsListToString(nl);
		String curLastText = nl.get(0).toString();
		curNews = nl.get(nl.size() - 1);

		Log.e("===temp===", "tv=" + tv);
		Log.e("===temp===", "tv.getPaint=" + tv.getPaint());
		Log.e("===temp===", "curText=" + curText);
		curTextWidth = tv.getPaint().measureText(curText);

		// calc initial positions
		if (firstTime) {
			startLeft = 0;
			if (or == Orientation.Horizontal) {
				repCount = (int) ((curTextWidth - scrWidth) / 6); // TODO: !!!
			} else if (or == Orientation.Vertical) {
				repCount = (int) ((curTextWidth - scrHeight) / 6); // TODO: !!!
			}
		} else {
			float w = tv.getPaint().measureText(curLastText);
			if (or == Orientation.Horizontal) {
				startLeft = (int) (scrWidth - w);// TODO: check!!!!!!!!
			} else if (or == Orientation.Vertical) {
				startLeft = (int) (scrHeight - w);// TODO: check!!!!!!!!
			}
			repCount = (int) ((curTextWidth - w) / 6); // TODO: !!!
		}
	}

	class LongTextMovingAnimationListener implements AnimationListener {

		private int curLeft;

		public LongTextMovingAnimationListener() {

		}

		public void onAnimationEnd(Animation animation) {
			Log.e("===ANIM===", "[ *** end *** ]");
			firstTime = false;

			NewsInTimeApp app = ((NewsInTimeApp) a.getApplication());
			app.updateProgress(curNews);

			prepareText();

			setRepeatCount(repCount);

			animation.startNow();
		}

		public void onAnimationRepeat(Animation animation) {
			// Log.e("===ANIM===", "[ *** . *** ]");
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					new ViewGroup.MarginLayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT));
			if (or == Orientation.Horizontal) {
				lp.setMargins((curLeft -= 5), 50, 0, 0); // TODO: 5 may be not
			} else if (or == Orientation.Vertical) {
				lp.setMargins(0, 50 + (curLeft -= 5), 0, 0); // TODO: 5 may be
																// not
			}
			// proper
			tv.setLayoutParams(lp);
		}

		public void onAnimationStart(Animation animation) {
			Log.e("===ANIM===", "[ *** start *** ]Text: " + curText);

			tv.setText(curText);
			curLeft = startLeft;

			// setDuration(animationDuration);
			// TODO: not exactly!!!
			// setRepeatCount(repCount);

			Log.e("===ANIMATION===", "duration = " + 10 + ", repcount = "
					+ repCount);
		}
	}
}
