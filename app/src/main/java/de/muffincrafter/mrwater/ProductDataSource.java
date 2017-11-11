package de.muffincrafter.mrwater;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ProductDataSource
{
	private static final String LOG_TAG = ProductDataSource.class.getSimpleName();

	private SQLiteDatabase database;
	private ProductDbHelper dbHelper;

	private String[] columns = {
		ProductDbHelper.COLUMN_ID,
		ProductDbHelper.COLUMN_NAME,
		ProductDbHelper.COLUMN_WATER,
		ProductDbHelper.COLUMN_TAGS
	};

	public ProductDataSource(Context context)
	{
		Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
		dbHelper = new ProductDbHelper(context);
	}

	public void open()
	{
		Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefertigt.");
		database = dbHelper.getWritableDatabase();
		Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
	}

	public void close()
	{
		dbHelper.close();
		Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
	}

	public Product createShoppingMemo(String name, int water, String tags)
	{
		ContentValues values = new ContentValues();
		values.put(ProductDbHelper.COLUMN_NAME, name);
		values.put(ProductDbHelper.COLUMN_WATER, water);
		values.put(ProductDbHelper.COLUMN_TAGS, tags);

		long insertId = database.insert(ProductDbHelper.TABLE_PRODUCTS, null, values);

		Cursor cursor = database.query(ProductDbHelper.TABLE_PRODUCTS,
									   columns, ProductDbHelper.COLUMN_ID + "=" + insertId,
									   null, null, null, null);

		cursor.moveToFirst();
		Product product = cursorToProduct(cursor);
		cursor.close();

		return product;
	}

	public Product updateProduct(long id, String newName, int newWater, String newTags)
	{
		ContentValues values = new ContentValues();
		values.put(ProductDbHelper.COLUMN_NAME, newName);
		values.put(ProductDbHelper.COLUMN_WATER, newWater);
		values.put(ProductDbHelper.COLUMN_TAGS, newTags);

		database.update(ProductDbHelper.TABLE_PRODUCTS,
						values,
						ProductDbHelper.COLUMN_ID + "=" + id,
						null);

		Cursor cursor = database.query(ProductDbHelper.TABLE_PRODUCTS,
									   columns, ProductDbHelper.COLUMN_ID + "=" + id,
									   null, null, null, null);

		cursor.moveToFirst();
		Product product = cursorToProduct(cursor);
		cursor.close();

		return product;
	}

	public void deleteProduct(Product product)
	{
		long id = product.getId();

		database.delete(ProductDbHelper.TABLE_PRODUCTS,
						ProductDbHelper.COLUMN_ID + "=" + id,
						null);

		Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + product.toString());
	}

	private Product cursorToProduct(Cursor cursor)
	{
		int idIndex = cursor.getColumnIndex(ProductDbHelper.COLUMN_ID);
		int idName = cursor.getColumnIndex(ProductDbHelper.COLUMN_NAME);
		int idWater = cursor.getColumnIndex(ProductDbHelper.COLUMN_WATER);
		int idTags = cursor.getColumnIndex(ProductDbHelper.COLUMN_TAGS);

		String name = cursor.getString(idName);
		int water = cursor.getInt(idWater);
		String tags = cursor.getString(idTags);
		long id = cursor.getLong(idIndex);

		Product product = new Product(name, water, tags, id);

		return product;
	}

	public List<Product> getAllProducts()
	{
		List<Product> productList = new ArrayList<>();

		Cursor cursor = database.query(ProductDbHelper.TABLE_PRODUCTS,
									   columns, null, null, null, null, null);

		cursor.moveToFirst();
		Product product;

		while (!cursor.isAfterLast())
		{
			product = cursorToProduct(cursor);
			productList.add(product);
			Log.d(LOG_TAG, "ID: " + product.getId() + ", Inhalt: " + product.toString());
			cursor.moveToNext();
		}

		cursor.close();

		return productList;
	}
	
	public List<Product> getFilteredProducts(String searchTerm) {
		List<Product> productList = new ArrayList<>();
		
		Cursor cursor = database.query(ProductDbHelper.TABLE_PRODUCTS,
		columns, ProductDbHelper.COLUMN_NAME + " LIKE " + "'%" + searchTerm + "%'",
		null, null, null, null);
		
		cursor.moveToFirst();
		Product product;
		
		while(!cursor.isAfterLast()) {
			product = cursorToProduct(cursor);
			productList.add(product);
			Log.d(LOG_TAG, "ID: " + product.getId() + ", Inhalt: " + product.toString());
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return productList;
	}
}
