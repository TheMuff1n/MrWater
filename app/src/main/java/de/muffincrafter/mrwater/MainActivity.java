package de.muffincrafter.mrwater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

import android.view.inputmethod.InputMethodManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.widget.AbsListView;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.AdapterView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
	public static final String LOG_TAG = MainActivity.class.getSimpleName();

	private ProductDataSource dataSource;

	private ListView mProductsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
		dataSource = new ProductDataSource(this);

		initializeProductsListView();

		activateSearchButton();
		activateResetSearchButton();
		initializeContextualActionBar();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
		dataSource.open();

		Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
		showAllListEntries();
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
		dataSource.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.action_settings)
		{
			return true;
		}
		if (id == R.id.action_add_product) {
			Log.d(LOG_TAG, "Produkterstellungs-Dialog wird geöffnet.");
			createNewProductDialog().show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showAllListEntries()
	{
		List<Product> productList = dataSource.getAllProducts();

		ArrayAdapter<Product> adapter = (ArrayAdapter<Product>) mProductsListView.getAdapter();

		adapter.clear();
		adapter.addAll(productList);
		adapter.notifyDataSetChanged();
	}
	
	private void showFilteredListEntries(String searchTerm)
	{
		List<Product> productList = dataSource.getFilteredProducts(searchTerm);

		ArrayAdapter<Product> adapter = (ArrayAdapter<Product>) mProductsListView.getAdapter();

		adapter.clear();
		adapter.addAll(productList);
		adapter.notifyDataSetChanged();
	}

	private void activateSearchButton()
	{
		Button buttonSearchProduct = (Button) findViewById(R.id.button_search_product);
		final EditText editTextSearch = (EditText) findViewById(R.id.editText_search);
		final TextView textViewFilter = (TextView) findViewById(R.id.textView_filter);
		final ViewGroup linearLayoutFilter = (ViewGroup) findViewById(R.id.linearLayout_filter);
		final ViewGroup linearLayoutSearch = (ViewGroup) findViewById(R.id.linearLayout_search);

		buttonSearchProduct.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					String search = editTextSearch.getText().toString();
					
					if (TextUtils.isEmpty(search)) {
						editTextSearch.setError(getString(R.string.editText_errorMessage));
						return;
					}

					hideKeyBoard();
					
					editTextSearch.setText("");
					textViewFilter.setText(getString(R.string.textView_filter) + " " + search);
					
					linearLayoutFilter.setVisibility(android.view.View.VISIBLE);
					linearLayoutSearch.setVisibility(android.view.View.GONE);
					
					showFilteredListEntries(search);
				}
			});
	}
	
	private void activateResetSearchButton()
	{
		Button buttonResetSearch = (Button) findViewById(R.id.button_reset_search);
		final ViewGroup linearLayoutFilter = (ViewGroup) findViewById(R.id.linearLayout_filter);
		final ViewGroup linearLayoutSearch = (ViewGroup) findViewById(R.id.linearLayout_search);

		buttonResetSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					linearLayoutFilter.setVisibility(android.view.View.GONE);
					linearLayoutSearch.setVisibility(android.view.View.VISIBLE);

					showAllListEntries();
				}
			});
	}

	private void initializeContextualActionBar()
	{
		final ListView shoppingMemosListView = (ListView) findViewById(R.id.listView_products);
		shoppingMemosListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

		shoppingMemosListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
				int selCount = 0;

				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
				{
					if (checked)
					{
						selCount++;
					}
					else
					{
						selCount--;
					}
					String cabTitle = selCount + " " + getString(R.string.cab_checked_string);
					mode.setTitle(cabTitle);
					mode.invalidate();
				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu)
				{
					getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu)
				{
					MenuItem item = menu.findItem(R.id.cab_change);
					if (selCount == 1)
					{
						item.setVisible(true);
					}
					else
					{
						item.setVisible(false);
					}

					return false;
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item)
				{
					boolean returnValue = true;
					SparseBooleanArray touchedShoppingMemosPosition = shoppingMemosListView.getCheckedItemPositions();

					switch (item.getItemId())
					{
						case R.id.cab_delete:
							for (int i = 0; i < touchedShoppingMemosPosition.size(); i++)
							{
								boolean isChecked = touchedShoppingMemosPosition.valueAt(i);
								if (isChecked)
								{
									int positionInListView = touchedShoppingMemosPosition.keyAt(i);
									Product shoppingMemo = (Product) shoppingMemosListView.getItemAtPosition(positionInListView);
									Log.d(LOG_TAG, "Position im ListView: " + positionInListView + " Inhalt: " + shoppingMemo.toString());
									dataSource.deleteProduct(shoppingMemo);
								}
							}
							showAllListEntries();
							mode.finish();
							return true;

						case R.id.cab_change:
							Log.d(LOG_TAG, "Eintrag ändern");
							for (int i = 0; i < touchedShoppingMemosPosition.size(); i++)
							{
								boolean isChecked = touchedShoppingMemosPosition.valueAt(i);
								if (isChecked)
								{
									int positionInListView = touchedShoppingMemosPosition.keyAt(i);
									Product shoppingMemo = (Product) shoppingMemosListView.getItemAtPosition(positionInListView);
									Log.d(LOG_TAG, "Position im ListView: " + positionInListView + " Inhalt: " + shoppingMemo.toString());

									AlertDialog editShoppingMemoDialog = createEditProductDialog(shoppingMemo);
									editShoppingMemoDialog.show();
								}
							}

							mode.finish();
							break;

						default:
							returnValue = false;
							break;
					}
					return returnValue;
				}

				@Override
				public void onDestroyActionMode(ActionMode mode)
				{
					selCount = 0;
				}
			});
	}

	private AlertDialog createEditProductDialog(final Product product)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();

		View dialogsView = inflater.inflate(R.layout.dialog_edit_product, null);

		final EditText editTextNewName = (EditText) dialogsView.findViewById(R.id.editText_new_name);
		editTextNewName.setText(product.getName());

		final EditText editTextNewWater = (EditText) dialogsView.findViewById(R.id.editText_new_water);
		editTextNewWater.setText(String.valueOf(product.getWater()));

		final EditText editTextNewTags = (EditText) dialogsView.findViewById(R.id.editText_new_tags);
		editTextNewTags.setText(product.getTags());

		builder.setView(dialogsView)
			.setTitle(R.string.dialog_title)
			.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					String name = editTextNewName.getText().toString();
					String waterString = editTextNewWater.getText().toString();
					String tags = editTextNewTags.getText().toString();

					if ((TextUtils.isEmpty(name)) || (TextUtils.isEmpty(waterString)))
					{
						Log.d(LOG_TAG, "Ein Eintrag enthielt keinen Text. Daher Abbruch der Änderung.");
						return;
					}

					int water = Integer.parseInt(waterString);

					Product updatedProduct = dataSource.updateProduct(product.getId(), name, water, tags);

					Log.d(LOG_TAG, "Alter Eintrag - ID: " + product.getId() + " Inhalt: " + product.toString());
					Log.d(LOG_TAG, "Neuer Eintrag - ID: " + updatedProduct.getId() + " Inhalt: " + updatedProduct.toString());

					showAllListEntries();
					dialog.dismiss();

					hideKeyBoard();
				}
			})
			.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.cancel();

					hideKeyBoard();
				}
			});

		return builder.create();
	}
	
	private AlertDialog createNewProductDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();

		View dialogsView = inflater.inflate(R.layout.dialog_new_product, null);

		final EditText editTextName = (EditText) dialogsView.findViewById(R.id.editText_name);
		final EditText editTextWater = (EditText) dialogsView.findViewById(R.id.editText_water);
		final EditText editTextTags = (EditText) dialogsView.findViewById(R.id.editText_tags);

		builder.setView(dialogsView)
			.setTitle(R.string.dialog_2_title)
			.setPositiveButton(R.string.dialog_2_button_positive, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					String name = editTextName.getText().toString();
					String waterString = editTextWater.getText().toString();
					String tags = editTextTags.getText().toString();

					if (TextUtils.isEmpty(waterString))
					{
						editTextWater.setError(getString(R.string.editText_errorMessage));
						return;
					}
					if (TextUtils.isEmpty(name))
					{
						editTextName.setError(getString(R.string.editText_errorMessage));
						return;
					}
					if (TextUtils.isEmpty(tags)) {
						editTextTags.setError(getString(R.string.editText_errorMessage));
						return;
					}

					int water = Integer.parseInt(waterString);

					Product product = dataSource.createShoppingMemo(name, water, tags);

					Log.d(LOG_TAG, "Neuer Eintrag - ID: " + product.getId() + " Inhalt: " + product.toString());

					showAllListEntries();
					dialog.dismiss();

					hideKeyBoard();
				}
			})
			.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.cancel();

					hideKeyBoard();
				}
			});

		return builder.create();
	}

	private void initializeProductsListView()
	{
		List<Product> emptyListForInitialization = new ArrayList<>();

		mProductsListView = (ListView) findViewById(R.id.listView_products);

		ArrayAdapter<Product> productArrayAdapter = new ArrayAdapter<Product>(
			this,
			android.R.layout.simple_list_item_multiple_choice,
			emptyListForInitialization);

		mProductsListView.setAdapter(productArrayAdapter);

		mProductsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
				{
					Product product = (Product) adapterView.getItemAtPosition(position);

					openProductInfos(product);
					Log.d(LOG_TAG, "Produkt-Infos von Eintrag: " + product.toString() + " werden geöffnet.");
					showAllListEntries();
				}
			});
	}

	private void hideKeyBoard()
	{
		InputMethodManager inputMethodManager;
		inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (getCurrentFocus() != null)
		{
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	private void openProductInfos(Product product)
	{
		// Fenster öffnen
	}
}
