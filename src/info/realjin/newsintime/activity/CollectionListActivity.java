package info.realjin.newsintime.activity;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.R;
import info.realjin.newsintime.dao.CollectionDao;
import info.realjin.newsintime.domain.Collection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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

	private Set<Integer> added;
	private Set<Integer> deleted;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectionlist);

		String lastActivity = getIntent().getExtras().getString("lastActivity");
		// check if jump from CollectionListActivity
		if (lastActivity.equals(MainActivity.class.getCanonicalName())) {
			Log.e("CL Activity", "last = MainActivity");
			// init added and deleted
			if (added == null || deleted == null) {
				added = new HashSet<Integer>();
				deleted = new HashSet<Integer>();
			} else {
				added.clear();
				deleted.clear();
			}
		}

		NewsInTimeApp app = (NewsInTimeApp) getApplication();

		// set button listener
		/**
		 * when add pressed, any added is persisted and id of it is cached for
		 * rollback use
		 */
		ImageButton btAdd = (ImageButton) findViewById(R.id.collectionlist_btadd);
		btAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ImageButton bt = (ImageButton) v;
				Intent intent = new Intent(CollectionListActivity.this,
						CollectionItemListActivity.class);
				intent.putExtra("action", "add");
				intent.putExtra("lastActivity",
						CollectionListActivity.class.getCanonicalName());
				CollectionListActivity.this.startActivityForResult(intent, 200);
			}
		});

		/**
		 * when delete pressed, the flag column of any deleted is set in
		 * database, and the id is cached for rollback use
		 */
		ImageButton btDelete = (ImageButton) findViewById(R.id.collectionlist_btdelete);
		btDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.e("CI", "ondelete");
				NewsInTimeApp app = (NewsInTimeApp) getApplication();
				CollectionDao dao = app.getDbmService().getCollectionDao();

				ListView lv = (ListView) findViewById(R.id.collectionlist_lv);
				CollectionListAdapter a = (CollectionListAdapter) lv
						.getAdapter();

				for (Integer i : a.getSelected().keySet()) {
					if (a.getSelected().get(i) == true) {
						Log.e("CI", "pos i=" + i);
						Collection c = a.getColls().get(i);
						Log.e("CI", "collid=" + c.getId());
						// 1. cache all ids for rollback use
						deleted.add(c.getId());
						// 2. hide all
						dao.hideCollection(c.getId());
					}
				}
				// refresh view from db
				refreshListView();
			}
		});

		// get data dynamically
		CollectionDao dao = app.getDbmService().getCollectionDao();
		List<Collection> collections = dao.getAllCollectionsWithItems();
		CollectionListAdapter adapter = new CollectionListAdapter(this,
				collections);
		adapter.setColls(collections);
		// listView = new ListView(this);// ʵ���б���ͼ
		ListView listView = (ListView) findViewById(R.id.collectionlist_lv);
		listView.setAdapter(adapter);
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// TODO: useless!
		// listView.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Log.e("CL Activity", "lv item selected");
		// CollectionListViewHolder vHollder = (CollectionListViewHolder) view
		// .getTag();
		// // ��ÿ�λ�ȡ�����itemʱ�����ڵ�checkbox״̬�ı䣬ͬʱ�޸�map��ֵ��
		// vHollder.cBox.toggle();
		// CollectionListAdapter.isSelected.put(position,
		// vHollder.cBox.isChecked());
		// }
		// });

		// //��ʾ�б���ͼ
		// this.setContentView(listView);

		// set button listener
		// Button btCancel = (Button)
		// findViewById(R.id.collectionlist_btcancel);
		// btCancel.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// abandonModification();
		// setResult(RESULT_CANCELED, null);
		// CollectionListActivity.this.finish();
		// }
		// });

		ImageButton btSave = (ImageButton) findViewById(R.id.collectionlist_btsave);
		btSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				saveModification();
				setResult(RESULT_CANCELED, null);
				CollectionListActivity.this.finish();
			}
		});

	}

	public void onBackPressed() {
		if ((added == null || added.size() == 0)
				&& (deleted == null || deleted.size() == 0)) {
			setResult(RESULT_CANCELED, null);
			CollectionListActivity.this.finish();
		} else {
			AlertDialog.Builder adbd = new AlertDialog.Builder(this)
					.setTitle("Warning")
					.setMessage("Save modification?")
					.setNegativeButton("CANCEL",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									abandonModification();
									setResult(RESULT_CANCELED, null);
									CollectionListActivity.this.finish();
								}
							})
					.setPositiveButton("SAVE",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									saveModification();
									setResult(RESULT_OK, null);
									CollectionListActivity.this.finish();
								}
							});

			adbd.show();
		}

		// super.onBackPressed();
	}

	private void abandonModification() {

		NewsInTimeApp app = (NewsInTimeApp) getApplication();
		CollectionDao dao = app.getDbmService().getCollectionDao();
		// 1. delete the added
		for (Integer collId : added) {
			dao.deleteCollection(collId);
		}
		// 2. restore the flag bit of deleted
		for (Integer collId : deleted) {
			dao.unhideCollection(collId);
		}
		// 3. clear cache
		added.clear();
		deleted.clear();

	}

	private void saveModification() {
		Log.e("DELLLL", "saveModification");

		NewsInTimeApp app = (NewsInTimeApp) getApplication();
		CollectionDao dao = app.getDbmService().getCollectionDao();

		// 1. delete the hided permanently
		for (Integer collId : deleted) {
			Log.e("DELLLL", "collid=" + collId);
			dao.deleteCollection(collId);
		}

		// 2. clear cache
		added.clear();
		deleted.clear();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_CANCELED) {
			// do nothing
		} else if (resultCode == RESULT_OK) {
			// check if from CIL activity
			Bundle bundle = data.getExtras();
			String lastActivityName = bundle.getString("lastActivity");
			// check if from coll add or edit
			if (lastActivityName.equals(CollectionItemListActivity.class
					.getCanonicalName())) {
				// Log.e("CIL", "lastAct = CollectionItemListActivity");
				Integer collId = bundle.getInt("newCollId");
				added.add(collId);
			}
			// refresh view from db
			refreshListView();
		}

	}

	private void refreshListView() {
		// refresh view from db
		NewsInTimeApp app = (NewsInTimeApp) getApplication();
		CollectionDao dao = app.getDbmService().getCollectionDao();
		List<Collection> collList = dao.getAllCollectionsWithItems();
		ListView lv = (ListView) findViewById(R.id.collectionlist_lv);
		CollectionListAdapter a = (CollectionListAdapter) lv.getAdapter();
		a.setColls(collList);
		a.notifyDataSetChanged();

		// clear checkbox
		Log.e("", "a count = " + a.getCount());
		Log.e("", "lv = " + lv);
		for (int i = 0; i < lv.getChildCount(); i++) {
			View v = lv.getChildAt(i);
			Log.e("", "v = " + v);
			CheckBox cb = (CheckBox) v
					.findViewById(R.id.collectionlist_listview_checkbox);
			cb.setChecked(false);
		}
		a.getSelected().clear();
	}
}

class CollectionListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Collection> colls;
	// public static Map<Integer, Boolean> isSelected;
	private Map<Integer, Boolean> selected;
	private Activity activity;

	public CollectionListAdapter(Activity a, List<Collection> c) {
		activity = a;
		colls = c;
		mInflater = LayoutInflater.from(a);
		selected = new HashMap<Integer, Boolean>();
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
		// �����isSelected���map�Ǽ�¼ÿ��listitem��״̬����ʼ״̬ȫ��Ϊfalse��
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
		final int pos = position;
		// convertViewΪnull��ʱ���ʼ��convertView��
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

						public void onCheckedChanged(CompoundButton cb,
								boolean flag) {
							CollectionListViewHolder h = (CollectionListViewHolder) cb
									.getTag();
							// Log.e("OCC", "collname=" + h.coll.getName());
							// Log.e("OCC", "selected=" + flag);
							selected.put(pos, flag);
							// TODO: bug fixing code here
						}

					});
			holder.cBox.setTag(holder);
			holder.btEdit = (ImageButton) convertView
					.findViewById(R.id.collectionlist_listview_btedit);
			holder.btEdit.setTag(holder);
			holder.btEdit.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					ImageButton bt = (ImageButton) v;
					CollectionListViewHolder h = (CollectionListViewHolder) bt
							.getTag();
					Intent intent = new Intent(activity,
							CollectionItemListActivity.class);
					intent.putExtra("lastActivity",
							CollectionListActivity.class.getCanonicalName());
					intent.putExtra("action", "update");
					intent.putExtra("collId", h.coll.getId());
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

	public Map<Integer, Boolean> getSelected() {
		return selected;
	}

	public void setSelected(Map<Integer, Boolean> selected) {
		this.selected = selected;
	}

}

final class CollectionListViewHolder {
	// public ImageView img;
	public TextView title;
	public CheckBox cBox;
	public ImageButton btEdit;
	public Collection coll;
}
