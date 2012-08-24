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
		
		// listView = new ListView(this);// ʵ�����б���ͼ
		ListView listView = (ListView) findViewById(R.id.collectionlistitem_lv);
		listView.setAdapter(adapter);
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CollectionListItemViewHolder vHollder = (CollectionListItemViewHolder) view
						.getTag();
				// ��ÿ�λ�ȡ�����itemʱ�����ڵ�checkbox״̬�ı䣬ͬʱ�޸�map��ֵ��
				vHollder.cBox.toggle();
				CollectionListAdapter.isSelected.put(position,
						vHollder.cBox.isChecked());
			}
		});

		// //��ʾ�б���ͼ
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

	// ��ʼ��
	private void init() {
		// mData = new ArrayList<Map<String, Object>>();
		// for (int i = 0; i < 5; i++) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("img", R.drawable.ic_launcher);
		// map.put("title", "��" + (i + 1) + "�еı���");
		// mData.add(map);
		// }
		// �������isSelected���map�Ǽ�¼ÿ��listitem��״̬����ʼ״̬ȫ��Ϊfalse��
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
		// convertViewΪnull��ʱ���ʼ��convertView��
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
