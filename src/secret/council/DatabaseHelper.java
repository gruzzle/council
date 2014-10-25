package secret.council;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {
	private static final String TAG = "DatabaseHelper";

	private static final String DATABASE_NAME = "council.db";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public Cursor getEventCursor() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		qb.setTables("Events");

		Cursor c = qb.query(db, null, null, null, null, null, null);
		c.moveToFirst();
		
		return c;
	}
		
	// reload db from asset directory
	public static void forceDatabaseReload(Context context){
	    DatabaseHelper dbHelper = new DatabaseHelper(context);
	    dbHelper.setForcedUpgradeVersion(DATABASE_VERSION);
	    SQLiteDatabase db = dbHelper.getWritableDatabase();
	    db.setVersion(-1);
	    db.close();
	    db = dbHelper.getWritableDatabase();
	}
		
	public String get(int id, String columnName) {
		SQLiteDatabase db = getReadableDatabase();		
		Cursor c = db.rawQuery("SELECT * FROM Events WHERE id=" + id, null);		
		c.moveToFirst();
		
		return c.getString(c.getColumnIndexOrThrow(columnName));
	}
}