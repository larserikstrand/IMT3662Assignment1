package no.hig.strand.lars.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Checks last open activity in order to restore application state,
		checkLastActivity();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Generates event handlers for the buttons.
		setupUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void checkLastActivity() {
		// Get the id of the last open Activity
        int lastActivity = PreferenceManager.getDefaultSharedPreferences(
        		this).getInt("lastActivity", -1);
        if (lastActivity == TextActivity.ACTIVITY_ID) {
            startActivityForResult(new Intent(this, TextActivity.class), 1);
        } else if (lastActivity == DownloadActivity.ACTIVITY_ID) {
            startActivityForResult(new Intent(this, DownloadActivity.class), 1);
        } else if (lastActivity == MapActivity.ACTIVITY_ID) {
        	startActivityForResult(new Intent(this, MapActivity.class), 1);
        }
	}
	
	public void onActivityResult() {
        // Terminate the last activity upon return.
        finish();
    }
	
	private void setupUI() {
		// Create event handler for the texts-button.
		Button button = (Button) findViewById(R.id.text_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				handleTexts();
			}
		});
		
		// Create event handler for the web-button.
		button = (Button) findViewById(R.id.web_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleWeb();
			}
		});
		
		// Create event handler for the map-button.
		button = (Button) findViewById(R.id.map_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleMap();
			}
		});
	}
	
	private void handleTexts() {
		Intent intent = new Intent(this, TextActivity.class);
		startActivity(intent);
	}
	
	private void handleWeb() {
		Intent intent = new Intent(this, DownloadActivity.class);
		startActivity(intent);
	}

	private void handleMap() {
		//LocationManager locationManager =
		//		(LocationManager) getSystemService(this.LOCATION_SERVICE);
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
	}
}
