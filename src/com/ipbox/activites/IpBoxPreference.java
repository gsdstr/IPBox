package com.ipbox.activites;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.ipbox.IpBoxApp;
import com.ipbox.R;

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
		IpBoxApp.getPlayListsHolder().fillLists(getPreferenceManager(), this);
	}

	@Override
	public void onStop() {
		IpBoxApp.getPlayListsHolder().storePlayLists();
		super.onStop();
	}
}