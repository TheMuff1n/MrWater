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

public class ProductInformationFragment extends Fragment
{
	private Product product;
	
	public ProductInformationFragment(Product product) {
		this.product = product;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//((MainActivity)getActivity()).drawerToggle.setDrawerIndicatorEnabled(false);
		return inflater.inflate(R.layout.fragment_product_information, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		((TextView)view.findViewById(R.id.product_name)).setText(getString(R.string.product_name) + " " + product.getName());
		((TextView)view.findViewById(R.id.product_water)).setText(getString(R.string.product_water) + " " + String.valueOf(product.getWater()));
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
