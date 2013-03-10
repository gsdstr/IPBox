package com.gsdstr.ipbox.fragments;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gsdstr.ipbox.R;
import com.gsdstr.ipbox.activites.BaseActivity;
import com.gsdstr.ipbox.playlist.Channel;
import com.gsdstr.ipbox.playlist.Playlist;

/**
 * User: gsd
 * Date: 5/29/12
 * Time: 12:51 PM
 */
public class ChannelsAdapter extends ArrayAdapter<Channel> {
	protected final Playlist _list;
	protected final BaseActivity _activity;

	public ChannelsAdapter(FragmentActivity activity, Playlist list) {
		super(activity, R.layout.channel_list_item, R.id.channel_title, list.getChannels());
		_list = list;
		_activity = (BaseActivity) activity;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);

		final Channel channel = getItem(position);

		ImageView channelIcon = (ImageView) view.findViewById(R.id.channel_icon);
		channelIcon.setImageDrawable(_activity.getResources().getDrawable(channel.getIconId()));
		TextView subText = (TextView) view.findViewById(R.id.channel_sub_text);
		subText.setText(channel.getUrl());

		RelativeLayout channelLayout = (RelativeLayout) view.findViewById(R.id.channel_layout);
		if (_list.isSelect(position))
			channelLayout.setBackgroundResource(R.drawable.border);
		else
			channelLayout.setBackground(null);

		return view;
	}

	public void select(int position){
		_list.select(position);
	}

	public boolean isSelect(int position){
		return _list.isSelect(position);
	}

}
