package com.gsdstr.ipbox.playlist;

import com.gsdstr.ipbox.R;

/**
 * User: gsd
 * Date: 5/27/12
 * Time: 5:02 PM
 */
public class Channel {
	protected String _title;
	protected String _info;
	protected String _url;
	protected int _iconId = R.drawable.wheel;

	public Channel() {
	}

	public Channel(String title, String url) {
		_title = title;
		_url = url;
	}

	@Override
	public String toString() {
		if (_title != null)
			return _title;
		else
			return _url;
	}

	public void setInfo(String info) {
		_info = info;
		if (_info == null)
			return;
		String[] strings = _info.split(",");
		if (strings.length > 1)
			setTitle(strings[1]);
	}

	public void setUrl(String url) {
		_url = url;
	}

	public String getUrl() {
		return _url;
	}

	public int getIconId() {
		return _iconId;
	}

	protected void setTitle(String string) {
		_title = string;
		_iconId = findIcon();
	}

	protected int findIcon() {
		if (contains(_title, "Первый канал"))
			return R.drawable.ico_ch_one;
		else if (contains(_title, "Россия 1"))
			return R.drawable.ico_ch_rtr_1;
		else if (contains(_title, "Россия 24"))
			return R.drawable.ico_ch_rtr_24;
		else if (contains(_title, "Россия 2"))
			return R.drawable.ico_ch_rtr_2;
		else if (contains(_title, "Областное ТВ"))
			return R.drawable.wheel;
		else if (contains(_title, "НТВ"))
			return R.drawable.ico_ch_ntv;
		else if (contains(_title, "Карусель"))
			return R.drawable.ico_ch_karusel;
		else if (contains(_title, "ТНТ"))
			return R.drawable.ico_ch_tnt;
		else if (contains(_title, "Перец"))
			return R.drawable.ico_ch_perez;
		else if (contains(_title, "СТС"))
			return R.drawable.ico_ch_ctc;

		else if (contains(_title, "ТВ3"))
			return R.drawable.ico_ch_tv3;
		else if (contains(_title, "4 канал"))
			return R.drawable.ico_ch_4;
		else if (contains(_title, "8 канал"))
			return R.drawable.ico_ch_8;
		else if (contains(_title, "9 канал"))
			return R.drawable.ico_ch_9;
		else if (contains(_title, "ОРТ"))
			return R.drawable.ico_ch_one;
		else if (contains(_title, "RTR Planeta"))
			return R.drawable.ico_ch_rtr_planeta;

		else if (contains(_title, "Дождь"))
			return R.drawable.ico_ch_rain;
		else if (contains(_title, "Индия ТВ"))
			return R.drawable.ico_ch_india;
		else if (contains(_title, "Кухня ТВ"))
			return R.drawable.ico_ch_kyh;
		else if (contains(_title, "Сарафан ТВ"))
			return R.drawable.ico_ch_sarafan;
		else if (contains(_title, "Знание"))
			return R.drawable.ico_ch_znanie;

		return R.drawable.wheel;
	}

	protected boolean contains(String title, String find) {
		return title.toLowerCase().contains(find.toLowerCase());
	}

}
