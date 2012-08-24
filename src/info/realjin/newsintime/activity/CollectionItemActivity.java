package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.CollectionItem;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectionlistitemitem);

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
				ListView lv = (ListView) pwView
						.findViewById(R.id.lvCollectionlistitemitem_select);
				lv.setAdapter(adapter);
				lv.setItemsCanFocus(false);
				lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						CollectionListItemViewHolder vHollder = (CollectionListItemViewHolder) view
								.getTag();
						// 在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。
						vHollder.cBox.toggle();
						CollectionListAdapter.isSelected.put(position,
								vHollder.cBox.isChecked());
					}
				});

				PopupWindow pw = new PopupWindow(pwView, 300, 200, true);
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

	}
}

class CollectionListItemSelectAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<CollectionItem> collitems;
	public static Map<Integer, Boolean> isSelected;
	private Activity activity;

	public CollectionListItemSelectAdapter(Activity a, List<CollectionItem> c) {
		this.activity = a;
		this.collitems = c;
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
		// convertView为null的时候初始化convertView。
		if (convertView == null) {
			holder = new CollectionListItemSelectViewHolder();
			convertView = mInflater.inflate(
					R.layout.collectionlistitem_listview, null);
			// holder.img = (ImageView) convertView.findViewById(R.id.co);
			holder.title = (TextView) convertView
					.findViewById(R.id.collectionlistitem_listview_text);
			convertView.setTag(holder);
		} else {
			holder = (CollectionListItemSelectViewHolder) convertView.getTag();
		}
		// holder.img.setBackgroundResource((Integer) colls.get(position).get(
		// "img"));
		holder.title.setText(collitems.get(position).getUrl());
		// holder.cBox.setChecked(isSelected.get(position));
		return convertView;
	}

	public List<CollectionItem> getCollitems() {
		return collitems;
	}

	public void setCollitems(List<CollectionItem> collitems) {
		this.collitems = collitems;
	}
}

final class CollectionListItemSelectViewHolder {
	// public ImageView img;
	public TextView title;
	public CollectionItem collItem;
}