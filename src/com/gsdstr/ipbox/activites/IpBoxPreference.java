package com.gsdstr.ipbox.activites;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.gsdstr.ipbox.IpBoxApp;
import com.gsdstr.ipbox.R;

/**
 * User: gsd
 * Date: 6/9/12
 * Time: 3:46 PM
 */
public class IpBoxPreference extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		IpBoxApp.getPlayListArray().fillLists(getPreferenceManager(), this);
	}

	@Override
	public void onStop() {
		IpBoxApp.getPlayListArray().storePlayLists();
		super.onStop();
	}
}