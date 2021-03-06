package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.dao.CollectionDao;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.AppMessage;
import info.realjin.newsintime.domain.CollectionItem;
import info.realjin.newsintime.domain.PredefinedCollectionItem;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CollectionItemActivity extends Activity {

	private String currentCollId;
	private CollectionItem newCi;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectionlistitemitem);

		currentCollId = getIntent().getExtras().getString("collId");
		Log.e("[Activity]CI", "collId=" + currentCollId);

		NewsInTimeApp app = (NewsInTimeApp) getApplication();

		final RadioGroup rgSelect = (RadioGroup) this
				.findViewById(R.id.collectionlistitemitem_rgSelect);
		final RadioButton rbSelect = (RadioButton) this
				.findViewById(R.id.collectionlistitemitem_rbSelect);

		final RadioGroup rgManual = (RadioGroup) this
				.findViewById(R.id.collectionlistitemitem_rgManual);
		final RadioButton rbManual = (RadioButton) this
				.findViewById(R.id.collectionlistitemitem_rbManual);

		final Button btSelect = (Button) this
				.findViewById(R.id.collectionlistitemitem_btSelect);
		final EditText etManual = (EditText) this
				.findViewById(R.id.collectionlistitemitem_etManual);

		rgSelect.check(R.id.collectionlistitemitem_rbSelect);
		rgManual.clearCheck();
		etManual.setEnabled(false);

		rbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					rgManual.clearCheck();
					btSelect.setEnabled(true);
					etManual.setEnabled(false);
				}
			}
		});

		rbManual.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					rgSelect.clearCheck();
					etManual.setEnabled(true);
					btSelect.setEnabled(false);
				}
			}
		});

		btSelect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater) CollectionItemActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View pwView = inflater.inflate(
						R.layout.collectionlistitemitem_select, null, false);

				// get collection data
				NewsInTimeApp app = (NewsInTimeApp) getApplication();
				AppData data = app.getData();

				// add listview
				CollectionListItemSelectAdapter adapter = new CollectionListItemSelectAdapter(
						CollectionItemActivity.this, data
								.getPredefinedCollectionItemList());

				final PopupWindow pw = new PopupWindow(pwView, 300, 400, true);

				ListView lv = (ListView) pwView
						.findViewById(R.id.lvCollectionlistitemitem_select);
				lv.setAdapter(adapter);
				lv.setItemsCanFocus(false);
				lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						CollectionListItemSelectViewHolder vHolder = (CollectionListItemSelectViewHolder) v
								.getTag();
						CollectionItem ci = vHolder.collItem;

						// show name of selected
						Button bt = (Button) CollectionItemActivity.this
								.findViewById(R.id.collectionlistitemitem_btSelect); // btedit
						bt.setText(ci.getName());

						// TODO: update cached data
						newCi = new CollectionItem();
						newCi.setName(ci.getName());
						newCi.setUrl(ci.getUrl());

						// hide popup
						pw.dismiss();
					}
				});

				// TODO: multiple times???
				pw.setBackgroundDrawable(new BitmapDrawable());
				// pw.setOutsideTouchable(true);
				pw.setFocusable(true);
				// The code below assumes that the root container has an
				// id
				// called 'main'
				pw.showAtLocation(CollectionItemActivity.this
						.findViewById(R.id.tlcollectionlistitemitem),
						Gravity.CENTER, 0, 0);
			}
		});

		Button btOk = (Button) findViewById(R.id.collectionlistitemitem_btOk);
		btOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NewsInTimeApp app = (NewsInTimeApp) CollectionItemActivity.this
						.getApplication();
				CollectionDao dao = app.getDbmService().getCollectionDao();

				// TODO: update DB!!! (put currentCi to db); no!

				// put message before finish

				app.putMessage(AppMessage.MSG_CIACT_CILACT_NEWCOLLITEM, newCi);

				Intent intent = getIntent();
				Bundle bundle = new Bundle();
				bundle.putString("name", "This is from ShowMsg!");
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				CollectionItemActivity.this.finish();
			}
		});
		Button btCancel = (Button) findViewById(R.id.collectionlistitemitem_btCancel);
	}
}

class CollectionListItemSelectAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<PredefinedCollectionItem> collitems;
	public static Map<Integer, Boolean> isSelected;
	private Activity activity;

	public CollectionListItemSelectAdapter(Activity a, List<PredefinedCollectionItem> c) {
		this.activity = a;
		this.collitems = c;
		Log.e("COLLITEMSIN CollectionListItemSelectAdapter, size=",
				"" + c.size());
		mInflater = LayoutInflater.from(a);
	}

	public int getCount() {
		return collitems.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		CollectionListItemSelectViewHolder holder = null;
		// convertViewΪnull��ʱ���ʼ��convertView��
		if (convertView == null) {
			holder = new CollectionListItemSelectViewHolder();
			convertView = mInflater.inflate(
					R.layout.collectionlistitemitem_select_listview, null);
			// holder.img = (ImageView) convertView.findViewById(R.id.co);
			holder.title = (TextView) convertView
					.findViewById(R.id.collectionlistitemitem_select_listview_tv);
			holder.title.setTag(holder);

			convertView.setTag(holder);
		} else {
			holder = (CollectionListItemSelectViewHolder) convertView.getTag();
		}

		CollectionItem ci = collitems.get(position);

		holder.collItem = ci;

		// holder.img.setBackgroundResource((Integer) colls.get(position).get(
		// "img"));
		holder.title.setText(ci.getName());
		// holder.cBox.setChecked(isSelected.get(position));
		return convertView;
	}

	public List<PredefinedCollectionItem> getCollitems() {
		return collitems;
	}

	public void setCollitems(List<PredefinedCollectionItem> collitems) {
		this.collitems = collitems;
	}
}

final class CollectionListItemSelectViewHolder {
	// public ImageView img;
	public TextView title;
	public CollectionItem collItem;
}