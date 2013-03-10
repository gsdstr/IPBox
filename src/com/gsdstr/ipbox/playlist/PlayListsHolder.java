package com.gsdstr.ipbox.playlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.*;
import com.gsdstr.ipbox.Const;
import com.gsdstr.ipbox.IpBoxApp;
import com.gsdstr.ipbox.R;
import com.gsdstr.ipbox.activites.OpenFileDialog;

import java.io.File;
import java.util.ArrayList;

/**
 * User: gsd
 * Date: 6/9/12
 * Time: 12:42 PM
 */
public class PlayListsHolder {

	protected ArrayList<Playlist> _playlists;

	public PlayListsHolder() {
		_playlists = new ArrayList<Playlist>();
	}

	public Playlist getPlayList(int i) {
		if (i < _playlists.size())
			return _playlists.get(i);
		return null;
	}

	public ArrayList<Playlist> getPlaylists() {
		return _playlists;
	}

	protected PreferenceManager _preferenceManager;
	protected Context _context;
	protected PreferenceCategory _allPlaylists;

	public void fillLists(final PreferenceManager preferenceManager, final Context context) {

		_preferenceManager = preferenceManager;
		_context = context;
		_allPlaylists = (PreferenceCategory) preferenceManager.findPreference("playlists_category");

		PreferenceScreen addNewPlaylist = (PreferenceScreen) preferenceManager.findPreference("playlists_new");
		addNewPlaylist.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				addNewPlaylist();
				return false;
			}
		});

		int i = 0;
		for (final Playlist playlist : _playlists) {
			addPlaylistScreen(i, playlist);
			i++;
		}

	}

	protected void addNewPlaylist() {

		int pos = _allPlaylists.getPreferenceCount();
		Playlist playlist = new Playlist("New", Const.TYPE_URL, "");
		_playlists.add(playlist);
		addPlaylistScreen(pos - 1, playlist);

		//PreferenceScreen mainScreen = (PreferenceScreen)preferenceManager.findPreference("mainScreen");
		//mainScreen.onItemClick( null, null, 5 + pos, 0 );//TODO ??

	}

	protected void addPlaylistScreen(int i, final Playlist playlist) {

		final PreferenceScreen playListCategory = _preferenceManager.createPreferenceScreen(_context);
		playListCategory.setTitle(playlist.getTitle());

		final EditTextPreference titlePreference = new EditTextPreference(_context);
		titlePreference.setKey(Const.KEY_TITLE + i);
		titlePreference.setTitle(R.string.preferences_playlist_title);
		titlePreference.setSummary(R.string.preferences_playlist_title_sum);
		titlePreference.setText(playlist.getTitle());
		titlePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				playListCategory.setTitle(newValue.toString());
				playlist.setTitle(newValue.toString());
				return true;
			}
		});
		playListCategory.addPreference(titlePreference);

		final EditTextPreference valuePreference = new EditTextPreference(_context);
		valuePreference.setTitle("Value");
		valuePreference.setSummary("Value for list");
		valuePreference.setText(playlist.getValue());
		updateType(playlist, valuePreference);

		ListPreference typePreference = new ListPreference(_context);
		typePreference.setTitle("Type");
		typePreference.setSummary("Type for list");
		typePreference.setEntries(R.array.playlist_type_title);
		typePreference.setEntryValues(R.array.playlist_type_values);
		for (CharSequence sequence : typePreference.getEntryValues()) {
			String stSequence = (String) sequence;
			if (stSequence.equals(playlist.getType())) {
				typePreference.setValue(stSequence);
				break;
			}
		}
		typePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				playlist.setType(newValue.toString());
				updateType(playlist, valuePreference);
				return true;
			}
		});
		playListCategory.addPreference(typePreference);
		playListCategory.addPreference(valuePreference);

		final PreferenceScreen deletePlayList = _preferenceManager.createPreferenceScreen(_context);
		deletePlayList.setTitle(R.string.preferences_playlist_delete);
		deletePlayList.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				titlePreference.setText(null);
				playListCategory.getDialog().dismiss();
				_allPlaylists.removePreference(playListCategory);
				playlist.setTitle(null);
				//_playlists.remove(playlist);
				//updateKey();
				return false;  //To change body of implemented methods use File | Settings | File Templates.
			}
		});
		playListCategory.addPreference(deletePlayList);

		_allPlaylists.addPreference(playListCategory);
	}

	private void updateType(final Playlist playlist, final EditTextPreference valuePreference) {
		if (playlist.getType().equals(Const.TYPE_ASSETS)) {
			valuePreference.setEnabled(false);
		} else if (playlist.getType().equals(Const.TYPE_FILE)) {
			valuePreference.setEnabled(true);
			valuePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					OpenFileDialog ofd = new OpenFileDialog(_context, null, new String[] {".m3u"},
						new OpenFileDialog.OnFileSelectedListener() {
							public void onFileSelected(File f) {
								valuePreference.setText(f.getPath());
							}
						});
					ofd.show();
					return true;
				}
			});
			valuePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					playlist.setValue(newValue.toString());
					return true;
				}
			});
		} else {
			valuePreference.setEnabled(true);
			valuePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					playlist.setValue(newValue.toString());
					return true;
				}
			});
		}
	}

	public void storePlayLists() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(IpBoxApp.getContext());
		SharedPreferences.Editor editor = preferences.edit();
		int i = 0;
		for (final Playlist playlist : _playlists) {
			if (playlist.getTitle() == null)
				continue;
			editor.putString(Const.KEY_TITLE + i, playlist.getTitle());
			editor.putString(Const.KEY_TYPE + i, playlist.getType());
			editor.putString(Const.KEY_VALUE + i, playlist.getValue());
			i++;
		}
		editor.putString(Const.KEY_TITLE + i, null);
		editor.commit();
	}

	public void loadPlayLists() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(IpBoxApp.getContext());
		int i = 0;
		while (true) {
			String title = preferences.getString(Const.KEY_TITLE + i, null);
			String type = preferences.getString(Const.KEY_TYPE + i, null);
			String value = preferences.getString(Const.KEY_VALUE + i, null);
			if (title == null)
				return;
			_playlists.add(new Playlist(title, type, value));
			i++;
		}
	}

	public void clear() {
		_playlists.clear();
	}

	public void addNewUrl(String url) {
		String[] parts = url.split("/");
		String title = "New";
		if (parts.length > 1)
			title = parts[parts.length - 1];
		Playlist list = new Playlist(title, Const.TYPE_URL, url);
		_playlists.add(list);
		//TODO
		storePlayLists();
	}

	public void addNewProvider(Object s) {
		if("(Russia)4pda.ru".equals(s)){
			Playlist list = new Playlist("4pda", R.raw.pda);
			_playlists.add(list);
			storePlayLists();
		} else if("(Russia)Convex".equals(s)){
			Playlist list = new Playlist("Convex", Const.TYPE_URL, "http://79.172.33.85/tv_All.m3u");
			_playlists.add(list);
			storePlayLists();
		} else if("(Russia)Corbina-Beeline".equals(s)){
			Playlist list = new Playlist("Corbina", R.raw.beeline);
			_playlists.add(list);
			storePlayLists();
		} else if("(Russia)kynashak_narod".equals(s)){
			Playlist list = new Playlist("Kynashak", R.raw.kynashak_narod);
			_playlists.add(list);
			storePlayLists();
		}
	}
}
