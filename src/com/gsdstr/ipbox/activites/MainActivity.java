package com.gsdstr.ipbox.activites;

import android.os.Bundle;
import com.gsdstr.ipbox.R;

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
