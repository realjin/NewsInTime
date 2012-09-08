package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.AppMessage;
import info.realjin.newsintime.domain.CollectionItem;
import info.realjin.newsintime.domain.PredefinedCategory;
import info.realjin.newsintime.domain.PredefinedCollectionItem;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class CollectionItemSelectDialog extends Dialog {

	// private Context ctx;
	private CollectionItemListActivity activity;

	public CollectionItemSelectDialog(CollectionItemListActivity a) {
		super(a);
		this.activity = a;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collectionitem_select);

		NewsInTimeApp app = (NewsInTimeApp) (a.getApplication());
		AppData data = app.getData();

		// setTitle("New source");

		// 1. config category spinner
		Spinner spnCat = (Spinner) findViewById(R.id.collectionitem_select_spnCategory);

		CollectionItemCategoryAdapter spnCatAdapter = new CollectionItemCategoryAdapter(
				a, data.getPdCategoryList());
		spnCat.setAdapter(spnCatAdapter);
		spnCat.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterView, View v,
					int position, long id) {
				NewsInTimeApp app = (NewsInTimeApp) (activity.getApplication());
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

			}

			public void onNothingSelected(AdapterView<?> adapter) {
			}
		});

		// 2. config listview
		// add listview
		CollectionItemSelectAdapter adapter = new CollectionItemSelectAdapter(
				activity, data.getPredefinedCollectionItemList());
		ListView lv = (ListView) findViewById(R.id.collectionitem_select_lv);
		lv.setAdapter(adapter);
		lv.setItemsCanFocus(false);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				CollectionItemSelectViewHolder vHolder = (CollectionItemSelectViewHolder) v
						.getTag();
				CollectionItem ci = vHolder.collItem;

				CollectionItem newCi = new CollectionItem();
				newCi.setName(ci.getName());
				newCi.setUrl(ci.getUrl());

//				NewsInTimeApp app = (NewsInTimeApp) ((activity)
//						.getApplication());
				// put message before finish
//				app.putMessage(AppMessage.MSG_CIACT_CILACT_NEWCOLLITEM, newCi);

				CollectionItemSelectDialog.this.dismiss();
				activity.onCollectionItemSelectDialogReturn(newCi);
				
			}
		});
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

class CollectionItemCategoryAdapter extends ArrayAdapter<PredefinedCategory> {
	private LayoutInflater mInflater;
	private List<PredefinedCategory> pdCats;
	public static Map<Integer, Boolean> isSelected;
	private Context context;

	public CollectionItemCategoryAdapter(Context ctx, List<PredefinedCategory> c) {
		super(ctx, android.R.layout.simple_spinner_item, c);
		this.context = ctx;
		this.pdCats = c;
		Log.e("CollectionItemSelectDialog CollectionItemCategoryAdapter, size=",
				"" + c.size());
		mInflater = LayoutInflater.from(ctx);
	}

	public int getCount() {
		return pdCats.size();
	}

	public PredefinedCategory getItem(int position) {
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

		((TextView) convertView).setWidth(60);
		((TextView) convertView).setTextColor(Color.RED);
		((TextView) convertView).setText(c.getName());
		convertView.setTag(c);

		return convertView;
	}

	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		PredefinedCategory c = pdCats.get(position);

		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.collectionitem_select_spinner_dropdown, null);
		} else {
		}

		TextView tv = (TextView) convertView.findViewById(R.id.label);
		tv.setText(c.getName());
		// label.setText(getItem(position));

		// if (spinner.getSelectedItemPosition() == position) {
		// label.setTextColor(getResources().getColor(R.color.selected_fg));
		// view.setBackgroundColor(getResources()
		// .getColor(R.color.selected_bg));
		// view.findViewById(R.id.icon).setVisibility(View.VISIBLE);
		// }

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
