package de.muffincrafter.mrwater.Fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import de.muffincrafter.mrwater.R;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import de.muffincrafter.mrwater.MainActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import de.muffincrafter.mrwater.Product;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.util.Log;
import android.support.v4.app.FragmentTransaction;
import de.muffincrafter.mrwater.ProductDataSource;
import de.muffincrafter.mrwater.ProductDbHelper;

public class ProductInformationFragment extends Fragment
{
	public static final String LOG_TAG = ProductInformationFragment.class.getSimpleName();
	
	ProductDataSource dataSource;
	
	private View rootView;
	
	private Product product;
	
	private ListView mSimilarProductsListView;
	
	public ProductInformationFragment(Product product) {
		this.product = product;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
		dataSource = new ProductDataSource(getContext());
	}
	
	@Override
	public void onResume()
	{
		super.onResume();

		Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
		dataSource.open();

		Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
		showFilteredListEntries(product.getTags());
	}

	private void showFilteredListEntries(String searchTerm)
	{
		List<Product> productList = dataSource.getFilteredProducts(searchTerm, ProductDbHelper.COLUMN_TAGS, ProductDbHelper.COLUMN_WATER + " ASC");

		ArrayAdapter<Product> adapter = (ArrayAdapter<Product>) mSimilarProductsListView.getAdapter();

		adapter.clear();
		adapter.addAll(productList);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onPause()
	{
		super.onPause();

		Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
		dataSource.close();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_product_information, container, false);
		
		//((MainActivity)getActivity()).drawerToggle.setDrawerIndicatorEnabled(false);
		initializeSimilarProductsList();
		
		return rootView;
	}

	private void initializeSimilarProductsList()
	{
		List<Product> emptyListForInitialization = new ArrayList<>();

		mSimilarProductsListView = (ListView) getView().findViewById(R.id.listView_similar_products);

		ArrayAdapter<Product> mSimilarProductsArrayAdapter = new ArrayAdapter<Product>(
			getContext(),
			android.R.layout.simple_list_item_1,
			emptyListForInitialization);

		mSimilarProductsListView.setAdapter(mSimilarProductsArrayAdapter);

		mSimilarProductsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
				{
					Product product = (Product) adapterView.getItemAtPosition(position);

					openProductInfos(product);
					Log.d(LOG_TAG, "Produkt-Infos von Eintrag: " + product.toString() + " werden geöffnet.");
					//showAllListEntries();
				}

				private void openProductInfos(Product product)
				{
					FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.replace(R.id.fragment_container, new ProductInformationFragment(product))
						.commit();
				}
			});
	}

	@Override
	public View getView()
	{
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		((TextView)view.findViewById(R.id.product_name)).setText(getString(R.string.product_name) + " " + product.getName());
		((TextView)view.findViewById(R.id.product_water)).setText(getString(R.string.product_water) + " " + String.valueOf(product.getWater()) + " l/kg");
		((TextView)view.findViewById(R.id.product_tags)).setText(getString(R.string.product_tags) + " " + product.getTags());
		((TextView)view.findViewById(R.id.product_barcode)).setText(getString(R.string.product_barcode) + " " + String.valueOf(product.getBarcode()));
	}

	// TODO:Get this to work
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Toast.makeText(getContext(), item.getItemId(), Toast.LENGTH_LONG);
		switch(item.getItemId())
		{
			case android.support.v7.appcompat.R.id.home:
				Toast.makeText(getContext(), "PopBackStack", Toast.LENGTH_LONG);
				((AppCompatActivity)getActivity()).getSupportFragmentManager().popBackStack();
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
