package de.muffincrafter.mrwater;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProductDbHelper extends SQLiteOpenHelper
{
	private static final String LOG_TAG = ProductDbHelper.class.getSimpleName();

	public static final String DB_NAME = "products.db";
	public static final int DB_VERSION = 2;

	public static final String TABLE_PRODUCTS = "products";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_WATER = "water";
	public static final String COLUMN_BARCODE = "barcode";
	public static final String COLUMN_TAGS = "tags";

	public static final String SQL_CREATE =
	"CREATE TABLE " + TABLE_PRODUCTS +
	"(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	COLUMN_NAME + " TEXT NOT NULL, " +
	COLUMN_WATER + " INTEGER NOT NULL, " +
	COLUMN_BARCODE + " INTEGER, " +
	COLUMN_TAGS + " TEXT);";

	public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_PRODUCTS;

	public ProductDbHelper(Context context)
	{
		//super(context, "PLATZHALTER_DATENBANKNAME", null, 1);
		super(context, DB_NAME, null, DB_VERSION);
		Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
			db.execSQL(SQL_CREATE);
		}
		catch (Exception ex)
		{
			Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.d(LOG_TAG, "Die Tabelle mit Versionsnummer " + oldVersion + " wird entfernt.");
		db.execSQL(SQL_DROP);
		onCreate(db);
	}
}
