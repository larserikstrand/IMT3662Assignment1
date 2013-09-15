package no.hig.strand.lars.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Class that handles the database connection.
 * @author LarsErik
 *
 */
public class DatabaseHandler {
	// Database columns.
	public static final String KEY_TEXT = "text";
    public static final String KEY_ROWID = "_id";
    
    private static final String TAG = "DatabaseHandler";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "texts";
    private static final int DATABASE_VERSION = 1;
    
    /**
     * String to create the database
     */
    private static final String DATABASE_CREATE =
    		"create table " + DATABASE_TABLE + " (_id integer primary key autoincrement," +
    		"text text not null);";
    
    private final Context mCtx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
    	
    	DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

		@Override
		public void onCreate(SQLiteDatabase db) {
			// Create the database if not exists.
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
		}
    	
    }
    
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    DatabaseHandler(Context ctx) {
        this.mCtx = ctx;
    }
    
    public DatabaseHandler open() throws SQLException {
    	mDbHelper = new DatabaseHelper(mCtx);
    	mDb = mDbHelper.getWritableDatabase();
    	return this;
    }
    
    public void close() {
    	mDbHelper.close();
    }
    
    public long createText(String text) {
    	ContentValues values = new ContentValues();
    	values.put(KEY_TEXT, text);
    	
    	return mDb.insert(DATABASE_TABLE, null, values);
    }
    
    public boolean deleteText(long rowId) {
    	return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public Cursor fetchTexts() {
    	return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TEXT },
    					 null, null, null, null, null);
    }
    
    
}
