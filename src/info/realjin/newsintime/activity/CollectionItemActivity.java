package info.realjin.newsintime.activity;

import info.realjin.newsintime.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CollectionItemActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collectionlistitemitem);

		final RadioGroup rgSelect = (RadioGroup) this
				.findViewById(R.id.collectionlistitemitem_rgSelect);
		final RadioButton rbSelect = (RadioButton) this
				.findViewById(R.id.collectionlistitemitem_rbSelect);

		final RadioGroup rgManual = (RadioGroup) this
				.findViewById(R.id.collectionlistitemitem_rgManual);
		final RadioButton rbManual = (RadioButton) this
				.findViewById(R.id.collectionlistitemitem_rbManual);

		
		final Button btSelect = (Button) this
				.findViewById(R.id.collectionlistitemitem_btSelect);
		final EditText etManual = (EditText) this
				.findViewById(R.id.collectionlistitemitem_etManual);
		
		rgSelect.check(R.id.collectionlistitemitem_rbSelect);
		rgManual.clearCheck();

		rbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					rgManual.clearCheck();
					btSelect.setEnabled(true);
					etManual.setEnabled(false);
				}
			}
		});

		rbManual.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					rgSelect.clearCheck();
					etManual.setEnabled(true);
					btSelect.setEnabled(false);
				}
			}
		});

		// final Button btSelect = (Button) this
		// .findViewById(R.id.collectionlistitemitem_btSelect);
		// final EditText etManual = (EditText) this
		// .findViewById(R.id.collectionlistitemitem_etManual);
		// rgp.setOnCheckedChangeListener(new
		// RadioGroup.OnCheckedChangeListener() {
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// if (checkedId == R.id.collectionlistitemitem_rbManual) {
		// etManual.setEnabled(true);
		// btSelect.setEnabled(false);
		// } else {
		// etManual.setEnabled(false);
		// btSelect.setEnabled(true);
		// }
		// }
		// });

	}
}