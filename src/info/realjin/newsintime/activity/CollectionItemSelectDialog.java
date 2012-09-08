package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.CollectionItem;
import info.realjin.newsintime.domain.PredefinedCategory;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class CollectionItemSelectDialog extends Dialog {

	private Context ctx;

	public CollectionItemSelectDialog(Context context) {
		super(context);
		this.ctx = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collectionitem_select);

		NewsInTimeApp app = (NewsInTimeApp) (((Activity) context)
				.getApplication());
		AppData data = app.getData();

		// setTitle("New source");

		// 1. config category spinner
		Spinner spnCat = (Spinner) findViewById(R.id.collectionitem_select_spnCategory);

		CollectionItemCategoryAdapter spnCatAdapter = new CollectionItemCategoryAdapter(
				context, data.getPdCategoryList());
		spnCat.setAdapter(spnCatAdapter);
		spnCat.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterView, View v,
					int position, long id) {
				NewsInTimeApp app = (NewsInTimeApp) (((Activity) ctx)
						.getApplication());
				AppData data = app.getData();

				// TextView tv = (TextView)v;
				PredefinedCategory pdcat = (PredefinedCategory) v.getTag();
				if (pdcat != null) {
					String catId = pdcat.getId();
					List<PredefinedCollectionItem> pdCiList = data
							.getPredefinedCollectionItemListByCat(catId);

					ListView lv = (ListView) (CollectionItemSelectDialog.this
							.findViewById(R.id.collectionitem_select_lv));
					CollectionItemSelectAdapter a = (CollectionItemSelectAdapter) lv
							.getAdapter();
					a.setCollitems(pdCiList);
					a.notifyDataSetChanged();

				} else {
					Log.e("spnCat onItemSelected ", "pdcat is null");

				}
				// Log.e("view class=", v.getClass().getCanonicalName());

				// // Here you get the current item (a User object) that is
				// // selected by its position
				// User user = adapter.getItem(position);
				// // Here you can do the action you want to...
				// Toast.makeText(Main.this,
				// "ID: " + user.getId() + "\nName: " + user.getName(),
				// Toast.LENGTH_SHORT).show();
			}

			public void onNothingSelected(AdapterView<?> adapter) {
			}
		});

		// 2. config listview

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

class CollectionItemCategoryAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<PredefinedCategory> pdCats;
	public static Map<Integer, Boolean> isSelected;
	private Context context;

	public CollectionItemCategoryAdapter(Context ctx, List<PredefinedCategory> c) {
		this.context = ctx;
		this.pdCats = c;
		Log.e("CollectionItemSelectDialog CollectionItemCategoryAdapter, size=",
				"" + c.size());
		mInflater = LayoutInflater.from(ctx);
	}

	public int getCount() {
		return pdCats.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// CollectionItemSelectViewHolder holder = null;

		PredefinedCategory c = pdCats.get(position);

		// convertViewΪnull��ʱ���ʼ��convertView��
		if (convertView == null) {

			// holder = new CollectionItemSelectViewHolder();
			convertView = new TextView(context);

		} else {
			// holder = (CollectionItemSelectViewHolder) convertView.getTag();
		}

		((TextView) convertView).setText(c.getName());
		convertView.setTag(c);

		return convertView;
	}

	public List<PredefinedCategory> getPdCats() {
		return pdCats;
	}

	public void setPdCats(List<PredefinedCategory> pdCats) {
		this.pdCats = pdCats;
	}
}

// final class CollectionItemCategoryViewHolder {
// public TextView title;
// public PredefinedCategory pdcat;
// }
