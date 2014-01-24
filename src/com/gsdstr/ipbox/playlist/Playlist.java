package com.gsdstr.ipbox.playlist;

import android.webkit.MimeTypeMap;
import com.gsdstr.ipbox.Const;

import java.util.ArrayList;

/**
 * User: gsd
 * Date: 28.06.12
 * Time: 10:30
 */
public class Playlist {

	protected String _title;
	protected String _type;
	protected String _value;

	protected ArrayList<Channel> _channels;

	protected int _lastSelect = -1;

	public Playlist(String title, String type, String value) {
		this();
		_title = title;
		_value = value;
		_type = type;
	}

	public Playlist() {
		_channels = new ArrayList<Channel>();
	}

	public Playlist(String title, int value) {
		this(title, Const.TYPE_ASSETS, String.valueOf(value));
	}

	public ArrayList<Channel> getChannels() {
		return _channels;
	}

	public Channel getChannel(int index) {
		return _channels.get(index);
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setType(String type) {
		_type = type;
	}

	public String getType() {
		return _type;
	}

	public String getValue() {
		return _value;
	}

	public void setValue(String value) {
		_value = value;
	}

	public void select(int position){
		_lastSelect = position;
	}

	public boolean isSelect(int position){
		return _lastSelect == position;
	}

	@Override
	public String toString() {
		return _title;
	}

	public boolean load() {
		_channels.clear();
		if (_type.equals(Const.TYPE_ASSETS))
			return loadAssets();
		else if (_type.equals(Const.TYPE_URL))
			return loadUrl();
		return true;
	}

	protected boolean loadAssets() {
		/*try {
			int id = Integer.parseInt(_value);
			InputStream inputStream = IpBoxApp.getContext().getResources().openRawResource(id);
			parseM3U(inputStream);
		} catch (Exception e) {
			return false;
		}*/
		return true;
	}

	protected boolean loadUrl() {
		try {
			String ext = MimeTypeMap.getFileExtensionFromUrl(_value);
			if (ext.equals("vlc"))
				return new ParserVLC().parse(this);
		} catch (Exception ex){
			/**/
		}
		return new ParserM3U().parse(this);
	}



}
