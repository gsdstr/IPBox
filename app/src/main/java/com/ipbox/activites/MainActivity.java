package com.ipbox.activites;

import android.os.Bundle;
import android.view.MenuItem;
import com.ipbox.R;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_select:
			case android.R.id.home:
				showPlaylistDialog();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}  */

}
