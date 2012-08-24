package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.domain.Collection;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;

/*
 * http://www.cnblogs.com/allin/archive/2010/05/11/1732200.html
 * http://appfulcrum.com/2010/09/12/listview-example-3-simple-multiple-selection-checkboxes/
 * http://gqdy365.iteye.com/blog/992340
 * http://www.himigame.com/android-game/374.html
 * http://xniwdzef.blogbus.com/logs/175054319.html
 * http://webknox.com/q/checkbox-auto-call-oncheckedchange-when-listview-scroll
 * http://www.eoeandroid.com/thread-74855-1-1.html
 */

public class CollectionListActivity extends Activity {
	private SimpleAdapter adapter;// 声明适配器对象
	private ListView listView; // 声明列表视图对象
	private List<Map<String, Object>> list;// 声明列表容器
	public static CollectionListActivity ma;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectionlist);

		NewsInTimeApp app = (NewsInTimeApp) getApplication();

		CollectionListAdapter adapter = new CollectionListAdapter(this);
		// get data dynamically
		List<Collection> collections = app.getData().getCollectionList();
		adapter.setColls(collections);
		// listView = new ListView(this);// 实例化列表视图
		listView = (ListView) findViewById(R.id.collectionlist_lv);
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

	public CollectionListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
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
							
						}
						
					});
			convertView.setTag(holder);
		} else {
			holder = (CollectionListViewHolder) convertView.getTag();
		}
		// holder.img.setBackgroundResource((Integer) colls.get(position).get(
		// "img"));
		holder.title.setText(colls.get(position).getName());
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
}
