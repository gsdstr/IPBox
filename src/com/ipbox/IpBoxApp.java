package com.ipbox;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.ipbox.activites.NewActivity;
import com.ipbox.playlist.PlayListsHolder;

/**
 * User: gsd
 * Date: 6/9/12
 * Time: 1:14 PM
 */
public class IpBoxApp extends Application {

	private static Context _context;
	protected static PlayListsHolder _playListsHolder;

	public static PlayListsHolder getPlayListsHolder() {
		return _playListsHolder;
	}

	public static Context getContext() {
		return _context;
	}

	@Override
	public void onCreate() {
		_context = this;
		_playListsHolder = new PlayListsHolder();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (preferences.getBoolean(Const.PREFERENCE_FIRST_TIME, true)) {

			Intent intent = new Intent(getBaseContext(), NewActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean(Const.PREFERENCE_FIRST_TIME, false);
			editor.commit();
		}

		_playListsHolder.loadPlayLists();
		super.onCreate();
	}


}
