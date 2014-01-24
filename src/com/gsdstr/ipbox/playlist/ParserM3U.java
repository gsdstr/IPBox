package com.gsdstr.ipbox.playlist;

import com.gsdstr.ipbox.Const;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: gsd
 * Date: 16.03.13
 * Time: 10:32
 */
public class ParserM3U implements Parser {

	protected Playlist _playlist;
	protected ArrayList<Channel> _channels;

	@Override
	public boolean parse(Playlist playlist) {
		_playlist = playlist;
		_channels = playlist.getChannels();
		return loadM3UUrl();
	}


	protected boolean loadM3UUrl() {
		try {
			URLConnection cn = new URL(_playlist.getValue()).openConnection();
			cn.setUseCaches(true);
			cn.setAllowUserInteraction(false);
			cn.connect();
			InputStream inputStream = cn.getInputStream();
			parseM3U(inputStream);
		} catch (IOException e) {
			return false;
		} catch (Exception e) { //TODO
			return false;
		}
		return true;
	}

	protected boolean _ext;

	protected void parseM3U(InputStream inputStream) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line = reader.readLine();
			if (line.contains("#EXTM3U")) {
				_ext = true;
				line = reader.readLine();
			}

			while (line != null) {
				parseLine(line);
				line = reader.readLine();
			}
		} finally {
			inputStream.close();
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
