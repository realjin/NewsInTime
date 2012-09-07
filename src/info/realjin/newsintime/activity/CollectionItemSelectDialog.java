package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.CollectionItem;
import info.realjin.newsintime.domain.PredefinedCollectionItem;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionItemSelectDialog extends Dialog {

	public CollectionItemSelectDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collectionitem_select);

		// setTitle("New source");

		// get collection data
		NewsInTimeApp app = (NewsInTimeApp) (((Activity) context)
				.getApplication());
		AppData data = app.getData();

		// add listview
		CollectionItemSelectAdapter adapter = new CollectionItemSelectAdapter(
				context, data.getPredefinedCollectionItemList());
		ListView lv = (ListView) findViewById(R.id.collectionitem_select_lv);
		lv.setAdapter(adapter);
		lv.setItemsCanFocus(false);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

}

class CollectionItemSelectAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<PredefinedCollectionItem> collitems;
	public static Map<Integer, Boolean> isSelected;
	private Context context;

	public CollectionItemSelectAdapter(Context ctx,
			List<PredefinedCollectionItem> c) {
		this.context = ctx;
		this.collitems = c;
		Log.e("CollectionItemSelectDialog CollectionListItemSelectAdapter, size=",
				"" + c.size());
		mInflater = LayoutInflater.from(ctx);
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
		CollectionItemSelectViewHolder holder = null;
		// convertViewΪnull��ʱ���ʼ��convertView��
		if (convertView == null) {
			holder = new CollectionItemSelectViewHolder();
			convertView = mInflater.inflate(
					R.layout.collectionlistitemitem_select_listview, null);
			// holder.img = (ImageView) convertView.findViewById(R.id.co);
			holder.title = (TextView) convertView
					.findViewById(R.id.collectionlistitemitem_select_listview_tv);
			holder.title.setTag(holder);

			convertView.setTag(holder);
		} else {
			holder = (CollectionItemSelectViewHolder) convertView.getTag();
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

final class CollectionItemSelectViewHolder {
	// public ImageView img;
	public TextView title;
	public CollectionItem collItem;
}
