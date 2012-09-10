package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.anim.LongTextMovingAnimation;
import info.realjin.newsintime.dao.CollectionDao;
import info.realjin.newsintime.domain.AppConfig;
import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.view.VerticalListView;
import info.realjin.newsintime.view.VerticalSlider;
import info.realjin.newsintime.view.VerticalTextView;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/**
	 * main text view
	 */
	private TextView tvMain;
	// private TextView tvProgress;

	// collection selector
	private PopupWindow colSelector;
	private View colSelectorView;
	private PopupWindow pwCols;
	private View vPwCols;
	// TODO: no calc!!!
	int colSelectorWidth;// = 300;
	int colSelectorHeight = 400;
	int colSelectorLeft = 125;
	int colSelectorTop = 390;

	//
	// private Button btPlay;

	// private ProgressBar pbarMain;
	private VerticalSlider vsMain;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		NewsInTimeApp app = (NewsInTimeApp) getApplication();
		app.notifyFirstActivityStart(this);

		// get screen size and save it
		Display display = getWindowManager().getDefaultDisplay();
		app.getData().setScrHeight(display.getHeight());
		app.getData().setScrWidth(display.getWidth());
		Log.e("===APPDATA===", app.getData().toString());

		// init main textview
		RelativeLayout llMain = (RelativeLayout) findViewById(R.id.llMain);

		tvMain = new VerticalTextView(this);
		tvMain.setText("");
		tvMain.setTextSize(28.0f);
		tvMain.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);

		RelativeLayout.LayoutParams rlp;
		rlp = new RelativeLayout.LayoutParams(50, 50);
		rlp.leftMargin = 100;
		rlp.topMargin = 0;
		tvMain.setLayoutParams(rlp);

		llMain.addView(tvMain);

		// init slider
		vsMain = (VerticalSlider) findViewById(R.id.vsMain);

		// init test button
		// btPlay = new Button(this);
		// btPlay.setText("Play");
		// btPlay.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// /*
		// * check if there is any news yet(maybe connection error and
		// * there's no news yet
		// */
		// int nNews = ((NewsInTimeApp) getApplication()).getData()
		// .getNewsList().size();
		// if (nNews == 0) {
		// Log.e("===MainActivity===", "playing failed: news null");
		// }
		// tvMain.startAnimation(new LongTextMovingAnimation(
		// MainActivity.this, tvMain));
		// }
		// });
		// rlp = new RelativeLayout.LayoutParams(100, 50);
		// rlp.leftMargin = 200;
		// rlp.topMargin = 400;
		// btPlay.setLayoutParams(rlp);
		// llMain.addView(btPlay);

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
		// load collection data from database
		NewsInTimeApp app = (NewsInTimeApp) getApplication();
		CollectionDao dao = app.getDbmService().getCollectionDao();
		List<Collection> colls = dao.getAllCollectionsWithItems();

		// check if there is any collection loaded
		if (colls == null || colls.size() == 0) {
			Log.e("===MainActivity===", "showColSelector: coll empty!");
			return;
			// TODO: show "NULL" text selector which is disabled(just a view)
		}

		if (colSelector == null) {
			Log.d("===MainActivity===", "colSelector creating");

			// prepare text
			final TextView tvColSel = new VerticalTextView(this);
			tvColSel.setText(colls.get(0).getName());
			tvColSel.setTextSize(28.0f);
			// tvColSel.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					new ViewGroup.MarginLayoutParams(
							RelativeLayout.LayoutParams.FILL_PARENT,
							RelativeLayout.LayoutParams.FILL_PARENT));
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

					AdapterView lvCols;
					lvCols = new VerticalListView(MainActivity.this);
					AdapterView.OnItemClickListener lvColsOnItemClickListener = new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> adapter,
								View arg1, int position, long id) {
							Log.d("===0812===", "click! position=" + position
									+ ", id=" + id);

							// change collection
							GroupAdapter a = (GroupAdapter) adapter
									.getAdapter();
							// mmm: right? position or id?
							Collection coll = (Collection) a.getItem(position);
							// hide pw
							pwCols.dismiss();
							Toast.makeText(MainActivity.this,
									"Collection \"" + coll.getName() + "\"",
									Toast.LENGTH_LONG).show();
							NewsInTimeApp app = (NewsInTimeApp) getApplication();
							app.getNrService().restartByNewColItem(coll);

						}
					};
					lvCols.setOnItemClickListener(lvColsOnItemClickListener);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT);
					lvCols.setLayoutParams(lp);
					((LinearLayout) vPwCols).addView(lvCols);

					// = (ListView) vPwCols.findViewById(R.id.lvCols);
					// �������
					// List<String> collNames = new ArrayList<String>();

					// for (Collection coll : colls) {
					// collNames.add(coll.getName());
					// }

					NewsInTimeApp app = (NewsInTimeApp) getApplication();
					CollectionDao dao = app.getDbmService().getCollectionDao();
					List<Collection> colls = dao.getAllCollectionsWithItems();
					GroupAdapter groupAdapter = new GroupAdapter(
							MainActivity.this, colls);
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

					// Log.e("===VIEW===", "top="
					// + colSelector.getContentView().getTop() + ", left="
					// + colSelector.getContentView().getLeft());
					pwCols.showAtLocation(vPwCols, Gravity.NO_GRAVITY,
							colSelectorLeft, colSelectorTop);
				}
			});

			// get longest colname
			Collection colLongestColName;
			float wLongestColName = 0;
			float mw = 0;
			for (Collection col : colls) {
				mw = tvColSel.getPaint().measureText(col.getName());
				if (mw > wLongestColName) {
					colLongestColName = col;
				}
			}
			colSelectorWidth = (int) (mw + 2);

			// prepare colsel view
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			colSelectorView = layoutInflater
					.inflate(R.layout.colselector, null);

			colSelector = new PopupWindow(colSelectorView, 30, colSelectorWidth);
			colSelector.setBackgroundDrawable(new BitmapDrawable());
			colSelector.setOutsideTouchable(true);
			colSelector.setFocusable(true);

			// add tv to colsel view
			// colSelector.showAsDropDown(llMain, 200, 200);
			Log.e("===0910===", "tv mlen=" + tvColSel.getMeasuredWidth()
					+ ", mheight=" + tvColSel.getMeasuredHeight());
			Log.e("===0910===", "tv len=" + tvColSel.getWidth() + ", height="
					+ tvColSel.getHeight());
			Log.e("===0910===",
					"tv measuretext.w="
							+ tvColSel.getPaint().measureText(
									tvColSel.getText().toString()));
			((LinearLayout) colSelectorView).addView(tvColSel);
		}
		Log.d("===MainActivity===", "colSelector created");
		// TODO: no position calculation!!!
		colSelector.showAtLocation(colSelectorView, Gravity.NO_GRAVITY,
				colSelectorLeft, colSelectorTop);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * 
		 * add()�������ĸ����������ǣ�
		 * 
		 * 1�����������Ļ���дMenu.NONE,
		 * 
		 * 2��Id���������Ҫ��Android������Id��ȷ����ͬ�Ĳ˵�
		 * 
		 * 3��˳���Ǹ��˵�������ǰ�����������Ĵ�С����
		 * 
		 * 4���ı����˵�����ʾ�ı�
		 */

		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "Edit Collection").setIcon(
				android.R.drawable.ic_menu_edit);
		// setIcon()����Ϊ�˵�����ͼ�꣬����ʹ�õ���ϵͳ�Դ��ͼ�꣬ͬѧ������һ��,��
		// android.R��ͷ����Դ��ϵͳ�ṩ�ģ������Լ��ṩ����Դ����R��ͷ��
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "Play").setIcon(
				android.R.drawable.ic_media_play);
		menu.add(Menu.NONE, Menu.FIRST + 3, 5, "Menu3").setIcon(
				android.R.drawable.ic_menu_help);
		menu.add(Menu.NONE, Menu.FIRST + 4, 6, "Menu4").setIcon(
				android.R.drawable.ic_menu_day);
		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case Menu.FIRST + 1:
			Intent intent = new Intent(this, CollectionListActivity.class);
			intent.putExtra("lastActivity",
					MainActivity.class.getCanonicalName());
			this.startActivity(intent);

			break;
		case Menu.FIRST + 2:
			Toast.makeText(this, "Playing", Toast.LENGTH_LONG).show();

			int nNews = ((NewsInTimeApp) getApplication()).getData()
					.getNewsList().size();
			if (nNews == 0) {
				Log.e("===MainActivity===", "playing failed: news null");
			}
			tvMain.startAnimation(new LongTextMovingAnimation(
					MainActivity.this, tvMain));
			break;
		}

		return false;

	}

	// private class UpdateBarTask extends AsyncTask<Void, Integer, Void> {
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	// int max = pbarMain.getMax();
	// Log.e("MAINACTIVITY", "pbar.max=" + max);
	// for (int i = 0; i <= max; i++) {
	// try {
	// // update every second
	// Thread.sleep(100);
	// } catch (InterruptedException e) {
	//
	// }
	//
	// publishProgress(i);
	// }
	//
	// return null;
	// }
	//
	// @Override
	// protected void onProgressUpdate(Integer... values) {
	// pbarMain.setProgress(values[0]);
	// }
	// }

	public TextView getTvMain() {
		return tvMain;
	}

	public void setTvMain(TextView tvMain) {
		this.tvMain = tvMain;
	}

	public VerticalSlider getVsMain() {
		return vsMain;
	}

	public void setVsMain(VerticalSlider vsMain) {
		this.vsMain = vsMain;
	}

	// public TextView getTvProgress() {
	// return tvProgress;
	// }
	//
	// public void setTvProgress(TextView tvProgress) {
	// this.tvProgress = tvProgress;
	// }

	// public ProgressBar getPbarMain() {
	// return pbarMain;
	// }
	//
	// public void setPbarMain(ProgressBar pbarMain) {
	// this.pbarMain = pbarMain;
	// }
}

// ------------------- class

class GroupAdapter extends BaseAdapter {

	private Context context;

	private List<Collection> list;

	public GroupAdapter(Context context, List<Collection> list) {

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
			tv.setTextColor(Color.RED);
			/*
			 * TODO: mmmmmmmmmm what if total text height exceeds listview
			 * height?
			 */
			NewsInTimeApp app = (NewsInTimeApp) (((MainActivity) context)
					.getApplication());
			tv.setTextSize(Integer.parseInt(app.getConfig().get(
					AppConfig.CFGNAME_UI_MAIN_COLSELECTOR_TEXTSIZE)));
			((LinearLayout) convertView).addView(tv);
			holder.groupItem = tv;
			// holder.groupItem = (TextView) convertView
			// .findViewById(R.id.tvcolselector_menu_item);

		} else {
			// TODO: something missing?!!!
			holder = (ViewHolder) convertView.getTag();
		}
		// holder.groupItem.setTextColor(Color.BLACK);
		holder.groupItem.setText(list.get(position).getName());
		holder.collId = list.get(position).getId();

		return convertView;
	}

	static class ViewHolder {
		TextView groupItem;
		Integer collId;
	}

	public List<Collection> getList() {
		return list;
	}

	public void setList(List<Collection> list) {
		this.list = list;
	}

}
