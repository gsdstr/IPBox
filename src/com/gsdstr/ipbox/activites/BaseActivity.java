package com.gsdstr.ipbox.activites;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.gsdstr.ipbox.Const;
import com.gsdstr.ipbox.IpBoxApp;
import com.gsdstr.ipbox.R;
import com.gsdstr.ipbox.fragments.ChannelsFragment;
import com.gsdstr.ipbox.playlist.Channel;
import com.gsdstr.ipbox.playlist.Playlist;

/**
 * User: gsd
 * Date: 5/29/12
 * Time: 10:12 AM
 */
public class BaseActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener, SearchView.OnQueryTextListener {

	protected static final int ID_MENU_SEARCH = 1;
	protected static final int ID_MENU_PREFERENCES = 2;
	protected static final int ID_MENU_SHARE = 3;

	protected ChannelsFragment _channels;

	protected MenuItem _miPlay;
	protected MenuItem _miChannel;
	protected MenuItem _miShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		setPlaylist(preferences.getInt(Const.PREFERENCE_LAST_PLAYLIST, 0));
		/*String theme = preferences.getString(Const.PREFERENCE_PLAYER_THEME, "White");
		if (theme.equals("Black")) {
			setTheme(R.style.BoxTheme_Black);
		} else
			setTheme(R.style.BoxTheme_Light);*/

		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<Playlist> list = new ArrayAdapter<Playlist>(context,  R.layout.sherlock_spinner_item, IpBoxApp.getPlayListArray().getPlaylists());
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(list, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		//Create the search view
		SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
		searchView.setQueryHint("Search");
		searchView.setOnQueryTextListener(this);

		_miChannel = menu.add(Menu.NONE, ID_MENU_PREFERENCES, Menu.NONE, R.string.menu_play);
		_miChannel.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		_miChannel.setIcon(R.drawable.ic_menu_preferences);
		_miChannel.setVisible(false);

		_miPlay = menu.add(Menu.NONE, ID_MENU_PREFERENCES, Menu.NONE, R.string.menu_play);
		_miPlay.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		_miPlay.setIcon(R.drawable.ic_menu_preferences);
		_miPlay.setVisible(false);

		MenuItem miSearch = menu.add(Menu.NONE, ID_MENU_SEARCH, Menu.NONE, R.string.menu_search);
		miSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		miSearch.setActionView(searchView);
		miSearch.setIcon(R.drawable.ic_menu_search);

		_miShare = menu.add(Menu.NONE, ID_MENU_SHARE, Menu.NONE, R.string.menu_share);
		_miShare.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		_miShare.setIcon(R.drawable.ic_menu_edit);
		_miShare.setEnabled(false);

		MenuItem miPrefs = menu.add(Menu.NONE, ID_MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);
		miPrefs.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		miPrefs.setIcon(R.drawable.ic_menu_preferences);

		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		openPlaylist(itemPosition);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

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

	protected String getUrl(Channel channel){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String url = channel.getUrl();
		Boolean isProxy = preferences.getBoolean("udpEnable", false);
		if (!isProxy)
			return url;
		String proxy = preferences.getString("udpUrl", "http://192.168.1.1:8888/udp/");
		if (url.contains("udp://@"))
			return url.replace("udp://@", proxy);
		if (url.contains("udp://"))
			return url.replace("udp://", proxy);
		return url;
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
		viewMediaIntent.setDataAndType(Uri.parse(getUrl(channel)), "video/*");
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

	protected void setPlaylist(int index) {
		if (index < 0 || IpBoxApp.getPlayListArray().getPlaylists().size() < index)
			index = 0;

		// Check what fragment is shown, replace if needed.
		ChannelsFragment channels = (ChannelsFragment) getSupportFragmentManager().findFragmentById(R.id.titles);
		if (channels == null || channels.getShownIndex() != index) {
			// Make new fragment to show this selection.
			_channels = ChannelsFragment.newInstance(index);

			// Execute a transaction, replacing any existing
			// fragment with this one inside the frame.
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.titles, _channels);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		}
	}

	public void openPlaylist(int index) {
		try{
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(Const.PREFERENCE_LAST_PLAYLIST, index);
			editor.commit();
			setPlaylist(index);
		} catch (Exception ex){
			//TODO
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return onQueryTextChange(query);
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (_channels == null)
			return false;
		_channels.setQuery(newText);
		return true;
	}

	public void onSelect(Channel item) {
		if (item == null){
			_miChannel.setVisible(false);
			_miPlay.setVisible(false);
			_miShare.setEnabled(false);
			return;
		}
		_miChannel.setTitle(item.toString());
		_miChannel.setVisible(true);
		_miPlay.setVisible(true);
		_miShare.setEnabled(true);
	}
}
