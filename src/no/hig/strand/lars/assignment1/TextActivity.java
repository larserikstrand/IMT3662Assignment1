package no.hig.strand.lars.assignment1;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TextActivity extends ListActivity {
	public final static int ACTIVITY_ID = 1;
	private DatabaseHandler mDbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);
		// Show the Up button in the action bar.
		setupActionBar();
		// Set up UI and event handlers.
		setupUI();
		
		mDbHandler = new DatabaseHandler(this);
		mDbHandler.open();
		// Display the stored texts from database.
		displayTexts();
		// Set context menu on the texts (for delete function).
		registerForContextMenu(getListView());
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	
	
	@Override
	public void onResume() {
		// Set this activity as the last open activity.
	    Editor e = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    e.putInt("lastActivity", ACTIVITY_ID);
	    e.commit();
	    
	    super.onResume();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(R.string.delete_text);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		mDbHandler.deleteText(info.id);
		displayTexts();
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Store the text in the editText view to support orientation change.
		EditText text = (EditText) findViewById(R.id.text);
		String data = text.getText().toString();
		if(data != null) {
			outState.putString(DatabaseHandler.KEY_TEXT, data);
		}
	}
	
	@Override
	public void onRestoreInstanceState(Bundle inState) {
		// Restore the text in the editText view (for orientation change).
		if(inState != null) {
			super.onRestoreInstanceState(inState);
			EditText text = (EditText) findViewById(R.id.text);
			text.setText(inState.getString(DatabaseHandler.KEY_TEXT));
		}
	}
	
	// Set up the UI with the event handler.
	private void setupUI() {
		Button button = (Button) findViewById(R.id.save_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				createText();
			}
		});
	}
	
	private void displayTexts() {
		// Get all texts from database.
		Cursor textsCursor = mDbHandler.fetchTexts();
		// Manage the cursor automatically.
		startManagingCursor(textsCursor);
		
		String[] from = new String[] { DatabaseHandler.KEY_TEXT };
		
		int[] to = new int[] { R.id.text_container };
		
		// Put texts in an adapter for the list view.
		SimpleCursorAdapter texts =
				new SimpleCursorAdapter(this,
						R.layout.text_container, textsCursor, from, to);
		
		setListAdapter(texts);
	}
	
	// Create a new text entry in the database.
	private void createText() {
		TextView text = (TextView) findViewById(R.id.text);
		String data = text.getText().toString();
		if(data != null) {
			mDbHandler.createText(data);
			text.setText(null);
			displayTexts();
		}
	}
	
}
