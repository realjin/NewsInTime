package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.domain.Collection;

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

/*
 * http://www.cnblogs.com/allin/archive/2010/05/11/1732200.html
 * http://appfulcrum.com/2010/09/12/listview-example-3-simple-multiple-selection-checkboxes/
 * http://gqdy365.iteye.com/blog/992340
 * http://www.himigame.com/android-game/374.html
 * http://xniwdzef.blogbus.com/logs/175054319.html
 * http://webknox.com/q/checkbox-auto-call-oncheckedchange-when-listview-scroll
 * http://www.eoeandroid.com/thread-74855-1-1.html
 * http://www.itivy.com/android/archive/2011/12/14/android-listview-checkbox-problem.html
 */

public class CollectionListActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectionlist);

		NewsInTimeApp app = (NewsInTimeApp) getApplication();

		// set button listener
		Button btAdd = (Button) findViewById(R.id.collectionlist_btadd);
		btAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Button bt = (Button) v;
				Intent intent = new Intent(CollectionListActivity.this,
						CollectionItemListActivity.class);
				intent.putExtra("action", "add");
				CollectionListActivity.this.startActivity(intent);
			}
		});

		// get data dynamically
		List<Collection> collections = app.getData().getCollectionList();
		CollectionListAdapter adapter = new CollectionListAdapter(this,
				collections);
		adapter.setColls(collections);
		// listView = new ListView(this);// 实例化列表视图
		ListView listView = (ListView) findViewById(R.id.collectionlist_lv);
		listView.setAdapter(adapter);
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CollectionListViewHolder vHollder = (CollectionListViewHolder) view
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

class CollectionListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Collection> colls;
	public static Map<Integer, Boolean> isSelected;
	private Activity activity;

	public CollectionListAdapter(Activity a, List<Collection> c) {
		activity = a;
		colls = c;
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
		return colls.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		CollectionListViewHolder holder = null;
		// convertView为null的时候初始化convertView。
		if (convertView == null) {
			holder = new CollectionListViewHolder();
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
							// TODO: bug fixing code here
						}

					});
			holder.btEdit = (Button) convertView
					.findViewById(R.id.collectionlist_listview_btedit);
			holder.btEdit.setTag(holder);
			holder.btEdit.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Button bt = (Button) v;
					CollectionListViewHolder h = (CollectionListViewHolder) bt
							.getTag();
					Intent intent = new Intent(activity,
							CollectionItemListActivity.class);
					intent.putExtra("action", "update");
					intent.putExtra("collid", h.coll.getId());
					activity.startActivity(intent);

				}
			});

			convertView.setTag(holder);
		} else {
			holder = (CollectionListViewHolder) convertView.getTag();
		}

		Collection c = colls.get(position);
		holder.coll = c;

		// holder.img.setBackgroundResource((Integer) colls.get(position).get(
		// "img"));
		holder.title.setText(c.getName());
		// holder.cBox.setChecked(isSelected.get(position));
		return convertView;
	}

	public List<Collection> getColls() {
		return colls;
	}

	public void setColls(List<Collection> colls) {
		this.colls = colls;
	}

}

final class CollectionListViewHolder {
	// public ImageView img;
	public TextView title;
	public CheckBox cBox;
	public Button btEdit;
	public Collection coll;
}
