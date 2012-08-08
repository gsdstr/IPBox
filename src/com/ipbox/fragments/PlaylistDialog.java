package com.ipbox.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import com.ipbox.IpBoxApp;
import com.ipbox.R;
import com.ipbox.activites.BaseActivity;
import com.ipbox.playlist.Playlist;

/**
 * User: gsd
 * Date: 6/7/12
 * Time: 3:00 PM
 */
public class PlaylistDialog extends DialogFragment {

	protected BaseActivity _activity;

	public PlaylistDialog(BaseActivity activity) {
		_activity = activity;
	}

	public static PlaylistDialog newInstance(BaseActivity activity) {
		return new PlaylistDialog(activity);
	}

	@Override
	public Dialog onCreateDialog(Bundle args) {
		ListAdapter adapter = new ArrayAdapter<Playlist>(getActivity().getApplicationContext(), R.layout.simple_list_item_checkable, IpBoxApp.getPlayListsHolder().getPlaylists());

		return new AlertDialog.Builder(getActivity())
			.setIcon(R.drawable.ic_dialog_info)
			.setTitle(R.string.playlist_dialog_title)
			.setSingleChoiceItems(adapter, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int index) {
						_activity.openPlaylist(index);
						dialog.dismiss();
					}
				})
			.setNegativeButton(R.string.alert_dialog_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						onCancel(dialog);
					}
				}
			)
			.setCancelable(true)
			.create();
	}
}