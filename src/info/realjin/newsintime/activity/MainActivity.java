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
	private PopupWindow pwColSelectorBar;
	private View vPwColSelectorBar;

	// collection menu
	private PopupWindow pwColSelectorMenu;
	private View vPwColSelectorMenu;

	// TODO: no calc!!!
	int colSelectorBarWidth;// = 300;
	int colSelectorBarHeight = 50;
	int colSelectorBarLeft = 125;
	int colSelectorBarTop = 390;
	final int colSelectorTextPadding = 20;

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
		tvMain.setTextColor(0xfff12938);
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

		// /*
		// * check if there is any news yet(maybe connection error and
		// * there's no news yet
		// */
		// int nNews = ((NewsInTimeApp) getApplication()).getData()
		// .getNewsList().size();
		// if (nNews == 0) {
		// Log.e("===MainActivity===", "playing failed: news null");
		// }

		// init
		pwColSelectorBar = null;

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
		AppConfig cfg = app.getConfig();
		CollectionDao dao = app.getDbmService().getCollectionDao();
		List<Collection> colls = dao.getAllCollectionsWithItems();

		// check if there is any collection loaded
		if (colls == null || colls.size() == 0) {
			Log.e("===MainActivity===", "showColSelector: coll empty!");
			return;
			// TODO: show "NULL" text selector which is disabled(just a view)
		}

		if (pwColSelectorBar == null) {
			Log.d("===MainActivity===", "colSelector creating");

			// prepare text
			final TextView tvColSelBar = new VerticalTextView(this);
			tvColSelBar.setText(colls.get(0).getName());
			tvColSelBar.setTextSize(28.0f);
			// TODO: heuristic! may be not correct under certain circumstance!
			tvColSelBar.setPadding(5, -10, 0, 0);

			// tvColSel.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					new ViewGroup.MarginLayoutParams(
							RelativeLayout.LayoutParams.FILL_PARENT,
							RelativeLayout.LayoutParams.FILL_PARENT));
			lp.addRule(RelativeLayout.CENTER_IN_PARENT, 0); // or -1
			// lp.setMargins(0, colSelectorTextPadding, 0,
			// colSelectorTextPadding);
			tvColSelBar.setLayoutParams(lp);
			tvColSelBar.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					NewsInTimeApp app = (NewsInTimeApp) MainActivity.this
							.getApplication();
					AppConfig cfg = app.getConfig();

					Log.e("===MainActivity===", "tvCol onclick!");

					// 1. hide this tv
					v.setVisibility(View.GONE);

					// 2. create menu view
					LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					vPwColSelectorMenu = layoutInflater.inflate(
							R.layout.colselector_menu, null);
					String strColorPwColSelectorMenu = cfg.getProperty(
							AppConfig.CFGNAME_COLOR_BG_COLSELECTOR_MENU,
							"0xFFFFFF");
					Integer colorPwColSelectorMenu = Integer
							.decode(strColorPwColSelectorMenu);
					vPwColSelectorMenu
							.setBackgroundColor(0xFF000000 + colorPwColSelectorMenu);

					// 3. create menu listview
					AdapterView lvColSelectorMenu;
					lvColSelectorMenu = new VerticalListView(MainActivity.this);
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
							pwColSelectorMenu.dismiss();
							Toast.makeText(MainActivity.this,
									"Collection \"" + coll.getName() + "\"",
									Toast.LENGTH_LONG).show();
							NewsInTimeApp app = (NewsInTimeApp) getApplication();
							app.getNrService().restartByNewColItem(coll);

						}
					};
					lvColSelectorMenu.setOnItemClickListener(lvColsOnItemClickListener);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					lvColSelectorMenu.setLayoutParams(lp);
					
					// 3. add menu listview to menu view
					((LinearLayout) vPwColSelectorMenu).addView(lvColSelectorMenu);

					// 4. set menu listview data
					CollectionDao dao = app.getDbmService().getCollectionDao();
					List<Collection> colls = dao.getAllCollectionsWithItems();
					GroupAdapter groupAdapter = new GroupAdapter(
							MainActivity.this, colls);
					lvColSelectorMenu.setAdapter(groupAdapter);

					// 5. create menu popup based on the menu view
					pwColSelectorMenu = new PopupWindow(vPwColSelectorMenu,
							colSelectorBarWidth - 5, colSelectorBarWidth - 5);
					pwColSelectorMenu
							.setBackgroundDrawable(new BitmapDrawable());
					pwColSelectorMenu.setOutsideTouchable(true);
					pwColSelectorMenu.setFocusable(true);
					pwColSelectorMenu
							.setOnDismissListener(new OnDismissListener() {

								public void onDismiss() {
									tvColSelBar.setVisibility(View.VISIBLE);
								}
							});

					// Log.e("===VIEW===", "top="
					// + colSelector.getContentView().getTop() + ", left="
					// + colSelector.getContentView().getLeft());
					
					// 5. show menu popup
					pwColSelectorMenu.showAtLocation(vPwColSelectorMenu,
							Gravity.NO_GRAVITY, colSelectorBarLeft,
							colSelectorBarTop);
				}
			});

			// get longest colname
			Collection colLongestColName;
			float wLongestColName = 0;
			float mw = 0;
			for (Collection col : colls) {
				mw = tvColSelBar.getPaint().measureText(col.getName());
				if (mw > wLongestColName) {
					wLongestColName = mw;
					colLongestColName = col;
				}
			}
			colSelectorBarWidth = (int) (wLongestColName + 2 * colSelectorTextPadding);

			// create bar view
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			vPwColSelectorBar = layoutInflater.inflate(
					R.layout.colselector_bar, null);
			String strColorPwColSelectorBar = cfg.getProperty(
					AppConfig.CFGNAME_COLOR_BG_COLSELECTOR_BAR, "0xFFFFFF");
			Integer colorPwColSelectorBar = Integer
					.decode(strColorPwColSelectorBar);
			vPwColSelectorBar
					.setBackgroundColor(0xFF000000 + colorPwColSelectorBar);

			// create bar popup
			pwColSelectorBar = new PopupWindow(vPwColSelectorBar,
					colSelectorBarHeight, colSelectorBarWidth);
			pwColSelectorBar.setBackgroundDrawable(new BitmapDrawable());
			pwColSelectorBar.setOutsideTouchable(true);
			pwColSelectorBar.setFocusable(true);

			// add tv to bar view
			// colSelector.showAsDropDown(llMain, 200, 200);
			Log.e("===0910===", "tv mlen=" + tvColSelBar.getMeasuredWidth()
					+ ", mheight=" + tvColSelBar.getMeasuredHeight());
			Log.e("===0910===", "tv len=" + tvColSelBar.getWidth()
					+ ", height=" + tvColSelBar.getHeight());
			Log.e("===0910===",
					"tv measuretext.w="
							+ tvColSelBar.getPaint().measureText(
									tvColSelBar.getText().toString()));
			((LinearLayout) vPwColSelectorBar).addView(tvColSelBar);
		}
		Log.d("===MainActivity===", "colSelector created");
		// TODO: no position calculation!!!
		pwColSelectorBar.showAtLocation(vPwColSelectorBar, Gravity.NO_GRAVITY,
				colSelectorBarLeft, colSelectorBarTop);
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

	private Activity activity;

	private List<Collection> list;

	public GroupAdapter(Activity a, List<Collection> list) {

		this.activity = a;
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
		NewsInTimeApp app = (NewsInTimeApp) ((activity).getApplication());
		AppConfig cfg = app.getConfig();

		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.colselector_menu_item, null);
			holder = new ViewHolder();

			convertView.setTag(holder);

			TextView tv = new VerticalTextView(viewGroup.getContext());
			String strColorTv = cfg.getProperty(
					AppConfig.CFGNAME_COLOR_TEXT_COLSELECTOR_MENU_ITEM_TV,
					"0x000000");
			Integer colorPwColSelector = Integer.decode(strColorTv);
			tv.setTextColor(0xFF000000 + colorPwColSelector);
			/*
			 * TODO: mmmmmmmmmm what if total text height exceeds listview
			 * height?
			 */

			tv.setTextSize(Integer.parseInt(cfg
					.getProperty(AppConfig.CFGNAME_UI_MAIN_COLSELECTOR_TEXTSIZE)));
			((LinearLayout) convertView).addView(tv,
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.FILL_PARENT);
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
