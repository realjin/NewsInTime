package info.realjin.newsintime.activity;

import info.realjin.newsintime.R;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/*
 * http://www.cnblogs.com/allin/archive/2010/05/11/1732200.html
 * http://appfulcrum.com/2010/09/12/listview-example-3-simple-multiple-selection-checkboxes/
 * http://gqdy365.iteye.com/blog/992340
 */

public class CollectionListActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectionlist);

		ListView list = (ListView) findViewById(R.id.lv);
		MyAdapter adapter = new MyAdapter(this);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ViewHolder vHollder = (ViewHolder) view.getTag();
				// ��ÿ�λ�ȡ�����itemʱ�����ڵ�checkbox״̬�ı䣬ͬʱ�޸�map��ֵ��
				vHollder.cBox.toggle();
				MyAdapter.isSelected.put(position, vHollder.cBox.isChecked());
			}
		});

	}

}

class MyAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mData;
	public static Map<Integer, Boolean> isSelected;

	public MyAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		init();
	}

	// ��ʼ��
	private void init() {
		mData = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img", R.drawable.ic_launcher);
			map.put("title", "��" + (i + 1) + "�еı���");
			mData.add(map);
		}
		// �������isSelected���map�Ǽ�¼ÿ��listitem��״̬����ʼ״̬ȫ��Ϊfalse��
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < mData.size(); i++) {
			isSelected.put(i, false);
		}
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// convertViewΪnull��ʱ���ʼ��convertView��
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.collectionlist_listview,
					null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.cBox = (CheckBox) convertView.findViewById(R.id.cb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setBackgroundResource((Integer) mData.get(position).get(
				"img"));
		holder.title.setText(mData.get(position).get("title").toString());
		holder.cBox.setChecked(isSelected.get(position));
		return convertView;
	}

}

final class ViewHolder {
	public ImageView img;
	public TextView title;
	public CheckBox cBox;
}
