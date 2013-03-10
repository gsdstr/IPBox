package com.gsdstr.ipbox.activites;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.FragmentActivity;
import com.gsdstr.ipbox.IpBoxApp;
import com.gsdstr.ipbox.R;

/**
 * User: gsd
 * Date: 6/9/12
 * Time: 2:56 PM
 */
public class IpBoxPreferenceHc extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
	}


	public static class PrefsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
			IpBoxApp.getPlayListsHolder().fillLists(getPreferenceManager(), this.getActivity());


		}

		@Override
		public void onStop() {
			IpBoxApp.getPlayListsHolder().storePlayLists();
			super.onStop();
		}
	}
}