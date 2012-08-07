package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.anim.LongTextMovingAnimation;
import info.realjin.newsintime.view.VerticalTextView;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	/**
	 * main text view
	 */
	private TextView tvMain;

	// collection selector
	private PopupWindow colSelector;
	private View colSelectorView;
	private PopupWindow pwCols;
	private View vPwCols;

	//
	private Button btPlay;

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

		// RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(50,
		// 50);
		// rlp.leftMargin = 100;
		// rlp.topMargin = 0;
		// tvMain.setLayoutParams(rlp);

		llMain.addView(tvMain);

		// init test button
		btPlay = new Button(this);
		btPlay.setText("Play");
		btPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tvMain.startAnimation(new LongTextMovingAnimation(
						MainActivity.this, tvMain));
			}
		});
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(100,
				50);
		rlp.leftMargin = 200;
		rlp.topMargin = 300;
		btPlay.setLayoutParams(rlp);
		llMain.addView(btPlay);

		// init
		colSelector = null;

	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

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

			TextView tvColSel = (TextView) colSelectorView
					.findViewById(R.id.tvTest1);
			tvColSel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Log.e("===MainActivity===", "tvCol onclick!");
					LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					vPwCols = layoutInflater.inflate(R.layout.colselector_menu,
							null);

					// add data
					ListView lvCols = (ListView) vPwCols
							.findViewById(R.id.lvCols);
					// 加载数据
					List<String> cols = new ArrayList<String>();
					cols.add("全部");
					cols.add("我的微博");
					cols.add("好友");
					cols.add("亲人");
					cols.add("同学");
					cols.add("朋友");
					cols.add("陌生人");

					GroupAdapter groupAdapter = new GroupAdapter(
							MainActivity.this, cols);
					lvCols.setAdapter(groupAdapter);

					pwCols = new PopupWindow(vPwCols, 200, 300);
					pwCols.setBackgroundDrawable(new BitmapDrawable());
					pwCols.setOutsideTouchable(true);
					pwCols.setFocusable(true);

					pwCols.showAtLocation(vPwCols, Gravity.TOP, 100, 30);
				}
			});

			// colSelector.showAsDropDown(llMain, 200, 200);
		}
		// TODO: no position calculation!!!
		colSelector.showAtLocation(colSelectorView, Gravity.TOP, 100, 30);
	}
}

class GroupAdapter extends BaseAdapter {

	private Context context;

	private List<String> list;

	public GroupAdapter(Context context, List<String> list) {

		this.context = context;
		this.list = list;

	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {

		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup viewGroup) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.colselector_menu_item, null);
			holder = new ViewHolder();

			convertView.setTag(holder);

			holder.groupItem = (TextView) convertView
					.findViewById(R.id.tvcolselector_menu_item);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.groupItem.setTextColor(Color.BLACK);
		holder.groupItem.setText(list.get(position));

		return convertView;
	}

	static class ViewHolder {
		TextView groupItem;
	}

}
