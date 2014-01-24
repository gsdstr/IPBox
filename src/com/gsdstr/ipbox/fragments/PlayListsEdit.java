package com.gsdstr.ipbox.fragments;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import com.gsdstr.ipbox.IpBoxApp;
import com.gsdstr.ipbox.playlist.Playlist;

/**
 * User: gsd
 * Date: 6/11/12
 * Time: 4:11 PM
 */
public class PlayListsEdit {

	public PreferenceCategory getCategory(PreferenceManager preferenceManager, Context context) {
		PreferenceCategory category = new PreferenceCategory(context);
		category.setKey("categ1");
		category.setTitle("Category 1");
		category.setSummary("Description of category 1");

		for (Playlist playlist : IpBoxApp.getPlayListArray().getPlaylists()) {
			CheckBoxPreference playListCategory = new CheckBoxPreference(context);
			category.setKey(playlist.getTitle());
			category.setTitle(playlist.getTitle());
			category.setSummary("Description of category 1");
			category.addPreference(playListCategory);
		}

		return category;
	}


}
