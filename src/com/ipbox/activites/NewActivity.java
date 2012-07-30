package com.ipbox.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.ipbox.IpBoxApp;
import com.ipbox.R;

/**
 * User: gsd
 * Date: 11.07.12
 * Time: 13:26
 */
public class NewActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.on_new);

		loadList();
		loadUrl();

		Button skip = (Button) findViewById(R.id.add_list_skip);
		skip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startMain();
			}
		});
	}

	protected void loadUrl() {
		final EditText editText = (EditText) findViewById(R.id.add_list_url_text);
		fillUrl(editText);

		final Button add = (Button) findViewById(R.id.add_list_url_button);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addNewUrl(editText.getText());
				startMain();
			}
		});
	}

	protected void addNewUrl(Editable text) {
		IpBoxApp.getPlayListsHolder().addNewUrl(text.toString());
	}

	protected void fillUrl(EditText editText) {
		Intent intent = getIntent();
		if (intent == null || intent.getData() == null || intent.getAction() == null ||
			!intent.getAction().equals("android.intent.action.VIEW"))
			return;
		editText.setText(intent.getData().toString());

		findViewById(R.id.add_list_hello_text).setVisibility(View.GONE);

		findViewById(R.id.add_list_select_spinner).setVisibility(View.GONE);
		findViewById(R.id.add_list_select_button).setVisibility(View.GONE);
	}

	protected void loadList() {

		final Spinner spinner = (Spinner) findViewById(R.id.add_list_select_spinner);
		fillList(spinner);


		Button add = (Button) findViewById(R.id.add_list_select_button);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addProvider(spinner.getSelectedItem());
				startMain();
			}
		});
	}

	private void addProvider(Object selectedItem) {
		IpBoxApp.getPlayListsHolder().addNewProvider(selectedItem.toString());
	}

	protected void fillList(Spinner spinner) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.providers_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}

	protected void startMain(){
		Intent intent = new Intent(getBaseContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

}

