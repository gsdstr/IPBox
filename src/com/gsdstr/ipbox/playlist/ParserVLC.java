package com.gsdstr.ipbox.playlist;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: gsd
 * Date: 16.03.13
 * Time: 10:32
 */
public class ParserVLC  implements Parser {

	protected Playlist _playlist;
	protected ArrayList<Channel> _channels;

	@Override
	public boolean parse(Playlist playlist) {
		_playlist = playlist;
		_channels = playlist.getChannels();
		return loadVLCUrl();
	}


	protected boolean loadVLCUrl() {
		try {
			URLConnection cn = new URL(_playlist.getValue()).openConnection();
			cn.setUseCaches(true);
			cn.setAllowUserInteraction(false);
			cn.connect();
			InputStream inputStream = cn.getInputStream();
			return parseVLC(inputStream);
		} catch (IOException e) {
			return false;
		} catch (Exception e) { //TODO
			return false;
		}
	}

	protected boolean parseVLC(InputStream inputStream) throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(inputStream, null);
		parser.nextTag();
		return readVlc(parser);
	}

	/*<?xml version="1.0" encoding="UTF-8"?>
	<playlist xmlns="http://xspf.org/ns/0/" xmlns:vlc="http://www.videolan.org/vlc/playlist/ns/0/" version="1">
	<title>Weburg.TV</title>
	<creator>Weburg Ltd.</creator>
	<date>2012-12-29T23:39:08+06:00</date>
	<trackList>
		<track>
			<location>udp://@239.255.2.122:1234</location>
			<title>Первый канал</title>
			<image>http://gl00.weburg.net/00/tv/channels/1/14/amino/380642.jpg</image>
			<creator>Weburg.TV</creator>
			<extension application="http://www.videolan.org/vlc/playlist/0">
				<vlc:id xmlns:vlc="vlc">1</vlc:id>
			</extension>
		</track>
		...
	</trackList>
	...
	</playlist>*/

	private static final String ns = null;

	protected boolean readVlc(XmlPullParser parser) throws XmlPullParserException, IOException {

		parser.require(XmlPullParser.START_TAG, ns, "playlist");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("title")) {
				_playlist.setTitle(readText(parser));
			} else if (name.equals("trackList")) {
				readChanels(parser);
			} else {
				skip(parser);
			}
		}
		return true;
	}

	private void readChanels(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "trackList");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("track")) {
				_channels.add(readChannel(parser));
			} else {
				skip(parser);
			}
		}
	}

	private Channel readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "track");
		String title = null;
		String url = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("title")) {
				title = readTitle(parser);
			} else if (name.equals("location")) {
				url = readUrl(parser);
			} else {
				skip(parser);
			}
		}
		return new Channel(title, url);
	}

	// Processes title tags in the feed.
	private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "title");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}

	// Processes title tags in the feed.
	private String readUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "location");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "location");
		return title;
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}

}
