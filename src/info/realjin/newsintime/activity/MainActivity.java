
package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.anim.LongTextMovingAnimation;
import info.realjin.newsintime.view.VerticalListView;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
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
	// TODO: no calc!!!
	int colSelectorWidth = 100;
	int colSelectorHeight = 200;
	int colSelectorLeft = 25;
	int colSelectorTop = 390;

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

			colSelector = new PopupWindow(colSelectorView, colSelectorWidth,
					colSelectorHeight);
			colSelector.setBackgroundDrawable(new BitmapDrawable());
			colSelector.setOutsideTouchable(true);
			colSelector.setFocusable(true);

			final TextView tvColSel = new VerticalTextView(this);
			tvColSel.setText("collections");
			tvColSel.setTextSize(28.0f);
			// tvColSel.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					new ViewGroup.MarginLayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT));
			lp.setMargins(0, 0, 0, 0);
			tvColSel.setLayoutParams(lp);
			// TextView tvColSel = (TextView) colSelectorView
			// .findViewById(R.id.tvTest1);
			tvColSel.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Log.e("===MainActivity===", "tvCol onclick!");

					// 1. hide this tv
					v.setVisibility(View.GONE);

					// 2.

					LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					vPwCols = layoutInflater.inflate(R.layout.colselector_menu,
							null);

					// add data
					AdapterView lvCols;
					lvCols = new VerticalListView(MainActivity.this);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT);
					lvCols.setLayoutParams(lp);
					((LinearLayout) vPwCols).addView(lvCols);

					// = (ListView) vPwCols.findViewById(R.id.lvCols);
					// ��������
					List<String> cols = new ArrayList<String>();
					cols.add("ȫ��");
					cols.add("�ҵ�΢��");
					cols.add("����");
					cols.add("����");
					cols.add("ͬѧ");
					cols.add("����");

					GroupAdapter groupAdapter = new GroupAdapter(
							MainActivity.this, cols);
					lvCols.setAdapter(groupAdapter);

					pwCols = new PopupWindow(vPwCols, colSelectorWidth - 5,
							colSelectorWidth - 5);
					pwCols.setBackgroundDrawable(new BitmapDrawable());
					pwCols.setOutsideTouchable(true);
					pwCols.setFocusable(true);
					pwCols.setOnDismissListener(new OnDismissListener() {
						
						public void onDismiss() {
							tvColSel.setVisibility(View.VISIBLE);
						}
					});

					Log.e("===VIEW===", "top="
							+ colSelector.getContentView().getTop() + ", left="
							+ colSelector.getContentView().getLeft());
					pwCols.showAtLocation(vPwCols, Gravity.NO_GRAVITY,
							colSelectorLeft, colSelectorTop);
				}
			});

			// colSelector.showAsDropDown(llMain, 200, 200);
			((LinearLayout) colSelectorView).addView(tvColSel);
		}
		// TODO: no position calculation!!!
		colSelector.showAtLocation(colSelectorView, Gravity.NO_GRAVITY,
				colSelectorLeft, colSelectorTop);
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

			TextView tv = new VerticalTextView(viewGroup.getContext());
			tv.setTextColor(Color.WHITE);
			((LinearLayout) convertView).addView(tv);
			holder.groupItem = tv;
			// holder.groupItem = (TextView) convertView
			// .findViewById(R.id.tvcolselector_menu_item);

		} else {
			// TODO: something missing?!!!
			holder = (ViewHolder) convertView.getTag();
		}
		// holder.groupItem.setTextColor(Color.BLACK);
		holder.groupItem.setText(list.get(position));

		return convertView;
	}

	static class ViewHolder {
		TextView groupItem;
	}

}
