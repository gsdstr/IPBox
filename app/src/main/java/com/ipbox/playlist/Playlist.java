package com.ipbox.playlist;

import com.ipbox.Const;
import com.ipbox.IpBoxApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
		try {
			int id = Integer.parseInt(_value);
			InputStream inputStream = IpBoxApp.getContext().getResources().openRawResource(id);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			load(reader);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	protected boolean loadUrl() {
		try {
			URLConnection cn = new URL(_value).openConnection();
			cn.setUseCaches(true);
			cn.setAllowUserInteraction(false);
			cn.connect();
			InputStream inputStream = cn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			load(reader);
		} catch (IOException e) {
			return false;
		} catch (Exception e) { //TODO
			return false;
		}
		return true;
	}

	protected boolean _ext;

	protected void load(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		if (line.contains("#EXTM3U")) {
			_ext = true;
			line = reader.readLine();
		}

		while (line != null) {
			parseLine(line);
			line = reader.readLine();
		}
	}

	protected boolean _nextUrl;
	protected String inf = null;

	protected void parseLine(String line) {
		if (line.length() < 1)
			return;
		if (line.startsWith("#EXTINF:")) {
			inf = line.substring(8, line.length());
		} else if (line.startsWith("#EXTVLCOPT:")) {
			inf = line.substring(11, line.length());
			_nextUrl = true;
		} else if (_nextUrl) {
			addSubList(line, inf);
		} else {
			Channel channel = new Channel();
			channel.setInfo(inf);
			channel.setUrl(line);
			_channels.add(channel);
		}
	}

	protected void addSubList(String line, String inf) {
		Playlist urlPlayList = new Playlist(inf, Const.TYPE_URL, line);
		if (urlPlayList.load()) {
			_channels.addAll(urlPlayList.getChannels());
		}
	}

}
