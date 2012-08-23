package info.realjin.newsintime.activity;

import info.realjin.newsintime.R;

import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/*
 * http://www.cnblogs.com/allin/archive/2010/05/11/1732200.html
 */

public class CollectionListActivity extends ListActivity {
	private List<Map<String, Object>> mData;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		setContentView(R.layout.collections);
		//
		// LinearLayout llCollections = (LinearLayout)
		// findViewById(R.id.llcollections);
		//
		// ListView lvCollections = new ListView(this);
		// llCollections.addView(lvCollections);

	}

	final class ViewHolder {
		// public ImageView img;
		public TextView text;
		public Button btEdit;
	}

	class CollectionListAdapter extends BaseAdapter {

		private LayoutInflater li;

		public CollectionListAdapter(Context context) {
			this.li = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				// convertView = li.inflate(R.layout.vlist2, null);
				// holder.img = (ImageView) convertView.findViewById(R.id.img);
				// holder.title = (TextView)
				// convertView.findViewById(R.id.title);
				// holder.btEdit = (Button)
				// convertView.findViewById(R.id.view_btn);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			holder.text.setText((String) mData.get(position).get("text"));

			holder.btEdit.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// showInfo();
				}
			});

			return convertView;
		}

	}
}
