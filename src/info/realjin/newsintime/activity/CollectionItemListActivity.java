package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.AppMessage;
import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.CollectionItem;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionItemListActivity extends Activity {
	enum Operation {
		ADD, UPDATE
	}

	private Operation operation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectionlistitem);

		NewsInTimeApp app = (NewsInTimeApp) getApplication();

		// set listener
		Button btAdd = (Button) findViewById(R.id.collectionlistitem_btadd);
		btAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Button bt = (Button) v;
				Intent intent = new Intent(CollectionItemListActivity.this,
						CollectionItemActivity.class);
				intent.putExtra("action", "add");
				CollectionItemListActivity.this.startActivityForResult(intent,
						200);
			}
		});

		// get name field
		EditText etName = (EditText) findViewById(R.id.collectionlistitem_etname);
		// LinearLayout llName = (LinearLayout)
		// findViewById(R.id.collectionlistitem_llname);

		String action = getIntent().getExtras().getString("action");
		if (action.equals("update")) {
			operation = Operation.UPDATE;
			String collid = getIntent().getExtras().getString("collid");
			// Log.e("===CILActivity===", "collid=" + collid);

			// get collection data
			AppData data = app.getData();
			Collection c = data.getCollectionById(collid,
					data.getCollectionList());

			// change title
			TextView tvTitle = (TextView) findViewById(R.id.collectionlistitem_title);
			tvTitle.setText("Edit \"" + c.getName() + "\"");

			// change rename filed
			etName.setText(c.getName());

			// get data dynamically
			// List<Collection> collections = app.getData().getCollectionList();
			CollectionListItemAdapter adapter = new CollectionListItemAdapter(
					this, c.getItems());

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
		} else if (action.equals("add")) {
			operation = Operation.ADD;
			// change title
			TextView tvTitle = (TextView) findViewById(R.id.collectionlistitem_title);
			tvTitle.setText("Add new");

			// change rename filed
			etName.setText("");
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// if (requestCode == REQUEST_CODE) {
		// if (resultCode == RESULT_CANCELED)
		// setTitle("cancle");
		// else if (resultCode == RESULT_OK) {
		// String temp = null;
		// Bundle bundle = data.getExtras();
		// if (bundle != null)
		// temp = bundle.getString("name");
		// setTitle(temp);
		// }
		// }
		Log.e("OOOOOOOO", "onActivityResult");
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
		CollectionItem ci = collitems.get(position);

		CollectionListItemViewHolder holder = null;
		// convertViewΪnull��ʱ���ʼ��convertView��
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
			holder.btEdit.setTag(holder);
			holder.btEdit.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					Intent intent = new Intent(activity,
							CollectionItemActivity.class);
					// intent.putExtra("collid", h.coll.getId());

					// set op
					intent.putExtra("action", "update");

					// send item to update as message(including id)
					NewsInTimeApp app = (NewsInTimeApp) activity
							.getApplication();
					CollectionListItemViewHolder h = (CollectionListItemViewHolder) v
							.getTag();
					app.putMessage(AppMessage.MSG_CILACT_CIACT_ITEM, h.collItem);

					activity.startActivityForResult(intent, 200);

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

		holder.title.setText(ci.getUrl());
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
