package com.ipbox.activites;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ipbox.Const;
import com.ipbox.IpBoxApp;
import com.ipbox.R;
import com.ipbox.fragments.ChannelsFragment;
import com.ipbox.fragments.PlaylistDialog;
import com.ipbox.playlist.Channel;

/**
 * User: gsd
 * Date: 5/29/12
 * Time: 10:12 AM
 */
public class BaseActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener {

	protected static final int ID_MENU_SEARCH = 1;
	protected static final int ID_MENU_PREFERENCES = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		setPlaylist(preferences.getInt(Const.PREFERENCE_LAST_PLAYLIST, 0));
		String theme = preferences.getString(Const.PREFERENCE_PLAYER_THEME, "White");
		if (theme.equals("Black")) {
			setTheme(R.style.BoxTheme_Black);
		} else
			setTheme(R.style.BoxTheme_Light);

		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.locations, R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(list, this);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuItem miSearch = menu.add(Menu.NONE, ID_MENU_SEARCH, Menu.NONE, R.string.menu_search);
		miSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		miSearch.setActionView(R.layout.collapsible_edittext);
		miSearch.setIcon(R.drawable.ic_menu_search);

		MenuItem miPrefs = menu.add(Menu.NONE, ID_MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);
		miPrefs.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		miPrefs.setIcon(R.drawable.ic_menu_preferences);

		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		//mSelected.setText("Selected: " + mLocations[itemPosition]);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case ID_MENU_SEARCH:
				Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
				break;

			case ID_MENU_PREFERENCES:
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
					startActivity(new Intent(this, IpBoxPreference.class));
				} else {
					startActivity(new Intent(this, IpBoxPreferenceHc.class));
				}
				break;

		}
		return super.onOptionsItemSelected(item);
	}

	public void loadChannel(Channel channel) {
		Toast.makeText(this, R.string.alert_loading, Toast.LENGTH_SHORT).show();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		try {
			loadChannel(channel, preferences.getString("playerType", "system"));
		} catch (Exception ex) {
			Toast.makeText(this, R.string.fail_loading, Toast.LENGTH_SHORT).show();
		}
	}

	protected void showPlaylistDialog() {
		//mStackLevel++;

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		ChannelsFragment details = (ChannelsFragment) getSupportFragmentManager().findFragmentById(R.id.titles);
		// Create and show the dialog.
		PlaylistDialog newFragment = PlaylistDialog.newInstance(this);
		newFragment.show(ft, "dialog");
	}

	protected void loadChannel(Channel channel, String playerType) {
		if (playerType.equals("MX Player") || playerType.equals("MX Player Pro")) {
			loadMx(channel, playerType.contains("Pro"));
			return;
		}
		if (playerType.equals("Daroon")) {
			loadDaroon(channel);
			return;
		}
		loadSystem(channel);

	}

	protected void loadDaroon(Channel channel) {
		Intent viewMediaIntent = new Intent();
		viewMediaIntent.setAction(android.content.Intent.ACTION_VIEW);
		viewMediaIntent.setDataAndType(Uri.parse(channel.getUrl()), "video/*");
		viewMediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		viewMediaIntent.setPackage("com.daroonplayer.dsplayer");
		startActivityForResult(viewMediaIntent, 0);
	}

	protected void loadSystem(Channel channel) {
		Intent viewMediaIntent = new Intent();
		viewMediaIntent.setAction(android.content.Intent.ACTION_VIEW);
		viewMediaIntent.setDataAndType(Uri.parse(channel.getUrl()), "video/*");
		viewMediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(viewMediaIntent, 0);
	}


	public static final String MXVP = "com.mxtech.videoplayer.ad";
	public static final String MXVP_PRO = "com.mxtech.videoplayer.pro";

	public static final String RESULT_VIEW = "com.mxtech.intent.result.VIEW";
	public static final String EXTRA_DECODE_MODE = "decode_mode";        // (byte)
	public static final String EXTRA_FAST_MODE = "fast_mode";                // (boolean)
	public static final String EXTRA_VIDEO_LIST = "video_list";
	public static final String EXTRA_SUBTITLES = "subs";
	public static final String EXTRA_SUBTITLES_ENABLE = "subs.enable";
	public static final String EXTRA_TITLE = "title";
	public static final String EXTRA_POSITION = "position";
	public static final String EXTRA_RETURN_RESULT = "return_result";

	protected void loadMx(Channel channel, boolean isPro) {
		Uri videoUri = Uri.parse(channel.getUrl());
		Intent i = new Intent(Intent.ACTION_VIEW);

		i.setDataAndType(videoUri, "video/*");
		// play only given videos.
		i.putExtra(EXTRA_VIDEO_LIST, new Parcelable[]{videoUri});
		// provides title text.
		i.putExtra(EXTRA_TITLE, channel.toString());
		// request result
		i.putExtra(EXTRA_RETURN_RESULT, true);

		i.putExtra(EXTRA_POSITION, 0);

		try {
			if (isPro)
				i.setPackage(MXVP_PRO);
			else
				i.setPackage(MXVP);
			startActivityForResult(i, 0);
		} catch (ActivityNotFoundException e) {
			//TODO
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	public void setPlaylist(int index) {
		if (index < 0 || IpBoxApp.getPlayListsHolder().getPlaylists().size() < index)
			index = 0;

		// Check what fragment is shown, replace if needed.
		ChannelsFragment channels = (ChannelsFragment) getSupportFragmentManager().findFragmentById(R.id.titles);
		if (channels == null || channels.getShownIndex() != index) {
			// Make new fragment to show this selection.
			channels = ChannelsFragment.newInstance(index);

			// Execute a transaction, replacing any existing
			// fragment with this one inside the frame.
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.titles, channels);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		}
	}

	public void openPlaylist(int index) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(Const.PREFERENCE_LAST_PLAYLIST, index);
		editor.commit();
		setPlaylist(index);
	}
}
