package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.CollectionItem;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionItemListActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectionlistitem);

		String collid = getIntent().getExtras().getString("collid");
		// Log.e("===CILActivity===", "collid=" + collid);

		// get collection data
		NewsInTimeApp app = (NewsInTimeApp) getApplication();
		AppData data = app.getData();
		Collection c = data.getCollectionById(collid, data.getCollectionList());

		// change title
		TextView tvTitle = (TextView) findViewById(R.id.collectionlistitem_title);
		tvTitle.setText("Edit \"" + c.getName() + "\"");

		// get data dynamically
		// List<Collection> collections = app.getData().getCollectionList();
		CollectionListItemAdapter adapter = new CollectionListItemAdapter(this,
				c.getItems());

		// listView = new ListView(this);// 实例化列表视图
		ListView listView = (ListView) findViewById(R.id.collectionlistitem_lv);
		listView.setAdapter(adapter);
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new OnItemClickListener() {
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

		// //显示列表视图
		// this.setContentView(listView);

	}

}

class CollectionListItemAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<CollectionItem> collitems;
	public static Map<Integer, Boolean> isSelected;
	private Activity activity;

	public CollectionListItemAdapter(Activity a, List<CollectionItem> c) {
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
		CollectionListItemViewHolder holder = null;
		// convertView为null的时候初始化convertView。
		if (convertView == null) {
			holder = new CollectionListItemViewHolder();
			convertView = mInflater.inflate(
					R.layout.collectionlistitem_listview, null);
			// holder.img = (ImageView) convertView.findViewById(R.id.co);
			holder.title = (TextView) convertView
					.findViewById(R.id.collectionlistitem_listview_text);
			holder.cBox = (CheckBox) convertView
					.findViewById(R.id.collectionlistitem_listview_checkbox);
			holder.cBox
					.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {

						}

					});
			holder.btEdit = (Button) convertView
					.findViewById(R.id.collectionlistitem_listview_btedit);
			holder.btEdit.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					Intent intent = new Intent(activity,
							CollectionItemActivity.class);
					// intent.putExtra("collid", h.coll.getId());
					// intent.putExtra("collitemid", );
					activity.startActivity(intent);

					// ----old

					// LayoutInflater inflater = (LayoutInflater) activity
					// .getSystemService(activity.LAYOUT_INFLATER_SERVICE);
					//
					// View pwView = inflater.inflate(
					// R.layout.collectionlistitemitem, null, false);
					//
					// RadioGroup rgp = (RadioGroup) pwView
					// .findViewById(R.id.radioSex);
					// final Button btSelect = (Button) pwView
					// .findViewById(R.id.collectionlistitemitem_btSelect);
					// final EditText etManual = (EditText) pwView
					// .findViewById(R.id.collectionlistitemitem_etManual);
					// rgp.setOnCheckedChangeListener(new
					// RadioGroup.OnCheckedChangeListener() {
					// public void onCheckedChanged(RadioGroup group,
					// int checkedId) {
					// if (checkedId == R.id.collectionlistitemitem_rbManual) {
					// etManual.setEnabled(true);
					// btSelect.setEnabled(false);
					// } else {
					// etManual.setEnabled(false);
					// btSelect.setEnabled(true);
					// }
					// }
					// });
					//
					// PopupWindow pw = new PopupWindow(pwView, 300, 200, true);
					// // The code below assumes that the root container has an
					// id
					// // called 'main'
					// pw.showAtLocation(
					// activity.findViewById(R.id.collectionlistitem_lv),
					// Gravity.CENTER, 0, 0);

				}
			});
			convertView.setTag(holder);
		} else {
			holder = (CollectionListItemViewHolder) convertView.getTag();
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

final class CollectionListItemViewHolder {
	// public ImageView img;
	public TextView title;
	public CheckBox cBox;
	public Button btEdit;
	public CollectionItem collItem;
}
