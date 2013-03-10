package com.gsdstr.ipbox.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.gsdstr.ipbox.Const;

/**
 * User: gsd
 * Date: 5/23/12
 * Time: 8:58 PM
 */
public class DetailsFragment extends Fragment {
	/**
	 * Create a new instance of DetailsFragment, initialized to
	 * show the text at 'index'.
	 */
	public static DetailsFragment newInstance(int index) {
		DetailsFragment f = new DetailsFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt(Const.ARGUMENT_INDEX, index);
		f.setArguments(args);

		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt(Const.ARGUMENT_INDEX, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			// Currently in a layout without a container, so no reason to create our view.
			return null;
		}

		ScrollView scroller = new ScrollView(getActivity());
		TextView text = new TextView(getActivity());
		int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
			4, getActivity().getResources().getDisplayMetrics());
		text.setPadding(padding, padding, padding, padding);
		scroller.addView(text);
		text.setText("" + getShownIndex());
		return scroller;
	}
}