package de.muffincrafter.mrwater.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import de.muffincrafter.mrwater.Product;
import de.muffincrafter.mrwater.ProductDataSource;
import de.muffincrafter.mrwater.ProductDbHelper;
import de.muffincrafter.mrwater.R;
import java.util.List;

public class BarcodeFragment extends Fragment
{
	public static final String LOG_TAG = BarcodeFragment.class.getSimpleName();
	
	private ProductDataSource dataSource;
	private boolean resultGot;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_barcode, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		resultGot = false;

		Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
		dataSource = new ProductDataSource(getContext());
		
		IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
		scanIntegrator.initiateScan();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		
		
		if (!resultGot) {
		
		}
	}

	@Override
	public void onStop()
	{
		super.onStop();

		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		
		if (scanningResult != null)
		{
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			resultGot = true;
			
			Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
			dataSource.open();
			openProductInfos(getFilteredListEntry(scanContent));
			Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
			dataSource.close();
			
			} else {
			Toast.makeText(getActivity().getApplicationContext(), "No scan data received!", Toast.LENGTH_LONG).show();
		}
	}
	
	private void openProductInfos(Product product)
	{
		FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.fragment_container, new ProductInformationFragment(product))
			.commit();
	}
	
	private Product getFilteredListEntry(String searchTerm)
	{
		List<Product> productList = dataSource.getFilteredProducts(searchTerm, ProductDbHelper.COLUMN_BARCODE, ProductDbHelper.COLUMN_NAME + " COLLATE NOCASE ASC");
		
		return productList.get(0);
	}
}
