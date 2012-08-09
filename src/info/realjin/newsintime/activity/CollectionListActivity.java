package info.realjin.newsintime.activity;

import info.realjin.newsintime.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CollectionListActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collections);

		LinearLayout llCollections = (LinearLayout) findViewById(R.id.llcollections);

		ListView lvCollections = new ListView(this);
		llCollections.addView(lvCollections);

	}
}
