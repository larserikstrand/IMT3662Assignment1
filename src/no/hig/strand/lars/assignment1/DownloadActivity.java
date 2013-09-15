package no.hig.strand.lars.assignment1;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class DownloadActivity extends Activity {
	public final static int ACTIVITY_ID = 2;
	// URL of image to download.
	private static final String IMAGE_URL = "http://us.123rf.com/450wm/rasslava/rasslava1306/rasslava130600359/20084546-colorful-abstract-sphere-on-white-background.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Download the image asynchronously.
		try {
			new DownloadImage().execute(new URL(IMAGE_URL));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.download, menu);
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
		// Set this activity as the last open activity (for restoring
		//  application state).
	    Editor e = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    e.putInt("lastActivity", ACTIVITY_ID);
	    e.commit();

	    super.onResume();
	}
	
	// Private inner class to download image asynchronously.
	private class DownloadImage extends AsyncTask<URL, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(URL... urls) {
			assert urls.length == 1;
			return downloadImage(urls[0]);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// Show the downloaded image in the view.
			final ImageView image = (ImageView) findViewById(R.id.img);
			image.setImageBitmap(result);
		}
		
	}
	
	private Bitmap downloadImage(final URL url) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			// Open connection.
			is = openHttpConnection(url);
			// Decode stream to a bitmap.
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	private InputStream openHttpConnection(final URL url) throws IOException {
		InputStream is = null;
		int response = -1;
		final URLConnection conn = url.openConnection();
		
		// If the opened connection is a HTTP connection.
		if(!(conn instanceof HttpURLConnection)) {
			throw new IOException("Not an HTTP connection");
		}
		
		try {
			final HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			
			// Get response from connection.
			response = httpConn.getResponseCode();
			// Response 200, everythink ok.
			if (response == HttpURLConnection.HTTP_OK) {
				is = httpConn.getInputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return is;
	}
	
}
