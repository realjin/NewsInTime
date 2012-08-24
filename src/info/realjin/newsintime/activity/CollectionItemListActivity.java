package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.CollectionItem;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
		// init();
	}

	// 初始化
	private void init() {
		// mData = new ArrayList<Map<String, Object>>();
		// for (int i = 0; i < 5; i++) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("img", R.drawable.ic_launcher);
		// map.put("title", "第" + (i + 1) + "行的标题");
		// mData.add(map);
		// }
		// 这儿定义isSelected这个map是记录每个listitem的状态，初始状态全部为false。
		// isSelected = new HashMap<Integer, Boolean>();
		// for (int i = 0; i < mData.size(); i++) {
		// isSelected.put(i, false);
		// }
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
			convertView = mInflater.inflate(R.layout.collectionlist_listview,
					null);
			// holder.img = (ImageView) convertView.findViewById(R.id.co);
			holder.title = (TextView) convertView
					.findViewById(R.id.collectionlist_listview_text);
			holder.cBox = (CheckBox) convertView
					.findViewById(R.id.collectionlist_listview_checkbox);
			holder.cBox
					.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {

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
}
