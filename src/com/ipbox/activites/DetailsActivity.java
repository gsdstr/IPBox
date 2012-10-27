package com.ipbox.activites;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import com.ipbox.R;
import com.ipbox.fragments.DetailsFragment;

/**
 * User: gsd
 * Date: 5/23/12
 * Time: 9:00 PM
 */
public class DetailsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			DetailsFragment details = new DetailsFragment();
			details.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
		}
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_select:
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}*/

}