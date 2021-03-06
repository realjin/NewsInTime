package info.realjin.newsintime.activity;

import info.realjin.newsintime.Constants;
import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.dao.CollectionDao;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.CollectionItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionItemListActivity extends Activity {
	enum Operation {
		ADD, UPDATE
	}

	private Operation operation;
	private Integer currentCollId;

	// for add
	private Collection newColl;

	// for update
	private Set<String> toDelCi;
	private Set<CollectionItem> toAddCi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectionlistitem);

		String lastActivity = getIntent().getExtras().getString("lastActivity");
		// check if jump from CollectionListActivity
		if (lastActivity
				.equals(CollectionListActivity.class.getCanonicalName())) {
			// init todel and toadd
			if (toDelCi == null || toAddCi == null) {
				toDelCi = new HashSet<String>();
				toAddCi = new HashSet<CollectionItem>();
			} else {
				toAddCi.clear();
				toDelCi.clear();
			}
		}

		NewsInTimeApp app = (NewsInTimeApp) getApplication();

		// set listener
		ImageButton btAdd = (ImageButton) findViewById(R.id.collectionlistitem_btaddselect);
		btAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// ((NewsInTimeApp)
				// getApplication().putMessage(AppMessage.MSG_CILACT_CIACT_COLL,
				// );

				// Intent intent = new Intent(CollectionItemListActivity.this,
				// CollectionItemActivity.class);
				showDialog(Constants.DIALOGID_CISELECT);

				// intent.putExtra("collId", currentCollId);
				// CollectionItemListActivity.this.startActivityForResult(intent,
				// 200);
			}
		});

		// get name field
		EditText etName = (EditText) findViewById(R.id.collectionlistitem_etname);
		// LinearLayout llName = (LinearLayout)
		// findViewById(R.id.collectionlistitem_llname);

		CollectionListItemAdapter adapter = null;

		String action = getIntent().getExtras().getString("action");
		if (action.equals("update")) {
			operation = Operation.UPDATE;

			currentCollId = getIntent().getExtras().getInt("collId");
			Log.e("[Activity]CIL", "collId=" + currentCollId);

			// get collection data
			AppData data = app.getData();
			CollectionDao dao = app.getDbmService().getCollectionDao();
			Collection c = dao.getCollectionWithItems(currentCollId);
			// Collection c = dao.getCollectionById(currentCollId,
			// data.getCollectionList());

			// change title
			TextView tvTitle = (TextView) findViewById(R.id.collectionlistitem_title);
			tvTitle.setText("Edit \"" + c.getName() + "\"");

			// change rename filed
			etName.setText(c.getName());

			// get data dynamically
			// List<Collection> collections = app.getData().getCollectionList();
			adapter = new CollectionListItemAdapter(this, c.getItems());

		} else if (action.equals("add")) {
			operation = Operation.ADD;
			// change title
			TextView tvTitle = (TextView) findViewById(R.id.collectionlistitem_title);
			tvTitle.setText("New collection");

			// change rename filed
			etName.setText("");

			// init new coll item in mem
			newColl = new Collection();

			// give empty data to adapter
			adapter = new CollectionListItemAdapter(this,
					new ArrayList<CollectionItem>());
		}

		// ʵ���б���ͼ
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
				CollectionListItemAdapter.isSelected.put(position,
						vHollder.cBox.isChecked());
			}
		});

		// init button "save" and "cancel"
		ImageButton btSave = (ImageButton) findViewById(R.id.collectionlistitem_btsave);
		btSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// save to db
				NewsInTimeApp app = (NewsInTimeApp) getApplication();
				CollectionDao dao = app.getDbmService().getCollectionDao();
				if (operation == Operation.ADD) {
					// save to db
					EditText etName = (EditText) findViewById(R.id.collectionlistitem_etname);
					String collName = etName.getText().toString();
					newColl.setName(collName);
					Integer collId = dao.addCollectionWithItems(newColl);

					// return to CL activity and refresh view
					Intent intent = getIntent();
					Bundle bundle = new Bundle();
					bundle.putString("name", "This is from ShowMsg!");
					bundle.putString("lastActivity",
							CollectionItemListActivity.class.getCanonicalName());
					bundle.putInt("newCollId", collId);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					CollectionItemListActivity.this.finish();
				}
			}
		});
		// ImageButton btCancel = (ImageButton)
		// findViewById(R.id.collectionlistitem_btcancel);

	}

	// public void onBackPressed() {
	// Log.e("CIL Activity", "onBackPressed Called");
	// Intent setIntent = new Intent(Intent.ACTION_MAIN);
	// setIntent.addCategory(Intent.CATEGORY_HOME);
	// setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// startActivity(setIntent);
	// }

	public void onCollectionItemSelectDialogReturn(CollectionItem newCi) {
		newColl.getItems().add(newCi);

		// refresh listview using cache
		ListView listView = (ListView) findViewById(R.id.collectionlistitem_lv);
		// Log.e("0830", "listview=" + listView);
		CollectionListItemAdapter adapter = (CollectionListItemAdapter) listView
				.getAdapter();
		// Log.e("0830", "adapter=" + adapter);
		// Log.e("0830", "newColl=" + newColl);
		adapter.setCollitems(newColl.getItems());
		adapter.notifyDataSetChanged();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("OOOOOOOO", "onActivityResult");
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case Constants.DIALOGID_CISELECT:
			dialog = new CollectionItemSelectDialog(this);
			break;
		default:
			dialog = null;
		}
		return dialog;
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
			// holder.btEdit = (Button) convertView
			// .findViewById(R.id.collectionlistitem_listview_btedit);
			// holder.btEdit.setTag(holder);
			// holder.btEdit.setOnClickListener(new OnClickListener() {
			//
			// public void onClick(View v) {
			//
			// Intent intent = new Intent(activity,
			// CollectionItemActivity.class);
			// // intent.putExtra("collid", h.coll.getId());
			//
			// // set op
			// intent.putExtra("action", "update");
			//
			// // send item to update as message(including id)
			// NewsInTimeApp app = (NewsInTimeApp) activity
			// .getApplication();
			// CollectionListItemViewHolder h = (CollectionListItemViewHolder) v
			// .getTag();
			// app.putMessage(AppMessage.MSG_CILACT_CIACT_ITEM, h.collItem);
			//
			// activity.startActivityForResult(intent, 200);
			//
			//
			//
			// }
			// });
			holder.collItem = ci;

			convertView.setTag(holder);
		} else {
			holder = (CollectionListItemViewHolder) convertView.getTag();
		}
		// holder.img.setBackgroundResource((Integer) colls.get(position).get(
		// "img"));

		holder.title.setText(ci.getName());

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
	// public Button btEdit;
	public CollectionItem collItem;
}
