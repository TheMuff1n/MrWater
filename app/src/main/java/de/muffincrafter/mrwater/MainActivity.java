package de.muffincrafter.mrwater;

import android.support.v4.app.Fragment;
import de.muffincrafter.mrwater.Fragments.ProductsFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.os.PersistableBundle;
import android.content.res.Configuration;
import android.widget.Adapter;
import de.muffincrafter.mrwater.Fragments.InformationFragment;

public class MainActivity extends AppCompatActivity
{
	public static final String LOG_TAG = MainActivity.class.getSimpleName();

	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawerLayout;
	private String activityTitle;

	private ListView drawerList;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		activityTitle = getTitle().toString();
		setupDrawer();

		drawerList = (ListView) findViewById(R.id.navList);
		initializeDrawer();

		getSupportFragmentManager().beginTransaction()
			.replace(R.id.fragment_container, new ProductsFragment())
			.commit();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (drawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void setupDrawer()
	{
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
												 R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(getString(R.string.navigation));
				invalidateOptionsMenu();
			}

			public void onDrawerClosed(View drawerView)
			{
				super.onDrawerClosed(drawerView);
				getSupportActionBar().setTitle(activityTitle);
				invalidateOptionsMenu();
			}
		};
		drawerToggle.setDrawerIndicatorEnabled(true);

		drawerLayout.addDrawerListener(drawerToggle);
	}

	private void initializeDrawer()
	{
		String[] fragmentsArray = getResources().getStringArray(R.array.fragments_string_array);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fragmentsArray);
		drawerList.setAdapter(adapter);

		drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					switch (position)
					{
						case 0:
							showFragment(new ProductsFragment());
							break;
						case 1:
							// show barcode scanner
							break;
						case 2:
							showFragment(new InformationFragment());
							break;

						default:
					}
					
					drawerLayout.closeDrawer(drawerList);
				}

				private void showFragment(Fragment fragment)
				{
					getSupportFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, fragment)
						.commit();
				}
			});
	}
}
